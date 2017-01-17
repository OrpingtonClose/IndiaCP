package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.*
import com.barclays.indiacp.cordapp.schemas.PersistentDepositoryAccountSchemaState
import net.corda.contracts.ICommercialPaperState
import net.corda.contracts.asset.sumCashBy
import net.corda.contracts.clause.AbstractIssue
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.Clause
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.node.services.VaultService
import net.corda.core.random63BitValue
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.contracts.asset.DUMMY_CASH_ISSUER
import net.corda.core.crypto.*
import net.corda.core.days
import java.security.PublicKey
import java.time.Instant
import java.util.*
import javax.print.attribute.IntegerSyntax

/**
 * Created for Barclays Mumbai Rise Accelerator Demo. This is intended to be a proof of concept of how the primary
 * market flows of India Commercial Paper asset class can be managed by using Smart Contracts on R3 Corda Release-M6.0
 *
 * Created by ritukedia on 07/01/17.
 */
val INDIA_CP_ID = IndiaCommercialPaper()

// TODO: Generalise the notion of an owned instrument into a superclass/supercontract. Consider composition vs inheritance.
class IndiaCommercialPaper : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    data class Terms(
            val asset: Issued<Currency>,
            val maturityDate: Instant
    )

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaper.Clauses.Group(), tx.commands.select<IndiaCommercialPaper.Commands>())

    data class State(
            val issuer: Party,
            val beneficiary: Party,
            val investor: Party,
            val ipa: Party,
            val depository: Party,

            val cpProgramID: String,
            val cpTradeID: String,
            val tradeDate: Instant,
            val valueDate: Instant,
            val faceValue: Amount<Issued<Currency>>,
            val faceValuePerUnit: Amount<Currency>,
            val noOfunits: Int,
            val yieldOnMaturity: Float,
            val maturityDate: Instant,
            var isin: String,
            var dealConfirmationDocId: String? = null,

            var issuerSettlementDetails: SettlementDetails? = null,
            var investorSettlementDetails: SettlementDetails? = null,
            var ipaSettlementDetails: SettlementDetails? = null,

            val status: String? = IndiaCPProgramStatusEnum.UNKNOWN.name,
            val modifiedBy: String? = "",//todo default to logged in user
            val lastModifiedDate: Instant? = Instant.now(),
            val version: Int? = ModelUtils.STARTING_VERSION
    ) : LinearState, QueryableState {
        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_ID

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(cpTradeID)

        //Only the Issuer and Investor should be party to the full state of this transaction
        val parties: List<Party>
            get() = listOf(issuer, beneficiary, investor, ipa, depository)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, beneficiary, investor, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        val token: Issued<IndiaCommercialPaper.Terms>
            get() = Issued(issuer.ref(CPUtils.getReference(cpTradeID)), IndiaCommercialPaper.Terms(faceValue.token, maturityDate))

        override fun toString() = "${Emoji.newspaper}CommercialPaper($cpProgramID:$cpTradeID of $faceValue redeemable on $maturityDate by '$issuer', owned by ${beneficiary.owningKey.toString()})"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(IndiaCommercialPaperSchemaV1)

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is IndiaCommercialPaperSchemaV1 ->
                {
                    val cpPersistedObject = IndiaCommercialPaperSchemaV1.PersistentIndiaCommericalPaperState(
                        issuanceParty = this.issuer.owningKey.toBase58String(),
                        beneficiaryParty = this.beneficiary.owningKey.toBase58String(),
                        investorParty = this.investor.owningKey.toBase58String(),
                        ipaParty = this.ipa.owningKey.toBase58String(),
                        depositoryParty = this.depository.owningKey.toBase58String(),

                        cpProgramID = this.cpProgramID,
                        cpTradeID = this.cpTradeID,
                        tradeDate = this.tradeDate,
                        valueDate = this.valueDate,
                        maturity = this.maturityDate,
                        faceValue = this.faceValue.quantity,
                        currency = this.faceValue.token.product.currencyCode,
                        faceValuePerUnit = this.faceValuePerUnit.quantity.toInt(),
                        noOfunits = this.noOfunits,
                        yieldOnMaturity = this.yieldOnMaturity,
                        isin = this.isin,

                        hashDealConfirmationDoc = this.dealConfirmationDocId,
                        settlementDetails = getSettlementPersistedStates(this.issuerSettlementDetails, this.investorSettlementDetails, this.ipaSettlementDetails),

                        last_modified = this.lastModifiedDate!!,
                        version = this.version!!,
                        modified_by = this.modifiedBy!!,
                        status = this.status!!
                    )

                    cpPersistedObject.settlementDetails = cpPersistedObject.settlementDetails?.map {setCPDetails(it, cpPersistedObject)}

                    return cpPersistedObject

                }
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }

        private fun  setCPDetails(it: PersistentSettlementSchemaState, cpPersistedObject: IndiaCommercialPaperSchemaV1.PersistentIndiaCommericalPaperState): PersistentSettlementSchemaState {
            it.cpDetails = cpPersistedObject
            return it
        }

        private fun  getSettlementPersistedStates(issuerSettlementDetails: IndiaCommercialPaper.SettlementDetails?, investorSettlementDetails: IndiaCommercialPaper.SettlementDetails?, ipaSettlementDetails: IndiaCommercialPaper.SettlementDetails?): List<PersistentSettlementSchemaState>? {
            val persistedSettlementStates = ArrayList<PersistentSettlementSchemaState>()
            var sd = getSettlementPersistedState(issuerSettlementDetails)
            if (sd != null) persistedSettlementStates.add(sd)
            sd = getSettlementPersistedState(investorSettlementDetails)
            if (sd != null) persistedSettlementStates.add(sd)
            sd = getSettlementPersistedState(ipaSettlementDetails)
            if (sd != null) persistedSettlementStates.add(sd)

            return persistedSettlementStates!!
        }

        private fun  getSettlementPersistedState(settlementDetails: IndiaCommercialPaper.SettlementDetails?): PersistentSettlementSchemaState? {
            if (settlementDetails == null) {
                return null
            }
            val persistentSD =  PersistentSettlementSchemaState (
                    party_type = settlementDetails.partyType,
                    creditorName = settlementDetails.paymentAccountDetails?.creditorName,
                    bankAccountDetails = settlementDetails.paymentAccountDetails?.bankAccountDetails,
                    bankName = settlementDetails.paymentAccountDetails?.bankName,
                    rtgsCode = settlementDetails.paymentAccountDetails?.rtgsCode,
                    depositoryAccounts = settlementDetails.depositoryAccountDetails?.map { getDepositoryAccountPersistedState(it) }
            )
            persistentSD.depositoryAccounts = persistentSD.depositoryAccounts?.map {setSettlementDetails(it, persistentSD)}

            return persistentSD
        }

        private fun  setSettlementDetails(it: PersistentDepositoryAccountSchemaState, persistentSD: PersistentSettlementSchemaState): PersistentDepositoryAccountSchemaState {
            it.settlementDetails = persistentSD
            return it
        }

        private fun  getDepositoryAccountPersistedState(depositoryAccountDetails: DepositoryAccountDetails): PersistentDepositoryAccountSchemaState {
            return PersistentDepositoryAccountSchemaState (
                    dpID = depositoryAccountDetails.dpID,
                    dpName = depositoryAccountDetails.dpName,
                    dpType = depositoryAccountDetails.dpType,
                    clientId = depositoryAccountDetails.clientId
            )
        }
    }

    data class SettlementDetails (
        val partyType : String,
        val paymentAccountDetails: PaymentAccountDetails?,
        val depositoryAccountDetails: List<DepositoryAccountDetails>?
    )

    data class PaymentAccountDetails (
        val creditorName: String?,
        val bankAccountDetails: String?,
        val bankName: String?,
        val rtgsCode: String?
    )

    data class DepositoryAccountDetails (
        val dpName: String?,
        val dpType: String?,
        val clientId: String?,
        val dpID: String?
    )

    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>(
                clause = AnyComposition(
                        IndiaCommercialPaper.Clauses.Issue(),
                        IndiaCommercialPaper.Clauses.AddDealConfirmationDoc(),
                        IndiaCommercialPaper.Clauses.AddISIN(),
                        IndiaCommercialPaper.Clauses.AddSettlementDetails(),
                        IndiaCommercialPaper.Clauses.Redeem()
                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaper.State, Issued<IndiaCommercialPaper.Terms>>>
                    = tx.groupStates<IndiaCommercialPaper.State, Issued<IndiaCommercialPaper.Terms>> { it.token }
        }

        abstract class AbstractIndiaCPClause : Clause<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>() {

            fun  verifyDocAsPerStatus(doc: Attachment, docId: String?): Boolean {
                val docHashAndStatus = CPUtils.getDocHashAndStatus(docId)

                return verifyDoc(doc, docHashAndStatus.first, docHashAndStatus.second)
            }

            fun  verifyDoc(doc: Attachment, docHash: SecureHash, docStatus: IndiaCPDocumentDetails.DocStatusEnum): Boolean {
                when (docStatus) {
                    IndiaCPDocumentDetails.DocStatusEnum.UNSIGNED -> {
                        //Verify that the Attachment has been uploaded and is accessible
                        return doc.open() != null
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_ISSUER -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_INVESTOR -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_NSDL -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_IPA -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    else -> return false
                }
            }

            fun  verifyDocIsSignedByRole(doc: Attachment, docId: String?, signer: IndiaCPDocumentDetails.DocStatusEnum): Boolean {
                val docHashAndStatus = CPUtils.getDocHashAndStatus(docId)

                return verifyDoc(doc, docHashAndStatus.first, signer)
            }

            fun  verifyDocIsSignedByIssuer (doc: Attachment, docId: String?): Boolean = verifyDocIsSignedByRole(doc, docId, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_ISSUER )

            fun  verifyDocIsSignedByInvestor (doc: Attachment, docId: String?): Boolean = verifyDocIsSignedByRole(doc, docId, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_INVESTOR )

            fun  verifyDocIsSignedByIPA (doc: Attachment, docId: String?): Boolean = verifyDocIsSignedByRole(doc, docId, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_IPA )

            fun  verifyDocIsSignedByDepository (doc: Attachment, docId: String?): Boolean = verifyDocIsSignedByRole(doc, docId, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_NSDL )
        }

        class Issue : AbstractIssue<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, IndiaCommercialPaper.Terms>(
                { map { Amount(it.faceValue.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.faceValue.quantity, it.token) }.sumOrZero(token) }) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaper.Commands.Issue>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddDealConfirmationDoc: AbstractIndiaCPClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.AddDealConfirmationDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.AddDealConfirmationDoc>()

                val output = tx.outputs.filterIsInstance<IndiaCommercialPaper.State>().single()

                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("ISIN Request must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Beneficiary" by (output.beneficiary.owningKey in command.signers)
                    "the Deal Confirmation Document has been attached" by (attachment.id.toString() == output.dealConfirmationDocId!!.split(":").first())
                    "the Deal Confirmation Document matches the status as uploaded" by verifyDocAsPerStatus(attachment, output.dealConfirmationDocId)
                }
                return setOf(command.value)
            }
        }

        class AddISIN: AbstractIndiaCPClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.AddISIN::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.AddISIN>()

                val outputs = tx.outputs.filterIsInstance<IndiaCommercialPaper.State>()
                val output = outputs.first()


                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("ISIN Request must be timestamped")

                for (output in outputs) {
                    requireThat {
                        "the ISIN is not NULL" by (!output.isin.isNullOrBlank())
                    }
                }
                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                }
                return setOf(command.value)
            }
        }

        class AddSettlementDetails: AbstractIndiaCPClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.AddSettlementDetails::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.AddSettlementDetails>()
                val input = tx.inputs.filterIsInstance<IndiaCommercialPaper.State>().single()

                val output = tx.outputs.filterIsInstance<IndiaCommercialPaper.State>().single()
                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("Add Settlement Details Request must be timestamped")

                when (output.status) {
                    IndiaCPIssueStatusEnum.CP_ISSUER_SETTLEMENT_DETAILS_AMENDED.name -> {
                        requireThat {
                            "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                        }
                    }
                    IndiaCPIssueStatusEnum.CP_INVESTOR_SETTLEMENT_DETAILS_AMENDED.name -> {
                        requireThat {
                            "the transaction is signed by the Issuer" by (output.investor.owningKey in command.signers)
                        }
                    }
                    IndiaCPIssueStatusEnum.CP_IPA_SETTLEMENT_DETAILS_AMENDED.name -> {
                        requireThat {
                            "the transaction is signed by the Issuer" by (output.ipa.owningKey in command.signers)
                        }
                    }
                }

                return setOf(command.value)
            }
        }

        class Redeem(): Clause<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.Redeem::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {
                // TODO: This should filter commands down to those with compatible subjects (underlying product and maturity date)
                // before requiring a single command
                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.Redeem>()
                val timestamp = tx.timestamp

                val input = inputs.single()
                val received = tx.outputs.sumCashBy(input.beneficiary.owningKey)
                val time = timestamp?.after ?: throw IllegalArgumentException("Redemptions must be timestamped")
                requireThat {
                    "the paper must have matured" by (time >= input.maturityDate)
                    "the received amount equals the face value" by (received == input.faceValue)
                    "the paper must be destroyed" by outputs.isEmpty()
                    "the transaction is signed by the beneficiary of the CP" by (input.beneficiary.owningKey in command.signers)
                }

                return setOf(command.value)
            }

        }

    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaper.Commands
        data class Move(override val contractHash: SecureHash? = null) : FungibleAsset.Commands.Move, IndiaCommercialPaper.Commands
        class Redeem : TypeOnlyCommandData(), IndiaCommercialPaper.Commands
        class AddSettlementDetails() : IndiaCommercialPaper.Commands
        class AddDealConfirmationDoc : TypeOnlyCommandData(), IndiaCommercialPaper.Commands
        class AddISIN : TypeOnlyCommandData(), IndiaCommercialPaper.Commands
    }

    fun generateIssue(cpContractState: IndiaCommercialPaper.State,
                      cpProgramStateRef: StateAndRef<IndiaCommercialPaperProgram.State>,
                      beneficiary: Party,
                      notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramStateRef)

        //Adding Outputs
        val cpProgramInputState = cpProgramStateRef.state.data

        tx.addOutputState(
                cpContractState.copy(
                        beneficiary = beneficiary,
                        status = IndiaCPIssueStatusEnum.CP_ISSUED.name,
                        lastModifiedDate = Instant.now(),
                        version = ModelUtils.STARTING_VERSION
                ),
                notary
        )
        tx.addOutputState(
                cpProgramInputState.copy(
                        status = IndiaCPProgramStatusEnum.CP_ISSUEED.name,
                        lastModifiedDate = Instant.now(),
                        version = cpProgramInputState.version!! + 1,
                        programAllocatedValue = cpProgramInputState.programAllocatedValue!!.copy (
                                quantity = cpContractState.faceValue.quantity + cpProgramInputState.programAllocatedValue!!.quantity
                        )
                ),
                cpProgramStateRef.state.notary
        )

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaper.Commands.Issue(), listOf(cpContractState.issuer.owningKey))
        tx.addCommand(IndiaCommercialPaperProgram.Commands.Amend(), listOf(cpContractState.issuer.owningKey))

        return tx
    }

    fun  generateDealConfirmation(cpStateAndRef: StateAndRef<IndiaCommercialPaper.State>, notary: Party): TransactionBuilder? {
        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpStateAndRef.state.data.copy(
                        version = cpStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPIssueStatusEnum.CP_DEAL_CONFIRMATION_DOC_ADDED.name
                ),
                cpStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpStateAndRef.state.data.dealConfirmationDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaper.Commands.AddDealConfirmationDoc(), listOf(cpStateAndRef.state.data.issuer.owningKey,
                cpStateAndRef.state.data.beneficiary.owningKey))

        return tx
    }

    fun generateAddSettlementDetails(cp: StateAndRef<State>, settlementDetails: SettlementDetails, initiator: Party, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cp)

        var issuerSettlementDetails : SettlementDetails? = cp.state.data.issuerSettlementDetails
        var investorSettlementDetails : SettlementDetails? = cp.state.data.investorSettlementDetails
        var ipaSettlementDetails : SettlementDetails? = cp.state.data.ipaSettlementDetails
        var status : String? = cp.state.data.status

        when (settlementDetails.partyType) {
            com.barclays.indiacp.model.SettlementDetails.PartyTypeEnum.ISSUER.name -> {
                issuerSettlementDetails = settlementDetails
                status = IndiaCPIssueStatusEnum.CP_ISSUER_SETTLEMENT_DETAILS_AMENDED.name
            }
            com.barclays.indiacp.model.SettlementDetails.PartyTypeEnum.INVESTOR.name -> {
                investorSettlementDetails = settlementDetails
                status = IndiaCPIssueStatusEnum.CP_INVESTOR_SETTLEMENT_DETAILS_AMENDED.name
            }
            com.barclays.indiacp.model.SettlementDetails.PartyTypeEnum.IPA.name -> {
                ipaSettlementDetails = settlementDetails
                status = IndiaCPIssueStatusEnum.CP_IPA_SETTLEMENT_DETAILS_AMENDED.name
            }
        }

        //Adding Outputs
        tx.addOutputState(
                cp.state.data.copy(
                        version = cp.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = status,
                        issuerSettlementDetails = issuerSettlementDetails,
                        investorSettlementDetails = investorSettlementDetails,
                        ipaSettlementDetails = ipaSettlementDetails
                ),
                cp.state.notary
        )

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaper.Commands.AddSettlementDetails(), listOf(initiator.owningKey))

        return tx
    }

}



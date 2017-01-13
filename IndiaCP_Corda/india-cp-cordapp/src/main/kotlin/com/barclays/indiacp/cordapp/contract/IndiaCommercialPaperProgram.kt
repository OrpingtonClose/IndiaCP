package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.IndiaCommercialPaperProgramSchemaV1
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.contracts.asset.DUMMY_CASH_ISSUER
import net.corda.contracts.clause.AbstractIssue
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.Clause
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.days
import net.corda.core.random63BitValue
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import java.security.PublicKey
import java.time.Instant
import java.util.*

/**
 * This is the Smart Contract to manage the India Commercial Paper Program - the umbrella program under which the
 * Commercial Papers are Issued. There is a ceiling associated with each program and the total allocation cannot
 * exceed the ceiling.
 *
 * Created by ritukedia
 */
val INDIA_CP_PROGRAM_ID = IndiaCommercialPaperProgram()

class IndiaCommercialPaperProgram : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaperProgram.Clauses.Group(), tx.commands.select<IndiaCommercialPaperProgram.Commands>())

    data class State(

            val issuer: Party,
            val ipa: Party,
            val depository: Party,

            val programId: String,
            val name: String,
            val type: String,
            val purpose: String,
            val issueCommencementDate: Instant,
            val programSize: Amount<Issued<Currency>>,
            val programCurrency: Currency,
            val programAllocatedValue: Amount<Currency>? = Amount(0, programCurrency),
            val maturityDate: Instant,
            val isin: String?,

            val issuerId: String,
            val issuerName: String,
            val ipaId: String,
            val ipaName: String,
            val depositoryId: String,
            val depositoryName: String,

            val isinGenerationRequestDocId: String?,
            val ipaVerificationRequestDocId: String?,
            val ipaCertificateDocId: String?,
            val corporateActionFormDocId: String?,
            val allotmentLetterDocId: String?,

            val status: String? = IndiaCPProgramStatusEnum.UNKNOWN.name,
            val modifiedBy: String? = "",//todo default to logged in user
            val lastModifiedDate: Instant? = Instant.now(),
            val version: Int? = ModelUtils.STARTING_VERSION

    ) : LinearState, QueryableState {

        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_PROGRAM_ID

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(programId)

        val parties: List<Party>
            get() = listOf(issuer, ipa, depository)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        override fun toString() = "${Emoji.newspaper}IndiaCommercialPaperProgram(of $programSize issued by '$issuer' on '$issueCommencementDate' with a maturity period of '$maturityDate')"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(IndiaCommercialPaperProgramSchemaV1)

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is IndiaCommercialPaperProgramSchemaV1 -> IndiaCommercialPaperProgramSchemaV1.PersistentIndiaCommericalPaperProgramState(

                        issuanceParty = this.issuer.owningKey.toBase58String(),
                        ipaParty = this.ipa.owningKey.toBase58String(),
                        depositoryParty = this.depository.owningKey.toBase58String(),

                        program_id = this.programId,
                        name = this.name,
                        type = this.type,
                        purpose = this.purpose,
                        issue_commencement_date = this.issueCommencementDate,
                        program_size = this.programSize.quantity,
                        program_allocated_value = this.programAllocatedValue!!.quantity,
                        program_currency = this.programCurrency.symbol,
                        maturity_days = this.maturityDate,
                        isin = this.isin,

                        issuer_id = this.issuerId,
                        issuer_name = this.issuerName,
                        ipa_id = this.ipaId,
                        ipa_name = this.ipaName,
                        depository_id = this.depositoryId,
                        depository_name = this.depositoryName,

                        isin_generation_request_doc_id = this.isinGenerationRequestDocId,
                        ipa_verification_request_doc_id = this.ipaVerificationRequestDocId,
                        ipa_certificate_doc_id = this.ipaCertificateDocId,
                        corporate_action_form_doc_id = this.corporateActionFormDocId,
                        allotment_letter_doc_id = this.allotmentLetterDocId,

                        last_modified = this.lastModifiedDate!!,
                        version = this.version!!,
                        modified_by = this.modifiedBy!!,
                        status = this.status!!
                )
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }
    }

    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, UniqueIdentifier>(
                AnyComposition(
                        IndiaCommercialPaperProgram.Clauses.Issue(),
                        IndiaCommercialPaperProgram.Clauses.AddIsinGenDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddIsin()
//                    IndiaCommercialPaperProgram.Clauses.AddIPAVerificationDoc(),
//                    IndiaCommercialPaperProgram.Clauses.AddIPACertifcateDoc(),
//                    IndiaCommercialPaperProgram.Clauses.AddCorpActionFormDoc(),
//                    IndiaCommercialPaperProgram.Clauses.AddAllotmentLetterDoc()
                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaperProgram.State, UniqueIdentifier>>
                    // Group by Program ID for in / out states
                    = tx.groupStates() { state -> state.linearId }
        }

        abstract class AbstractIndiaCPProgramClause : Clause<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, UniqueIdentifier>() {

            fun  verifyDocAsPerStatus(doc: Attachment, docId: String): Boolean {
                val docHashAndStatus = getDocHashAndStatus(docId)

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

            fun  verifyDocIsSignedByIssuer(doc: Attachment, docId: String?): Boolean {
                val docHashAndStatus = getDocHashAndStatus(docId)

                return verifyDoc(doc, docHashAndStatus.first, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_ISSUER)
            }

            fun  getDocHashAndStatus(docId: String?): Pair<SecureHash, IndiaCPDocumentDetails.DocStatusEnum> {
                if (docId == null || docId.isEmpty()) {
                    throw IndiaCPException(CPProgramError.DOC_UPLOAD_ERROR, Error.SourceEnum.DL_R3CORDA)
                }
                val docHashAndStatus = docId?.split(":")
                val docHash = SecureHash.parse(docHashAndStatus[0])
                val docStatus = IndiaCPDocumentDetails.DocStatusEnum.fromValue(docHashAndStatus[1])
                return Pair(docHash, docStatus)
            }

        }

        class Issue : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.Issue>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return setOf(command.value)
            }
        }

        class AddIsinGenDoc: AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsinGenDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsinGenDoc>()

                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()

                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("ISIN Request must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                    "the ISIN Request Document - Letter of Intent - has been attached" by (attachment.id.toString() == output.isinGenerationRequestDocId!!.split(":").first())
                    "the ISIN Request Document - Letter of Intent - matches the status as uploaded" by verifyDocAsPerStatus(attachment, output.isinGenerationRequestDocId)
                }
                return setOf(command.value)
            }
        }

        class AddIsin : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsin::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsin>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()
                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                    "the ISIN Request Document - Letter of Intent - has been signed & uploaded by the Issuer" by (verifyDocIsSignedByIssuer(attachment, output.isinGenerationRequestDocId))
                }

                return setOf(command.value)
            }
        }


//        class AddIPAVerificationDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
//                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
//                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
//        ) {
//            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPAVerification::class.java)
//
//            override fun verify(tx: TransactionForContract,
//                                inputs: List<IndiaCommercialPaperProgram.State>,
//                                outputs: List<IndiaCommercialPaperProgram.State>,
//                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
//                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
//                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
//                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPAVerification>()
//                val timestamp = tx.timestamp
//                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")
//
//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }
//
//                return consumedCommands
//            }
//        }
//
//        class AddIPACertifcateDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
//                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
//                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
//        ) {
//            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc::class.java)
//
//            override fun verify(tx: TransactionForContract,
//                                inputs: List<IndiaCommercialPaperProgram.State>,
//                                outputs: List<IndiaCommercialPaperProgram.State>,
//                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
//                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
//                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
//                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc>()
//                val timestamp = tx.timestamp
//                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")
//
//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }
//
//                return consumedCommands
//            }
//        }
//
//        class AddCorpActionFormDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
//                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
//                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
//        ) {
//            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc::class.java)
//
//            override fun verify(tx: TransactionForContract,
//                                inputs: List<IndiaCommercialPaperProgram.State>,
//                                outputs: List<IndiaCommercialPaperProgram.State>,
//                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
//                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
//                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
//                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc>()
//                val timestamp = tx.timestamp
//                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")
//
//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }
//
//                return consumedCommands
//            }
//        }
//
//        class AddAllotmentLetterDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
//                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
//                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
//        ) {
//            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddAllotmentLetterDoc::class.java)
//
//            override fun verify(tx: TransactionForContract,
//                                inputs: List<IndiaCommercialPaperProgram.State>,
//                                outputs: List<IndiaCommercialPaperProgram.State>,
//                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
//                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
//                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
//                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddAllotmentLetterDoc>()
//                val timestamp = tx.timestamp
//                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")
//
//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }
//
//                return consumedCommands
//            }
//        }

    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaperProgram.Commands
        class AddIsinGenDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddIsin() : IndiaCommercialPaperProgram.Commands
        class AddIPAVerification(ipaVerificationRequestDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddIPACertifcateDoc(ipaCertificateDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddCorpActionFormDoc(corporateActionFormDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddAllotmentLetterDoc(allotmentLetterDocId: String) : IndiaCommercialPaperProgram.Commands
    }

    /**
     * Returns a transaction that issues commercial paper, owned by the issuing parties key. Does not update
     * an existing transaction because you aren't able to issue multiple pieces of CP in a single transaction
     * at the moment: this restriction is not fundamental and may be lifted later.
     */
    fun generateIssue(cpProgramContractState: IndiaCommercialPaperProgram.State,
                      creditRatingStateRef: StateAndRef<CreditRating.State>,
                      boardResolutionStateRef: StateAndRef<BorrowingLimitBoardResolution.State>,
                      notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(creditRatingStateRef)
        tx.addInputState(boardResolutionStateRef)

        //Adding Outputs
        val creditRatingInputState = creditRatingStateRef.state.data
        val boardResolutionInputState = boardResolutionStateRef.state.data

        tx.addOutputState(
                cpProgramContractState.copy(
                        status = IndiaCPProgramStatusEnum.CP_PROGRAM_CREATED.name
                ),
                notary
        )
        tx.addOutputState(
                creditRatingInputState.copy(
                        version = creditRatingInputState.version!! + 1,
                        currentOutstandingCreditBorrowing = creditRatingInputState.currentOutstandingCreditBorrowing!!.copy (
                                quantity = cpProgramContractState.programSize.quantity + creditRatingInputState.currentOutstandingCreditBorrowing!!.quantity
                        )
                ),
                creditRatingStateRef.state.notary
        )
        tx.addOutputState(
                boardResolutionInputState.copy(
                        version = boardResolutionInputState.version!! + 1,
                        currentOutstandingCreditBorrowing = boardResolutionInputState.currentOutstandingCreditBorrowing!!.copy (
                                quantity = cpProgramContractState.programSize.quantity + boardResolutionInputState.currentOutstandingCreditBorrowing!!.quantity
                        )
                ),
                boardResolutionStateRef.state.notary
        )

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.Issue(), listOf(cpProgramContractState.issuer.owningKey))
        tx.addCommand(CreditRating.Commands.Amend(), listOf(cpProgramContractState.issuer.owningKey))
        tx.addCommand(BorrowingLimitBoardResolution.Commands.Amend(), listOf(cpProgramContractState.issuer.owningKey))

        return tx
    }

    fun generateTransactionWithISINDocAttachment (  cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>,
                                                    notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.ISIN_GEN_DOC_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.isinGenerationRequestDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIsinGenDoc(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                                                                                    cpProgramContractStateAndRef.state.data.depository.owningKey))

        return tx
    }

    fun  generateTransactionWithISIN(cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.ISIN_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.isinGenerationRequestDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIsin(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.depository.owningKey))

        return tx
    }



    /**
     * Returns a transaction that that updates the ISIN on to the CP Program.
     * It should also stamp the ISIN Generated proof document on to DL
     */
    fun addIsinToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, isin:String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
//        ptx.addInputState(indiaCPProgramSF)
//
//        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)
//
//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                isin  = isin,
//                status = status,
//                version = newVersion
//        ))

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addIPAVerificationDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, docDetails:IndiaCPDocumentDetails, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)

//        val docHash:String = docDetails[0].docHash
//
//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                ipaVerificationRequestDocId = docHash,
//                status = status,
//                version = newVersion
//        ))
//
//        for(doc in docDetails)
//        {
//            val docState = TransactionState(IndiaCommercialPaperProgram.DocState(indiaCPProgramSF.state.data.issuer, indiaCPProgramSF.state.data.programId, doc.docType.toString(), doc.docSubType,
//                    doc.docHash, doc.docStatus, doc.modifiedBy, Instant.now()), notary)
//
//
//            ptx.addOutputState(docState)
//        }

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addIPACertifcateDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, docDetails:IndiaCPDocumentDetails, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)

//        val docHash:String = docDetails[0].docHash
//
//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                ipaCertificateDocId = docHash,
//                status = status,
//                version = newVersion
//        ))
//
//        for(doc in docDetails)
//        {
//            val docState = TransactionState(IndiaCommercialPaperProgram.DocState(indiaCPProgramSF.state.data.issuer, indiaCPProgramSF.state.data.programId, doc.docType.toString(), doc.docSubType,
//                    doc.docHash, doc.docStatus, doc.modifiedBy, Instant.now()), notary)
//
//
//            ptx.addOutputState(docState)
//        }

        return ptx
    }


    /**
     * Returns a transaction that that updates the ISIN on to the CP Program.
     * It should also stamp the ISIN Generated proof document on to DL
     */
    fun addIsinGenDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, issuer: Party, notary: Party, docDetails:IndiaCPDocumentDetails, status:String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)

//        val docHash = docDetails[0].docHash
//
//
//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                isinGenerationRequestDocId = docHash,
//                status = status,
//                version = newVersion
//        ))
//
//        for(doc in docDetails)
//        {
//            val docState = TransactionState(IndiaCommercialPaperProgram.DocState(indiaCPProgramSF.state.data.issuer, indiaCPProgramSF.state.data.programId, doc.docType.toString(), doc.docSubType,
//                    doc.docHash, doc.docStatus, doc.modifiedBy, Instant.now()), notary)
//
//
//            ptx.addOutputState(docState)
//        }

        return ptx
    }

    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addCorpActionFormDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, docDetails:IndiaCPDocumentDetails, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)

//        val docHash = docDetails[0].docHash
//
//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                corporateActionFormDocId = docHash,
//                status = status,
//                version = newVersion
//        ))
//
//        for(doc in docDetails)
//        {
//            val docState = TransactionState(IndiaCommercialPaperProgram.DocState(indiaCPProgramSF.state.data.issuer, indiaCPProgramSF.state.data.programId, doc.docType.toString(), doc.docSubType,
//                    doc.docHash, doc.docStatus, doc.modifiedBy, Instant.now()), notary)
//
//
//            ptx.addOutputState(docState)
//        }

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addAllotmentLetterDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, docDetails:IndiaCPDocumentDetails, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version!!.toInt() + 1)

        val docHash = docDetails.docHash

//        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
//                allotmentLetterDocId = docHash,
//                status = status,
//                version = newVersion
//        ))
//
//        for(doc in docDetails)
//        {
//            val docState = TransactionState(IndiaCommercialPaperProgram.DocState(indiaCPProgramSF.state.data.issuer, indiaCPProgramSF.state.data.programId, doc.docType.toString(), doc.docSubType,
//                    doc.docHash, doc.docStatus, doc.modifiedBy, Instant.now()), notary)
//
//
//            ptx.addOutputState(docState)
//        }

        return ptx
    }

    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun createCPIssueWithinCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, issuer: Party, beneficiary: Party, ipa: Party, depository: Party, notary: Party, programAllocatedValue : Amount<Currency>, newCP: IndiaCPIssue, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = indiaCPProgramSF.state.data.version!!+ 1

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                programAllocatedValue = programAllocatedValue,
                status = status,
                version = newVersion
        ))

        val indiaCPState = TransactionState(IndiaCommercialPaper.State(issuer, beneficiary, ipa, depository,
                newCP.cpProgramId, newCP.cpTradeId, newCP.tradeDate, newCP.valueDate,
                (newCP.facevaluePerUnit * newCP.noOfUnits).DOLLARS `issued by` DUMMY_CASH_ISSUER, Instant.now() + newCP.maturityDays.days,
                newCP.isin, (if(newCP.version != null) Integer(newCP.version) else Integer(1)), newCP.dealConfirmationDocId,
                getSettlementDetails(newCP.issuerSettlementDetails), getSettlementDetails(newCP.investorSettlementDetails), getSettlementDetails(newCP.ipaSettlementDetails)
                ),
                notary)


        ptx.addOutputState(indiaCPState)

        return ptx
    }

    private fun  getSettlementDetails(settlementDetails: SettlementDetails?): IndiaCommercialPaper.SettlementDetails? {
        return IndiaCommercialPaper.SettlementDetails (
                partyType = settlementDetails?.partyType.toString(),
                paymentAccountDetails = getPaymentAccountDetails(settlementDetails?.paymentAccountDetails),
                depositoryAccountDetails = getDepositoryAccountDetails(settlementDetails?.depositoryAccountDetails)
        )
    }

    private fun  getDepositoryAccountDetails(depositoryAccountDetails: List<DepositoryAccountDetails>?): List<IndiaCommercialPaper.DepositoryAccountDetails>? {
        if (depositoryAccountDetails == null) {
            return null
        }
        val dpDetails = ArrayList<IndiaCommercialPaper.DepositoryAccountDetails>()
        for (dp in depositoryAccountDetails) {
            dpDetails.add(
                    IndiaCommercialPaper.DepositoryAccountDetails(
                            dpID = dp.dpId,
                            dpName = dp.dpName,
                            dpType = dp.dpType.toString(),
                            clientId = dp.clientId
                    )
            )
        }
        return dpDetails
    }

    private fun  getPaymentAccountDetails(paymentAccountDetails: PaymentAccountDetails?): IndiaCommercialPaper.PaymentAccountDetails {
        return IndiaCommercialPaper.PaymentAccountDetails (
                creditorName = paymentAccountDetails?.creditorName,
                bankAccountDetails = paymentAccountDetails?.bankAccountNo,
                bankName = paymentAccountDetails?.bankName,
                rtgsCode = paymentAccountDetails?.rtgsIfscCode
        )
    }

}



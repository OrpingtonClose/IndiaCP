package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.dto.IndiaCPProgramJSON
import com.barclays.indiacp.cordapp.schemas.IndiaCommercialPaperProgramSchemaV1
import net.corda.contracts.asset.sumCashBy
import net.corda.contracts.clause.AbstractIssue
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.Clause
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.random63BitValue
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.contracts.ICommercialPaperState
import net.corda.core.crypto.*
import java.security.PublicKey
import java.time.Instant
import java.util.*

/**
 * This is an ultra-trivial implementation of commercial paper, which is essentially a simpler version of a corporate
 * bond. It can be seen as a company-specific currency. A company issues CP with a particular face value, say $100,
 * but sells it for less, say $90. The paper can be redeemed for cash at a given date in the future. Thus this example
 * would have a 10% interest rate with a single repayment. Commercial paper is often rolled over (the maturity date
 * is adjusted as if the paper was redeemed and immediately repurchased, but without having to front the cash).
 *
 * This contract is not intended to realistically model CP. It is here only to act as a next step up above cash in
 * the prototyping phase. It is thus very incomplete.
 *
 * Open issues:
 *  - In this model, you cannot merge or split CP. Can you do this normally? We could model CP as a specialised form
 *    of cash, or reuse some of the cash code? Waiting on response from Ayoub and Rajar about whether CP can always
 *    be split/merged or only in secondary markets. Even if current systems can't do this, would it be a desirable
 *    feature to have anyway?
 *  - The funding steps of CP is totally ignored in this model.
 *  - No attention is paid to the existing roles of custodians, funding banks, etc.
 *  - There are regional variations on the CP concept, for instance, American CP requires a special "CUSIP number"
 *    which may need to be tracked. That, in turn, requires validation logic (there is a bean validator that knows how
 *    to do this in the Apache BVal project).
 */

val INDIA_CP_PROGRAM_ID = IndiaCommercialPaperProgram()

// TODO: Generalise the notion of an owned instrument into a superclass/supercontract. Consider composition vs inheritance.
class IndiaCommercialPaperProgram : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    data class Terms(
            val asset: Issued<Currency>,
            val maturityDate: Instant
    )

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaperProgram.Clauses.Group(), tx.commands.select<IndiaCommercialPaperProgram.Commands>())

    data class State(

            val issuer: Party,

            val ipa: Party,

            val depository: Party,

            val programId: String,

            val name: String,

            val type: String,

            val purpose: String,

            val issuerId: String,

            val issuerName: String,

            val issueCommencementDate: Instant,

            val programSize: Amount<Issued<Currency>>,

            val programAllocatedValue: Amount<Issued<Currency>>,

            val programCurrency: Currency,

            val maturityDate: Instant,

            val ipaId: String,

            val ipaName: String,

            val depositoryId: String,

            val depositoryName: String,

            val isin: String,

            val isinGenerationRequestDocId: String,

            val ipaVerificationRequestDocId: String,

            val ipaCertificateDocId: String,

            val corporateActionFormDocId: String,

            val allotmentLetterDocId: String,

            val status: String,

            val lastModified: Instant,

            val version: Integer

    ) : LinearState, QueryableState {

        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_PROGRAM_ID

//        val ref = program_id

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(programId)

        //Only the Issuer should be party to the full state of this transaction
        val parties: List<Party>
            get() = listOf(issuer)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        val token: Issued<IndiaCommercialPaperProgram.Terms>
            get() = Issued(issuer.ref(CPUtils.getReference(programId)), IndiaCommercialPaperProgram.Terms(programSize.token, maturityDate))

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

                        issuer_id = this.issuerId,

                       issuer_name = this.issuerName,

                        issue_commencement_date = this.issueCommencementDate,

                        program_size = this.programSize.quantity.toDouble(),

                        program_allocated_value = this.programAllocatedValue.quantity.toDouble(),

                        program_currency = this.programCurrency.symbol,

                       maturity_days = this.maturityDate,

                        ipa_id = this.ipaId,

                        ipa_name = this.ipaName,

                        depository_id = this.depositoryId,

                        depository_name = this.depositoryName,

                        isin = this.isin,

                        isin_generation_request_doc_id = this.isinGenerationRequestDocId,

                        ipa_verification_request_doc_id = this.ipaVerificationRequestDocId,

                        ipa_certificate_doc_id = this.ipaCertificateDocId,

                       corporate_action_form_doc_id = this.corporateActionFormDocId,

                        allotment_letter_doc_id = this.allotmentLetterDocId,

                        last_modified = this.lastModified,

                        version = this.version

                )
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }
    }


    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, Issued<IndiaCommercialPaperProgram.Terms>>(
                AnyComposition(
                        IndiaCommercialPaperProgram.Clauses.Issue(),
                        IndiaCommercialPaperProgram.Clauses.AddIsinGenDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddIsin(),
                        IndiaCommercialPaperProgram.Clauses.AddIPAVerification(),
                        IndiaCommercialPaperProgram.Clauses.AddIPACertifcateDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddCorpActionFormDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddAllotmentLetterDoc()
//
//                        class AddIPAVerification(ipaVerificationRequestDocId: String) : IndiaCommercialPaperProgram.Commands
//        class AddIPACertifcateDoc(ipaCertificateDocId: String) : IndiaCommercialPaperProgram.Commands
//        class AddCorpActionFormDoc(corporateActionFormDocId: String) : IndiaCommercialPaperProgram.Commands
//        class AddAllotmentLetterDoc(allotmentLetterDocId: String) : IndiaCommercialPaperProgram.Commands

                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaperProgram.State, Issued<IndiaCommercialPaperProgram.Terms>>>
                    = tx.groupStates<IndiaCommercialPaperProgram.State, Issued<IndiaCommercialPaperProgram.Terms>> { it.token }
        }

        class Issue : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.Issue>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddIsin : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsin::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsin>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddIsinGenDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsinGenDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsin>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }



        class AddIPAVerification : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPAVerification::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPAVerification>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddIPACertifcateDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddCorpActionFormDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class AddAllotmentLetterDoc : AbstractIssue<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, IndiaCommercialPaperProgram.Terms>(
                { map { Amount(it.programSize.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.programSize.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddAllotmentLetterDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperProgram.Terms>?): Set<IndiaCommercialPaperProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddAllotmentLetterDoc>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaperProgram.Commands
        data class Move(override val contractHash: SecureHash? = null) : FungibleAsset.Commands.Move, IndiaCommercialPaperProgram.Commands
        class Redeem : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddIsin(isin:String, isinGenerationRequestDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddIsinGenDoc(isinGenerationRequestDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddIPAVerification(ipaVerificationRequestDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddIPACertifcateDoc(ipaCertificateDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddCorpActionFormDoc(corporateActionFormDocId: String) : IndiaCommercialPaperProgram.Commands
        class AddAllotmentLetterDoc(allotmentLetterDocId: String) : IndiaCommercialPaperProgram.Commands
//        class Agree : TypeOnlyCommandData(), Commands  // Both sides agree to trade
//        class AddSettlementDetails(settlementDetails: SettlementDetails) : IndiaCommercialPaper.Commands
    }

    /**
     * Returns a transaction that issues commercial paper, owned by the issuing parties key. Does not update
     * an existing transaction because you aren't able to issue multiple pieces of CP in a single transaction
     * at the moment: this restriction is not fundamental and may be lifted later.
     */
    fun generateIssue( indiaCPProgram: IndiaCommercialPaperProgram.State, notary: Party): TransactionBuilder {

        val state = TransactionState(indiaCPProgram, notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(IndiaCommercialPaperProgram.Commands.Issue(), indiaCPProgram.issuer.owningKey))
    }

    /**
     * Returns a transaction that that updates the ISIN on to the CP Program.
     * It should also stamp the ISIN Generated proof document on to DL
     */
    fun addIsinToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, isin:String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                isin  = isin,
                status = status,
                version = newVersion
        ))

        return ptx
    }

    /**
     * Returns a transaction that that updates the ISIN on to the CP Program.
     * It should also stamp the ISIN Generated proof document on to DL
     */
    fun addIsinGenDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, isinGenerationRequestDocId: String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                isinGenerationRequestDocId = isinGenerationRequestDocId,
                status = status,
                version = newVersion
        ))

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addIPAVerificationDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, ipaVerificationRequestDocId: String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                ipaVerificationRequestDocId = ipaVerificationRequestDocId,
                status = status,
                version = newVersion
        ))

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addIPACertifcateDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, ipaCertificateDocId: String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                ipaCertificateDocId = ipaCertificateDocId,
                status = status,
                version = newVersion
        ))

        return ptx
    }

    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addCorpActionFormDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, corporateActionFormDocId: String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                corporateActionFormDocId = corporateActionFormDocId,
                status = status,
                version = newVersion
        ))

        return ptx
    }


    /**
     * Returns a transaction that that updates the IPA Verification Cert on to the CP Program.
     */
    fun addAllotmentLetterDocToCPProgram(indiaCPProgramSF: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party, allotmentLetterDocId: String, status: String): TransactionBuilder {

        val ptx = TransactionType.General.Builder(notary)
        ptx.addInputState(indiaCPProgramSF)

        val newVersion = Integer(indiaCPProgramSF.state.data.version.toInt() + 1)

        ptx.addOutputState(indiaCPProgramSF.state.data.copy(
                allotmentLetterDocId = allotmentLetterDocId,
                status = status,
                version = newVersion
        ))

        return ptx
    }



}



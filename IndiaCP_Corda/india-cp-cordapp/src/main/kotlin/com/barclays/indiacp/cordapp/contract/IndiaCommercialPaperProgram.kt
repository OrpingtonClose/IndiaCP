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
                        IndiaCommercialPaperProgram.Clauses.Issue()
//                          IndiaCommercialPaperProgram.Clauses.AddISINGenerationDocs()
//                        IndiaCommercialPaper.Clauses.Agree()
//                        IndiaCommercialPaperProgram.Clauses.AddSettlementDetails()
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

//        class AddSettlementDetails: Clause<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>() {
//            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.AddSettlementDetails::class.java)
//
//            override fun verify(tx: TransactionForContract,
//                                inputs: List<IndiaCommercialPaper.State>,
//                                outputs: List<IndiaCommercialPaper.State>,
//                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
//                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {
//                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.AddSettlementDetails>()
//                val input = inputs.single()
//                val timestamp = tx.timestamp
//                val time = timestamp?.before ?: throw IllegalArgumentException("AddSettlementDetails must be timestamped")
//                //Ownership is still with issuer so we should check with issuer key
//                requireThat { "the transaction is signed by the issuer of the CP" by (input.issuer.owningKey in command.signers)}
//
//                return setOf(command.value)
//            }
//        }

    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaperProgram.Commands
        data class Move(override val contractHash: SecureHash? = null) : FungibleAsset.Commands.Move, IndiaCommercialPaperProgram.Commands
        class Redeem : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
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

}



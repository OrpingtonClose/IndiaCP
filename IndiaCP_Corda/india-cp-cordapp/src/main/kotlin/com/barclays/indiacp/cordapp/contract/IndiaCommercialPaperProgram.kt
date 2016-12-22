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
            val maturityDate: Instant
    )

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaperProgram.Clauses.Group(), tx.commands.select<IndiaCommercialPaperProgram.Commands>())

    data class State(

            val issuer: Party,

            val ipa: Party,

            val depository: Party,

            val program_id: String,

            val name: String,

            val type: String,

            val purpose: String,

            val issuer_id : String,

            val issuer_name : String,

            val issue_commencement_date: Instant,

            val program_size: Double,

            val program_allocated_value: Double,

            val program_currency: String,

            val maturity_days: Instant,

            val ipa_id: String,

            val ipa_name: String,

            val depository_id: String,

            val depository_name: String,

            val isin_generation_request_doc_id: String,

            val ipa_verification_request_doc_id: String,

            val ipa_certificate_doc_id: String,

            val corporate_action_form_doc_id: String,

            val allotment_letter_doc_id: String,

            val last_modified : Instant,

            val version: Integer

    ) : LinearState, QueryableState {

        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_PROGRAM_ID

//        val ref = program_id

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(program_id)

        //Only the Issuer should be party to the full state of this transaction
        val parties: List<Party>
            get() = listOf(issuer)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        val token: Issued<IndiaCommercialPaperProgram.Terms>
            get() = Issued(issuer.ref(), IndiaCommercialPaperProgram.Terms(maturity_days))

        override fun toString() = "${Emoji.newspaper}IndiaCommercialPaperProgram(of $program_size issued by '$issuer' on '$issue_commencement_date' with a maturity period of '$maturity_days')"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(IndiaCommercialPaperProgramSchemaV1)

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is IndiaCommercialPaperProgramSchemaV1 -> IndiaCommercialPaperProgramSchemaV1.PersistentIndiaCommericalPaperProgramState(

                        issuanceParty = this.issuer.owningKey.toBase58String(),

                        ipaParty = this.ipa.owningKey.toBase58String(),

                        depositoryParty = this.depository.owningKey.toBase58String(),

                        program_id = this.program_id,

                        name = this.name,

                        type = this.type,

                        purpose = this.purpose,

                        issuer_id = this.issuer_id,

                       issuer_name = this.issuer_name,

                        issue_commencement_date = this.issue_commencement_date,

                        program_size = this.program_size,

                        program_allocated_value = this.program_allocated_value,

                        program_currency = this.program_currency,

                       maturity_days = this.maturity_days,

                        ipa_id = this.ipa_id,

                        ipa_name = this.ipa_name,

                        depository_id = this.depository_id,

                        depository_name = this.depository_name,

                        isin_generation_request_doc_id = this.isin_generation_request_doc_id,

                        ipa_verification_request_doc_id = this.ipa_verification_request_doc_id,

                        ipa_certificate_doc_id = this.ipa_certificate_doc_id,

                       corporate_action_form_doc_id = this.corporate_action_form_doc_id,

                        allotment_letter_doc_id = this.allotment_letter_doc_id,

                        last_modified = this.last_modified,

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
                { map { Amount(it.program_allocated_value.toLong(), it.token) }.sumOrThrow() },
                { token -> map { Amount(it.program_allocated_value.toLong(), it.token) }.sumOrZero(token) }
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

                require(outputs.all { time < it.maturity_days }) { "maturity date is not in the past" }

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
    fun generateIssue( notary: Party, issuer: Party, ipa: Party, depository: Party,

                       indiaCPProgramJSON : IndiaCPProgramJSON): TransactionBuilder {

        val state = TransactionState(IndiaCommercialPaperProgram.State(issuer, ipa, depository,
                indiaCPProgramJSON.program_id, indiaCPProgramJSON.name, indiaCPProgramJSON.type, indiaCPProgramJSON.purpose,
                indiaCPProgramJSON.issuer_id, indiaCPProgramJSON.issuer_name,
                indiaCPProgramJSON.issue_commencement_date.toInstant(), indiaCPProgramJSON.program_size,
                indiaCPProgramJSON.program_allocated_value, indiaCPProgramJSON.program_currency,
                indiaCPProgramJSON.maturity_days.toInstant(), indiaCPProgramJSON.ipa_id, indiaCPProgramJSON.ipa_name,
                indiaCPProgramJSON.depository_id, indiaCPProgramJSON.depository_name, indiaCPProgramJSON.isin_generation_request_doc_id,
                indiaCPProgramJSON.ipa_verification_request_doc_id,
                indiaCPProgramJSON.ipa_certificate_doc_id, indiaCPProgramJSON.corporate_action_form_doc_id,
                indiaCPProgramJSON.allotment_letter_doc_id, Instant.now(), Integer(0)
        ), notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(IndiaCommercialPaperProgram.Commands.Issue(), issuer.owningKey))
    }

}



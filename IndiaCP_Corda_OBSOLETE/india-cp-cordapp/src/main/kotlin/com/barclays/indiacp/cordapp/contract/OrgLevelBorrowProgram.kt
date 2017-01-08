package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.OrgLevelBorrowerSchemaV1
import net.corda.contracts.clause.AbstractIssue
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.core.crypto.*
import net.corda.core.random63BitValue
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

val OrgLevelBorrowProgram_ID = OrgLevelBorrowProgram()


// TODO: Generalise the notion of an owned instrument into a superclass/supercontract. Consider composition vs inheritance.
class OrgLevelBorrowProgram : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    data class Terms(
            val asset: Issued<Currency>
    )

    override fun verify(tx: TransactionForContract) = verifyClause(tx, OrgLevelBorrowProgram.Clauses.Group(), tx.commands.select<OrgLevelBorrowProgram.Commands>())



        //Adding Document Audit Schema so that we are able to keep Audit for all our document
        data class OrgState(

                val issuer: Party,

                var orgId: String,

                var name: String,

                var purpose: String,

                var commencementDate: Instant,

                var borrowingLimit: Amount<Issued<Currency>>,

                var borrowedValue: Amount<Issued<Currency>>,

                var programCurrency: String,

                var status: String,

                var modifiedBy: String,

                var lastModified: Instant,

                var version: Integer

        ): QueryableState, LinearState
        {

            override val contract = OrgLevelBorrowProgram_ID

            override val linearId: UniqueIdentifier
                get() = UniqueIdentifier(orgId)


            override val participants: List<CompositeKey>
                get() = listOf(issuer).map { it.owningKey }

            val token: Issued<OrgLevelBorrowProgram.Terms>
                get() = Issued(issuer.ref(CPUtils.getReference(orgId)), OrgLevelBorrowProgram.Terms((borrowingLimit).token))

            override fun toString() = "${Emoji.newspaper}OrgLevelBorrowProgram object [ORG ID : $orgId, " + "])"

            //Only the Issuer should be party to the full state of this transaction
            val parties: List<Party>
                get() = listOf(issuer)

            override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
                return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
            }

            /** Object Relational Mapping support. */
            override fun supportedSchemas(): Iterable<MappedSchema> = listOf(OrgLevelBorrowerSchemaV1)

            /** Object Relational Mapping support. */
            override fun generateMappedObject(schema: MappedSchema): PersistentState {
                return when (schema) {
                    is OrgLevelBorrowerSchemaV1 -> OrgLevelBorrowerSchemaV1.PersistentOrgLevelBorrowerSchemaState(
                            issuanceParty = this.issuer.owningKey.toBase58String(),

                            orgId = this.orgId,

                            name = this.name,

                            purpose = this.purpose,

                            commencementDate = this.commencementDate,

                            borrowingLimit = this.borrowingLimit.quantity.toDouble(),

                            borrowedValue = this.borrowedValue.quantity.toDouble(),

                            programCurrency = this.programCurrency,

                            status = this.status,

                            lastModified = this.lastModified,

                            modifiedBy = this.modifiedBy,

                            version = this.version
                    )
                    else -> throw IllegalArgumentException("Unrecognised schema $schema")
                }
            }
        }



    interface Clauses {
        class Group : GroupClauseVerifier<OrgLevelBorrowProgram.OrgState, OrgLevelBorrowProgram.Commands, Issued<OrgLevelBorrowProgram.Terms>>(
                AnyComposition(
                        OrgLevelBorrowProgram.Clauses.AddDocumentAudit(),
                        OrgLevelBorrowProgram.Clauses.Issue()

                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<OrgLevelBorrowProgram.OrgState, Issued<OrgLevelBorrowProgram.Terms>>>
                    = tx.groupStates<OrgLevelBorrowProgram.OrgState, Issued<OrgLevelBorrowProgram.Terms>> { it.token }
        }

        class AddDocumentAudit : AbstractIssue<OrgLevelBorrowProgram.OrgState, OrgLevelBorrowProgram.Commands, OrgLevelBorrowProgram.Terms>(
                { map { Amount(100, it.token) }.sumOrThrow() },
                { token -> map { Amount(100, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(OrgLevelBorrowProgram.Commands.AddDocumentAuditLog::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<OrgLevelBorrowProgram.OrgState>,
                                outputs: List<OrgLevelBorrowProgram.OrgState>,
                                commands: List<AuthenticatedObject<OrgLevelBorrowProgram.Commands>>,
                                groupingKey: Issued<OrgLevelBorrowProgram.Terms>?): Set<OrgLevelBorrowProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<OrgLevelBorrowProgram.Commands.AddDocumentAuditLog>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }

        class Issue : AbstractIssue<OrgLevelBorrowProgram.OrgState, OrgLevelBorrowProgram.Commands, OrgLevelBorrowProgram.Terms>(
                { map { Amount(it.borrowingLimit.quantity, it.token) }.sumOrThrow() },
                { token -> map { Amount(it.borrowingLimit.quantity, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(OrgLevelBorrowProgram.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<OrgLevelBorrowProgram.OrgState>,
                                outputs: List<OrgLevelBorrowProgram.OrgState>,
                                commands: List<AuthenticatedObject<OrgLevelBorrowProgram.Commands>>,
                                groupingKey: Issued<OrgLevelBorrowProgram.Terms>?): Set<OrgLevelBorrowProgram.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<OrgLevelBorrowProgram.Commands.Issue>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                //There is no maturity date for an organisation.
//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }


    }

    interface Commands : CommandData {
        class AddDocumentAuditLog() : OrgLevelBorrowProgram.Commands
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, OrgLevelBorrowProgram.Commands
    }

    /**
     * Single method for creating a document row for audit purpose.
     * Idea is that we have it same for both CP Program and CP ISSUE.
     *
     */
    fun createDocumentAuditLog(docStateObj: OrgLevelBorrowProgram.OrgState, issuer: Party, notary: Party): TransactionBuilder {

        val state = TransactionState(docStateObj, notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(OrgLevelBorrowProgram.Commands.AddDocumentAuditLog(), issuer.owningKey))
    }

    /**
     * Returns a transaction that issues commercial paper, owned by the issuing parties key. Does not update
     * an existing transaction because you aren't able to issue multiple pieces of CP in a single transaction
     * at the moment: this restriction is not fundamental and may be lifted later.
     */
    fun generateIssue( orgProgram: OrgLevelBorrowProgram.OrgState, notary: Party): TransactionBuilder {

        val state = TransactionState(orgProgram, notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(OrgLevelBorrowProgram.Commands.Issue(), orgProgram.issuer.owningKey))
    }

}



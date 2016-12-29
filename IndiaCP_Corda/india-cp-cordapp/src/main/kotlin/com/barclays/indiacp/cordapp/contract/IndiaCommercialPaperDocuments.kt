package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.DocumentAuditSchemaV1
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
import net.corda.contracts.asset.DUMMY_CASH_ISSUER
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

val INDIA_CP_DOCUMENTS_ID = IndiaCommercialPaperDocuments()

val INDIA_CP_DOCUMENT_AUDIT_ID = "INDIA_CP_DOCUMENT_AUDIT_ID"

// TODO: Generalise the notion of an owned instrument into a superclass/supercontract. Consider composition vs inheritance.
class IndiaCommercialPaperDocuments : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    data class Terms(
            val asset: Issued<Currency>
    )

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaperDocuments.Clauses.Group(), tx.commands.select<IndiaCommercialPaperDocuments.Commands>())



    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaperDocuments.DocState, IndiaCommercialPaperDocuments.Commands, Issued<IndiaCommercialPaperDocuments.Terms>>(
                AnyComposition(
                        IndiaCommercialPaperDocuments.Clauses.AddDocumentAudit()


                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaperDocuments.DocState, Issued<IndiaCommercialPaperDocuments.Terms>>>
                    = tx.groupStates<IndiaCommercialPaperDocuments.DocState, Issued<IndiaCommercialPaperDocuments.Terms>> { it.token }
        }

        class AddDocumentAudit : AbstractIssue<IndiaCommercialPaperDocuments.DocState, IndiaCommercialPaperDocuments.Commands, IndiaCommercialPaperDocuments.Terms>(
                { map { Amount(100, it.token) }.sumOrThrow() },
                { token -> map { Amount(100, it.token) }.sumOrZero(token) }
        ) {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperDocuments.Commands.AddDocumentAuditLog::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperDocuments.DocState>,
                                outputs: List<IndiaCommercialPaperDocuments.DocState>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperDocuments.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaperDocuments.Terms>?): Set<IndiaCommercialPaperDocuments.Commands> {
                val consumedCommands = super.verify(tx, inputs, outputs, commands, groupingKey)
                commands.requireSingleCommand<IndiaCommercialPaperDocuments.Commands.AddDocumentAuditLog>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

//                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return consumedCommands
            }
        }
    }


        //Adding Document Audit Schema so that we are able to keep Audit for all our document
        data class DocState(

                val issuer: Party,

                var cpProgramID: String,

                var docType: String,

                var docSubType:String,


                var docHash: String,


                var doc_status: String,


                var modifiedBy: String,


                var lastModified: Instant
        ): QueryableState, LinearState
        {

            override val contract = INDIA_CP_DOCUMENTS_ID

            override val linearId: UniqueIdentifier
                get() = UniqueIdentifier(cpProgramID)


            override val participants: List<CompositeKey>
                get() = listOf(issuer).map { it.owningKey }

            val token: Issued<IndiaCommercialPaperDocuments.Terms>
                get() = Issued(issuer.ref(CPUtils.getReference(INDIA_CP_DOCUMENT_AUDIT_ID)), IndiaCommercialPaperDocuments.Terms((100.DOLLARS `issued by` DUMMY_CASH_ISSUER).token))

            override fun toString() = "${Emoji.newspaper}DocumentAuditState object [PROGRAM_ID : $cpProgramID, " +
//                    "TRADE_ID: $cpTradeID, " +
                    "DOC_TYPE: $docType ])"

            //Only the Issuer should be party to the full state of this transaction
            val parties: List<Party>
                get() = listOf(issuer)

            override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
                return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
            }

            /** Object Relational Mapping support. */
            override fun supportedSchemas(): Iterable<MappedSchema> = listOf(DocumentAuditSchemaV1)

            /** Object Relational Mapping support. */
            override fun generateMappedObject(schema: MappedSchema): PersistentState {
                return when (schema) {
                    is DocumentAuditSchemaV1 -> DocumentAuditSchemaV1.PersistentDocumentAuditSchemaState(
                            cpProgramID = this.cpProgramID,
                            cpTradeID = "",
                            doc_status = this.doc_status,
                            docHash = this.docHash,
                            docSubType = this.docSubType,
                            docType = this.docType,
                            lastModified = this.lastModified,
                            modifiedBy = this.modifiedBy
                    )
                    else -> throw IllegalArgumentException("Unrecognised schema $schema")
                }
            }
        }



    interface Commands : CommandData {
        class AddDocumentAuditLog() : IndiaCommercialPaperDocuments.Commands
    }

    /**
     * Single method for creating a document row for audit purpose.
     * Idea is that we have it same for both CP Program and CP ISSUE.
     *
     */
    fun createDocumentAuditLog(docStateObj: IndiaCommercialPaperDocuments.DocState, issuer: Party, notary: Party): TransactionBuilder {

        val state = TransactionState(docStateObj, notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(IndiaCommercialPaperDocuments.Commands.AddDocumentAuditLog(), issuer.owningKey))
    }


}



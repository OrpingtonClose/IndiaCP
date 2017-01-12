package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.CreditRatingSchemaV1
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.Clause
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import sun.misc.CEStreamExhausted
import java.security.PublicKey
import java.util.*

val CREDIT_RATING_ID = CreditRating()

/**
 * This is the Smart Contract to manage the Credit Rating Document Issued by the Credit Rating Agency that provides
 * the approved Credit Limits for Short Term Borrowing instruments like Commercial Paper
 *
 * Created by ritukedia
 */
class CreditRating : Contract, LegalEntityDocumentContract {
    //override this with actual CR Document Hash at the time of Contract Issue
    //ICRA is the Credit Rating Agency for India Commercial Papers for Barclays
    override val legalContractReference: SecureHash = SecureHash.sha256("http://www.icra.in/Files/Articles/RM-CommPapers.pdf")

    override fun verify(tx: TransactionForContract) = verifyClause(tx, CreditRating.Clauses.Group(), tx.commands.select<CreditRating.Commands>())

    data class State(
            override val issuer: Party,
            val owner: CompositeKey = issuer.owningKey,
            val creditRatingAgencyName: String,
            val creditRatingAmount: Amount<Currency>,
            val currency: Currency,
            val currentOutstandingCreditBorrowing: Amount<Currency>? = Amount(0, currency),
            val creditRating: String,
            val creditRatingIssuanceDate: Date,
            val creditRatingEffectiveDate: Date,
            val creditRatingExpiryDate: Date,
            override val docHash: String,
            val modifiedBy: String,
            val lastModifiedDate: Date? = Date(),
            val version: Int? = 1,
            val status: String? = ModelUtils.DocumentStatus.ACTIVE.name
    ) : LegalEntityDocumentOwnableState, LinearState, QueryableState {

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return participants.any { ck -> ck.containsAny(ourKeys) }
        }

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(issuer.name)

        override val contract = BORROWING_LIMIT_BOARD_RESOLUTION_ID
        override val participants: List<CompositeKey>
            get() = listOf(owner)

        //override fun withNewOwner(newOwner: CompositeKey): Pair<CommandData, OwnableState> = throw IllegalStateException()
        override fun toString() = "${Emoji.newspaper}Credit Rating Document (issued by $creditRatingAgencyName, applicable from $creditRatingEffectiveDate, expiring on $creditRatingExpiryDate for '$issuer', owned by ${owner.toString()})"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(CreditRatingSchemaV1)

        /** This will be used as a grouping key. */
        val token = owner.toBase58String()

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is CreditRatingSchemaV1 -> CreditRatingSchemaV1.PersistentCreditRatingState(
                        issuanceParty = this.issuer.name,
                        owner = this.owner.toBase58String(),
                        creditRatingAgencyName = this.creditRatingAgencyName,
                        creditRatingAmount = this.creditRatingAmount.quantity,
                        currency = this.creditRatingAmount.token.symbol,
                        currentOutstandingCreditBorrowing = this.currentOutstandingCreditBorrowing!!.quantity,
                        creditRating = this.creditRating,
                        creditRatingIssuanceDate = this.creditRatingIssuanceDate,
                        creditRatingEffectiveDate = this.creditRatingEffectiveDate,
                        creditRatingExpiryDate = this.creditRatingExpiryDate,
                        modifiedBy = this.modifiedBy,
                        lastModifiedDate = this.lastModifiedDate!!,
                        version = this.version!!,
                        status = this.status!!
                )
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }

    }

    interface Clauses {
        class Group : GroupClauseVerifier<CreditRating.State, CreditRating.Commands, String>(
                AnyComposition(
                        CreditRating.Clauses.Issue(),
                        CreditRating.Clauses.Amend(),
                        CreditRating.Clauses.Cancel())) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<CreditRating.State, String>>
                    = tx.groupStates<CreditRating.State, String> { it.token }
        }

        class Issue: Clause<CreditRating.State, CreditRating.Commands, String>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(CreditRating.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<CreditRating.State>,
                                outputs: List<CreditRating.State>,
                                commands: List<AuthenticatedObject<CreditRating.Commands>>,
                                groupingKey: String?): Set<CreditRating.Commands> {

                val command = commands.requireSingleCommand<CreditRating.Commands.Issue>()

                val output = outputs.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("Credit Rating Issuance must be timestamped")

                //TODO: verify signature on the credit rating document

                requireThat {
                    "the credit rating has not expired" by (time <= output.creditRatingExpiryDate.toInstant())
                    "the transaction is signed by the owner of the Credit Rating Document" by (output.owner in command.signers)
                    "the state is propagated" by (outputs.size == 1)
                }
                return setOf(command.value)
            }
        }

        class Amend: Clause<CreditRating.State, CreditRating.Commands, String>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(CreditRating.Commands.Amend::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<CreditRating.State>,
                                outputs: List<CreditRating.State>,
                                commands: List<AuthenticatedObject<CreditRating.Commands>>,
                                groupingKey: String?): Set<CreditRating.Commands> {

                val command = commands.requireSingleCommand<CreditRating.Commands.Amend>()

                val output = outputs.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("Credit Rating Amendment must be timestamped")

                //TODO: verify signature on the credit rating document

                requireThat {
                    "the credit rating has not expired" by (time <= output.creditRatingExpiryDate.toInstant())
                    "the transaction is signed by the owner of the Credit Rating Document" by (output.owner in command.signers)
                    "the state is propagated" by (outputs.size == 1)
                }
                return setOf(command.value)
            }
        }

        class Cancel(): Clause<CreditRating.State, CreditRating.Commands, String>()  {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(CreditRating.Commands.Cancel::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<CreditRating.State>,
                                outputs: List<CreditRating.State>,
                                commands: List<AuthenticatedObject<CreditRating.Commands>>,
                                groupingKey: String?): Set<CreditRating.Commands> {

                val command = commands.requireSingleCommand<CreditRating.Commands.Cancel>()

                val input = inputs.single()

                requireThat {
                    "the credit rating must be cancelled" by outputs.isEmpty()
                    "the transaction is signed by the owner of the credit rating" by (input.owner in command.signers)
                }

                return setOf(command.value)
            }

        }
    }

    interface Commands : CommandData {
        class Issue : TypeOnlyCommandData(), CreditRating.Commands
        class Amend : TypeOnlyCommandData(), CreditRating.Commands
        class Cancel : TypeOnlyCommandData(), CreditRating.Commands
    }

    /**
     * Returns a transaction that issues credit rating, owned by the issuing parties key.
     */
    fun generateIssue(contractState: LegalEntityDocumentOwnableState, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        if (contractState is CreditRating.State) {
            val state = TransactionState(contractState, notary)
            tx.withItems(state, Command(CreditRating.Commands.Issue(), contractState.owner))
        }
        return tx
    }

    /**
     * Returns a transaction that amends an existing credit rating, owned by the issuing parties key.
     */
    fun generateAmend(currentCRRef: StateAndRef<State>, amendedCRState: LegalEntityDocumentOwnableState, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        if (amendedCRState is CreditRating.State) {
            tx.addInputState(currentCRRef)
            val newVersion = currentCRRef.state.data.version!! + 1
            val currentOutstandingCreditBorrowing = currentCRRef.state.data.currentOutstandingCreditBorrowing
            tx.addOutputState(
                    amendedCRState.copy(status = ModelUtils.DocumentStatus.ACTIVE.name,
                                        version = newVersion,
                                        currentOutstandingCreditBorrowing = (currentOutstandingCreditBorrowing!! + amendedCRState.currentOutstandingCreditBorrowing!!)),
                    currentCRRef.state.notary
            )
            tx.addCommand(Commands.Amend(), listOf(amendedCRState.owner))
        }
        return tx
    }

    /**
     * Returns a transaction that cancels(consumes/extinguishes) an existing credit rating, owned by the issuing parties key.
     */
    fun generateCancel(currentCRRef: StateAndRef<State>, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        //Adding only input without the new output will extinguish this Credit Rating State i.e. mark it as Consumed
        tx.addInputState(currentCRRef)
        tx.addCommand(Commands.Cancel(), listOf(currentCRRef.state.data.owner))
        return tx;
    }
}



package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.BorrowingLimitBoardResolutionSchema
import com.barclays.indiacp.cordapp.schemas.BorrowingLimitBoardResolutionSchemaV1
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
import java.util.*

val BORROWING_LIMIT_BOARD_RESOLUTION_ID = BorrowingLimitBoardResolution()

class BorrowingLimitBoardResolution : Contract, LegalEntityDocumentContract {
    //override this with actual BR Document Hash at the time of Contract Issue
    override val legalContractReference: SecureHash = SecureHash.sha256("http://www.icra.in/Files/Articles/RM-CommPapers.pdf")

    override fun verify(tx: TransactionForContract) = verifyClause(tx, BorrowingLimitBoardResolution.Clauses.Group(), tx.commands.select<BorrowingLimitBoardResolution.Commands>())

    data class State(
            override val issuer: Party,
            override val owner: CompositeKey,
            val boardResolutionBorrowingLimit: Integer,
            val boardResolutionIssuanceDate: Date,
            val boardResolutionExpiryDate: Date,
            override val docHash: String,
            val modifiedBy: String,
            val lastModifiedDate: Date? = Date(),
            val version: Integer? = Integer(1),
            val status: String? = ModelUtils.DocumentStatus.ACTIVE.name
    ) : LegalEntityDocumentOwnableState, OwnableState, QueryableState {
        override val contract = BORROWING_LIMIT_BOARD_RESOLUTION_ID
        override val participants: List<CompositeKey>
            get() = listOf(owner)

        override fun withNewOwner(newOwner: CompositeKey): Pair<CommandData, OwnableState> = throw IllegalStateException()
        override fun toString() = "${Emoji.newspaper}Borrowing Limit Board Resolution (issued by ${issuer.name}, applicable from $boardResolutionIssuanceDate, expiring on $boardResolutionExpiryDate for '$issuer', owned by ${owner.toString()})"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(BorrowingLimitBoardResolutionSchemaV1)

        /** This will be used as a grouping key. */
        val token = owner.toBase58String()

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is BorrowingLimitBoardResolutionSchemaV1 -> BorrowingLimitBoardResolutionSchemaV1.PersistentBorrowingLimitBRState(
                        issuanceParty = this.issuer.name,
                        owner = this.owner.toBase58String(),
                        boardResolutionBorrowingLimit = this.boardResolutionBorrowingLimit,
                        boardResolutionIssuanceDate = this.boardResolutionIssuanceDate,
                        boardResolutionExpiryDate = this.boardResolutionExpiryDate,
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
        class Group : GroupClauseVerifier<BorrowingLimitBoardResolution.State, BorrowingLimitBoardResolution.Commands, String>(
                AnyComposition(
                        BorrowingLimitBoardResolution.Clauses.Issue(),
                        BorrowingLimitBoardResolution.Clauses.Amend(),
                        BorrowingLimitBoardResolution.Clauses.Cancel())) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<BorrowingLimitBoardResolution.State, String>>
                    = tx.groupStates<BorrowingLimitBoardResolution.State, String> { it.token }
        }

        class Issue: Clause<BorrowingLimitBoardResolution.State, BorrowingLimitBoardResolution.Commands, String>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(BorrowingLimitBoardResolution.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<BorrowingLimitBoardResolution.State>,
                                outputs: List<BorrowingLimitBoardResolution.State>,
                                commands: List<AuthenticatedObject<BorrowingLimitBoardResolution.Commands>>,
                                groupingKey: String?): Set<BorrowingLimitBoardResolution.Commands> {

                val command = commands.requireSingleCommand<BorrowingLimitBoardResolution.Commands.Issue>()

                val output = outputs.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("Board Resolution Issuance must be timestamped")

                //TODO: verify signature on the board resolution document

                requireThat {
                    "the board resolution has not expired" by (time <= output.boardResolutionExpiryDate.toInstant())
                    "the transaction is signed by the owner of the Board Resolution Document" by (output.owner in command.signers)
                    "the state is propagated" by (outputs.size == 1)
                }
                return setOf(command.value)
            }
        }

        class Amend: Clause<BorrowingLimitBoardResolution.State, BorrowingLimitBoardResolution.Commands, String>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(BorrowingLimitBoardResolution.Commands.Amend::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<BorrowingLimitBoardResolution.State>,
                                outputs: List<BorrowingLimitBoardResolution.State>,
                                commands: List<AuthenticatedObject<BorrowingLimitBoardResolution.Commands>>,
                                groupingKey: String?): Set<BorrowingLimitBoardResolution.Commands> {

                val command = commands.requireSingleCommand<BorrowingLimitBoardResolution.Commands.Amend>()

                val output = outputs.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("Board Resolution Amendment must be timestamped")

                //TODO: verify signature on the board resolution document

                requireThat {
                    "the board resolution has not expired" by (time <= output.boardResolutionExpiryDate.toInstant())
                    "the transaction is signed by the owner of the Board Resolution Document" by (output.owner in command.signers)
                    "the state is propagated" by (outputs.size == 1)
                }
                return setOf(command.value)
            }
        }

        class Cancel(): Clause<BorrowingLimitBoardResolution.State, BorrowingLimitBoardResolution.Commands, String>()  {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(BorrowingLimitBoardResolution.Commands.Cancel::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<BorrowingLimitBoardResolution.State>,
                                outputs: List<BorrowingLimitBoardResolution.State>,
                                commands: List<AuthenticatedObject<BorrowingLimitBoardResolution.Commands>>,
                                groupingKey: String?): Set<BorrowingLimitBoardResolution.Commands> {

                val command = commands.requireSingleCommand<BorrowingLimitBoardResolution.Commands.Cancel>()

                val input = inputs.single()

                requireThat {
                    "the board resolution must be cancelled" by outputs.isEmpty()
                    "the transaction is signed by the owner of the board resolution" by (input.owner in command.signers)
                }

                return setOf(command.value)
            }

        }
    }

    interface Commands : CommandData {
        class Issue : TypeOnlyCommandData(), BorrowingLimitBoardResolution.Commands
        class Amend : TypeOnlyCommandData(), BorrowingLimitBoardResolution.Commands
        class Cancel : TypeOnlyCommandData(), BorrowingLimitBoardResolution.Commands
    }

    /**
     * Returns a transaction that issues board resolution, owned by the issuing parties key.
     */
    fun generateIssue(contractState: LegalEntityDocumentOwnableState, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        if (contractState is BorrowingLimitBoardResolution.State) {
            val state = TransactionState(contractState, notary)
            tx.withItems(state, Command(BorrowingLimitBoardResolution.Commands.Issue(), contractState.owner))
        }
        return tx
    }

    /**
     * Returns a transaction that amends an existing board resolution, owned by the issuing parties key.
     */
    fun generateAmend(currentBRRef: StateAndRef<State>, amendedBRState: LegalEntityDocumentOwnableState, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        if (amendedBRState is BorrowingLimitBoardResolution.State) {
            tx.addInputState(currentBRRef)
            val newVersion = Integer(currentBRRef.state.data.version!!.toInt() + 1)
            tx.addOutputState(
                    amendedBRState.copy(status = ModelUtils.DocumentStatus.ACTIVE.name, version = newVersion),
                    currentBRRef.state.notary
            )
            tx.addCommand(Commands.Amend(), listOf(amendedBRState.owner))
        }
        return tx
    }

    /**
     * Returns a transaction that cancels(consumes/extinguishes) an existing board resolution, owned by the issuing parties key.
     */
    fun generateCancel(currentBRRef: StateAndRef<State>, notary: Party): TransactionBuilder {
        val tx = TransactionType.General.Builder(notary = notary)
        //Adding only input without the new output will extinguish this Board resolution State i.e. mark it as Consumed
        tx.addInputState(currentBRRef)
        tx.addCommand(Commands.Cancel(), listOf(currentBRRef.state.data.owner))
        return tx;
    }
}



package com.barclays.indiacp.cordapp.contract

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
import com.barclays.indiacp.cordapp.schemas.IndiaCommercialPaperSchemaV1
import com.barclays.indiacp.cordapp.schemas.StandardSettlementSchema
import com.barclays.indiacp.cordapp.schemas.StandardSettlementSchemaV1
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.core.crypto.*
import java.security.PublicKey
import java.time.Instant
import java.util.*
import javax.print.attribute.IntegerSyntax

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
            val ipa: Party,
            val depository: Party,
            val cpProgramID: String,
            val cpTradeID: String,
            val tradeDate: String,
            val valueDate: String,
            val faceValue: Amount<Issued<Currency>>,
            val maturityDate: Instant,
            var isin: String,
            var version: Integer = Integer(1),
            var hashDealConfirmationDoc: String? = null,
            var issuerSettlementDetails: SettlementDetails? = null,
            var investorSettlementDetails: SettlementDetails? = null
    ) : DealState, QueryableState {
        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_ID

        override val ref = cpTradeID

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(cpTradeID)

        //Only the Issuer and Investor should be party to the full state of this transaction
        override val parties: List<Party>
            get() = listOf(issuer, beneficiary)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, beneficiary, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        override fun generateAgreement(notary: Party): TransactionBuilder {
            throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        val token: Issued<IndiaCommercialPaper.Terms>
            get() = Issued(issuer.ref(CPUtils.getReference(ref)), IndiaCommercialPaper.Terms(faceValue.token, maturityDate))

        override fun toString() = "${Emoji.newspaper}CommercialPaper($cpProgramID:$cpTradeID of $faceValue redeemable on $maturityDate by '$issuer', owned by ${beneficiary.owningKey.toString()})"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(IndiaCommercialPaperSchemaV1, StandardSettlementSchemaV1)

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is IndiaCommercialPaperSchemaV1 -> IndiaCommercialPaperSchemaV1.PersistentIndiaCommericalPaperState(
                        issuanceParty = this.issuer.owningKey.toBase58String(),
                        beneficiaryParty = this.beneficiary.owningKey.toBase58String(),
                        ipaParty = this.ipa.owningKey.toBase58String(),
                        depositoryParty = this.depository.owningKey.toBase58String(),
                        cpProgramID = this.cpProgramID,
                        cpTradeID = this.cpTradeID,
                        tradeDate = this.tradeDate,
                        valueDate = this.valueDate,
                        maturity = this.maturityDate,
                        faceValue = this.faceValue.quantity,
                        currency = this.faceValue.token.product.currencyCode,
                        isin = this.isin,
                        version = this.version,
                        hashDealConfirmationDoc = this.hashDealConfirmationDoc
                )
                is StandardSettlementSchemaV1 -> StandardSettlementSchemaV1.PersistentStandardSettlementSchemaState(
                        settlement_key = this.issuer.owningKey.toBase58String(),
                        cpTradeID = this.cpTradeID,
                        cpProgramID = this.cpProgramID,
                        creditorName = this.issuerSettlementDetails?.paymentAccountDetails?.creditorName?:"",
                        bankAccountDetails = this.issuerSettlementDetails?.paymentAccountDetails?.bankAccountDetails?:"",
                        bankName = this.issuerSettlementDetails?.paymentAccountDetails?.bankName?:"",
                        rtgsCode = this.issuerSettlementDetails?.paymentAccountDetails?.rtgsCode?:"",
                        dpName = this.issuerSettlementDetails?.depositoryAccountDetails?.dpName?:"",
                        clientId = this.issuerSettlementDetails?.depositoryAccountDetails?.clientId?:"",
                        dpID = this.issuerSettlementDetails?.depositoryAccountDetails?.dpID?:""
                )
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }
    }

    data class SettlementDetails (
        val paymentAccountDetails: PaymentAccountDetails,
        val depositoryAccountDetails: DepositoryAccountDetails
    )

    data class PaymentAccountDetails (
        val creditorName: String,
        val bankAccountDetails: String,
        val bankName: String,
        val rtgsCode: String
    )

    data class DepositoryAccountDetails (
        val dpName: String,
        val clientId: String,
        val dpID: String
    )

    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>(
                AnyComposition(
                        IndiaCommercialPaper.Clauses.Redeem(),
                        IndiaCommercialPaper.Clauses.Issue(),
                        IndiaCommercialPaper.Clauses.Agree(),
                        IndiaCommercialPaper.Clauses.AddIssuerSettlementDetails())) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaper.State, Issued<IndiaCommercialPaper.Terms>>>
                    = tx.groupStates<IndiaCommercialPaper.State, Issued<IndiaCommercialPaper.Terms>> { it.token }
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

        class Agree: Clause<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.Move::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {
                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.Move>()
                val input = inputs.single()
                requireThat {
                    "the transaction is signed by the beneficiary of the CP" by (input.beneficiary.owningKey in command.signers)
                    "the state is propagated" by (outputs.size == 1)
                    // Don't need to check anything else, as if outputs.size == 1 then the output is equal to
                    // the input ignoring the owner field due to the grouping.
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

        class AddIssuerSettlementDetails: Clause<IndiaCommercialPaper.State, IndiaCommercialPaper.Commands, Issued<IndiaCommercialPaper.Terms>>() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaper.Commands.AddIssuerSettlementDetails::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaper.State>,
                                outputs: List<IndiaCommercialPaper.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaper.Commands>>,
                                groupingKey: Issued<IndiaCommercialPaper.Terms>?): Set<IndiaCommercialPaper.Commands> {
                val command = commands.requireSingleCommand<IndiaCommercialPaper.Commands.AddIssuerSettlementDetails>()
                val input = inputs.single()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("AddIssuerSettlementDetails must be timestamped")
                requireThat { "the transaction is signed by the issuer of the CP" by (input.beneficiary.owningKey in command.signers)}

                return setOf(command.value)
            }
        }
    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaper.Commands
        data class Move(override val contractHash: SecureHash? = null) : FungibleAsset.Commands.Move, IndiaCommercialPaper.Commands
        class Redeem : TypeOnlyCommandData(), IndiaCommercialPaper.Commands
        class Agree : TypeOnlyCommandData(), Commands  // Both sides agree to trade
        class AddIssuerSettlementDetails(issuerSettlementDetails: SettlementDetails) : IndiaCommercialPaper.Commands
    }

    /**
     * Returns a transaction that issues commercial paper, owned by the issuing parties key. Does not update
     * an existing transaction because you aren't able to issue multiple pieces of CP in a single transaction
     * at the moment: this restriction is not fundamental and may be lifted later.
     */
    fun generateIssue(issuer: Party, beneficiary: Party, ipa: Party, depository: Party, notary: Party,
                      cpProgramID: String, cpTradeID: String, tradeDate: String, valueDate: String,
                      faceValue: Amount<Issued<Currency>>, maturityDate: Instant,
                      isin: String): TransactionBuilder {

        val state = TransactionState(IndiaCommercialPaper.State(issuer, beneficiary, ipa, depository,
                                                                cpProgramID, cpTradeID, tradeDate, valueDate,
                                                                faceValue, maturityDate,
                                                                isin), notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(IndiaCommercialPaper.Commands.Issue(), issuer.owningKey))
    }

    /**
     * Returns a transaction that issues commercial paper, owned by the issuing parties key. Does not update
     * an existing transaction because you aren't able to issue multiple pieces of CP in a single transaction
     * at the moment: this restriction is not fundamental and may be lifted later.
     */
    fun generateAgreement(issuer: Party, beneficiary: Party, ipa: Party, depository: Party,
                      cpProgramID: String, cpTradeID: String, tradeDate: String, valueDate: String,
                      faceValue: Amount<Issued<Currency>>, maturityDate: Instant,
                      isin: String, notary: Party): TransactionBuilder {

        val state = TransactionState(IndiaCommercialPaper.State(issuer, beneficiary, ipa, depository,
                cpProgramID, cpTradeID, tradeDate, valueDate,
                faceValue, maturityDate,
                isin), notary)
        return TransactionType.General.Builder(notary = notary).withItems(state, Command(IndiaCommercialPaper.Commands.Agree(), listOf(issuer.owningKey, beneficiary.owningKey)))
    }

    fun addIssuerSettlementDetails(tx: TransactionBuilder, cp: StateAndRef<State>, issuerSettlementDetails: SettlementDetails): TransactionBuilder {
        tx.addInputState(cp)
        val newVersion = Integer(cp.state.data.version.toInt() + 1)
        tx.addOutputState(
                cp.state.data.copy(issuerSettlementDetails = issuerSettlementDetails, version = newVersion),
                cp.state.notary
        )
        tx.addCommand(Commands.AddIssuerSettlementDetails(issuerSettlementDetails), listOf(cp.state.data.issuer.owningKey))
        return tx;
    }

    fun addInvestorSettlementDetails(tx: TransactionBuilder, cp: StateAndRef<State>, isin: String): TransactionBuilder {
        return tx;
    }

    fun attachIssuerSignedDealConfirmation(tx: TransactionBuilder, cp: StateAndRef<State>, isin: String): TransactionBuilder {
        return tx;
    }

    fun attachInvestorSignedDealConfirmation(tx: TransactionBuilder, cp: StateAndRef<State>, isin: String): TransactionBuilder {
        return tx;
    }
}



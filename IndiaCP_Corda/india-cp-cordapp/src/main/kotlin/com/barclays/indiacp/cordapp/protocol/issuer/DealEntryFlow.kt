package com.barclays.indiacp.cordapp.protocol.issuer

import co.paralleluniverse.fibers.Suspendable
import com.barclays.indiacp.cordapp.utilities.CPUtils
import net.corda.core.contracts.*
import net.corda.core.crypto.Party
import net.corda.core.flows.FlowLogic
import net.corda.core.node.NodeInfo
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.ProgressTracker
import net.corda.flows.TwoPartyTradeFlow

/**
 * This whole class is really part of a demo just to initiate the agreement of a deal with a simple
 * API call from a single party without bi-directional access to the database of offers etc.
 *
 * In the "real world", we'd probably have the offers sitting in the platform prior to the agreement step
 * or the protocol would have to reach out to external systems (or users) to verify the deals.
 */
class DealEntryFlow(val cpRefId: String, val investor: Party) : FlowLogic<SignedTransaction>() {

    companion object {
        //val PROSPECTUS_HASH = SecureHash.parse("decd098666b9657314870e192ced0c3519c2c9d395507a238338f8d003929de9")

        object ANNOUNCING : ProgressTracker.Step("Announcing to the buyer node")

        object SELF_ISSUING : ProgressTracker.Step("Got session ID back, issuing and timestamping some commercial paper")

        object TRADING : ProgressTracker.Step("Starting the trade protocol")
        {
            override fun childProgressTracker(): ProgressTracker = TwoPartyTradeFlow.Seller.tracker()
        }

        // We vend a progress tracker that already knows there's going to be a TwoPartyTradingProtocol involved at some
        // point: by setting up the tracker in advance, the user can see what's coming in more detail, instead of being
        // surprised when it appears as a new set of tasks below the current one.
        fun tracker() = ProgressTracker(ANNOUNCING, SELF_ISSUING, TRADING)
    }

    override val progressTracker: ProgressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = ANNOUNCING

        val notary: NodeInfo = serviceHub.networkMapCache.notaryNodes[0]
        val comemrcialPaper = CPUtils.getReferencedCommercialPaper(serviceHub, cpRefId)
        val cpOwnerKey = serviceHub.keyManagementService.toKeyPair(comemrcialPaper.state.data.owner.keys)

        progressTracker.currentStep = TRADING

        //TODO: Hardcoded the amount. Need to replace with actual contract state references.
        send(investor, 1000.DOLLARS)
        val seller = TwoPartyTradeFlow.Seller(investor, notary, comemrcialPaper, 1000.DOLLARS, cpOwnerKey,
                progressTracker.getChildProgressTracker(TRADING)!!)
        val tradeTX: SignedTransaction = subFlow(seller, shareParentSessions = true)
        serviceHub.recordTransactions(listOf(tradeTX))

        return tradeTX
    }
}


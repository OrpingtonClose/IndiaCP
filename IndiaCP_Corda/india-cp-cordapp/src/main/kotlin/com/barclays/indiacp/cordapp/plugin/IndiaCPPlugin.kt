package com.barclays.indiacp.cordapp.plugin

import com.barclays.indiacp.cordapp.api.BorrowingLimitBoardResolutionApi
import com.barclays.indiacp.cordapp.api.CreditRatingApi
import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.protocol.issuer.BorrowingLimitBoardResolutionFlows
import com.barclays.indiacp.cordapp.protocol.issuer.CreditRatingFlows
import com.barclays.indiacp.model.LegalEntityCreditRatingDocument
import com.esotericsoftware.kryo.Kryo
import net.corda.core.node.CordaPluginRegistry

class IndiaCPPlugin : CordaPluginRegistry() {
    // A list of classes that expose web APIs.
    override val webApis: List<Class<*>> = listOf(
            CreditRatingApi::class.java,
            BorrowingLimitBoardResolutionApi::class.java)

    // A list of protocol that are required for this cordapp
    override val requiredFlows: Map<String, Set<String>> = mapOf(
            CreditRatingFlows::class.java.name to setOf(CreditRating.State::class.java.name, String::class.java.name),
            BorrowingLimitBoardResolutionFlows::class.java.name to setOf(BorrowingLimitBoardResolution.State::class.java.name, String::class.java.name)

            //DealEntryFlow::class.java.name to setOf(String::class.java.name, Party::class.java.name),
            //IssueCPFlow::class.java.name to setOf(IndiaCPApi.CPJSONObject::class.java.name),
            //ISINGenerationFlow::class.java.name to setOf(String::class.java.name, String::class.java.name)
    )

    //override val servicePlugins = listOf(Function(BuyerFlow::Service))

    override fun registerRPCKryoTypes(kryo: Kryo): Boolean {
        kryo.apply {
            register(IndiaCommercialPaper.State::class.java)
            register(CreditRating.State::class.java)
            register(LegalEntityCreditRatingDocument::class.java)
        }
        return true
    }

}
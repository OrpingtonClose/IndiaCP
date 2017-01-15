package com.barclays.indiacp.cordapp.plugin

import com.barclays.indiacp.cordapp.api.BorrowingLimitBoardResolutionApi
import com.barclays.indiacp.cordapp.api.CreditRatingApi
import com.barclays.indiacp.cordapp.api.IndiaCPApi
import com.barclays.indiacp.cordapp.api.IndiaCPProgramApi
import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.protocol.agreements.AddCPDocFlow
import com.barclays.indiacp.cordapp.protocol.agreements.AddISINDocFlow
import com.barclays.indiacp.cordapp.protocol.agreements.AddISINFlow
import com.barclays.indiacp.cordapp.protocol.issuer.BorrowingLimitBoardResolutionFlows
import com.barclays.indiacp.cordapp.protocol.issuer.CreditRatingFlows
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPFlow
import com.barclays.indiacp.cordapp.protocol.issuer.IssueCPProgramFlow
import com.barclays.indiacp.model.IndiaCPDocumentDetails
import com.barclays.indiacp.model.IndiaCPProgram
import com.barclays.indiacp.model.IndiaCPProgramStatusEnum
import com.barclays.indiacp.model.LegalEntityCreditRatingDocument
import com.esotericsoftware.kryo.Kryo
import net.corda.core.contracts.StateAndRef
import net.corda.core.crypto.Party
import net.corda.core.node.CordaPluginRegistry
import net.corda.node.services.vault.CashBalanceAsMetricsObserver
import java.util.*

class IndiaCPPlugin : CordaPluginRegistry() {
    // A list of classes that expose web APIs.
    override val webApis: List<Class<*>> = listOf(
            CreditRatingApi::class.java,
            BorrowingLimitBoardResolutionApi::class.java,
            IndiaCPProgramApi::class.java,
            IndiaCPApi::class.java
    )

    // A list of protocol that are required for this cordapp
    override val requiredFlows: Map<String, Set<String>> = mapOf(
            CreditRatingFlows::class.java.name to setOf(CreditRating.State::class.java.name, String::class.java.name),
            BorrowingLimitBoardResolutionFlows::class.java.name to setOf(BorrowingLimitBoardResolution.State::class.java.name, String::class.java.name),
            IssueCPProgramFlow::class.java.name to setOf(IndiaCommercialPaperProgram.State::class.java.name),
            AddISINDocFlow::class.java.name to setOf(StateAndRef::class.java.name),
            AddISINFlow::class.java.name to setOf(StateAndRef::class.java.name),
            IssueCPFlow::class.java.name to setOf(IndiaCommercialPaper.State::class.java.name),
            AddCPDocFlow::class.java.name to setOf(StateAndRef::class.java.name, IndiaCPDocumentDetails.DocTypeEnum::class.java.name, Party::class.java.name)

            //DealEntryFlow::class.java.name to setOf(String::class.java.name, Party::class.java.name),
            //IssueCPFlow::class.java.name to setOf(IndiaCPApi.CPJSONObject::class.java.name),
    )

    override val servicePlugins = listOf(
            AddISINDocFlow.Services::class.java,
            AddISINFlow.Services::class.java,
            AddCPDocFlow.Services::class.java
    )

    override fun registerRPCKryoTypes(kryo: Kryo): Boolean {
        kryo.apply {
            //registering Contract State
            register(CreditRating.State::class.java)
            //MM For India CP Propgram
            register(Date::class.java)
            register(IndiaCPProgram::class.java)
            register(IndiaCommercialPaperProgram::class.java)
            register(IndiaCommercialPaperProgram.State::class.java)
            //Just this is not enought. it needs all inside values as well somehow.
            register(IndiaCPProgramStatusEnum::class.java)
            register(IndiaCPProgramStatusEnum.ALLOTMENT_LETTER_DOC_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.CORP_ACTION_FORM_DOC_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.CP_ISSUEED::class.java)
            register(IndiaCPProgramStatusEnum.CP_PROGRAM_CLOSED::class.java)
            register(IndiaCPProgramStatusEnum.CP_PROGRAM_CREATED::class.java)
            register(IndiaCPProgramStatusEnum.IPA_CERTIFICATE_DOC_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.IPA_VERIFICATION_DOC_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.ISIN_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.ISIN_GEN_DOC_ADDED::class.java)
            register(IndiaCPProgramStatusEnum.UNKNOWN::class.java)

            register(net.corda.node.services.api.MonitoringService::class.java)
            register(CashBalanceAsMetricsObserver::class.java)

            //registering Model
            register(LegalEntityCreditRatingDocument::class.java)
            register(IndiaCPProgram::class.java)
        }
        return true
    }

}
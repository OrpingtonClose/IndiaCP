package com.barclays.indiacp.cordapp.plugin

import com.barclays.indiacp.cordapp.api.IndiaCPApi
import com.barclays.indiacp.cordapp.api.IndiaCPProgramApi
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.cordapp.dto.IndiaCPProgramJSON
import com.barclays.indiacp.cordapp.protocol.issuer.*
import com.barclays.indiacp.cordapp.utilities.CP_PROGRAM_FLOW_STAGES
import com.esotericsoftware.kryo.Kryo
import net.corda.core.crypto.Party
import net.corda.core.node.CordaPluginRegistry
import net.corda.flows.NotaryError
import net.corda.flows.NotaryException
import java.util.*
import java.util.function.Function

class IndiaCPPlugin : CordaPluginRegistry() {
    // A list of classes that expose web APIs.
    //override val webApis = listOf(Function(::IndiaCPApi))
    override val webApis: List<Class<*>> = listOf(IndiaCPApi::class.java, IndiaCPProgramApi::class.java)

    // A list of protocol that are required for this cordapp
    override val requiredFlows: Map<String, Set<String>> = mapOf(
            DealEntryFlow::class.java.name to setOf(String::class.java.name, Party::class.java.name),
            IssueCPFlow::class.java.name to setOf(IndiaCPApi.CPJSONObject::class.java.name),
            AddSettlementDetailsFlow::class.java.name to setOf(String::class.java.name, IndiaCPApi.SettlementDetailsJSONObject::class.java.name),


            //Each flow needs to be indivisually registered in this format.
            CPProgramFlows::class.java.name to setOf(IndiaCPProgramJSON::class.java.name, CP_PROGRAM_FLOW_STAGES.ISSUE_CP_PROGRAM::class.java.name,
                    CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC::class.java.name, CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC::class.java.name,
                    CP_PROGRAM_FLOW_STAGES.ADDISIN::class.java.name, CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC::class.java.name,
                    CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC::class.java.name, CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC::class.java.name,
                    CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC::class.java.name, CP_PROGRAM_FLOW_STAGES.ISSUE_CP::class.java.name,
                    CP_PROGRAM_FLOW_STAGES.CLOSE_CP_PROGRAM::class.java.name),


            //ISSUE CP within exsiting CP Program
                    IssueCPWithinCPProgramFlow::class.java.name to setOf(IndiaCPApi.CPJSONObject::class.java.name)


    )

    override fun registerRPCKryoTypes(kryo: Kryo): Boolean {
        kryo.apply {
            register(IndiaCPApi.CPJSONObject::class.java)
            register(IndiaCommercialPaper::class.java)
            register(IndiaCommercialPaper.State::class.java)
            register(IndiaCommercialPaper.SettlementDetails::class.java)
            register(IndiaCPApi.SettlementDetailsJSONObject::class.java)
            register(IndiaCPApi.PaymentAccountDetailsJSONObject::class.java)
            register(IndiaCPApi.DepositoryAccountDetailsJSONObject::class.java)
            register(IndiaCPApi.CPReferenceAndAcceptablePrice::class.java)
            register(IndiaCPApi.Cash::class.java)

            //MM For India CP Propgram
            register(Date::class.java)
            register(IndiaCPProgramJSON::class.java)
            register(IndiaCommercialPaperProgram::class.java)
            register(IndiaCommercialPaperProgram.State::class.java)
            //Just this is not enought. it needs all inside values as well somehow.
            register(CP_PROGRAM_FLOW_STAGES::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ISSUE_CP_PROGRAM::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ISSUE_CP::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADD_ISIN_GEN_DOC::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADDISIN::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADD_ALLOT_LETTER_DOC::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADD_IPA_CERT_DOC::class.java)
            register(CP_PROGRAM_FLOW_STAGES.CLOSE_CP_PROGRAM::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADD_IPA_VERI_DOC::class.java)
            register(CP_PROGRAM_FLOW_STAGES.ADD_CORP_ACT_FORM_DOC::class.java)




            //MM : Getting error due to this class not registered.
            register(NotaryException::class.java)
            register(NotaryError::class.java)
            register(NotaryError.TransactionInvalid::class.java)

            register(net.corda.contracts.CommercialPaper::class.java)
        }
        return true
    }

}
//package com.barclays.indiacp.cordapp
//
//import com.barclays.indiacp.cordapp.dto.IndiaCPProgramJSON
//import com.barclays.indiacp.cordapp.protocol.issuer.CPProgramFlows
//import com.barclays.indiacp.cordapp.utilities.CP_PROGRAM_FLOW_STAGES
//import net.corda.client.CordaRPCClient
//import net.corda.core.contracts.DOLLARS
//import net.corda.core.node.services.ServiceInfo
//import net.corda.core.serialization.OpaqueBytes
//import net.corda.flows.CashCommand
//import net.corda.flows.CashFlow
//import net.corda.node.driver.NodeInfoAndConfig
//import net.corda.node.driver.driver
//import net.corda.node.services.User
//import net.corda.node.services.config.configureTestSSL
//import net.corda.node.services.messaging.ArtemisMessagingComponent
//import net.corda.node.services.messaging.startFlow
//import net.corda.node.services.startFlowPermission
//import net.corda.node.services.transactions.ValidatingNotaryService
//import org.junit.Test
//import java.util.*
//import java.util.concurrent.Future
//import kotlin.concurrent.thread
//import net.corda.core.transactions.SignedTransaction
//import net.corda.core.flows.FlowLogic
//
///**
// * Created by chaos on 27/12/16.
// * Test case for IndiaCP Program.
// * This should capture all kinds of flows within the
// * program.
// *
// */
//class CPProgramTest
//{
//
//    var issuer:NodeInfoAndConfig? = null;
//    var investor1:NodeInfoAndConfig? = null;
//    var investor2:NodeInfoAndConfig? = null;
//    var notary:NodeInfoAndConfig? = null;
//
//
//    //private method for setup
//    fun loadAllNodes()
//    {
//        // START 1
//        driver {
//            val testUser = User("testUser", "testPassword", permissions = setOf(startFlowPermission<CPProgramFlows>()))
//
//            val issuerFuture = startNode("Issuer", rpcUsers = listOf(testUser))
//            val investor1Future = startNode("Investor1", rpcUsers = listOf(testUser))
//            val investor2Future = startNode("Investor2", rpcUsers = listOf(testUser))
//            val notaryFuture = startNode("Controller", advertisedServices = setOf(ServiceInfo(ValidatingNotaryService.type)))
//            issuer = issuerFuture.get()
//            investor1 = investor1Future.get()
//            investor2 = investor2Future.get()
//            notary = notaryFuture.get()
//
//            // END 1
//        }
//    }
//
//
//    @Test
//    fun indiaCPProgramDoubleSpendTest()
//    {
//
//        //Setting proxy & updates
//
//        // START 2
//        val issuerClient = CordaRPCClient(
//                host = ArtemisMessagingComponent.toHostAndPort(issuer!!.nodeInfo.address),
//                config = configureTestSSL()
//        )
//        issuerClient.start("testUser", "testPassword")
//        val issuerProxy = issuerClient.proxy()
//
//
//        //TEST 1 : Issue CP Program Flow
//        val issueRef = OpaqueBytes.of(0)
//        for (i in 1 .. 10) {
//            thread {
//                val indiaCPProgramJSON:IndiaCPProgramJSON = IndiaCPProgramJSON(
//                        issuer = "Issuer",
//
//                ipa = "Investor1",
//
//                depository = "Investor1",
//
//                program_id = "TEST_CP_PROG1",
//
//                name = "TEST_CP_PROG1",
//
//                type = "type",
//
//                purpose = "TEST CP Program",
//
//                issuer_id = "Issuer",
//
//                issuer_name = "Issuer_test",
//
//                issue_commencement_date = Date(),
//
//                program_size = 1000.0,
//
//                program_allocated_value = 0.0,
//
//                program_currency = "INR",
//
//                maturity_days = Date(),
//
//                ipa_id = "ipa_id_test",
//
//                ipa_name = "ipa_id_test_name",
//
//                depository_id = "depository_id_test",
//
//                depository_name = "depository_id_test_name",
//
//                isin = "",
//
//                isin_generation_request_doc_id = "",
//
//                isin_generation_request_doc_status = "",
//
//                ipa_verification_request_doc_id = "",
//
//                ipa_certificate_doc_id = "",
//
//                corporate_action_form_doc_id = "",
//
//                allotment_letter_doc_id = "",
//
//                status = CP_PROGRAM_FLOW_STAGES.ISSUE_CP_PROGRAM.endStatus
//
//                )
//
//                issuerClient.startFlow(::CPProgramFlows, indiaCPProgramJSON, CP_PROGRAM_FLOW_STAGES.ISSUE_CP_PROGRAM)
//            }
//        }
//
//
//
//
//
//
//
//
//
//        //End of Settings and Proxy updates
//
//
//
//    }
//}
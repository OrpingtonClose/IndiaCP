package com.barclays.indiacp.cordapp

import com.barclays.indiacp.cordapp.utilities.postJson
import com.barclays.indiacp.cordapp.utilities.putJson
import com.google.common.net.HostAndPort
import com.typesafe.config.Config
import net.corda.core.contracts.DOLLARS
import net.corda.core.getOrThrow
import net.corda.core.node.services.ServiceInfo
import net.corda.node.driver.driver
import net.corda.node.services.messaging.startFlow
import net.corda.node.services.transactions.SimpleNotaryService
import net.corda.testing.IntegrationTestCategory
import org.junit.Test
import java.net.URL



/**
 * Created by chaos on 27/12/16.
 * Integration test suit for IndiaCPProgram.
 * We will cover all scenarios of normal flow as well as concurrent updates
 * so that we are able to ensure system integritiy of IndiaCP Program
 * This can also have a few behaviour based scenario testing but then BDD should be
 * done saparately.
 *
 * TODO: Still need to put a shutdown hook for this test in tear down.
 *
 */
class CPProgramTest : IntegrationTestCategory
{

    val programID:String = "CP_PROGRAM_1"

    fun Config.getHostAndPort(name: String): HostAndPort = HostAndPort.fromString(getString(name))!!

    @Test fun `runs CPProgram demo`()
    {

        driver(
                dsl = {
            val controller = startNode("Notary", setOf(ServiceInfo(SimpleNotaryService.type))).getOrThrow()
            val issuer = startNode("Issuer").getOrThrow()
            val investor1 = startNode("Investor1").getOrThrow()


            System.out.println("--------------------------- ALL NODES HAVE STARTED SUCCESSFULLY --------------------------" )

            System.out.println("------------------------------------------------------------------------------------------" )


            runIssueCPProgram(issuer.config.getHostAndPort("webAddress"))

//            putWait(120)

            runAddISINGenDoc(issuer.config.getHostAndPort("webAddress"))


//            runIssueCPWithinCPProgram(issuer.config.getHostAndPort("webAddress"))

            waitForAllNodesToFinish()

        }, useTestClock = true, isDebug = true)
    }


    private fun runIssueCPProgram(nodeAddr: HostAndPort)
    {
        val url = URL("http://$nodeAddr/api/indiacpprogram/issueCPProgram")

        System.out.println("Target URL : " + url)
        
        val indiaCPProgram:String = "{\"issuer\": \"Issuer\", \"ipa\": \"Investor1\", \"depository\": \"Investor1\", " +
                "\"program_id\": \""+programID+"\", \"name\": \"name\", \"type\": " +
                "\"type\", \"purpose\": \"no special purpose\", \"issuer_id\": " +
                "\"Issuer\", \"issuer_name\": \"issuer_name-1\", \"issue_commencement_date\": \"2016-12-22\", " +
                "\"program_size\": 100.0, \"program_allocated_value\": 0, \"program_currency\": \"INR\", \"maturity_days\"" +
                ": \"2017-12-17\", \"ipa_id\": \"ipa_id-1\", \"ipa_name\": \"ipa_name-1\", \"depository_id\": \"" +
                "depository_id-1\", \"depository_name\": \"depository_name-1\", \"isin\":\"isin\", \"" +
                "isin_generation_request_doc_id\": \"isin_generation_request_doc_id-1\", \"" +
                "ipa_verification_request_doc_id\": \"ipa_verification_request_doc_id-1\", \"" +
                "ipa_certificate_doc_id\": \"ipa_certificate_doc_id-1\", \"corporate_action_form_doc_id\": \"" +
                "corporate_action_form_doc_id-1\", \"allotment_letter_doc_id\": \"allotment_letter_doc_id-1\", \"" +
                "status\":\"ISSUE INDIA CP PROGRAM\", \"last_modified\" : \"2017-12-17\", \"version\": 0}"


        //This is to hold program for some time so that we can check something
        // on the background.
//        putWait(30)


        var retflag : Boolean  = postJson(url, indiaCPProgram)

        if(retflag)
        {
            println("runIssueCPProgram is SUCCESSFUL.......")
            putWait(1)
        }
        assert(retflag)

    }


    private fun runIssueCPWithinCPProgram(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for IssueCPWithinCPProgram -------------------------------")



        val url = URL("http://$nodeAddr/api/indiacpprogram/issueCPWithinCPProgram/CP_PROGRAM_1/")

        System.out.println("Target URL : " + url)

                val newCPJSON: String = "{\"issuer\": \"Issuer\", \"beneficiary\": \"Investor1\", \"ipa\": \"Investor1\", \"depository\": " +
                        "\"Investor1\", \"cpProgramID\": \""+programID+"\", \"cpTradeID\": \"INDIA_CP_1\", " +
                        "\"tradeDate\": \"19-12-2016\", \"valueDate\": \"23-12-2017\", \"faceValue\": 10, " +
                        "\"maturityDays\": 7, \"isin\": \"INCP123456-1\"}"

//                assert(postJson(url, newCPJSON))

                var retflag : Boolean  = postJson(url, newCPJSON)

                if(retflag)
                {
                    println("runIssueCPProgram is SUCCESSFUL.......")
                    putWait(5)
                }
                assert(retflag)
    }



    //This is test for trying to issue multiple CP within a given CP program
    //all at same time. This is expected to fail.
    private fun runIssueCPWithinCPProgramConcurrently(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for IssueCPWithinCPProgram -------------------------------")



        val url = URL("http://$nodeAddr/api/indiacpprogram/issueCPWithinCPProgram/CP_PROGRAM_1/")

        System.out.println("Target URL : " + url)

        for (i in 1 .. 4) {
            kotlin.concurrent.thread {

                println("Trigger CP ISSUE Within CP PROGRAM : REQUEST " + i)

                val newCPJSON: String = "{\"issuer\": \"Issuer\", \"beneficiary\": \"Investor1\", \"ipa\": \"Investor1\", \"depository\": " +
                        "\"Investor1\", \"cpProgramID\": \""+programID+"\", \"cpTradeID\": \"INDIA_CP_"+i+"\", " +
                        "\"tradeDate\": \"19-12-2016\", \"valueDate\": \"23-12-2017\", \"faceValue\": 10, " +
                        "\"maturityDays\": 7, \"isin\": \"INCP123456-1\"}"

//                assert(postJson(url, newCPJSON))

                var retflag : Boolean  = postJson(url, newCPJSON)

                if(retflag)
                {
                    println("runIssueCPProgram is SUCCESSFUL.......")
                    putWait(5)
                }

//                assert(retflag)

            }
        }
    }

    private fun putWait(s : Int)
    {
        System.out.println("Forceful sleep for "+s+" sec......")

        Thread.sleep(s * 1000L)

        System.out.println("After Forceful sleep for "+s+" sec.....")
    }



    private fun runAddISINGenDoc(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/issueCPWithinCPProgram/"+programID+"/docHashId/docStatus444")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = ""

//                assert(postJson(url, newCPJSON))

        var retflag : Boolean  = postJson(url, newCPJSON)

        if(retflag)
        {
            println("runAddISINGenDoc is SUCCESSFUL.......")
            putWait(5)
        }

//        assert(retflag)

    }



}

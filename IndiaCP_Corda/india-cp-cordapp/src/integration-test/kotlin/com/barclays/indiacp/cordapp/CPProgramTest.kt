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
import java.util.*


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

val programID:String = "CP_PROGRAM_1"

val orgUnit:String = "BARCLAYS_TEST_ORG"

class TestResult(val testName:String, val retFlag: Boolean, val error:String = "")
{
    override fun toString(): String
    {
        var msg:String = ""

        if(retFlag) {
            msg =  testName + " is SUCCESSFUL......."
        }

        try
        {
            assert(retFlag)
        }
        catch(e:AssertionError)
        {
            msg = testName + " FAILED WITH ERROR ......" + error
        }

        return msg
    }
}


class CPProgramTest : IntegrationTestCategory
{

    val testResults:ArrayList<TestResult> = ArrayList<TestResult>()


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

            runIssueOrgBorrowingContract(issuer.config.getHostAndPort("webAddress"))


            runIssueCPProgram(issuer.config.getHostAndPort("webAddress"))

//            putWait(120)

            runAddISINGenDoc(issuer.config.getHostAndPort("webAddress"))

            runAddISIN(issuer.config.getHostAndPort("webAddress"))

            runaddIPAVerificationDocs(issuer.config.getHostAndPort("webAddress"))

            runAddIPACertifcateDoc(issuer.config.getHostAndPort("webAddress"))

            runAddCorpActionFormDoc(issuer.config.getHostAndPort("webAddress"))

            runAddAllotmentLetterDoc(issuer.config.getHostAndPort("webAddress"))

            runIssueCPWithinCPProgram(issuer.config.getHostAndPort("webAddress"))

            System.out.println("-----------------------------------------ALL TEST RUN COMPLETE-------------------------------------------------" )

            for(test in testResults)
            {
                System.out.println(test);
            }


            waitForAllNodesToFinish()

        }, useTestClock = true, isDebug = true)
    }


    private fun runIssueOrgBorrowingContract(nodeAddr: HostAndPort)
    {
        val url = URL("http://$nodeAddr/api/orglevelcontract/issueOrgLevelBorrowingProgram")

        System.out.println("Target URL : " + url)

        val superOrgProgram:String = "{\"issuer\": \"Issuer\"," +
                " \"org_id\": \""+orgUnit+"\", \"name\": \"Barclays India Org\", \"purpose\": \"masti\", " +
                "\"commencement_date\": \"2016-12-30\", " +
                "\"borrowing_limit\": 1000, \"borrowed_value\": 0, \"program_currency\": \"INR\", " +
                "\"user_id\" : \"JOHN RAMBO\" }"


        //This is to hold program for some time so that we can check something
        // on the background.
//        putWait(30)


        var retflag : Boolean  = false
        var error:String = ""
        try
        {
            retflag = postJson(url, superOrgProgram)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runIssueOrgBorrowingContract", retflag, error))

    }



    private fun runIssueCPProgram(nodeAddr: HostAndPort)
    {
        val url = URL("http://$nodeAddr/api/orglevelcontract/issueCPProgramWithInOrg")

        System.out.println("Target URL : " + url)
        
        val indiaCPProgram:String = "{\"orgUnit\":\""+ orgUnit+"\", \"issuer\": \"Issuer\", \"ipa\": \"Investor1\", " +
                "\"depository\": \"Investor1\", " +
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


        var retflag : Boolean  = false
        var error:String = ""
        try
        {
            retflag = postJson(url, indiaCPProgram)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runIssueCPProgram", retflag, error))

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

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runIssueCPWithinCPProgram", retflag, error))
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

                var retflag : Boolean  = true
                var error:String = ""
                try
                {
                    retflag = postJson(url, newCPJSON)
                }catch (e:Exception)
                {
                    error = e.toString()
                }

                testResults.add(TestResult("runIssueCPWithinCPProgramConcurrently", retflag, error))

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

        val url = URL("http://$nodeAddr/api/indiacpprogram/addISINGenerationDocs/"+programID+"/docHashId/docStatus444")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runAddISINGenDoc", retflag, error))

    }

    private fun runAddISIN(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/addISIN/"+programID+"/test_isin/docHashId/docStatus444")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runAddISIN", retflag, error))

    }

    private fun runaddIPAVerificationDocs(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/addIPAVerificationDocs/"+programID+"/docHashId_ipa_ver/docStatus_ipa")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runaddIPAVerificationDocs", retflag, error))

    }

    private fun runAddIPACertifcateDoc(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/addIPACertifcateDoc/"+programID+"/docHashId_ipa_cer/docStatus_ipa_cer")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runAddIPACertifcateDoc", retflag, error))

    }

    private fun runAddCorpActionFormDoc(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/addCorpActionFormDoc/"+programID+"/docHashId_corpAct/docStatus_corp")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runAddCorpActionFormDoc", retflag, error))

    }


    private fun runAddAllotmentLetterDoc(nodeAddr: HostAndPort) {

        println("\n\n\n\n\n\n\n\n")
        println("------------------------ Running test case for runAddISINGenDoc -------------------------------")

        val url = URL("http://$nodeAddr/api/indiacpprogram/addAllotmentLetterDoc/"+programID+"/docHashId_corpAct/docStatus_corp")

        System.out.println("Target URL : " + url)

        val newCPJSON: String = "{}"

        var retflag : Boolean  = true
        var error:String = ""
        try
        {
            retflag = postJson(url, newCPJSON)
        }catch (e:Exception)
        {
            error = e.toString()
        }

        testResults.add(TestResult("runAddAllotmentLetterDoc", retflag, error))

    }


}

package com.barclays.indiacp.cordapp.utilities

import com.barclays.indiacp.model.Error
import com.barclays.indiacp.model.IndiaCPException
import java.io.PrintWriter
import java.io.StringWriter
import javax.ws.rs.core.Response

object ErrorUtils {

    fun errorHttpResponse(ex: Throwable, errorCode: Any = "Unknown", errorMessage: String? = null, errorDetails: String? = null): Response {
        if (ex is IndiaCPException)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getError()).build()
        else {
            val error = Error()
            error.source(Error.SourceEnum.DL_R3CORDA)
            error.code(errorCode.toString())
            error.message(errorMessage ?: ex.message)
            error.details(errorDetails ?: getCustomStackTrace(ex))
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build()
        }
    }

    fun getStackTrace(aThrowable: Throwable): String {
        val result = StringWriter()
        val printWriter = PrintWriter(result)
        aThrowable.printStackTrace(printWriter)
        return result.toString()
    }

    /**
     * Defines a custom format for the stack trace as String.
     */
    fun getCustomStackTrace(aThrowable: Throwable): String {
        //add the class name and any message passed to constructor
        val result = StringBuilder("BOO-BOO: ")
        result.append(aThrowable.toString())
        val NEW_LINE = System.getProperty("line.separator")
        result.append(NEW_LINE)

        //add each element of the stack trace
        val element: StackTraceElement? = null
        for(element in aThrowable.stackTrace) {
            result.append(element)
            result.append(NEW_LINE)
        }
        return result.toString()
    }
}
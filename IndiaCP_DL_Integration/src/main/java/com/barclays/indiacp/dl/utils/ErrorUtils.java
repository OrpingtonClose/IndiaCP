package com.barclays.indiacp.dl.utils;

import com.barclays.indiacp.model.Error;
import com.barclays.indiacp.model.IndiaCPException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.Response;

public class ErrorUtils {

    public static Response errorHttpResponse(Object errorCode, String errorMessage, String errorDetails) {
        IndiaCPException ex = new IndiaCPException("Internal DL Integration Layer Exception", Error.SourceEnum.DL_INTEGRATION_LAYER);
        return errorHttpResponse(ex = ex, errorCode = errorCode, errorMessage = errorMessage, errorDetails = errorDetails);
    }

    public static Response errorHttpResponse(Throwable ex, String errorMessage) {
        return errorHttpResponse(ex, errorMessage, errorMessage, ex.getMessage());
    }

    public static Response errorHttpResponse(Throwable ex, Object errorCode, String errorMessage, String errorDetails) {
        if (ex instanceof IndiaCPException)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(((IndiaCPException)ex).getError()).build();
        else {
            Error error = new Error();
            error.source(Error.SourceEnum.DL_INTEGRATION_LAYER);
            error.code(errorCode.toString());
            error.message(errorMessage == null ? ex.getMessage() : errorMessage);
            error.details(errorDetails == null ? getStackTrace(ex) : errorDetails);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }

    public static String getStackTrace(Throwable aThrowable) {
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

}
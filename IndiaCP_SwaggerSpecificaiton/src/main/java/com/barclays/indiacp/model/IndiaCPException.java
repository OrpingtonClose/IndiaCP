package com.barclays.indiacp.model;

/**
 * Created by ritukedia on 06/01/17.
 */
public class IndiaCPException extends Exception {

    public Error error;

    public final String DEFAULT_ERROR_CODE = "INTERNAL SERVER ERROR";

    public IndiaCPException(Error error)
    {
        super(error.getMessage());
        this.error = error;
    }

    public IndiaCPException(String errorCode, Error.SourceEnum sourceOfError, String errorMessage, String errorDetails)
    {
        super(errorMessage);
        error = new Error();
        error.setCode((errorCode == null || errorCode.isEmpty())? DEFAULT_ERROR_CODE: errorCode);
        error.setMessage(errorMessage);
        error.setDetails(errorDetails);
        error.setSource(sourceOfError);
    }

    public IndiaCPException(String errorMessage)
    {
        this("", Error.SourceEnum.UNKNOWN_SOURCE, errorMessage, "");
        this.error = error;
    }

    public IndiaCPException(CPIssueError cpIssueError, Error.SourceEnum sourceOfError, String errorMessage, String errorDetails)
    {
        this(cpIssueError.name(), sourceOfError, errorMessage, "");
    }

    public IndiaCPException(CPIssueError cpIssueError, Error.SourceEnum sourceOfError, String errorMessage)
    {
        this(cpIssueError, sourceOfError, errorMessage, "");
    }

    public IndiaCPException(CPIssueError cpIssueError, Error.SourceEnum sourceOfError)
    {
        this(cpIssueError, sourceOfError, "");
    }

    public IndiaCPException(CPProgramError cpProgramError, Error.SourceEnum sourceOfError, String errorMessage, String errorDetails) {
        this(cpProgramError.name(), sourceOfError, errorMessage, errorDetails);
    }

    public IndiaCPException(CPProgramError cpProgramError, Error.SourceEnum sourceOfError, String errorMessage) {
        this(cpProgramError, sourceOfError, errorMessage, "");
    }

    public IndiaCPException(CPProgramError cpProgramError, Error.SourceEnum sourceOfError) {
        this(cpProgramError, sourceOfError, "");
    }

    public IndiaCPException(CreditRatingError creditRatingError, Error.SourceEnum sourceOfError, String errorMessage, String errorDetails) {
        this(creditRatingError.name(), sourceOfError, errorMessage, errorDetails);
    }

    public IndiaCPException(CreditRatingError creditRatingError, Error.SourceEnum sourceOfError, String errorMessage) {
        this(creditRatingError, sourceOfError, errorMessage, "");
    }

    public IndiaCPException(CreditRatingError creditRatingError, Error.SourceEnum sourceOfError) {
        this(creditRatingError, sourceOfError, "");
    }

    public IndiaCPException(BoardResolutionError boardResolutionError, Error.SourceEnum sourceOfError, String errorMessage, String errorDetails) {
        this(boardResolutionError.name(), sourceOfError, errorMessage, errorDetails);
    }

    public IndiaCPException(BoardResolutionError boardResolutionError, Error.SourceEnum sourceOfError, String errorMessage) {
        this(boardResolutionError, sourceOfError, errorMessage, "");
    }

    public IndiaCPException(BoardResolutionError boardResolutionError, Error.SourceEnum sourceOfError) {
        this(boardResolutionError, sourceOfError, "");
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }


}

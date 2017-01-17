package com.barclays.indiacp.model;

/**
 * Created by ritukedia on 09/01/17.
 */
public enum IndiaCPProgramStatusEnum {

        UNKNOWN,
        CP_PROGRAM_CREATED,
        CP_PROGRAM_FULLY_ALLOCATED,
        ISIN_GEN_DOC_ADDED,
        ISIN_ADDED,
        CP_ISSUEED,
        IPA_VERIFICATION_DOC_ADDED,
        IPA_CERTIFICATE_DOC_ADDED,
        CORP_ACTION_FORM_DOC_ADDED,
        ALLOTMENT_LETTER_DOC_ADDED, //IPA INITIATED ROLE - MAY NOT BE NEEDED IF THE DVP IS AUTOMATED
        CP_PROGRAM_CLOSED

}

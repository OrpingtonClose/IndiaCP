package com.barclays.indiacp.model;

/**
 * Created by ritukedia on 09/01/17.
 */
public enum IndiaCPProgramLifecycleEnum {

    ISSUE_CP_PROGRAM (IndiaCPProgramStatusEnum.CP_PROGRAM_CREATED),

    ADD_ISIN_GEN_DOCS (IndiaCPProgramStatusEnum.ISIN_GEN_DOC_ADDED),

    ADD_ISIN (IndiaCPProgramStatusEnum.ISIN_ADDED),

    ISSUE_CP (IndiaCPProgramStatusEnum.CP_ISSUEED),

    ADD_IPA_VERIFICATION_DOC (IndiaCPProgramStatusEnum.IPA_VERIFICATION_DOC_ADDED),

    ADD_IPA_CERTIFICATE_DOC (IndiaCPProgramStatusEnum.IPA_CERTIFICATE_DOC_ADDED),

    ADD_CORP_ACTION_FORM_DOC (IndiaCPProgramStatusEnum.CORP_ACTION_FORM_DOC_ADDED),

    ADD_ALLOTMENT_LETTER_DOC (IndiaCPProgramStatusEnum.ALLOTMENT_LETTER_DOC_ADDED),

    CLOSE_CP_PROGRAM (IndiaCPProgramStatusEnum.CP_PROGRAM_CLOSED);

    private IndiaCPProgramStatusEnum endStatus = IndiaCPProgramStatusEnum.UNKNOWN;

    IndiaCPProgramLifecycleEnum(IndiaCPProgramStatusEnum endStatus) {
        this.endStatus = endStatus;
    }

    public IndiaCPProgramStatusEnum endStatus() { return endStatus; }

}


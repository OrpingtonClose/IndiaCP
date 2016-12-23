package com.barclays.indiacp.cordapp.utilities

/**
 * This enum is for all stages of CP_PROGRAM_STAGES
 */


enum class CP_PROGRAM_FLOW_STAGES {
    ISSUE_CP_PROGRAM,
    ADD_ISIN_GEN_DOC,
    ADDISIN,
    ISSUE_CP,
    ADD_IPA_VERI_DOC,
    ADD_IPA_CERT_DOC,
    ADD_CORP_ACT_FORM_DOC,
    ADD_ALLOT_LETTER_DOC,
    CLOSE_CP_PROGRAM
}

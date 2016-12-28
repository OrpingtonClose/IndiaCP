package com.barclays.indiacp.cordapp.utilities

/**
 * This enum is for all stages of CP_PROGRAM_STAGES
 */


enum class CP_PROGRAM_FLOW_STAGES {


    ISSUE_CP_PROGRAM{
        override val endStatus:String = "CP_PROGRAM_CREATED";

    },
    ADD_ISIN_GEN_DOC{
        override val endStatus:String = "ISIN_GEN_DOC_ADDED";
    },
    ADDISIN{
        override val endStatus:String = "ISIN_ADDED";
    },
    ISSUE_CP{
        override val endStatus:String = "CP_ISSUEED";
    },
    ADD_IPA_VERI_DOC{
        override val endStatus:String = "IPA_VERI_DOC_ADDED";
    },
    ADD_IPA_CERT_DOC{
        override val endStatus:String = "IPA_CERT_DOC_ADDED";
    },
    ADD_CORP_ACT_FORM_DOC{
        override val endStatus:String = "CORP_ACT_FORM_DOC_ADDED";
    },
    ADD_ALLOT_LETTER_DOC{
        override val endStatus:String = "ALLOT_LETTER_DOC_ADDED";
    },
    CLOSE_CP_PROGRAM{
        override val endStatus:String = "CP_PROGRAM_CLOSED";
    };


    abstract val endStatus:String;

//    fun getEndStatus() :String = endStatus;




}

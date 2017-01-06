module app.models {
    "use strict";

     export class IndiaCPDocumentDetails {
        /**
         * Unique identifier of the CP Program that this document is associated with
         */
        "cpProgramId"?: string;

        /**
         * Unique identifier of the CP Issue that this document is associated with
         */
        "cpIssueId"?: string;

        /**
         * Type of Legal Documents exchanged and signed/countersigned by participants in the CPProgram
         */
        "docType"?: app.models.DocTypeEnum;

        /**
         * Sub-Type of the Document. May or may not be applicable. For e.g. for IPA_DOC is a collection of documents that need to be sent to the IPA - like FIMMDA.pdf and others.
         */
        "docSubType"?: string;

        /**
         * File extension of the document. E.g. PDF. The doc_sub_type.doc_extension would be the full file name in the zipped file uploaded to the DL.
         */
        "docExtension"?: string;

        /**
         * SHA256 Hash of the Content of the Document. This hash uniquely identifies the document.
         */
        "docHash"?: string;

        /**
         * The current status of the document. Possible values are UNSIGNED, SIGNED_BY_ISSUER, SIGNED_BY_INVESTOR, SIGNED_BY_IPA, SIGNED_BY_NSDL
         */
        "docStatus"?: string;

        /**
         * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
         */
        "modifiedBy"?: string;

        /**
         * Last Modified Date for this CPIssue. This is required for Audit History
         */
        "lastModified"?: Date;
    }

    export enum DocTypeEnum {
            CREDITRATINGDOC = <any> 'CREDIT_RATING_DOC',
            BOARDRESOLUTIONBORROWINGLIMITDOC = <any> 'BOARD_RESOLUTION_BORROWING_LIMIT_DOC',
            DEPOSITORYDOCS = <any> 'DEPOSITORY_DOCS',
            IPADOCS = <any> 'IPA_DOCS',
            IPACERTIFICATEDOC = <any> 'IPA_CERTIFICATE_DOC',
            CORPORATEACTIONFORM = <any> 'CORPORATE_ACTION_FORM',
            DEALCONFIRMATIONDOC = <any> 'DEAL_CONFIRMATION_DOC'
        }
}
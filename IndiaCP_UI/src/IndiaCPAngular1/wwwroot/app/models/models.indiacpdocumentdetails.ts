module app.models {
    "use strict";

     export interface IndiaCPDocumentDetails {
        /**
         * Unique identifier of the CP Program that this document is associated with
         */
        "cpProgramId"?: string;

        /**
         * Unique identifier of the CP Issue that this document is associated with
         */
        "cpIssueId"?: string;

        /**
         * Type of Document. Possible Values are ISIN_DOC, IPA_DOC, DEAL_CONFIRMATION_DOC, ALLOTMENT_LETTER, CORPORATE_ACTION_FORM
         */
        "docType"?: string;

        /**
         * Sub-Type of the Document. May or may not be applicable. For e.g. for IPA_DOC is a collection of documents that need to be sent to the IPA
         */
        "docSubType"?: string;

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
}
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
        "docType"?: DocTypeEnum;

        /**
         * SHA256 Hash of the Content of the Document. This hash uniquely identifies the document.
         */
        "docHash"?: string;

        /**
         * The current status of the document. Possible values are UNSIGNED, SIGNED_BY_ISSUER, SIGNED_BY_INVESTOR, SIGNED_BY_IPA, SIGNED_BY_NSDL
         */
        "docStatus"?: DocStatusEnum;

        /**
         * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
         */
        "modifiedBy"?: string;

        /**
         * Last Modified Date for this CPIssue. This is required for Audit History
         */
        "lastModifiedDate"?: Date;

    }

    export enum DocTypeEnum {
            CREDITRATINGDOC = <any> "CREDIT_RATING_DOC",
            BOARDRESOLUTIONBORROWINGLIMITDOC = <any> "BOARD_RESOLUTION_BORROWING_LIMIT_DOC",
            DEPOSITORYDOCS = <any> "DEPOSITORY_DOCS",
            IPADOCS = <any> "IPA_DOCS",
            IPACERTIFICATEDOC = <any> "IPA_CERTIFICATE_DOC",
            CORPORATEACTIONFORM = <any> "CORPORATE_ACTION_FORM",
            DEALCONFIRMATIONDOC = <any> "DEAL_CONFIRMATION_DOC"
        }
        export enum DocStatusEnum {
            UNSIGNED = <any> "UNSIGNED",
            SIGNEDBYISSUER = <any> "SIGNED_BY_ISSUER",
            SIGNEDBYINVESTOR = <any> "SIGNED_BY_INVESTOR",
            SIGNEDBYIPA = <any> "SIGNED_BY_IPA",
            SIGNEDBYNSDL = <any> "SIGNED_BY_NSDL"
        }
}
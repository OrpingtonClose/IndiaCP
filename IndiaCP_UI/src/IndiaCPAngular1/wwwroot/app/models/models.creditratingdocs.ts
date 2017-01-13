module app.models {
    "use strict";

    export class CreditRatingDocs {
         /**
         * Unique identifier of the Legal Entity that this document is Issued for
         */
        "legalEntityId"?: string;

        /**
         * The Credit Rating Agency
         */
        "creditRatingAgencyName"?: string;

        /**
         * Total Approved Borrowing Credit Limit
         */
        "creditRatingAmount"?: number;

        /**
         * Outstanding Credit Borrowing. This is an auto computed value. It is computed by the Smart Contract based on the outstanding open CP Program Sizes.
         */
        "currentOutstandingCreditBorrowing"?: number;

        /**
         * Currency. Defaulted to INR for India Commercial Paper
         */
        "currency"?: string;

        /**
         * Rating assigned by the CRA
         */
        "creditRating"?: string;

        /**
         * Rating issuance date
         */
        "creditRatingIssuanceDate"?: Date;

        /**
         * Rating effective date, which can be different from the issuance date
         */
        "creditRatingEffectiveDate"?: Date;

        /**
         * Rating expiry date
         */
        "creditRatingExpiryDate"?: Date;

        /**
         * SHA256 Hash of the Credit Rating Document
         */
        "docHash"?: string;

        /**
         * Current version of the CR Document. This is auto incremented by the DL.
         */
        "version"?: number;

        /**
         * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
         */
        "modifiedBy"?: string;

        /**
         * Last Modified Date for this CPIssue. This is required for Audit History
         */
        "lastModifiedDate"?: Date;

        /**
         * There can be only one active CR Document at any given time
         */
        "status"?: StatusEnum;

    }
}
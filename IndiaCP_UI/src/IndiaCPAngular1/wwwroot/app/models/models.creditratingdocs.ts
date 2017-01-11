module app.models {
    "use strict";

    export class CreditRatingDocs {
        /**
         * Unique identifier of the Legal Entity that this document is Issued for
         */
        "legalEntityId"?: string;

        /**
         * Unique identifier for the Credit Rating Agency
         */
        "creditRatingAgencyName"?: string;

        /**
         * Unique identifier of the CP Program that this document is associated with
         */
        "creditRatingAmount"?: number;

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
         * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
         */
        "modifiedBy"?: string;

        /**
         * Last Modified Date for this CPIssue. This is required for Audit History
         */
        "lastModifiedDate"?: Date;

        "docHash"?: string;

        "currency"?: string;

    }
}
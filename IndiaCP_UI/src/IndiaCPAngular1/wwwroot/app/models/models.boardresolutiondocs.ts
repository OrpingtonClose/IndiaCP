module app.models {
    "use strict";

    export class BoardResolutionDocs {
         /**
         * Unique identifier of the Legal Entity that this document is issued by
         */
        "legalEntityId"?: string;

        /**
         * Borrowing Limit approved by board
         */
        "boardResolutionBorrowingLimit"?: number;

        /**
         * Date on which the BR was issued
         */
        "boardResolutionIssuanceDate"?: Date;

        /**
         * Date on which the BR will become obsolete
         */
        "boardResolutionExpiryDate"?: Date;

        /**
         * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
         */
        "modifiedBy"?: string;

        /**
         * Last Modified Date for this BR upload. This is required for Audit History
         */
        "lastModifiedDate"?: Date;

        "docHash"?: string;
    }
}
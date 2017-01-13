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
         * Outstanding Credit Borrowing. This is an auto computed value. It is computed by the Smart Contract based on the outstanding open CP Program Sizes.
         */
        "currentOutstandingCreditBorrowing"?: number;

        /**
         * Currency. Defaulted to INR for India Commercial Paper
         */
        "currency"?: string;

        /**
         * Date on which the BR was issued
         */
        "boardResolutionIssuanceDate"?: Date;

        /**
         * Date on which the BR will become obsolete
         */
        "boardResolutionExpiryDate"?: Date;

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
         * Last Modified Date for this BR upload. This is required for Audit History
         */
        "lastModifiedDate"?: Date;

        /**
         * There can be only one active CR Document at any given time
         */
        "status"?: StatusEnum;
    }

    export enum StatusEnum {
            ACTIVE = <any> "ACTIVE",
            OBSOLETE = <any> "OBSOLETE"
        }
}
module app.models {
    "use strict";

    export class IndiaCPIssue {
    /**
         * Unique identifier representing a specific CP Program raised by an Issuer. This CP Issue is allotted under this umbrella program
         */
        "cpProgramId"?: string;

        /**
         * Unique identifier representing a specific CP Issue under the umbrella CP Program
         */
        "cpTradeId"?: string;

        /**
         * Internal Book Id that this trade is booked under
         */
        "bookId"?: string;

        /**
         * Unique CP Security Identifier No. In India this is issued by NSDL for Commercial Paper type of securities.
         */
        "isin"?: string;

        /**
         * Unique identifier of the trader booking this trade
         */
        "traderId"?: string;

        /**
         * Unique identifier of the Issuer
         */
        "issuerId"?: string;

        /**
         * Display name of the Issuer
         */
        "issuerName"?: string;

        /**
         * Unique identifier of the Investor. This also uniquely identifies the Investor DL Node
         */
        "beneficiaryId"?: string;

        /**
         * Display name of the Investor
         */
        "beneficiaryName"?: string;

        /**
         * Unique identifier of the IPA
         */
        "ipaId"?: string;

        /**
         * Display name of the IPA
         */
        "ipaName"?: string;

        /**
         * Unique identifier of the Depository (NSDL)
         */
        "depositoryId"?: string;

        /**
         * Display name of the Depository
         */
        "depositoryName"?: string;

        /**
         * Date on which the trade was captured
         */
        "tradeDate"?: Date;

        /**
         * Date on which the trade was settled and the Cash and CP securities were swapped between the Issuer and the Investor
         */
        "valueDate"?: Date;

        /**
         * Tenor of the CP maturity calculated from value date
         */
        "maturityDays"?: number;

        /**
         * Currency of the issued CP Notes
         */
        "currency"?: string;

        /**
         * Face Value per Unit * NoOfUnits is the amount that will be paid by the Issuer to the Investor on redemption
         */
        "facevaluePerUnit"?: number;

        /**
         * No. of CP Units Issued
         */
        "noOfUnits"?: number;

        /**
         * Rate at which the yield is calculated
         */
        "rate"?: number;

        "issuerSettlementDetails"?: SettlementDetails;

        "investorSettlementDetails"?: SettlementDetails;

        "ipaSettlementDetails"?: SettlementDetails;

        /**
         * Unique identifier of the deal confirmation document signed by both the Issuer and the Investor
         */
        "dealConfirmationDocId"?: string;

        /**
         * Current status of the CP Issue
         */
        "status"?: string;

        /**
         * Current version of the CP Issue
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

    }
}
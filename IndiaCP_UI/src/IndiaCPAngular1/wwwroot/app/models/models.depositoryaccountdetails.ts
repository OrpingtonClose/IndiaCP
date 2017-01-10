module app.models {
    "use strict";

    export class DepositoryAccountDetails {
        /**
         * Unique identifier of the DP Account at the Depository
         */
        "dpId"?: string;

        /**
         * Name under which the DP Account is registered. For e.g. Barclays Securities India Pvt Ltd
         */
        "dpName"?: string;

        /**
         * Depository Account Type. For now only the IPA has two depository accounts one for Allotment and one for Redemption
         */
        "dpType"?: DpTypeEnum;

        /**
         * Unique identifier of the Client at the Depository
         */
        "clientId"?: string;
    }

    export enum DpTypeEnum {
        ALLOTTMENT = <any>"ALLOTTMENT",
        REDEMPTION = <any>"REDEMPTION"
    }
}
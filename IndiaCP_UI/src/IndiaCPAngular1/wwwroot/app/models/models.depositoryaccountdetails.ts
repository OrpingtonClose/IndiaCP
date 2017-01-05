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
         * Unique identifier of the Client at the Depository
         */
        "clientId"?: string;

}
}
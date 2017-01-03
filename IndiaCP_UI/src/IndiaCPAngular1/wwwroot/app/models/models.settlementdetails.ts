module app.models {
    "use strict";

    export interface SettlementDetails {
        "paymentAccountDetails"?: app.models.PaymentAccountDetails;

        "depositoryAccountDetails"?: app.models.DepositoryAccountDetails;

    }
}
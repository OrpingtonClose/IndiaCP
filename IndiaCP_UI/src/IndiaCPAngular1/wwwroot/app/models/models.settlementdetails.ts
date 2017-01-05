module app.models {
    "use strict";

    export class SettlementDetails {
        "paymentAccountDetails"?: app.models.PaymentAccountDetails;

        "depositoryAccountDetails"?: app.models.DepositoryAccountDetails;

    }
}
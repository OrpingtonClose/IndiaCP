module app.models {
    "use strict";

    export class SettlementDetails {
        /**
         * partyType
         */
        "partyType"?: app.models.PartyTypeEnum;

        "paymentAccountDetails"?: app.models.PaymentAccountDetails;

        "depositoryAccountDetails"?: Array<app.models.DepositoryAccountDetails>;

    }
    export enum PartyTypeEnum {
        ISSUER = <any>'ISSUER',
        INVESTOR = <any>'INVESTOR',
        IPA = <any>'IPA'
    }
}
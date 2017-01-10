module app.models {
    "use strict";

    export class SettlementDetails {
        /*
        * partyType
        */
        "partyType"?: PartyTypeEnum;

        "paymentAccountDetails"?: PaymentAccountDetails;

        "depositoryAccountDetails"?: Array<DepositoryAccountDetails>;

    }
    export enum PartyTypeEnum {
        ISSUER = <any>"ISSUER",
        INVESTOR = <any>"INVESTOR",
        IPA = <any>"IPA"
    }
}
module app.models {
    "use strict";

    
    export class PaymentAccountDetails {
         /**
         * Name in which the payment has to be made. For e.g. Barclays Investments & Loans (India) Ltd CP
         */
        "creditorName"?: string;

        /**
         * Bank account no. at the IPA Bank
         */
        "bankAccountNo"?: string;

        /**
         * Bank account type. For e.g. current account
         */
        "bankAccountType"?: string;

        /**
         * Name of the IPA Bank
         */
        "bankName"?: string;

        /**
         * RTGS IFSC code of the IPA Bank to receive payments
         */
        "rtgsIfscCode"?: string;
    }
}
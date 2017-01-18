var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var PaymentAccountDetails = (function () {
            function PaymentAccountDetails() {
                this.creditorName = "";
                this.bankAccountNo = "";
                this.bankAccountType = "";
                this.bankName = "";
                this.rtgsIfscCode = "";
            }
            return PaymentAccountDetails;
        }());
        models.PaymentAccountDetails = PaymentAccountDetails;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.paymentaccountdetails.js.map
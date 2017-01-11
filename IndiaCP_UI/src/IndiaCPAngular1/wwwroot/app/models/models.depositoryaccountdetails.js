var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var DepositoryAccountDetails = (function () {
            function DepositoryAccountDetails() {
            }
            return DepositoryAccountDetails;
        }());
        models.DepositoryAccountDetails = DepositoryAccountDetails;
        var DpTypeEnum;
        (function (DpTypeEnum) {
            DpTypeEnum[DpTypeEnum["ALLOTTMENT"] = "ALLOTTMENT"] = "ALLOTTMENT";
            DpTypeEnum[DpTypeEnum["REDEMPTION"] = "REDEMPTION"] = "REDEMPTION";
        })(DpTypeEnum = models.DpTypeEnum || (models.DpTypeEnum = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.depositoryaccountdetails.js.map
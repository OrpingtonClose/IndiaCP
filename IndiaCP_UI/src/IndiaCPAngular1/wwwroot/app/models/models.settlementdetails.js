var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var SettlementDetails = (function () {
            function SettlementDetails() {
            }
            return SettlementDetails;
        }());
        models.SettlementDetails = SettlementDetails;
        var PartyTypeEnum;
        (function (PartyTypeEnum) {
            PartyTypeEnum[PartyTypeEnum["ISSUER"] = 'ISSUER'] = "ISSUER";
            PartyTypeEnum[PartyTypeEnum["INVESTOR"] = 'INVESTOR'] = "INVESTOR";
            PartyTypeEnum[PartyTypeEnum["IPA"] = 'IPA'] = "IPA";
        })(PartyTypeEnum = models.PartyTypeEnum || (models.PartyTypeEnum = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.settlementdetails.js.map
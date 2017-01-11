var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var Error = (function () {
            function Error() {
            }
            return Error;
        }());
        models.Error = Error;
        var SourceEnum;
        (function (SourceEnum) {
            SourceEnum[SourceEnum["DLINTEGRATIONLAYER"] = "DL_INTEGRATION_LAYER"] = "DLINTEGRATIONLAYER";
            SourceEnum[SourceEnum["DLR3CORDA"] = "DL_R3CORDA"] = "DLR3CORDA";
            SourceEnum[SourceEnum["DLETHEREUMQUORUMCAKESHOP"] = "DL_ETHEREUM_QUORUM_CAKESHOP"] = "DLETHEREUMQUORUMCAKESHOP";
            SourceEnum[SourceEnum["DLETHEREUMQUORUM"] = "DL_ETHEREUM_QUORUM"] = "DLETHEREUMQUORUM";
            SourceEnum[SourceEnum["REFERENCEDATASERVICE"] = "REFERENCE_DATA_SERVICE"] = "REFERENCEDATASERVICE";
            SourceEnum[SourceEnum["UNKNOWNSOURCE"] = "UNKNOWN_SOURCE"] = "UNKNOWNSOURCE";
        })(SourceEnum = models.SourceEnum || (models.SourceEnum = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.error.js.map
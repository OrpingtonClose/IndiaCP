var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var IndiaCPDocumentDetails = (function () {
            function IndiaCPDocumentDetails() {
            }
            return IndiaCPDocumentDetails;
        }());
        models.IndiaCPDocumentDetails = IndiaCPDocumentDetails;
        var DocTypeEnum;
        (function (DocTypeEnum) {
            DocTypeEnum[DocTypeEnum["CREDITRATINGDOC"] = 'CREDIT_RATING_DOC'] = "CREDITRATINGDOC";
            DocTypeEnum[DocTypeEnum["BOARDRESOLUTIONBORROWINGLIMITDOC"] = 'BOARD_RESOLUTION_BORROWING_LIMIT_DOC'] = "BOARDRESOLUTIONBORROWINGLIMITDOC";
            DocTypeEnum[DocTypeEnum["DEPOSITORYDOCS"] = 'DEPOSITORY_DOCS'] = "DEPOSITORYDOCS";
            DocTypeEnum[DocTypeEnum["IPADOCS"] = 'IPA_DOCS'] = "IPADOCS";
            DocTypeEnum[DocTypeEnum["IPACERTIFICATEDOC"] = 'IPA_CERTIFICATE_DOC'] = "IPACERTIFICATEDOC";
            DocTypeEnum[DocTypeEnum["CORPORATEACTIONFORM"] = 'CORPORATE_ACTION_FORM'] = "CORPORATEACTIONFORM";
            DocTypeEnum[DocTypeEnum["DEALCONFIRMATIONDOC"] = 'DEAL_CONFIRMATION_DOC'] = "DEALCONFIRMATIONDOC";
        })(DocTypeEnum = models.DocTypeEnum || (models.DocTypeEnum = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.indiacpdocumentdetails.js.map
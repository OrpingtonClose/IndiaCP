var app;
(function (app) {
    var legalentity;
    (function (legalentity) {
        "use strict";
        var LegalEntityController = (function () {
            function LegalEntityController($sce, $state, authService, issuerService, localStorageService, Upload, growl, $uibModalInstance) {
                this.$sce = $sce;
                this.$state = $state;
                this.authService = authService;
                this.issuerService = issuerService;
                this.localStorageService = localStorageService;
                this.Upload = Upload;
                this.growl = growl;
                this.$uibModalInstance = $uibModalInstance;
                this.nodeType = this.localStorageService.get("nodeInfo").nodeType;
                // br details setup
                this.brDetails = new app.models.BoardResolutionDocs();
                this.brDetails.legalEntityId = "Issuer1";
                this.brDetails.boardResolutionBorrowingLimit = 10000;
                this.brDetails.boardResolutionIssuanceDate = new Date(); // Todays date
                this.brDetails.boardResolutionExpiryDate = new Date(2022, 12, 08); //5 years from now
                this.brDetails.modifiedBy = "Ritu";
                this.brDetails.currency = "INR";
                this.brDetails.docHash = "XXXXXXXXXXX";
                // cr detsils setup
                this.crDetails = new app.models.CreditRatingDocs();
                this.crDetails.legalEntityId = "Issuer1";
                this.crDetails.creditRatingAgencyName = "ICRA";
                this.crDetails.creditRatingAmount = 10000;
                this.crDetails.creditRating = "AAA";
                this.crDetails.creditRatingIssuanceDate = new Date();
                this.crDetails.creditRatingEffectiveDate = new Date();
                this.crDetails.creditRatingExpiryDate = new Date(2022, 12, 08); //5 years from now
                this.crDetails.modifiedBy = "Ritu";
                this.crDetails.currency = "INR";
                this.crDetails.docHash = "XXXXXXXXXXX";
                //legal entity setup
            }
            LegalEntityController.prototype.displayBR = function (file) {
                this.brFile = file;
                this.brFileUrl = this.$sce.trustAsResourceUrl(URL.createObjectURL(file));
            };
            LegalEntityController.prototype.signBR = function () {
                var _this = this;
                var httpUploadRequestParams = {
                    url: "http://52.172.46.253:8182/indiacp/indiacpdocuments/signDoc/BoardResolution",
                    data: { file: this.brFile },
                    method: "POST"
                };
                this.Upload.upload(httpUploadRequestParams).
                    then(function (response) {
                    _this.growl.success("BR document signed succesfully", { title: "BR Signed!" });
                    var streamData = response.data;
                    var url = "data:application/pdf;base64," + streamData;
                    _this.crFileUrl = _this.$sce.trustAsResourceUrl(url);
                }, function (error) {
                    _this.growl.error("Document signing unsuccesful", { title: "Signing Failed!" });
                });
            };
            LegalEntityController.prototype.uploadBR = function () {
                var _this = this;
                this.issuerService.issueBoardResolution(this.brDetails, this.brFile).
                    then(function (response) {
                    console.log(response);
                    _this.growl.success("Board Resolution document uploaded succesfully", { title: "BR Uploaded" });
                }, function (error) {
                    var errorMssg = error.data;
                    console.log(errorMssg.source + "-" + errorMssg.message);
                    _this.growl.error(errorMssg.message, { title: "Upload Failed - " + errorMssg.source });
                });
            };
            LegalEntityController.prototype.displayCR = function (file) {
                this.crFile = file;
                this.crFileUrl = this.$sce.trustAsResourceUrl(URL.createObjectURL(file));
                // var r: FileReader = new FileReader();
                // var data: ArrayBuffer;
                // r.onloadend = function (e) {
                //     data = (e.target as FileReader).result;
                //     var fileBlob: Blob = new Blob([data], { type: "application/pdf" });
                //     ctrl.signedCR = fileBlob;
                //     ctrl.crFileUrl = ctrl.$sce.trustAsResourceUrl(URL.createObjectURL(fileBlob));
                //     ctrl.$scope.$apply();
                // };
                // console.log("working");
                // // check out readAsDataUrl - https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
                // r.readAsArrayBuffer(file);
            };
            LegalEntityController.prototype.signCR = function () {
                var _this = this;
                var httpUploadRequestParams = {
                    url: "http://52.172.46.253:8182/indiacp/indiacpdocuments/signDoc/CreditRating",
                    data: { file: this.crFile },
                    method: "POST"
                };
                this.Upload.upload(httpUploadRequestParams).
                    then(function (response) {
                    _this.growl.success("Credit Details document signed succesfully", { title: "CR Signed!" });
                    var streamData = response.data;
                    var url = "data:application/pdf;base64," + streamData;
                    _this.crFileUrl = _this.$sce.trustAsResourceUrl(url);
                }, function (error) {
                    _this.growl.error("Document signing unsuccesful", { title: "Signing Failed!" });
                });
            };
            LegalEntityController.prototype.uploadCR = function () {
                var _this = this;
                this.issuerService.issueCreditRating(this.crDetails, this.crFile).
                    then(function (response) {
                    console.log(response);
                    _this.growl.success("Credit Details document uploaded succesfully", { title: "CR Uploaded" });
                }, function (error) {
                    var errorMssg = error.data;
                    console.log(errorMssg.source + "-" + errorMssg.message);
                    _this.growl.error(errorMssg.message, { title: "Upload Failed - " + errorMssg.source });
                });
            };
            LegalEntityController.prototype.close = function () {
                this.$uibModalInstance.close();
            };
            return LegalEntityController;
        }());
        LegalEntityController.$inject = ["$sce",
            "$state",
            "app.services.AuthenticationService",
            "app.services.IssuerService",
            "localStorageService",
            "Upload",
            "growl",
            "$uibModalInstance"];
        angular
            .module("app.legalentity")
            .controller("app.legalentity.LegalEntityController", LegalEntityController);
    })(legalentity = app.legalentity || (app.legalentity = {}));
})(app || (app = {}));
//# sourceMappingURL=legalentity.controller.js.map
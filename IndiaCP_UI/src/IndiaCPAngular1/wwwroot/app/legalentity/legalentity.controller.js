var app;
(function (app) {
    var legalentity;
    (function (legalentity) {
        "use strict";
        var LegalEntityController = (function () {
            function LegalEntityController($scope, $sce, $state, authService, localStorageService, Upload) {
                this.$scope = $scope;
                this.$sce = $sce;
                this.$state = $state;
                this.authService = authService;
                this.localStorageService = localStorageService;
                this.Upload = Upload;
                this.nodeType = this.localStorageService.get("nodeInfo").nodeType;
            }
            LegalEntityController.prototype.displayBR = function (file) {
                var ctrl = this;
                var r = new FileReader();
                var data;
                r.onloadend = function (e) {
                    data = e.target.result;
                    var fileBlob = new Blob([data], { type: "application/pdf" });
                    ctrl.brFileUrl = ctrl.$sce.trustAsResourceUrl(URL.createObjectURL(fileBlob));
                    ctrl.$scope.$apply();
                };
                console.log("working");
                // check out readAsDataUrl - https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
                r.readAsArrayBuffer(file);
            };
            LegalEntityController.prototype.uploadBR = function () {
                console.log("working");
            };
            LegalEntityController.prototype.uploadCR = function () {
                console.log("working");
            };
            return LegalEntityController;
        }());
        LegalEntityController.$inject = ["$scope", "$sce", "$state", "app.services.AuthenticationService", "localStorageService", "Upload"];
        angular
            .module("app.legalentity")
            .controller("app.legalentity.LegalEntityController", LegalEntityController);
    })(legalentity = app.legalentity || (app.legalentity = {}));
})(app || (app = {}));
//# sourceMappingURL=legalentity.controller.js.map
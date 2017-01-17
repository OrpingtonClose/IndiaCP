var app;
(function (app) {
    var main;
    (function (main) {
        "use strict";
        var MainController = (function () {
            function MainController($state, authService, localStorageService, $uibModal) {
                this.$state = $state;
                this.authService = authService;
                this.localStorageService = localStorageService;
                this.$uibModal = $uibModal;
                this.nodeInfo = this.localStorageService.get("nodeInfo");
            }
            MainController.prototype.logout = function () {
                this.authService.logout();
                this.$state.go("login");
            };
            MainController.prototype.uploadBRDoc = function () {
                var _this = this;
                var uploadBRModal = this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.legalentity.LegalEntityController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/legalentity/uploadbr.html"
                });
                uploadBRModal.closed.then(function () {
                    _this.$state.transitionTo("main.dashboard");
                });
            };
            MainController.prototype.uploadCRDoc = function () {
                var _this = this;
                var uploadCRModal = this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.legalentity.LegalEntityController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/legalentity/uploadcr.html",
                });
                uploadCRModal.closed.then(function () {
                    _this.$state.transitionTo("main.dashboard");
                });
            };
            return MainController;
        }());
        MainController.$inject = ["$state",
            "app.services.AuthenticationService",
            "localStorageService",
            "$uibModal"];
        angular
            .module("app.main")
            .controller("app.main.MainController", MainController);
    })(main = app.main || (app.main = {}));
})(app || (app = {}));
//# sourceMappingURL=main.controller.js.map
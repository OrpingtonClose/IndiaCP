var app;
(function (app) {
    var investments;
    (function (investments) {
        "use strict";
        var InvestmentsController = (function () {
            function InvestmentsController($state, authService, localStorageService) {
                this.$state = $state;
                this.authService = authService;
                this.localStorageService = localStorageService;
                // this.nodeType = (this.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
            }
            return InvestmentsController;
        }());
        InvestmentsController.$inject = ["$state", "app.services.AuthenticationService", "localStorageService"];
        angular
            .module("app.investments")
            .controller("app.investments.InvestmentsController", InvestmentsController);
    })(investments = app.investments || (app.investments = {}));
})(app || (app = {}));
//# sourceMappingURL=investments.controller.js.map
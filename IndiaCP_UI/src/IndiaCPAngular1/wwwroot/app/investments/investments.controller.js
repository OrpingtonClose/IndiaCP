var app;
(function (app) {
    var investments;
    (function (investments) {
        "use strict";
        var InvestmentsController = (function () {
            function InvestmentsController($state, authService, InvestorService, localStorageService) {
                this.$state = $state;
                this.authService = authService;
                this.InvestorService = InvestorService;
                this.localStorageService = localStorageService;
                this.nodeType = this.localStorageService.get("nodeInfo").nodeType;
            }
            InvestmentsController.prototype.fetchCPIssues = function () {
                var _this = this;
                this.InvestorService.fetchAllCP("ddd").then(function (response) {
                    _this.cpIssues = response.data;
                }, function (error) { });
            };
            return InvestmentsController;
        }());
        InvestmentsController.$inject = ["$state",
            "app.services.AuthenticationService",
            "app.services.InvestorService",
            "localStorageService"];
        angular
            .module("app.investments")
            .controller("app.investments.InvestmentsController", InvestmentsController);
    })(investments = app.investments || (app.investments = {}));
})(app || (app = {}));
//# sourceMappingURL=investments.controller.js.map
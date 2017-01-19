var app;
(function (app) {
    var investments;
    (function (investments) {
        "use strict";
        var InvestmentsController = (function () {
            function InvestmentsController($state, authService, InvestorService, localStorageService, growl) {
                this.$state = $state;
                this.authService = authService;
                this.InvestorService = InvestorService;
                this.localStorageService = localStorageService;
                this.growl = growl;
                this.nodeInfo = this.localStorageService.get("nodeInfo");
                this.fetchCPIssues();
            }
            InvestmentsController.prototype.fetchCPIssues = function () {
                var _this = this;
                this.InvestorService.fetchAllCPOnThisNode().then(function (response) {
                    _this.cpIssues = response.data;
                }, function (error) {
                    _this.growl.error("Could not fetch cpissues for this node.", { title: "Error!" });
                    console.log("CPIssues could not be fetched." + error);
                });
            };
            return InvestmentsController;
        }());
        InvestmentsController.$inject = ["$state",
            "app.services.AuthenticationService",
            "app.services.InvestorService",
            "localStorageService",
            "growl"];
        angular
            .module("app.investments")
            .controller("app.investments.InvestmentsController", InvestmentsController);
    })(investments = app.investments || (app.investments = {}));
})(app || (app = {}));
//# sourceMappingURL=investments.controller.js.map
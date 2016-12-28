var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        "use strict";
        var DashboardController = (function () {
            function DashboardController($http, $scope, issuerService) {
                this.$http = $http;
                this.$scope = $scope;
                this.issuerService = issuerService;
                this.fetchAllCPPrograms();
            }
            DashboardController.prototype.fetchAllCPPrograms = function () {
                this.issuerService.fetchAllCPProgram("groggy").then(function (response) {
                    this.$scope.cpprograms = response;
                });
            };
            return DashboardController;
        }());
        DashboardController.$inject = ["$http", "$scope", "app.services.IssuerService"];
        angular
            .module("app.dashboard")
            .controller("app.dashboard.DashboardController", DashboardController);
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=dashboard.controller.js.map
var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        "use strict";
        var DashboardController = (function () {
            function DashboardController($http, $scope, $uibModal, issuerService) {
                this.$http = $http;
                this.$scope = $scope;
                this.$uibModal = $uibModal;
                this.issuerService = issuerService;
                this.fetchAllCPPrograms();
            }
            DashboardController.prototype.fetchAllCPPrograms = function () {
                // this.issuerService.fetchAllCPProgram("groggy").then(function (response) {
                //     this.$scope.cpprograms = response;
                // });
            };
            DashboardController.prototype.generateISINDocs = function () {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.isingeneration.ISINGenerationController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/isingeneration/isingeneration.html"
                });
            };
            return DashboardController;
        }());
        DashboardController.$inject = ["$http", "$scope", "$uibModal", "app.services.IssuerService"];
        angular
            .module("app.dashboard")
            .controller("app.dashboard.DashboardController", DashboardController);
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=dashboard.controller.js.map
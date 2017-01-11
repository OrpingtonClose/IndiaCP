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
                this.workflowStates = new app.models.WorkflowStates();
                this.fetchAllCPPrograms();
            }
            DashboardController.prototype.fetchAllCPPrograms = function () {
                var vm = this;
                this.issuerService.fetchAllCPProgram().then(function (response) {
                    vm.cpPrograms = response.data;
                    vm.cpPrograms.forEach(function (cpProgram) {
                        vm.workflowStates.states.forEach(function (state) {
                            if (state.status === cpProgram.status) {
                                cpProgram.nextAction = state.nextAction;
                            }
                        });
                    });
                });
            };
            DashboardController.prototype.executeNextAction = function (nextAction, selectedCPProgram) {
                switch (nextAction) {
                    case "ADD_ISIN_GEN_DOC":
                        this.generateISINDocs();
                        break;
                    case "ISSUECP":
                        this.createCPISsue(selectedCPProgram);
                        break;
                    case "ADD_IPA_VERI_DOC":
                        this.createCPISsue(selectedCPProgram);
                        break;
                    default:
                        this.createCPISsue(selectedCPProgram);
                }
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
            DashboardController.prototype.createCPISsue = function (selectedCPProgram) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.cpissue.CPIssueController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/cpissue/cpissue.html",
                    resolve: {
                        cpProgram: function () {
                            return selectedCPProgram;
                        }
                    }
                });
            };
            DashboardController.prototype.createCPProgram = function () {
                var _this = this;
                var modalInstance = this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.cpprogramcreate.CPProgramCreateController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/cpprogramcreate/cpprogramcreate.html"
                });
                modalInstance.closed.then(function () {
                    _this.fetchAllCPPrograms();
                });
            };
            DashboardController.prototype.showCPIssueDetails = function () {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.cpissuedetails.CPIssueDetailsController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/cpissuedetails/cpissuedetails.html"
                });
            };
            DashboardController.prototype.showCPProgramDetails = function (cpProgramId) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.cpprogramdetails.CPProgramDetailsController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/cpprogramdetails/cpprogramdetails.html",
                    resolve: {
                        programId: function () {
                            return cpProgramId;
                        }
                    }
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
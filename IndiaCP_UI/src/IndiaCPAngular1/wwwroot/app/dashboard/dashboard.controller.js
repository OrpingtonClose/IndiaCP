var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        "use strict";
        var DashboardController = (function () {
            function DashboardController($http, $scope, $uibModal, $interval, localStorageService, issuerService) {
                this.$http = $http;
                this.$scope = $scope;
                this.$uibModal = $uibModal;
                this.$interval = $interval;
                this.localStorageService = localStorageService;
                this.issuerService = issuerService;
                this.workflowStates = new app.models.WorkflowStates();
                this.nodeInfo = this.localStorageService.get("nodeInfo");
                this.gridOptions = {};
                this.gridOptions.columnDefs = [{ field: "version", displayName: "#", width: 35, enableColumnMenu: false, cellTemplate: "<div>1</div>" },
                    { field: "issueCommencementDate", width: 125, displayName: "Date", cellTemplate: " <div><span class='small text-nowrap'>{{row.entity.issueCommencementDate | date:'dd-MM-yyyy'}}</span></div>" },
                    { field: "name", displayName: "Program Name", width: 170, enableColumnMenu: false, cellTemplate: "<div> <a href='' ng-click='grid.appScope.vm.showCPProgramDetails(row.entity.programId)' class='text-nowrap'>{{row.entity.name}}</a></div>" },
                    { field: "programAllocatedValue", width: 100, displayName: "Allotment", cellTemplate: "<div height='20px' justgage min='0' max='100' ></div>", enableColumnMenu: false },
                    { field: "status", displayName: "Status", enableColumnMenu: false, cellTemplate: "<span class='label label-default'>{{row.entity.status}}</span>" },
                    { field: "nextAction", displayName: "Action", enableColumnMenu: false, cellTemplate: "<div><button type='button' ng-click='grid.appScope.vm.executeNextAction(row.entity.nextAction.name, row.entity)' ng-disabled='row.entity.nextAction.allowedNodes.indexOf(\"ISSUER\") == -1'  class='btn btn-success btn-raised btn-xs'>{{row.entity.nextAction.name}}</button></div>" },
                    { field: "version", displayName: "Sell", width: 75, enableColumnMenu: false, cellTemplate: "<button type='button' ng-click='grid.appScope.vm.createCPISsue(row.entity)' class='btn btn-success btn-raised btn-sm'>Sell</button>" },
                    { field: "version", displayName: "", width: 150, enableColumnMenu: false, cellTemplate: "app/dashboard/gridtemplates/gridoptionstemplate.html" }
                ];
                this.gridOptions.data = this.cpPrograms;
                this.gridOptions.rowHeight = 75;
                // this.gridOptions = {
                //     data: this.cpPrograms,
                //     columnDefs: this.gridColumns,
                //     rowHeight: 75,
                //     // expandableRowTemplate: 'expandableRowTemplate.html',
                //     // expandableRowHeight: 150,
                //     // //subGridVariable will be available in subGrid scope
                //     // expandableRowScope: {
                //     //     subGridVariable: 'subGridScopeVariable'
                //     // }
                // };
                // { data: vm.cpPrograms, columnDefs: vm.gridColumns, rowHeight:75 }
                this.fetchAllCPPrograms();
            }
            DashboardController.prototype.$onDestroy = function () {
                this.$interval.cancel(this.dataRefresher);
            };
            DashboardController.prototype.fetchAllCPPrograms = function () {
                var _this = this;
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
                this.dataRefresher = this.$interval(function () {
                    var vm = _this;
                    _this.issuerService.fetchAllCPProgram().then(function (response) {
                        vm.cpPrograms = response.data;
                        vm.cpPrograms.forEach(function (cpProgram) {
                            vm.workflowStates.states.forEach(function (state) {
                                if (state.status === cpProgram.status) {
                                    cpProgram.nextAction = state.nextAction;
                                }
                            });
                        });
                    });
                }, 1000000);
            };
            DashboardController.prototype.executeNextAction = function (nextAction, selectedCPProgram) {
                switch (nextAction) {
                    case "ADD_ISIN_GEN_DOC":
                        this.generateISINDocs(selectedCPProgram);
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
            DashboardController.prototype.generateISINDocs = function (selectedCPProgram) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.dashboard.isingeneration.ISINGenerationController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                    resolve: { cpProgram: selectedCPProgram }
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
        DashboardController.$inject = ["$http",
            "$scope",
            "$uibModal",
            "$interval",
            "localStorageService",
            "app.services.IssuerService"];
        angular.module("app.dashboard")
            .controller("app.dashboard.DashboardController", DashboardController);
        angular.module("app.dashboard")
            .component("app.dashboard.DashboardComponent", {
            controller: "app.dashboard.DashboardController",
            controllerAs: "vm",
            templateUrl: "app/dashboard/dashboard.html",
        });
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=dashboard.controller.js.map
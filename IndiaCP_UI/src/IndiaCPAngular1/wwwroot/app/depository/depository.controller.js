var app;
(function (app) {
    var depository;
    (function (depository) {
        "use strict";
        var DepositoryController = (function () {
            function DepositoryController($http, $scope, $uibModal, $interval, localStorageService, issuerService) {
                this.$http = $http;
                this.$scope = $scope;
                this.$uibModal = $uibModal;
                this.$interval = $interval;
                this.localStorageService = localStorageService;
                this.issuerService = issuerService;
                this.workflowStates = new app.models.WorkflowStates();
                this.nodeInfo = localStorageService.get("nodeInfo");
                this.gridColumns = [{ field: "version", displayName: "#", width: 35, enableColumnMenu: false, cellTemplate: "<div>1</div>" },
                    { field: "issueCommencementDate", width: 125, displayName: "Date", cellTemplate: " <div><span class='small text-nowrap'>{{row.entity.issueCommencementDate | date:'dd-MM-yyyy'}}</span></div>" },
                    { field: "name", displayName: "Program Name", width: 170, enableColumnMenu: false, cellTemplate: "<div> <a href='' ng-click='grid.appScope.vm.showCPProgramDetails(row.entity.programId)' class='text-nowrap'>{{row.entity.name}}</a></div>" },
                    { field: "programAllocatedValue", width: 100, displayName: "Allotment", cellTemplate: "<div height='20px' justgage min='0' max='100' ></div>", enableColumnMenu: false },
                    { field: "status", displayName: "Status", enableColumnMenu: false, cellTemplate: "<span class='label label-default'>{{row.entity.status}}</span>" },
                    { field: "nextAction", displayName: "Action", enableColumnMenu: false, cellTemplate: "<div><button type='button' ng-click='grid.appScope.vm.executeNextAction(row.entity.nextAction.name, row.entity)' ng-disabled='row.entity.nextAction.allowedNodes.indexOf(\"ISSUER\") == -1'  class='btn btn-success btn-raised btn-xs'>{{row.entity.nextAction.name}}</button></div>" },
                    { field: "version", displayName: "Sell", width: 75, enableColumnMenu: false, cellTemplate: "<button type='button' ng-click='grid.appScope.vm.createCPISsue(row.entity)' class='btn btn-success btn-raised btn-sm'>Sell</button>" },
                    { field: "version", displayName: "", width: 150, enableColumnMenu: false, cellTemplate: "app/depository/gridtemplates/gridoptionstemplate.html" }
                ];
                this.gridOptions = {
                    data: this.cpPrograms,
                    columnDefs: this.gridColumns,
                    rowHeight: 75,
                    appScopeProvider: this
                };
                this.fetchAllCPProgramsOnce();
                this.fetchAllCPPrograms();
            }
            DepositoryController.prototype.$onDestroy = function () {
                this.$interval.cancel(this.dataRefresher);
            };
            DepositoryController.prototype.fetchAllCPPrograms = function () {
                var _this = this;
                this.dataRefresher = this.$interval(function () {
                    _this.fetchAllCPProgramsOnce();
                }, 10000);
            };
            DepositoryController.prototype.fetchAllCPProgramsOnce = function () {
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
            DepositoryController.prototype.executeNextAction = function (nextAction, selectedCPProgram) {
                switch (nextAction) {
                    case "ADD_ISIN":
                        this.addISIN(selectedCPProgram);
                        break;
                    default:
                        break;
                }
            };
            DepositoryController.prototype.addISIN = function (selectedCPProgram) {
                var _this = this;
                var addISINModal = this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.depository.addisin.AddISINController",
                    controllerAs: "vm",
                    size: "sm",
                    backdrop: "static",
                    templateUrl: "app/depository/addisin/addisin.html",
                    resolve: { cpProgram: selectedCPProgram }
                });
                addISINModal.closed.then(function () {
                    _this.fetchAllCPPrograms();
                });
            };
            DepositoryController.prototype.showCPProgramDetails = function (cpProgramId) {
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
            return DepositoryController;
        }());
        DepositoryController.$inject = ["$http",
            "$scope",
            "$uibModal",
            "$interval",
            "localStorageService",
            "app.services.IssuerService"];
        angular.module("app.depository")
            .controller("app.depository.DepositoryController", DepositoryController);
        angular.module("app.depository")
            .component("app.depository.DepositoryComponent", {
            controller: "app.depository.DepositoryController",
            controllerAs: "vm",
            templateUrl: "app/depository/depository.html",
        });
    })(depository = app.depository || (app.depository = {}));
})(app || (app = {}));
//# sourceMappingURL=depository.controller.js.map
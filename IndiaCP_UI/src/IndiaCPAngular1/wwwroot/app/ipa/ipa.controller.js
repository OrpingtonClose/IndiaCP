var app;
(function (app) {
    var ipa;
    (function (ipa) {
        "use strict";
        var IPAController = (function () {
            function IPAController($http, $scope, $uibModal, $interval, localStorageService, issuerService) {
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
                    { field: "version", displayName: "", width: 150, enableColumnMenu: false, cellTemplate: "app/ipa/gridtemplates/gridoptionstemplate.html" }
                ];
                this.gridOptions = {
                    data: this.cpPrograms,
                    columnDefs: this.gridColumns,
                    rowHeight: 75,
                    appScopeProvider: this
                };
                this.fetchAllCPPrograms();
            }
            IPAController.prototype.$onDestroy = function () {
                this.$interval.cancel(this.dataRefresher);
            };
            IPAController.prototype.fetchAllCPPrograms = function () {
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
                }, 20000);
            };
            IPAController.prototype.executeNextAction = function (nextAction, selectedCPProgram) {
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
            IPAController.prototype.generateISINDocs = function (selectedCPProgram) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.ipa.isingeneration.ISINGenerationController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/ipa/isingeneration/isingeneration.html",
                    resolve: { cpProgram: selectedCPProgram }
                });
            };
            IPAController.prototype.createCPISsue = function (selectedCPProgram) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.ipa.cpissue.CPIssueController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/ipa/cpissue/cpissue.html",
                    resolve: {
                        cpProgram: function () {
                            return selectedCPProgram;
                        }
                    }
                });
            };
            IPAController.prototype.createCPProgram = function () {
                var _this = this;
                var modalInstance = this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.ipa.cpprogramcreate.CPProgramCreateController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/ipa/cpprogramcreate/cpprogramcreate.html"
                });
                modalInstance.closed.then(function () {
                    _this.fetchAllCPPrograms();
                });
            };
            IPAController.prototype.showCPIssueDetails = function () {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.ipa.cpissuedetails.CPIssueDetailsController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/ipa/cpissuedetails/cpissuedetails.html"
                });
            };
            IPAController.prototype.showCPProgramDetails = function (cpProgramId) {
                this.$uibModal.open({
                    animation: true,
                    ariaLabelledBy: "modal-title",
                    ariaDescribedBy: "modal-body",
                    controller: "app.ipa.cpprogramdetails.CPProgramDetailsController",
                    controllerAs: "vm",
                    size: "lg",
                    backdrop: "static",
                    templateUrl: "app/ipa/cpprogramdetails/cpprogramdetails.html",
                    resolve: {
                        programId: function () {
                            return cpProgramId;
                        }
                    }
                });
            };
            return IPAController;
        }());
        IPAController.$inject = ["$http",
            "$scope",
            "$uibModal",
            "$interval",
            "localStorageService",
            "app.services.IssuerService"];
        angular.module("app.ipa")
            .controller("app.ipa.IPAController", IPAController);
        angular.module("app.ipa")
            .component("app.ipa.IPAComponent", {
            controller: "app.ipa.IPAController",
            controllerAs: "vm",
            templateUrl: "app/ipa/ipa.html",
        });
    })(ipa = app.ipa || (app.ipa = {}));
})(app || (app = {}));
//# sourceMappingURL=ipa.controller.js.map
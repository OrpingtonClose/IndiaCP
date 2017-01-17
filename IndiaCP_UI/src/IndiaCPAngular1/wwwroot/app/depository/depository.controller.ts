module app.depository {
    "use strict";

    interface IDepositoryScope {
        fetchAllCPPrograms(): void;
        showCPProgramDetails(cpProgramId: string): void;
        addISIN(cpProgramId:string):void;
        workflowStates: app.models.WorkflowStates;

    }

    class DepositoryController implements IDepositoryScope, ng.IController {
        loggedinUser: app.users.IUser;
        cpPrograms: app.models.IndiaCPProgram[];
        workflowStates: app.models.WorkflowStates;
        dataRefresher: ng.IPromise<any>;
        nodeInfo: app.models.NodeInfo;
        gridColumns: Array<any>;
        gridOptions: any;

        static $inject = ["$http",
            "$scope",
            "$uibModal",
            "$interval",
            "localStorageService",
            "app.services.IssuerService"];
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected $interval: ng.IIntervalService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected issuerService: app.services.IIssuerService) {
            this.workflowStates = new app.models.WorkflowStates();
            this.nodeInfo = localStorageService.get("nodeInfo") as app.models.NodeInfo;


            this.gridColumns = [{ field: "version", displayName: "#", width:35, enableColumnMenu: false, cellTemplate: "<div>1</div>" },
                { field: "issueCommencementDate", width:125, displayName:"Date", cellTemplate:" <div><span class='small text-nowrap'>{{row.entity.issueCommencementDate | date:'dd-MM-yyyy'}}</span></div>" },
            { field: "name", displayName: "Program Name",width:170, enableColumnMenu: false, cellTemplate:"<div> <a href='' ng-click='grid.appScope.vm.showCPProgramDetails(row.entity.programId)' class='text-nowrap'>{{row.entity.name}}</a></div>" },
            { field: "programAllocatedValue", width:100, displayName: "Allotment", cellTemplate: "<div height='20px' justgage min='0' max='100' ></div>",enableColumnMenu: false},
            { field: "status", displayName: "Status", enableColumnMenu: false, cellTemplate: "<span class='label label-default'>{{row.entity.status}}</span>" },
            { field: "nextAction", displayName: "Action", enableColumnMenu: false, cellTemplate: "<div><button type='button' ng-click='grid.appScope.vm.executeNextAction(row.entity.nextAction.name, row.entity)' ng-disabled='row.entity.nextAction.allowedNodes.indexOf(\"ISSUER\") == -1'  class='btn btn-success btn-raised btn-xs'>{{row.entity.nextAction.name}}</button></div>" },
            { field: "version", displayName: "Sell", width:75, enableColumnMenu: false, cellTemplate: "<button type='button' ng-click='grid.appScope.vm.createCPISsue(row.entity)' class='btn btn-success btn-raised btn-sm'>Sell</button>" },
            { field: "version", displayName: "", width:150, enableColumnMenu: false, cellTemplate: "app/depository/gridtemplates/gridoptionstemplate.html" }

            ];

            this.gridOptions = {
                data: this.cpPrograms,
                columnDefs: this.gridColumns,
                rowHeight: 75,
                appScopeProvider: this
            };


            this.fetchAllCPPrograms();
        }

        public $onDestroy() {
            this.$interval.cancel(this.dataRefresher);
        }
        
        public fetchAllCPPrograms(): void {
            var vm = this;
            this.issuerService.fetchAllCPProgram().then(function (response) {
                vm.cpPrograms = response.data;
                vm.cpPrograms.forEach((cpProgram: app.models.IndiaCPProgram) => {
                    vm.workflowStates.states.forEach((state: app.models.WorkflowState) => {
                        if (state.status === cpProgram.status) {
                            cpProgram.nextAction = state.nextAction;
                        }
                    });
                });
            });
            this.dataRefresher = this.$interval(() => {
                var vm = this;
                this.issuerService.fetchAllCPProgram().then(function (response) {
                    vm.cpPrograms = response.data;
                    vm.cpPrograms.forEach((cpProgram: app.models.IndiaCPProgram) => {
                        vm.workflowStates.states.forEach((state: app.models.WorkflowState) => {
                            if (state.status === cpProgram.status) {
                                cpProgram.nextAction = state.nextAction;
                            }
                        });
                    });
                });
            }, 10000);
        }

        public executeNextAction(nextAction: string, selectedCPProgram: app.models.IndiaCPProgram) {
            switch (nextAction) {
                case "ADD_ISIN":
                    this.addISIN(selectedCPProgram);
                    break;
                default:
                    break;
            }
        }

        public addISIN(selectedCPProgram:app.models.IndiaCPProgram):void{
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.depository.addisin.AddISINController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/depository/addisin/addisin.html",
                resolve: { cpProgram: selectedCPProgram }
            });
        }

        public showCPProgramDetails(cpProgramId: string): void {
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
                    programId: (): string => {
                        return cpProgramId;
                    }
                }
            });
        }
    }

    angular.module("app.depository")
        .controller("app.depository.DepositoryController",
        DepositoryController);

    angular.module("app.depository")
        .component("app.depository.DepositoryComponent",
        {
            controller: "app.depository.DepositoryController",
            controllerAs: "vm",
            templateUrl: "app/depository/depository.html",
        });
} 
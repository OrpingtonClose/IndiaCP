module app.ipa {
    "use strict";

    interface IIPAScope {
        fetchAllCPPrograms(): void;
        generateISINDocs(selectedCPProgram: app.models.IndiaCPProgram): void;
        createCPISsue(selectedCPProgram: app.models.IndiaCPProgram): void;
        createCPProgram(): void;
        showCPIssueDetails(): void;
        showCPProgramDetails(cpProgramId: string): void;
        workflowStates: app.models.WorkflowStates;

    }

    class IPAController implements IIPAScope, ng.IController {
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
            }, 20000);
        }

        public executeNextAction(nextAction: string, selectedCPProgram: app.models.IndiaCPProgram) {
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
                case "ADD_IPA_CERT_DOC":
                    this.addIPACertificationDoc(selectedCPProgram);
                    break;
                default:
                    this.createCPISsue(selectedCPProgram);
            }
        }

        public addIPACertificationDoc(selectedCPProgram: app.models.IndiaCPProgram): void {
             this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.ipacertificate.IPACertificateController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/ipacertificate/ipacertificate.html",
                resolve: { cpProgram: selectedCPProgram }
            });
        }

        public generateISINDocs(selectedCPProgram: app.models.IndiaCPProgram): void {
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
        }

        public createCPISsue(selectedCPProgram: app.models.IndiaCPProgram): void {
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
                    cpProgram: (): app.models.IndiaCPProgram => {
                        return selectedCPProgram;
                    }
                }
            });
        }

        public createCPProgram(): void {
            var modalInstance: ng.ui.bootstrap.IModalServiceInstance = this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.ipa.cpprogramcreate.CPProgramCreateController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/ipa/cpprogramcreate/cpprogramcreate.html"
            });
            modalInstance.closed.then((): void => {
                this.fetchAllCPPrograms();
            });
        }

        public showCPIssueDetails(): void {
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
        }
        public showCPProgramDetails(cpProgramId: string): void {
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
                    programId: (): string => {
                        return cpProgramId;
                    }
                }
            });
        }
    }

    angular.module("app.ipa")
        .controller("app.ipa.IPAController",
        IPAController);

    angular.module("app.ipa")
        .component("app.ipa.IPAComponent",
        {
            controller: "app.ipa.IPAController",
            controllerAs: "vm",
            templateUrl: "app/ipa/ipa.html",
        });
} 
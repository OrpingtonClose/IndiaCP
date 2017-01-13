module app.dashboard {
    "use strict";

    interface IDashboardScope {
        fetchAllCPPrograms(): void;
        generateISINDocs(selectedCPProgram: app.models.IndiaCPProgram): void;
        createCPISsue(selectedCPProgram: app.models.IndiaCPProgram): void;
        createCPProgram(): void;
        showCPIssueDetails(): void;
        showCPProgramDetails(cpProgramId: string): void;
        workflowStates: app.models.WorkflowStates;
    }

    class DashboardController implements IDashboardScope, ng.IController {
        loggedinUser: app.users.IUser;
        cpPrograms: app.models.IndiaCPProgram[];
        workflowStates: app.models.WorkflowStates;
        dataRefresher: ng.IPromise<any>;

        static $inject = ["$http",
            "$scope",
            "$uibModal",
            "$interval",
            "app.services.IssuerService"];
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected $interval: ng.IIntervalService,
            protected issuerService: app.services.IIssuerService) {
            this.workflowStates = new app.models.WorkflowStates();
            this.fetchAllCPPrograms();
        }

        public $onDestroy() {
            this.$interval.cancel(this.dataRefresher);
        }

        public fetchAllCPPrograms(): void {
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
        }

        public generateISINDocs(selectedCPProgram: app.models.IndiaCPProgram): void {
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
        }

        public createCPISsue(selectedCPProgram: app.models.IndiaCPProgram): void {
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
                controller: "app.dashboard.cpprogramcreate.CPProgramCreateController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/cpprogramcreate/cpprogramcreate.html"
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
                controller: "app.dashboard.cpissuedetails.CPIssueDetailsController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/cpissuedetails/cpissuedetails.html"
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

    angular.module("app.dashboard")
        .controller("app.dashboard.DashboardController",
        DashboardController);

    angular.module("app.dashboard")
        .component("app.dashboard.DashboardComponent",
        {
            controller: "app.dashboard.DashboardController",
            controllerAs: "vm",
            templateUrl: "app/dashboard/dashboard.html",
        });
} 
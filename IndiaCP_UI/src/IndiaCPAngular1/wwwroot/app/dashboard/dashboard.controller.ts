module app.dashboard {
    "use strict";

    interface IDashboardScope {
        fetchAllCPPrograms(): void;
        generateISINDocs(): void;
        createCPISsue(selectedCPProgram: app.models.IndiaCPProgram): void;
        createCPProgram(): void;
        showCPIssueDetails(): void;
        showCPProgramDetails(cpProgramId: string): void;
        workflowStates: app.models.WorkflowStates;
    }

    class DashboardController implements IDashboardScope {
        loggedinUser: app.users.IUser;
        cpPrograms: app.models.IndiaCPProgram[];
        workflowStates: app.models.WorkflowStates;

        static $inject = ["$http", "$scope", "$uibModal", "app.services.IssuerService"];
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected issuerService: app.services.IIssuerService) {
            this.workflowStates = new app.models.WorkflowStates();
            this.fetchAllCPPrograms();
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
        }

        public executeNextAction(nextAction: string, selectedCPProgram: app.models.IndiaCPProgram) {
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
        }



        public generateISINDocs(): void {
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
    angular
        .module("app.dashboard")
        .controller("app.dashboard.DashboardController",
        DashboardController);
} 
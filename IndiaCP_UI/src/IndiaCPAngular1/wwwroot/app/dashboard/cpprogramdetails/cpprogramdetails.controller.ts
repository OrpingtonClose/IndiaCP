module app.dashboard.cpprogramdetails {
    "use strict";

    interface ICPProgramDetailsScope {
        fetchCPProgram(): void;
        fetchCPIssuesForProgram(): void;
    }

    class CPProgramDetailsController implements ICPProgramDetailsScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        cpprogramMaturityDate: Date;
        transactionHistory: Array<app.models.IndiaCPProgram>;
        cpIssues: Array<app.models.IndiaCPIssue>;


        static $inject = ["$uibModalInstance",
            "$uibModal",
            "app.services.IssuerService",
            "app.services.InvestorService",
            "growl",
            "programId"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected issuerService: app.services.IIssuerService,
            protected investorService: app.services.IInvestorService,
            protected growl: ng.growl.IGrowlService,
            protected cpProgramId: string) {
            this.fetchCPProgram();
            this.fetchTransactionHistory();
        }
        public fetchCPProgram(): void {
            this.issuerService.fetchCPProgram(this.cpProgramId).then((response): void => {
                this.cpprogram = response.data;
                this.cpprogramMaturityDate = new Date(this.cpprogram.issueCommencementDate);
                this.cpprogramMaturityDate.setDate(this.cpprogramMaturityDate.getDate() + this.cpprogram.maturityDays);
            }, (error: any): void => {
                console.log("CPProgram could not be fetched.");
            });
        }

        public fetchCPIssuesForProgram(): void {
            this.investorService.fetchAllCP(this.cpProgramId).then((response): void => {
                this.cpIssues = response.data;
                console.log("CPIssues for CP Program fetched");
            }, (error: any): void => {
                this.growl.error("Could not fetch cpissues for program.", { title: "Error!" });
                console.log("CPProgram could not be fetched.");
            });
        }

        public fetchTransactionHistory(): void {
            this.issuerService.CPProgramGetTransactionHistory(this.cpProgramId).then((response) => {
                this.transactionHistory = response.data as Array<app.models.IndiaCPProgram>;
            }, (error: any) => {
                this.growl.error("Could not fetch transaction history.", { title: "Error!" });
                console.log("Could not fetch transaction history. " + error);

            });
        }


        public showISINDocument() {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.isingeneration.ISINGenerationController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                resolve: { cpProgram: this.cpprogram }
            });
        }


        public showIPAVerification() {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.isingeneration.ISINGenerationController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                resolve: { cpProgram: this.cpprogram }
            });
        }

        public showIPACertificate() {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.isingeneration.ISINGenerationController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                resolve: { cpProgram: this.cpprogram }
            });
        }

        public showCorpActionForm() {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.isingeneration.ISINGenerationController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                resolve: { cpProgram: this.cpprogram }
            });
        }

        public cancel(): void {
            this.$uibModalInstance.close();
        }
    }

    angular
        .module("app.dashboard.cpprogramdetails")
        .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController",
        CPProgramDetailsController);
} 
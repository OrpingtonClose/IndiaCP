module app.dashboard.cpprogramdetails {
    "use strict";

    interface ICPProgramDetailsScope {
        fetchCP(): void;
    }

    class CPProgramDetailsController implements ICPProgramDetailsScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        cpprogramMaturityDate: Date;
        transactionHistory: Array<app.models.IndiaCPProgram>;
        static $inject = ["$uibModalInstance", "app.services.IssuerService", "growl", "programId"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected growl: ng.growl.IGrowlService,
            protected cpProgramId: string) {
            this.fetchCP();
            this.fetchTransactionHistory();
        }
        public fetchCP(): void {
            this.issuerService.fetchCPProgram(this.cpProgramId).then((response): void => {
                this.cpprogram = response.data;
                this.cpprogramMaturityDate = new Date(this.cpprogram.issueCommencementDate);
                this.cpprogramMaturityDate.setDate(this.cpprogramMaturityDate.getDate() + this.cpprogram.maturityDays);
            }, (error: any): void => {
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



        public cancel(): void {
            this.$uibModalInstance.close();
        }
    }

    angular
        .module("app.dashboard.cpprogramdetails")
        .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController",
        CPProgramDetailsController);
} 
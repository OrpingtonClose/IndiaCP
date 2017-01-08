module app.dashboard.cpprogramdetails {
    "use strict";

    interface ICPProgramDetailsScope {
        fetchCP(): void;
    }

    class CPProgramDetailsController implements ICPProgramDetailsScope {
        isinDocPDF: string;
        cpprogram: any;
        static $inject = ["$uibModalInstance", "app.services.IssuerService", "programId"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected cpProgramId: any) {
                this.fetchCP();
             }
        public fetchCP(): void {
            this.issuerService.fetchCPProgram(this.cpProgramId).then((response): void => {
                this.cpprogram = response.data;
                this.cpprogram.maturityDate = new Date(this.cpprogram.maturityDate.epochSecond*1000);
            }, (error: any): void => {
                console.log("CPProgram could not be fetched.");
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
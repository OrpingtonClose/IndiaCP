module app.dashboard.cpissuedetails {
    "use strict";

    interface ICPIssueDetailsScope {
        issueCPForProgram(): void;
    }


    class CPIssueDetailsController implements ICPIssueDetailsScope {
        isinDocPDF: string;
        issueCP: app.models.IndiaCPIssue;
        static $inject = ["$uibModalInstance", "app.services.IssuerService", "cpProgram"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected cpProgram: any) {
        }
        public issueCPForProgram(): void {

        }

        public cancel(): void {
            this.$uibModalInstance.close();
        }
    }

    angular
        .module("app.dashboard.cpissuedetails")
        .controller("app.dashboard.cpissuedetails.CPIssueDetailsController",
        CPIssueDetailsController);
} 
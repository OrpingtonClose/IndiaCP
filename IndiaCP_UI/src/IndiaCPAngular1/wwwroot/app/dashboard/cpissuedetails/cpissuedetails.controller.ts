module app.dashboard.cpissuedetails {
    "use strict";

    interface ICPIssueDetailsScope {
        issueCP(): void;
    }

    class CPIssueDetailsController implements ICPIssueDetailsScope {
        isinDocPDF: string;
        public issueCP(): void {
        }
    }

    angular
        .module("app.dashboard.cpissuedetails")
        .controller("app.dashboard.cpissuedetails.CPIssueDetailsController",
        CPIssueDetailsController);
} 
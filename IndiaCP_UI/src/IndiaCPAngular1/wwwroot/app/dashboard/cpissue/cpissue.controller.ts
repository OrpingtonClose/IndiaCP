module app.dashboard.cpissue {
    "use strict";

    interface ICPIssueScope {
        issueCP(): void;
    }

    class CPIssueController implements ICPIssueScope {
        isinDocPDF: string;
        static $inject = ["$sce"]
        constructor(protected $sce: ng.ISCEService) {
        }
        public issueCP(): void {
        }
    }

    angular
        .module("app.dashboard.cpissue")
        .controller("app.dashboard.cpissue.CPIssueController",
        CPIssueController);
} 
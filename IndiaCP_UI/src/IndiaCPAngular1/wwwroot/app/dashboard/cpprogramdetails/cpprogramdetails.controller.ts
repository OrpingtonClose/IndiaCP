module app.dashboard.cpprogramdetails {
    "use strict";

    interface ICPProgramDetailsScope {
        issueCP(): void;
    }

    class CPProgramDetailsController implements ICPProgramDetailsScope {
        isinDocPDF: string;
        public issueCP(): void {
        }
    }

    angular
        .module("app.dashboard.cpprogramdetails")
        .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController",
        CPProgramDetailsController);
} 
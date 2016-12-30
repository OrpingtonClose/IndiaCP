module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        issueCP(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        static $inject = ["$sce"]
        constructor(protected $sce: ng.ISCEService) {
        }
        public issueCP(): void {
        }
    }

    angular
        .module("app.dashboard.cpprogramcreate")
        .controller("app.dashboard.cpprogramcreate.CPProgramCreateController",
        CPProgramCreateController);
} 
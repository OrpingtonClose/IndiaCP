module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        createCPProgram(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        static $inject = ["$sce","app.services.IIssuerService"]
        constructor(protected $sce: ng.ISCEService,
                    protected issuerService : app.services.IIssuerService) {
        }
        public createCPProgram(): void {
            // this.issuerService.issueCPProgram
        }
    }

    angular
        .module("app.dashboard.cpprogramcreate")
        .controller("app.dashboard.cpprogramcreate.CPProgramCreateController",
        CPProgramCreateController);
} 
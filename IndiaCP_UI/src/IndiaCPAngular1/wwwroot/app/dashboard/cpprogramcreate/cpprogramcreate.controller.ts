module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        createCPProgram(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        static $inject = ["$sce","app.services.IssuerService"]
        constructor(protected $sce: ng.ISCEService,
                    protected issuerService : app.services.IIssuerService) {
        }
        public createCPProgram(): void {
             this.issuerService.issueCPProgram(this.cpprogram).then(():void => {
                console.log("CPProgram created")
             },(error:any):void => {
                 console.log("CPProgram not created." + error)
             });
        }
    }

    angular
        .module("app.dashboard.cpprogramcreate")
        .controller("app.dashboard.cpprogramcreate.CPProgramCreateController",
        CPProgramCreateController);
} 
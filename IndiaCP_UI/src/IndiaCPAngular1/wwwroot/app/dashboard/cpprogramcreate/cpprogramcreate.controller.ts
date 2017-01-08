module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        createCPProgram(): void;
        updateProgramId(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        static $inject = ["$uibModalInstance", "app.services.IssuerService", "uuid4"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected uuid4: any) {
            this.cpprogram = new app.models.IndiaCPProgram();
            this.cpprogram.issuerId = "Issuer";
            this.cpprogram.programCurrency = "INR";
            this.cpprogram.issuerName = "Issuer";
            this.cpprogram.purpose = "ICP";
            this.cpprogram.issueCommencementDate = new Date();
            this.cpprogram.type = "India Commercial Paper";
            this.cpprogram.maturityDays = 7;
            this.cpprogram.programSize = 1000;
            this.cpprogram.depositoryId = "Issuer";
            this.cpprogram.depositoryName = "Issuer";
            this.cpprogram.ipaId = "Issuer";
            this.cpprogram.ipaName = "Issuer";
        }
        public createCPProgram(): void {
            this.issuerService.issueCPProgram(this.cpprogram).then((): void => {
                console.log("CPProgram created");
                 this.$uibModalInstance.close();
            }, (error: any): void => {
                console.log("CPProgram not created.");
            });
        }
        public cancel(): void {
            this.$uibModalInstance.close();
        }

        public updateProgramId(): void {
            this.cpprogram.programId = this.cpprogram.name + "-" + this.uuid4.generate();
        }
    }

    angular
        .module("app.dashboard.cpprogramcreate")
        .controller("app.dashboard.cpprogramcreate.CPProgramCreateController",
        CPProgramCreateController);
} 
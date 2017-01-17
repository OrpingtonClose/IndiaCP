module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        createCPProgram(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        static $inject = ["$uibModalInstance", "app.services.IssuerService", "uuid4", "growl"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected uuid4: any,
            protected growl: ng.growl.IGrowlService) {
            this.cpprogram = new app.models.IndiaCPProgram();
            this.cpprogram.issuerId = "Issuer1";
            this.cpprogram.programCurrency = "INR";
            this.cpprogram.issuerName = "Issuer1";
            this.cpprogram.purpose = "ICP";
            this.cpprogram.issueCommencementDate = new Date();
            this.cpprogram.type = "India Commercial Paper";
            this.cpprogram.maturityDays = 7;
            this.cpprogram.programSize = 1000;
            this.cpprogram.depositoryId = "Issuer1";
            this.cpprogram.depositoryName = "Issuer1";
            this.cpprogram.ipaId = "Issuer1";
            this.cpprogram.ipaName = "Issuer1";
        }
        public createCPProgram(): void {
            this.issuerService.issueCPProgram(this.cpprogram).then((): void => {
                console.log("CPProgram created");
                this.growl.success("CPProgram created suceesfully.", { title: "Success!" });
                this.$uibModalInstance.close();
            }, (error: any): void => {
                console.log("CPProgram not created.");
                this.growl.error("CPProgram not created.", { title: "Error!" });
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
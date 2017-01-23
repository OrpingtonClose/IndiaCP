module app.dashboard.cpprogramcreate {
    "use strict";

    interface ICPProgramCreateScope {
        createCPProgram(): void;
    }

    class CPProgramCreateController implements ICPProgramCreateScope {
        isinDocPDF: string;
        cpprogram: app.models.IndiaCPProgram;
        nodeInfo: app.models.NodeInfo;
        ipacollection: Array<app.models.IPA>;
        depositorycollection: Array<app.models.Depository>;
        static $inject = ["$uibModalInstance",
            "app.services.IssuerService",
            "uuid4",
            "growl",
            "localStorageService",
            "app.services.AuthenticationService"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected issuerService: app.services.IIssuerService,
            protected uuid4: any,
            protected growl: ng.growl.IGrowlService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected authService : app.services.IAuthenticationService) {

            this.ipacollection = [{ displayName: "HDFC", id: "HDFC_IPA" }, { displayName: "SBI", id: "SBI_IPA" }];
            this.depositorycollection = [{ displayName: "NSDL", id: "NSDL_DEPOSITORY" }, { displayName: "CDSL", id: "CDSL_IPA" }];

            this.nodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;

            this.cpprogram = new app.models.IndiaCPProgram();
            this.cpprogram.issuerId = this.nodeInfo.dlNodeName;
            this.cpprogram.programCurrency = "INR";
            this.cpprogram.issuerName = this.nodeInfo.nodeName;
            this.cpprogram.purpose = "ICP";
            this.cpprogram.issueCommencementDate = new Date();
            this.cpprogram.type = "India Commercial Paper";
            this.cpprogram.maturityDays = 7;
            this.cpprogram.programSize = 1000;
            this.cpprogram.depositoryId = "NSDL_DEPOSITORY";
            this.cpprogram.depositoryName = "NSDL";
            this.cpprogram.ipaId = "HDFC_IPA";
            this.cpprogram.ipaName = "HDFC";
            this.cpprogram.modifiedBy = authService.currentUser.username;
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
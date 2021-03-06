module app.depository.addisin {
    "use strict";

    interface IAddISINScope {
        addISIN(): void;
    }

    class AddISINController implements IAddISINScope {
        isin: string;
        static $inject = ["$uibModalInstance", "app.services.DepositoryService", "uuid4", "growl", "cpProgram"];
        constructor(
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
            protected depositoryService: app.services.IDepositoryService,
            protected uuid4: any,
            protected growl: ng.growl.IGrowlService,
            protected cpProgram: app.models.IndiaCPProgram) {
        }
        public addISIN(): void {
            this.depositoryService.addISIN(this.cpProgram.programId, this.isin).then((): void => {
                console.log("ISIN added");
                this.growl.success("ISIN added suceesfully.", { title: "Success!" });
                this.$uibModalInstance.close();
            }, (error: any): void => {
                let errorMssg: app.models.Error = error.data;
                console.log("ISIN not added." + `${errorMssg.source}-${errorMssg.message}`);
                this.growl.error(`ISIN not added. - ${errorMssg.message}`, { title: `${errorMssg.source}` });
            });
        }
        public cancel(): void {
            this.$uibModalInstance.close();
        }
    }

    angular
        .module("app.depository.addisin")
        .controller("app.depository.addisin.AddISINController",
        AddISINController);
} 
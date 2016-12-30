module app.dashboard.cpissue {
    "use strict";

    interface ICPIssueScope {
        issueCP(): void;
    }

    class CPIssueController implements ICPIssueScope {
        isinDocPDF: string;
        static $inject = ["$sce","$uibModalInstance","app.services.IssuerService"]
        constructor(protected $sce: ng.ISCEService,
                    protected $uibModalInstance:ng.ui.bootstrap.IModalServiceInstance,
                    protected issuerService : app.services.IIssuerService) {
        }
        public issueCP(): void {
           
        }
         public cancel():void{
            this.$uibModalInstance.close();
        }
    }

    angular
        .module("app.dashboard.cpissue")
        .controller("app.dashboard.cpissue.CPIssueController",
        CPIssueController);
} 
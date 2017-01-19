module app.investments {
    "use strict";

    interface IInvestmentsScope {
        fetchCPIssues(): void;
        workflowStates: app.models.WorkflowStates;
    }

    class InvestmentsController implements IInvestmentsScope {
        static $inject = ["$state",
            "app.services.AuthenticationService",
            "app.services.InvestorService",
            "localStorageService",
            "growl"];
        nodeInfo: app.models.NodeInfo;
        workflowStates: app.models.WorkflowStates;
        cpIssues: Array<app.models.IndiaCPIssue>;

        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected InvestorService: app.services.IInvestorService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected growl: ng.growl.IGrowlService) {
            this.nodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;
            this.fetchCPIssues();
        }

        public fetchCPIssues(): void {
            this.InvestorService.fetchAllCPOnThisNode().then((response) => {
                this.cpIssues = response.data;
            }, (error) => {
                this.growl.error("Could not fetch cpissues for this node.", { title: "Error!" });
                console.log("CPIssues could not be fetched." + error);
            })
        }
    }

    angular
        .module("app.investments")
        .controller("app.investments.InvestmentsController",
        InvestmentsController);
} 
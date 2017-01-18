module app.investments {
    "use strict";

    interface IInvestmentsScope {
        fetchCPIssues():void;
        workflowStates: app.models.WorkflowStates;
    }

    class InvestmentsController implements IInvestmentsScope {
        static $inject = ["$state",
            "app.services.AuthenticationService",
            "app.services.InvestorService",
            "localStorageService"];
        nodeInfo: app.models.NodeInfo;
        workflowStates: app.models.WorkflowStates;
        cpIssues:Array<app.models.IndiaCPIssue>;
        
        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected InvestorService:app.services.IInvestorService,
            protected localStorageService: ng.local.storage.ILocalStorageService, ) {
            this.nodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;
    }

    public fetchCPIssues():void{
        this.InvestorService.fetchAllCPOnThisNode().then((response)=> {
            this.cpIssues = response.data;
        },(error)=>{})
    }
    }

    angular
        .module("app.investments")
        .controller("app.investments.InvestmentsController",
        InvestmentsController);
} 
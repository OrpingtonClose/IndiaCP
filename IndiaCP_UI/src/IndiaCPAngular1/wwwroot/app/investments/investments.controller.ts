module app.investments {
    "use strict";

    interface IInvestmentsScope {
        nodeType:string;
    }

    class InvestmentsController implements IInvestmentsScope {
        static $inject = ["$state", "app.services.AuthenticationService", "localStorageService"];
        nodeType:string;
        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService, ) {
                // this.nodeType = (this.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
        }
    }

    angular
        .module("app.investments")
        .controller("app.investments.InvestmentsController",
        InvestmentsController);
} 
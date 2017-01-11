module app.main {
    "use strict";

    interface IMainScope {
        logout(): void;
        nodeType:string;
    }

    class MainController implements IMainScope {
        static $inject = ["$state", "app.services.AuthenticationService", "localStorageService"];
        nodeType:string;
        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService, ) {
                this.nodeType = (this.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
        }
        public logout(): void {
            this.authService.logout();
            this.$state.go("login");
      }
    }

    angular
        .module("app.main")
        .controller("app.main.MainController",
        MainController);
} 
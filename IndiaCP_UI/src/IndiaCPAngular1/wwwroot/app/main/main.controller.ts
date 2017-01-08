module app.main {
    "use strict";

    interface IMainScope {
        logout(): void;
    }

    class MainController implements IMainScope {
        static $inject = ["$state","app.services.AuthenticationService", "uuid4", ];
        constructor(protected $state:ng.ui.IStateService, protected authService:app.services.IAuthenticationService) {
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
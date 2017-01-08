module app.Login {
    "use strict";

    interface ILoginScope {
        login(): void;
    }

    class LoginController implements ILoginScope {
        userCredentials: app.models.CurrentUser;

        static $inject = ["$scope", "$state", "app.services.AuthenticationService"];
        constructor(protected $scope: ng.IScope,
            protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService) {
        }

        public login(): void {
            var ctrl = this;
            this.authService.login(this.userCredentials).then(
                function (result: any): void {
                    ctrl.$state.go("main.dashboard");
                },
                function (error: any): void {
                    ctrl.$state.go("login");
                }
            );
        }

    }

    angular
        .module("app.login")
        .controller("app.login.LoginController",
        LoginController);
} 
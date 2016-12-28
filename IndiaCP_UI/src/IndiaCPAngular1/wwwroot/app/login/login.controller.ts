module app.Login {
    "use strict";

    interface ILoginScope {
        login(): void;
    }

    class LoginController implements ILoginScope {
        userCredentials: ICurrentUser;

        static $inject = ["$scope", "$state", "app.services.AuthenticationService"];
        constructor(protected $scope: ng.IScope,
            protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService) {
        }

        public login(): void {
            this.authService.login(this.userCredentials).then(
                function (result: any): void {
                    this.$state.go("app.dashboard");
                },
                function (error: any): void {
                    this.$state.go("login");
                }
            );
        }

    }

    angular
        .module("app.login")
        .controller("app.login.LoginController",
        LoginController);
} 
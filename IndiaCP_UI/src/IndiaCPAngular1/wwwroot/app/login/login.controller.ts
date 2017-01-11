module app.Login {
    "use strict";

    interface ILoginScope {
        login(): void;
    }

    class LoginController implements ILoginScope {
        userCredentials: app.models.CurrentUser;

        static $inject = ["$scope", "$state", "app.services.AuthenticationService", "localStorageService"];
        constructor(protected $scope: ng.IScope,
            protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService, ) {
        }

        public login(): void {
            var ctrl = this;
            this.authService.login(this.userCredentials).then(
                function (result: any): void {
                    var nodeType: string = (ctrl.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
                    switch (nodeType) {
                        case "ISSUER":
                            ctrl.$state.go("main.dashboard");
                            break;
                        case "INVESTOR":
                            ctrl.$state.go("main.investments");
                            break;
                        case "IPA":
                            ctrl.$state.go("main.ipa");
                            break;
                        case "DEPOSITORY":
                            ctrl.$state.go("main.depository");
                            break;
                        default:
                            ctrl.$state.go("main.dashboard");
                    }
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
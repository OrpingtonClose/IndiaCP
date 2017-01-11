var app;
(function (app) {
    var Login;
    (function (Login) {
        "use strict";
        var LoginController = (function () {
            function LoginController($scope, $state, authService, localStorageService) {
                this.$scope = $scope;
                this.$state = $state;
                this.authService = authService;
                this.localStorageService = localStorageService;
            }
            LoginController.prototype.login = function () {
                var ctrl = this;
                this.authService.login(this.userCredentials).then(function (result) {
                    var nodeType = ctrl.localStorageService.get("nodeInfo").nodeType;
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
                }, function (error) {
                    ctrl.$state.go("login");
                });
            };
            return LoginController;
        }());
        LoginController.$inject = ["$scope", "$state", "app.services.AuthenticationService", "localStorageService"];
        angular
            .module("app.login")
            .controller("app.login.LoginController", LoginController);
    })(Login = app.Login || (app.Login = {}));
})(app || (app = {}));
//# sourceMappingURL=login.controller.js.map
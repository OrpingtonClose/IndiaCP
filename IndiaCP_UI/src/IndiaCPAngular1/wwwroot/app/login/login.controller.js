var app;
(function (app) {
    var Login;
    (function (Login) {
        "use strict";
        var LoginController = (function () {
            function LoginController($scope, $state, authService) {
                this.$scope = $scope;
                this.$state = $state;
                this.authService = authService;
            }
            LoginController.prototype.login = function () {
                var ctrl = this;
                this.authService.login(this.userCredentials).then(function (result) {
                    ctrl.$state.go("main.dashboard");
                }, function (error) {
                    ctrl.$state.go("login");
                });
            };
            return LoginController;
        }());
        LoginController.$inject = ["$scope", "$state", "app.services.AuthenticationService"];
        angular
            .module("app.login")
            .controller("app.login.LoginController", LoginController);
    })(Login = app.Login || (app.Login = {}));
})(app || (app = {}));
//# sourceMappingURL=login.controller.js.map
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
                this.authService.login(this.userCredentials).then(function (result) {
                    this.$state.go("app.dashboard");
                }, function (error) {
                    this.$state.go("login");
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
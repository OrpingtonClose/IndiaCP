var app;
(function (app) {
    var main;
    (function (main) {
        "use strict";
        var MainController = (function () {
            function MainController($state, authService) {
                this.$state = $state;
                this.authService = authService;
            }
            MainController.prototype.logout = function () {
                this.authService.logout();
                this.$state.go("login");
            };
            return MainController;
        }());
        MainController.$inject = ["$state", "app.services.AuthenticationService", "uuid4",];
        angular
            .module("app.main")
            .controller("app.main.MainController", MainController);
    })(main = app.main || (app.main = {}));
})(app || (app = {}));
//# sourceMappingURL=main.controller.js.map
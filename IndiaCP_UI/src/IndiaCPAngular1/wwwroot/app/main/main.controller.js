var app;
(function (app) {
    var main;
    (function (main) {
        "use strict";
        var MainController = (function () {
            function MainController($state, authService, localStorageService) {
                this.$state = $state;
                this.authService = authService;
                this.localStorageService = localStorageService;
                this.nodeType = this.localStorageService.get("nodeInfo").nodeType;
            }
            MainController.prototype.logout = function () {
                this.authService.logout();
                this.$state.go("login");
            };
            return MainController;
        }());
        MainController.$inject = ["$state", "app.services.AuthenticationService", "localStorageService"];
        angular
            .module("app.main")
            .controller("app.main.MainController", MainController);
    })(main = app.main || (app.main = {}));
})(app || (app = {}));
//# sourceMappingURL=main.controller.js.map
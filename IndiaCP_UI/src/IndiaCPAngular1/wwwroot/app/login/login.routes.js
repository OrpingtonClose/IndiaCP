(function () {
    "use strict";
    angular
        .module("app.login")
        .config(config);
    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("login", {
            url: "login",
            templateUrl: "app/login/login.html",
            controller: "app.login.LoginController",
            controllerAs: "vm",
            resolve: {}
        });
    }
})();
//# sourceMappingURL=login.routes.js.map
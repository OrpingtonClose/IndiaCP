(function () {
    "use strict";
    angular
        .module("app.main")
        .config(config);
    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("main", {
            url: "/",
            cache: false,
            abstract: true,
            templateUrl: "app/main/main.html",
            controller: "app.main.MainController",
            controllerAs: "vm"
        })
            .state("main.uploaddocs", {
            cache: false,
            url: "main/uploaddocs",
            templateUrl: "app/legalentity/uploaddocsmessage.html",
            controller: "app.main.MainController",
            controllerAs: "vm"
        });
    }
})();
//# sourceMappingURL=main.routes.js.map
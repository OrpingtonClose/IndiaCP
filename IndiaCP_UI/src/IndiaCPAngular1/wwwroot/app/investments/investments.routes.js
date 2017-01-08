(function () {
    "use strict";
    angular
        .module("app.investments")
        .config(config);
    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("main.investments", {
            cache: false,
            url: "investments",
            templateUrl: "app/investments/investments.html",
            controller: "app.investments.InvestmentsController",
            controllerAs: "vm"
        });
    }
})();
//# sourceMappingURL=investments.routes.js.map
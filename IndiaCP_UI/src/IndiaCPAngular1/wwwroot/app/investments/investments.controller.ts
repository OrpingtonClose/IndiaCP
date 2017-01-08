(function (): void {
    "use strict";
    angular
        .module("app.investments")
        .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void {
        $stateProvider
            .state("main.investments", {
                cache: false,
                url: "investments",
                templateUrl: "app/investments/investments.html",
                controller: "app.investments.InvestmentsController",
                controllerAs: "vm"
            })
    }
})();
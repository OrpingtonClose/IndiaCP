(function (): void {
    "use strict";
    angular
        .module("app.depository")
        .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void {
        $stateProvider
            .state("main.depository", {
                cache: false,
                url: "depository",
                component: "app.depository.DepositoryComponent"
            });
    }
})();
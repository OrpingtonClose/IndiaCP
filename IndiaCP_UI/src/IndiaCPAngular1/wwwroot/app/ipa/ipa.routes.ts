(function (): void {
    "use strict";
    angular
        .module("app.ipa")
        .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void {
        $stateProvider
            .state("main.ipa", {
                cache: false,
                url: "ipa",
                component: "app.ipa.IPAComponent"
            });
    }
})();
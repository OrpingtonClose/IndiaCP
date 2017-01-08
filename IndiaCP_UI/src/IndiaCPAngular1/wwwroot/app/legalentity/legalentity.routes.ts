(function (): void {
    "use strict";
    angular
        .module("app.legalentity")
        .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void {
        $stateProvider
            .state("main.legalentity", {
                cache:false,
                template: "<ui-view/>",
                abstract: true,
            })
            .state("main.legalentity.uploadbr", {
                cache:false,
                url: "legalentity/uploadbr",
                controller: "app.legalentity.LegalEntityController",
                templateUrl: "app/legalentity/uploadbr.html",
                controllerAs: "vm"
            })
            .state("main.legalentity.uploadcr", {
                cache:false,
                url: "legalentity/uploadcr",
                controller: "app.legalentity.LegalEntityController",
                templateUrl: "app/legalentity/uploadcr.html",
                controllerAs: "vm"
            })
            .state("main.legalentity.setup", {
                cache:false,
                url: "legalentity/setup",
                controller: "app.legalentity.LegalEntityController",
                templateUrl: "app/legalentity/setup.html",
                controllerAs: "vm"
            });
    }
})();
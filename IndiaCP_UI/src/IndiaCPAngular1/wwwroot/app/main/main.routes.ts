(function(): void {
    "use strict";
    angular
    .module("app.main")
    .config(config);

    config.$inject = ["$stateProvider","$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider):void{
        $stateProvider
            .state("main", {
                url: "/",
                templateUrl: "app/main/main.html",
                controller: "app.main.MainController"
            });
    }
    // resolveCPPrograms.$inject = ["app.services.CPProgramService"];
    // function resolveCPPrograms(cpprogramService):
    // {}
})();
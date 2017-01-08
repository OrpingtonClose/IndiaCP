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
            templateUrl: "app/main/main.html",
            controller: "app.main.MainController",
            controllerAs: "vm"
        });
    }
    // resolveCPPrograms.$inject = ["app.services.CPProgramService"];
    // function resolveCPPrograms(cpprogramService):
    // {}
})();
//# sourceMappingURL=main.routes.js.map
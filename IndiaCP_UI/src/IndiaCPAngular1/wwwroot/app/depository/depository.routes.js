(function () {
    "use strict";
    angular
        .module("app.depository")
        .config(config);
    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("main.depository", {
            cache: false,
            url: "depository",
            component: "app.depository.DepositoryComponent"
        });
    }
})();
//# sourceMappingURL=depository.routes.js.map
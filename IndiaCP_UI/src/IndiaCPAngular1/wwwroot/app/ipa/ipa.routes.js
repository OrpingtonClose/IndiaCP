(function () {
    "use strict";
    angular
        .module("app.ipa")
        .config(config);
    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("main.ipa", {
            cache: false,
            url: "ipa",
            component: "app.ipa.IPAComponent"
        });
    }
})();
//# sourceMappingURL=ipa.routes.js.map
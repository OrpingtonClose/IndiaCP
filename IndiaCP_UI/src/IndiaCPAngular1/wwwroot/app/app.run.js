(function () {
    "use strict";
    angular
        .module("app")
        .run(run);
    run.$inject = [
        "$state",
        "$rootScope",
        "$cookies",
        "$timeout",
        "$location",
        "$trace",
        "localStorageService",
        "app.services.AuthenticationService"
    ];
    function run($state, $rootScope, $cookies, $timeout, $location, $trace, localStorageService, authService) {
        //$trace.enable('TRANSITION');
        $rootScope.$on("$routeChangeError", function () { });
        localStorageService.set("nodeInfo", new app.models.NodeInfo("ISSUER", "52.172.46.253", 8182));
        $rootScope.$on("$stateChangeStart", function (event, toState) {
            if (!authService.isAuthenticated && toState.name !== "login") {
                console.log("DENY : Redirecting to Login");
                $timeout(function () {
                    $state.transitionTo("login");
                }, 1000);
            }
            else {
                console.log("ALLOW");
            }
        });
        $timeout(function () {
            $state.transitionTo("login");
        }, 1000);
    }
})();
//# sourceMappingURL=app.run.js.map
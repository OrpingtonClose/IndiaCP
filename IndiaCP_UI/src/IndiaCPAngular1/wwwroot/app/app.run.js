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
        "app.services.AuthenticationService"
    ];
    function run($state, $rootScope, $cookies, $timeout, $location, authService) {
        $rootScope.$on("$routeChangeError", function () { });
        // $rootScope.$on("$stateChangeStart", function (event:ng.IAngularEvent, toState:any):void {
        //    if (!authService.isAuthenticated && toState.name !== "login") {
        //        console.log("DENY : Redirecting to Login");
        //        event.preventDefault();
        //        $timeout(function () {
        //            $state.transitionTo("login");
        //        }, 1000);
        //    }
        //    else {
        //        console.log("ALLOW");
        //    }
        // });
        $timeout(function () {
            $state.transitionTo("main.dashboard");
        }, 1000);
    }
})();
//# sourceMappingURL=app.run.js.map
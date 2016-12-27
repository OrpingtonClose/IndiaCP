(function () {
    "use strict";
    angular
        .module("app")
        .run(run);
    run.$inject = [
        "$state",
        "$rootScope",
        "$cookies",
        "$timeout"
    ];
    function run($state, $rootScope, $cookies, $timeout) {
        $rootScope.$on("$routeChangeError", function () { });
        // "currentUser",
        // currentUser: ICurrentUser,
        // currentUser.userId = $cookies.userId;
        // pendingPostNotifyService.run();
        $timeout(function () {
            $state.transitionTo("main.dashboard");
        }, 1000);
    }
})();
//# sourceMappingURL=app.run.js.map
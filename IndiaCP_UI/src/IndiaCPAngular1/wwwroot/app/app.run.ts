interface IAppCookies {
    userId: string;
}

((): void => {
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
    function run(
        $state: ng.ui.IStateService,
        $rootScope: ng.IRootScopeService,
        $cookies: IAppCookies,
        $timeout: ng.ITimeoutService): void {

        $rootScope.$on("$routeChangeError", (): void => { });
        // "currentUser",
        // currentUser: ICurrentUser,
        // currentUser.userId = $cookies.userId;
        // pendingPostNotifyService.run();
        $timeout(function () {
            $state.transitionTo("main.dashboard");
        },1000);
    }
})();
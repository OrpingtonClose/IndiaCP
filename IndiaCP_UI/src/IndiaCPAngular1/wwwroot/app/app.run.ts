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
        "$timeout",
        "$location",
        "app.services.AuthenticationService"
    ];
    function run(
        $state: ng.ui.IStateService,
        $rootScope: ng.IRootScopeService,
        $cookies: IAppCookies,
        $timeout: ng.ITimeoutService,
        $location: ng.ILocationService,
        authService: app.services.IAuthenticationService): void {

        $rootScope.$on("$routeChangeError", (): void => { });
        // "currentUser",
        // currentUser: ICurrentUser,
        // currentUser.userId = $cookies.userId;
        // pendingPostNotifyService.run();

        $rootScope.$on("$stateChangeStart", function (event:ng.IAngularEvent, toState:any):void {
            if (!authService.isAuthenticated() && toState.name !== "login") {
                console.log("DENY : Redirecting to Login");
                event.preventDefault();
                $timeout(function () {
                    $state.transitionTo("login");
                }, 1000);
            }
            else {
                console.log("ALLOW");
            }
        });
        $timeout(function () {
             $state.transitionTo("main.dashboard");
                }, 1000);

    }
})();
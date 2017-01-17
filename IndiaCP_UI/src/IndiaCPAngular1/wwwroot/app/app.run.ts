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
        "$trace",
        "localStorageService",
        "app.services.AuthenticationService"
    ];
    function run(
        $state: ng.ui.IStateService,
        $rootScope: ng.IRootScopeService,
        $cookies: IAppCookies,
        $timeout: ng.ITimeoutService,
        $location: ng.ILocationService,
        $trace: any,
        localStorageService: ng.local.storage.ILocalStorageService,
        authService: app.services.IAuthenticationService): void {

        //$trace.enable('TRANSITION');
        $rootScope.$on("$routeChangeError", (): void => { });
        localStorageService.set("nodeInfo", new app.models.NodeInfo("DEPOSITORY", "52.172.46.253", 8183,"NSDL"));


        $rootScope.$on("$stateChangeStart", function (event: ng.IAngularEvent, toState: any): void {
            if (!authService.isAuthenticated && toState.name !== "login") {
                console.log("DENY : Redirecting to Login");
                $timeout(function () {
                    $state.transitionTo("login");
                }, 1000);
            }
            else{
                console.log("ALLOW");
            }
        });
        $timeout(function () {
            $state.transitionTo("main.depository");
        }, 1000);
    }
})();
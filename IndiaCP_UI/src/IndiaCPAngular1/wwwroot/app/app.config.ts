(function (): void {
    "use strict";
    angular
        .module("app")
        .config(config);

    config.$inject = ["$locationProvider",
        "$httpProvider",
        "growlProvider",
        "localStorageServiceProvider"];
    function config($locationProvider: ng.ILocationProvider,
        $httpProvider: ng.IHttpProvider,
        growlProvider: ng.growl.IGrowlProvider,
        localStorageServiceProvider: ng.local.storage.ILocalStorageServiceProvider): void {

        // localStorage Configuration
        localStorageServiceProvider.setPrefix("indiacp");

        $locationProvider.html5Mode(true);
        $httpProvider.interceptors.push(app.blocks.ApiCallInterceptor.factory);

        // growl Notifications configuration
        growlProvider.globalTimeToLive(8000);
        growlProvider.globalDisableCountDown(true);
    }
})();
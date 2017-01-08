(function (): void {
    "use strict";
    angular
    .module("app")
    .config(config);

    config.$inject = ["$locationProvider","$httpProvider","growlProvider", "localStorageServiceProvider"];
    function config($locationProvider: ng.ILocationProvider,$httpProvider:ng.IHttpProvider, growlProvider: ng.growl.IGrowlProvider, localStorageServiceProvider:ng.local.storage.ILocalStorageServiceProvider): void {
        
        //LocalStorage Configuration
        localStorageServiceProvider.setPrefix("indiacp");
        
        $locationProvider.html5Mode(true);
        $httpProvider.interceptors.push(app.blocks.ApiCallInterceptor.factory);
        
        //Growl Notifications configuration
        growlProvider.globalTimeToLive(4000);
        growlProvider.globalDisableCountDown(true);
    }
})();
(function (): void {
    "use strict";
    angular
    .module("app")
    .config(config);

    config.$inject = ["$locationProvider", "growlProvider"];
    function config($locationProvider: ng.ILocationProvider, growlProvider: ng.growl.IGrowlProvider): void {
        $locationProvider.html5Mode(true);

        //Growl Notifications configuration
        growlProvider.globalTimeToLive(4000);
        growlProvider.globalDisableCountDown(true);
    }
})();
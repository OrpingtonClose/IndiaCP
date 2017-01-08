(function () {
    "use strict";
    angular
        .module("app")
        .config(config);
    config.$inject = ["$locationProvider", "growlProvider"];
    function config($locationProvider, growlProvider) {
        $locationProvider.html5Mode(true);
        //Growl Notifications configuration
        growlProvider.globalTimeToLive(4000);
        growlProvider.globalDisableCountDown(true);
    }
})();
//# sourceMappingURL=app.config.js.map
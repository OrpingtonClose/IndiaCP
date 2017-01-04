(function (): void {
    "use strict";

    angular
        .module("app", [
            /*Core areas*/
            "app.core",
            "app.layout",
            "app.services",
            "app.blocks",

            /*Feature areas*/
            "app.login",
            "app.main",
            "app.models",
            "app.dashboard",
            "app.dashboard.isingeneration",
            "app.dashboard.cpissue",
            "app.dashboard.cpprogramcreate",
            "app.dashboard.cpprogramdetails",
            "app.sitesettings",
            "app.usersettings",
            "app.users"
        ]);
})();


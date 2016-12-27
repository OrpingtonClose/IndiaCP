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
            "app.dashboard",
            "app.sitesettings",
            "app.usersettings"
        ]);
})();


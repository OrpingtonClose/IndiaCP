(function () {
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
        "app.dashboard",
        "app.dashboard.isingeneration",
        "app.dashboard.cpissue",
        "app.sitesettings",
        "app.usersettings",
        "app.users"
    ]);
})();
//# sourceMappingURL=app.module.js.map
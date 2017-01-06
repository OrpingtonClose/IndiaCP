
((): void => {
    "use strict";
    var currentUser: app.models.CurrentUser = {
        username: "",
        password: ""

    };

    angular
        .module("app")
        .value("currentUser", currentUser);
})();
(function () {
    "use strict";
    var currentUser = {
        username: "",
        password: ""
    };
    angular
        .module("app")
        .value("currentUser", currentUser);
})();
//# sourceMappingURL=app.values.js.map
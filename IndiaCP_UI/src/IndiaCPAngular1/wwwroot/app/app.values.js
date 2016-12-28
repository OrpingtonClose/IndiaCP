(function () {
    "use strict";
    var currentUser = {
        userId: "",
        password: ""
    };
    angular
        .module("app")
        .value("currentUser", currentUser);
})();
//# sourceMappingURL=app.values.js.map
var app;
(function (app) {
    var main;
    (function (main) {
        "use strict";
        var MainController = (function () {
            function MainController() {
            }
            return MainController;
        }());
        angular
            .module("app.main")
            .controller("app.main.MainController", MainController);
    })(main = app.main || (app.main = {}));
})(app || (app = {}));
//# sourceMappingURL=main.controller.js.map
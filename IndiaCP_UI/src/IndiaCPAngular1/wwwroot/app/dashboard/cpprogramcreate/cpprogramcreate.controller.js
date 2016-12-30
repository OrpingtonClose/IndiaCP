var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramcreate;
        (function (cpprogramcreate) {
            "use strict";
            var CPProgramCreateController = (function () {
                function CPProgramCreateController($sce) {
                    this.$sce = $sce;
                }
                CPProgramCreateController.prototype.issueCP = function () {
                };
                return CPProgramCreateController;
            }());
            CPProgramCreateController.$inject = ["$sce"];
            angular
                .module("app.dashboard.cpprogramcreate")
                .controller("app.dashboard.cpprogramcreate.CPProgramCreateController", CPProgramCreateController);
        })(cpprogramcreate = dashboard.cpprogramcreate || (dashboard.cpprogramcreate = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramcreate.controller.js.map
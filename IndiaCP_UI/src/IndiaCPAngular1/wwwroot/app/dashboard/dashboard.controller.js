var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        "use strict";
        var DashboardController = (function () {
            function DashboardController($scope, $uibModal, issuerService) {
            }
            DashboardController.prototype.fetchAllCPPrograms = function () {
            };
            return DashboardController;
        }());
        angular
            .module("app.dashboard")
            .controller("app.dashboard.DashboardController", DashboardController);
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=dashboard.controller.js.map
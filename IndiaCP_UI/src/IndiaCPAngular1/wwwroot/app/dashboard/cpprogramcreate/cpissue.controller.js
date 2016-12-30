var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpissue;
        (function (cpissue) {
            "use strict";
            var CPIssueController = (function () {
                function CPIssueController($sce) {
                    this.$sce = $sce;
                }
                CPIssueController.prototype.issueCP = function () {
                };
                return CPIssueController;
            }());
            CPIssueController.$inject = ["$sce"];
            angular
                .module("app.dashboard.cpissue")
                .controller("app.dashboard.cpissue.CPIssueController", CPIssueController);
        })(cpissue = dashboard.cpissue || (dashboard.cpissue = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpissue.controller.js.map
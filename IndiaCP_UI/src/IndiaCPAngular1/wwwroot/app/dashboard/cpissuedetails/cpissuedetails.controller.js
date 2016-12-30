var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpissuedetails;
        (function (cpissuedetails) {
            "use strict";
            var CPIssueDetailsController = (function () {
                function CPIssueDetailsController() {
                }
                CPIssueDetailsController.prototype.issueCP = function () {
                };
                return CPIssueDetailsController;
            }());
            angular
                .module("app.dashboard.cpissuedetails")
                .controller("app.dashboard.cpissuedetails.CPIssueDetailsController", CPIssueDetailsController);
        })(cpissuedetails = dashboard.cpissuedetails || (dashboard.cpissuedetails = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpissuedetails.controller.js.map
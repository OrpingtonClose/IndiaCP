var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpissuedetails;
        (function (cpissuedetails) {
            "use strict";
            var CPIssueDetailsController = (function () {
                function CPIssueDetailsController($uibModalInstance, issuerService, cpProgram) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.cpProgram = cpProgram;
                }
                CPIssueDetailsController.prototype.issueCPForProgram = function () {
                };
                CPIssueDetailsController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return CPIssueDetailsController;
            }());
            CPIssueDetailsController.$inject = ["$uibModalInstance", "app.services.IssuerService", "cpProgram"];
            angular
                .module("app.dashboard.cpissuedetails")
                .controller("app.dashboard.cpissuedetails.CPIssueDetailsController", CPIssueDetailsController);
        })(cpissuedetails = dashboard.cpissuedetails || (dashboard.cpissuedetails = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpissuedetails.controller.js.map
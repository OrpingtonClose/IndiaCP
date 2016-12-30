var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpissue;
        (function (cpissue) {
            "use strict";
            var CPIssueController = (function () {
                function CPIssueController($sce, $uibModalInstance, issuerService) {
                    this.$sce = $sce;
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                }
                CPIssueController.prototype.issueCP = function () {
                };
                CPIssueController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return CPIssueController;
            }());
            CPIssueController.$inject = ["$sce", "$uibModalInstance", "app.services.IssuerService"];
            angular
                .module("app.dashboard.cpissue")
                .controller("app.dashboard.cpissue.CPIssueController", CPIssueController);
        })(cpissue = dashboard.cpissue || (dashboard.cpissue = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpissue.controller.js.map
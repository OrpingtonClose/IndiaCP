var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramdetails;
        (function (cpprogramdetails) {
            "use strict";
            var CPProgramDetailsController = (function () {
                function CPProgramDetailsController($uibModalInstance, issuerService, growl, cpProgramId) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.growl = growl;
                    this.cpProgramId = cpProgramId;
                    this.fetchCP();
                    this.fetchTransactionHistory();
                }
                CPProgramDetailsController.prototype.fetchCP = function () {
                    var _this = this;
                    this.issuerService.fetchCPProgram(this.cpProgramId).then(function (response) {
                        _this.cpprogram = response.data;
                        _this.cpprogramMaturityDate = new Date(_this.cpprogram.issueCommencementDate);
                        _this.cpprogramMaturityDate.setDate(_this.cpprogramMaturityDate.getDate() + _this.cpprogram.maturityDays);
                    }, function (error) {
                        console.log("CPProgram could not be fetched.");
                    });
                };
                CPProgramDetailsController.prototype.fetchTransactionHistory = function () {
                    var _this = this;
                    this.issuerService.CPProgramGetTransactionHistory(this.cpProgramId).then(function (response) {
                        _this.transactionHistory = response.data;
                    }, function (error) {
                        _this.growl.error("Could not fetch transaction history.", { title: "Error!" });
                        console.log("Could not fetch transaction history. " + error);
                    });
                };
                CPProgramDetailsController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return CPProgramDetailsController;
            }());
            CPProgramDetailsController.$inject = ["$uibModalInstance", "app.services.IssuerService", "growl", "programId"];
            angular
                .module("app.dashboard.cpprogramdetails")
                .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController", CPProgramDetailsController);
        })(cpprogramdetails = dashboard.cpprogramdetails || (dashboard.cpprogramdetails = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramdetails.controller.js.map
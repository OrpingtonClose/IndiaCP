var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramdetails;
        (function (cpprogramdetails) {
            "use strict";
            var CPProgramDetailsController = (function () {
                function CPProgramDetailsController($uibModalInstance, issuerService, cpProgramId) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.cpProgramId = cpProgramId;
                    this.fetchCP();
                }
                CPProgramDetailsController.prototype.fetchCP = function () {
                    var _this = this;
                    this.issuerService.fetchCPProgram(this.cpProgramId).then(function (response) {
                        _this.cpprogram = response.data;
                        _this.cpprogram.maturityDate = new Date(_this.cpprogram.maturityDate.epochSecond * 1000);
                    }, function (error) {
                        console.log("CPProgram could not be fetched.");
                    });
                };
                CPProgramDetailsController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return CPProgramDetailsController;
            }());
            CPProgramDetailsController.$inject = ["$uibModalInstance", "app.services.IssuerService", "programId"];
            angular
                .module("app.dashboard.cpprogramdetails")
                .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController", CPProgramDetailsController);
        })(cpprogramdetails = dashboard.cpprogramdetails || (dashboard.cpprogramdetails = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramdetails.controller.js.map
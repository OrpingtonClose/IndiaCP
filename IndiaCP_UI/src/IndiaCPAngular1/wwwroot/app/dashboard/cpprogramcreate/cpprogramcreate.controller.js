var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramcreate;
        (function (cpprogramcreate) {
            "use strict";
            var CPProgramCreateController = (function () {
                function CPProgramCreateController($uibModalInstance, issuerService, uuid4) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.uuid4 = uuid4;
                    this.cpprogram = new app.models.IndiaCPProgram();
                    this.cpprogram.issuerId = "Issuer";
                    this.cpprogram.programCurrency = "INR";
                    this.cpprogram.issuerName = "Issuer";
                    this.cpprogram.purpose = "ICP";
                    this.cpprogram.issueCommencementDate = new Date();
                    this.cpprogram.type = "India Commercial Paper";
                    this.cpprogram.maturityDays = 7;
                    this.cpprogram.programSize = 1000;
                    this.cpprogram.depositoryId = "Issuer";
                    this.cpprogram.depositoryName = "Issuer";
                    this.cpprogram.ipaId = "Issuer";
                    this.cpprogram.ipaName = "Issuer";
                }
                CPProgramCreateController.prototype.createCPProgram = function () {
                    var _this = this;
                    this.issuerService.issueCPProgram(this.cpprogram).then(function () {
                        console.log("CPProgram created");
                        _this.$uibModalInstance.close();
                    }, function (error) {
                        console.log("CPProgram not created.");
                    });
                };
                CPProgramCreateController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                CPProgramCreateController.prototype.updateProgramId = function () {
                    this.cpprogram.programId = this.cpprogram.name + "-" + this.uuid4.generate();
                };
                return CPProgramCreateController;
            }());
            CPProgramCreateController.$inject = ["$uibModalInstance", "app.services.IssuerService", "uuid4"];
            angular
                .module("app.dashboard.cpprogramcreate")
                .controller("app.dashboard.cpprogramcreate.CPProgramCreateController", CPProgramCreateController);
        })(cpprogramcreate = dashboard.cpprogramcreate || (dashboard.cpprogramcreate = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramcreate.controller.js.map
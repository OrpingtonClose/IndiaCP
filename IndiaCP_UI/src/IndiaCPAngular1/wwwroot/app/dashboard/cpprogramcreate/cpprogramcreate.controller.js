var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramcreate;
        (function (cpprogramcreate) {
            "use strict";
            var CPProgramCreateController = (function () {
                function CPProgramCreateController($uibModalInstance, issuerService, uuid4, growl, localStorageService) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.uuid4 = uuid4;
                    this.growl = growl;
                    this.localStorageService = localStorageService;
                    this.ipacollection = [{ displayName: "HDFC", id: "HDFC_IPA" }, { displayName: "SBI", id: "SBI_IPA" }];
                    this.depositorycollection = [{ displayName: "NSDL", id: "NSDL_DEPOSITORY" }, { displayName: "CDSL", id: "CDSL_IPA" }];
                    this.nodeInfo = this.localStorageService.get("nodeInfo");
                    this.cpprogram = new app.models.IndiaCPProgram();
                    this.cpprogram.issuerId = this.nodeInfo.dlNodeName;
                    this.cpprogram.programCurrency = "INR";
                    this.cpprogram.issuerName = this.nodeInfo.nodeName;
                    this.cpprogram.purpose = "ICP";
                    this.cpprogram.issueCommencementDate = new Date();
                    this.cpprogram.type = "India Commercial Paper";
                    this.cpprogram.maturityDays = 7;
                    this.cpprogram.programSize = 1000;
                    this.cpprogram.depositoryId = "HDFC_IPA";
                    this.cpprogram.depositoryName = "HDFC";
                    this.cpprogram.ipaId = "NSDL_DEPOSITORY";
                    this.cpprogram.ipaName = "NSDL";
                }
                CPProgramCreateController.prototype.createCPProgram = function () {
                    var _this = this;
                    this.issuerService.issueCPProgram(this.cpprogram).then(function () {
                        console.log("CPProgram created");
                        _this.growl.success("CPProgram created suceesfully.", { title: "Success!" });
                        _this.$uibModalInstance.close();
                    }, function (error) {
                        console.log("CPProgram not created.");
                        _this.growl.error("CPProgram not created.", { title: "Error!" });
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
            CPProgramCreateController.$inject = ["$uibModalInstance", "app.services.IssuerService", "uuid4", "growl", "localStorageService"];
            angular
                .module("app.dashboard.cpprogramcreate")
                .controller("app.dashboard.cpprogramcreate.CPProgramCreateController", CPProgramCreateController);
        })(cpprogramcreate = dashboard.cpprogramcreate || (dashboard.cpprogramcreate = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramcreate.controller.js.map
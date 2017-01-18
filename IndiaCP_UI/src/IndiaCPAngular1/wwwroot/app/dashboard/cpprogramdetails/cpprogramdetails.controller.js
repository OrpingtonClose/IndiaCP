var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramdetails;
        (function (cpprogramdetails) {
            "use strict";
            var CPProgramDetailsController = (function () {
                function CPProgramDetailsController($uibModalInstance, $uibModal, issuerService, investorService, growl, cpProgramId) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.$uibModal = $uibModal;
                    this.issuerService = issuerService;
                    this.investorService = investorService;
                    this.growl = growl;
                    this.cpProgramId = cpProgramId;
                    this.fetchCPProgram();
                    this.fetchTransactionHistory();
                }
                CPProgramDetailsController.prototype.fetchCPProgram = function () {
                    var _this = this;
                    this.issuerService.fetchCPProgram(this.cpProgramId).then(function (response) {
                        _this.cpprogram = response.data;
                        _this.cpprogramMaturityDate = new Date(_this.cpprogram.issueCommencementDate);
                        _this.cpprogramMaturityDate.setDate(_this.cpprogramMaturityDate.getDate() + _this.cpprogram.maturityDays);
                    }, function (error) {
                        console.log("CPProgram could not be fetched.");
                    });
                };
                CPProgramDetailsController.prototype.fetchCPIssuesForProgram = function () {
                    var _this = this;
                    this.investorService.fetchAllCP(this.cpProgramId).then(function (response) {
                        _this.cpIssues = response.data;
                        console.log("CPIssues for CP Program fetched");
                    }, function (error) {
                        _this.growl.error("Could not fetch cpissues for program.", { title: "Error!" });
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
                CPProgramDetailsController.prototype.showISINDocument = function () {
                    this.$uibModal.open({
                        animation: true,
                        ariaLabelledBy: "modal-title",
                        ariaDescribedBy: "modal-body",
                        controller: "app.dashboard.isingeneration.ISINGenerationController",
                        controllerAs: "vm",
                        size: "lg",
                        backdrop: "static",
                        templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                        resolve: { cpProgram: this.cpprogram }
                    });
                };
                CPProgramDetailsController.prototype.showIPAVerification = function () {
                    this.$uibModal.open({
                        animation: true,
                        ariaLabelledBy: "modal-title",
                        ariaDescribedBy: "modal-body",
                        controller: "app.dashboard.isingeneration.ISINGenerationController",
                        controllerAs: "vm",
                        size: "lg",
                        backdrop: "static",
                        templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                        resolve: { cpProgram: this.cpprogram }
                    });
                };
                CPProgramDetailsController.prototype.showIPACertificate = function () {
                    this.$uibModal.open({
                        animation: true,
                        ariaLabelledBy: "modal-title",
                        ariaDescribedBy: "modal-body",
                        controller: "app.dashboard.isingeneration.ISINGenerationController",
                        controllerAs: "vm",
                        size: "lg",
                        backdrop: "static",
                        templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                        resolve: { cpProgram: this.cpprogram }
                    });
                };
                CPProgramDetailsController.prototype.showCorpActionForm = function () {
                    this.$uibModal.open({
                        animation: true,
                        ariaLabelledBy: "modal-title",
                        ariaDescribedBy: "modal-body",
                        controller: "app.dashboard.isingeneration.ISINGenerationController",
                        controllerAs: "vm",
                        size: "lg",
                        backdrop: "static",
                        templateUrl: "app/dashboard/isingeneration/isingeneration.html",
                        resolve: { cpProgram: this.cpprogram }
                    });
                };
                CPProgramDetailsController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return CPProgramDetailsController;
            }());
            CPProgramDetailsController.$inject = ["$uibModalInstance",
                "$uibModal",
                "app.services.IssuerService",
                "app.services.InvestorService",
                "growl",
                "programId"];
            angular
                .module("app.dashboard.cpprogramdetails")
                .controller("app.dashboard.cpprogramdetails.CPProgramDetailsController", CPProgramDetailsController);
        })(cpprogramdetails = dashboard.cpprogramdetails || (dashboard.cpprogramdetails = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramdetails.controller.js.map
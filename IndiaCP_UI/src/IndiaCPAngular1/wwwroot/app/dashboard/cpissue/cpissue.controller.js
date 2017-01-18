var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpissue;
        (function (cpissue) {
            "use strict";
            var CPIssueController = (function () {
                function CPIssueController($uibModalInstance, issuerService, uuid4, cpProgram, growl) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.uuid4 = uuid4;
                    this.cpProgram = cpProgram;
                    this.growl = growl;
                    this.cpissue = new app.models.IndiaCPIssue();
                    this.cpissue.cpProgramId = this.cpProgram.programId;
                    this.cpissue.bookId = "book1";
                    // this.cpissue.isin = this.cpProgram.isin;
                    this.cpissue.isin = "INE123456";
                    this.cpissue.traderId = "trader1";
                    this.cpissue.issuerId = this.cpProgram.issuerId;
                    this.cpissue.issuerName = this.cpProgram.issuerName;
                    this.cpissue.cpTradeId = this.cpissue.issuerName + "-" + this.uuid4.generate();
                    this.cpissue.investorId = "BSS_INVESTOR";
                    this.cpissue.investorName = "Barclays Shared Services";
                    this.cpissue.beneficiaryId = this.cpProgram.issuerId;
                    this.cpissue.beneficiaryName = this.cpProgram.issuerName;
                    this.cpissue.ipaId = this.cpProgram.ipaId;
                    this.cpissue.ipaName = this.cpProgram.ipaName;
                    this.cpissue.depositoryId = this.cpProgram.depositoryId;
                    this.cpissue.depositoryName = this.cpProgram.depositoryName;
                    this.cpissue.tradeDate = new Date();
                    this.cpissue.valueDate = new Date();
                    this.cpissue.valueDate.setDate(new Date().getDate() + 2);
                    this.cpissue.maturityDays = this.cpProgram.maturityDays;
                    this.cpissue.currency = this.cpProgram.programCurrency;
                    this.cpissue.facevaluePerUnit = 100;
                    this.cpissue.noOfUnits = 10;
                    this.cpissue.rate = 7;
                    this.cpissue.investorSettlementDetails = new app.models.SettlementDetails();
                    this.cpissue.investorSettlementDetails.depositoryAccountDetails = [new app.models.DepositoryAccountDetails()];
                    this.cpissue.ipaSettlementDetails = new app.models.SettlementDetails();
                    this.cpissue.ipaSettlementDetails.depositoryAccountDetails = [new app.models.DepositoryAccountDetails(), new app.models.DepositoryAccountDetails()];
                    this.cpissue.issuerSettlementDetails = new app.models.SettlementDetails();
                    this.cpissue.issuerSettlementDetails.paymentAccountDetails = new app.models.PaymentAccountDetails();
                }
                CPIssueController.prototype.issueCP = function () {
                    var _this = this;
                    this.issuerService.issueCP(this.cpissue).then(function () {
                        console.log("CP Issue created");
                        _this.growl.success("CP Issue created for " + _this.cpProgram.name + ".", { title: "Success!" });
                    }, function (error) {
                        console.log("CP Issue not created" + error);
                        _this.growl.error("CP Issue not created", { title: "Error" });
                    });
                };
                CPIssueController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                CPIssueController.prototype.updateTradeId = function () {
                    this.cpissue.cpTradeId = this.uuid4.generate();
                };
                CPIssueController.prototype.updateMaturityandIssueVal = function () {
                    this.maturityValue = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
                    this.issueValue = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
                };
                CPIssueController.prototype.updateRate = function () {
                    this.cpissue.rate = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
                };
                return CPIssueController;
            }());
            CPIssueController.$inject = ["$uibModalInstance", "app.services.IssuerService", "uuid4", "cpProgram", "growl"];
            angular
                .module("app.dashboard.cpissue")
                .controller("app.dashboard.cpissue.CPIssueController", CPIssueController);
        })(cpissue = dashboard.cpissue || (dashboard.cpissue = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpissue.controller.js.map
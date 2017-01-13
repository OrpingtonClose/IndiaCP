var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var isingeneration;
        (function (isingeneration) {
            "use strict";
            var ISINGenerationController = (function () {
                function ISINGenerationController($sce, $uibModalInstance, issuerService, Upload, growl, cpProgram) {
                    this.$sce = $sce;
                    this.$uibModalInstance = $uibModalInstance;
                    this.issuerService = issuerService;
                    this.Upload = Upload;
                    this.growl = growl;
                    this.cpProgram = cpProgram;
                    this.docRefData = new app.models.DocData();
                    this.docRefData.cp.issuerName = "Barclays Investments & Loans (India) Ltd";
                    this.docRefData.cp.secondParty = "";
                    this.docRefData.cp.desc = "Commercial Paper 96";
                    this.docRefData.cp.boardResolutionDate = new Date();
                    this.docRefData.cp.valueDate = new Date();
                    this.docRefData.cp.tradeDate = new Date();
                    this.docRefData.cp.dateOfContract = new Date();
                    this.docRefData.cp.allotDate = new Date();
                    this.docRefData.cp.matDate = new Date();
                    this.docRefData.cp.issueValue = this.cpProgram.programAllocatedValue;
                    this.docRefData.cp.maturityValue = this.cpProgram.programAllocatedValue;
                    this.docRefData.cp.redemValue = this.cpProgram.programAllocatedValue;
                    this.docRefData.cp.totalAmount = 15000000000;
                    this.docRefData.cp.tax = 10000;
                    this.docRefData.cp.CIN = "U93090TN1937FLC001429";
                    this.docRefData.cp.NSDLHistory = "Yes";
                    this.docRefData.cp.issuerAddress = "Regus Chennai Citi Centre Level 6; 10/11; Dr.Radhakrishnan Salai Mylapore; Chennai; Tamilnadu-600004";
                    this.docRefData.cp.officialContactNum = "+91 (0)22 6175 4000";
                    this.docRefData.cp.officialFaxNum = "+91 (0)22 6175 4099";
                    this.docRefData.cp.officialEmail = "bililcompliance@barclayscapital.com";
                    this.docRefData.cp.officialWebsite = "barclays.in/bilil";
                    this.docRefData.cp.entityType = "Joint Stock Company";
                    this.docRefData.cp.price = "96.9018";
                    this.docRefData.cp.discRate = 7.78;
                    this.docRefData.cp.currency = this.cpProgram.programCurrency;
                    this.docRefData.cp.issueRef = "CP/2015/95";
                    this.docRefData.cp.isinCode = "INE704I14676";
                    this.docRefData.cp.creditRating = "ICRA A1 +";
                    this.docRefData.cp.ratingIssuedBy = "ICRA";
                    this.docRefData.cp.dateOfRating = new Date();
                    this.docRefData.cp.ratingValidityDate = new Date();
                    this.docRefData.cp.effectiveDateOfRating = new Date();
                    this.docRefData.cp.contactPerson = "";
                    this.docRefData.cp.complianceOfficer = "Ms. Yagya Turker";
                    this.docRefData.cp.complianceOfficerDept = "Compliance officer";
                    this.docRefData.cp.complianceOfficerAddress = "Nirlon Knowledge Park; 10th Floor; Block B-6; Off Western Express Highway; Goregaon (East);Mumbai India 400 063";
                    this.docRefData.cp.complianceOfficerContactNo = "61754109";
                    this.docRefData.cp.complianceOfficerEmail = "yagya.turker@barclays.com";
                    this.docRefData.cp.investorRelationsOfficer = "Ruzbeh Sutaria";
                    this.docRefData.cp.investorRelationsOfficerDept = "Manager";
                    this.docRefData.cp.investorRelationsOfficerAddress = "R-1;Nowroze Baug; Lalbaug; Mumbai-12";
                    this.docRefData.cp.investorRelationsOfficerContactNo = "61754244";
                    this.docRefData.cp.investorRelationsOfficerEmail = "Ruzbeh.sutaria@barclays.com";
                    this.docRefData.cp.operationsOfficer = "Mr. Avinash Ahirekar";
                    this.docRefData.cp.operationsOfficerDept = "Vice President";
                    this.docRefData.cp.operationsOfficerAddress = "Nirlon Knowledge Park; 9th Floor; Block B-6; Off Western Express Highway; Goregaon (East); Mumbai India 400 063";
                    this.docRefData.cp.operationsOfficerContactNo = "022-61754346/48/103";
                    this.docRefData.cp.operationsOfficerEmail = "mumbainbfcops@barclays.com";
                    this.docRefData.cp.conditions = "N.A.";
                    this.docRefData.cp.creditSupport = "N.A.";
                    this.docRefData.cp.creditInstructions = "Barclays Investments & Loans (India) Ltd CP   Current A/c Number 00600350084976 with HDFC Bank Ltd; Through RTGS IFSC Code HDFC0000060  for Rs. 242;254;500/-.";
                    this.docRefData.cp.marketConventions = "FIMMDA Conventions";
                    this.docRefData.cp.businessActivity = "NBFC";
                    this.docRefData.cp.stockExchange = "Bombay Stock Exchange";
                    this.docRefData.cp.netWorth = 5801.08;
                    this.docRefData.cp.workingCapitalLimit = 1000000000;
                    this.docRefData.cp.outstandingBankBorrowing = 0;
                    this.docRefData.cp.faceValue = 500000;
                    this.docRefData.cp.amountOfCPOutstanding = 14250000000.00;
                    this.docDetails = new app.models.DocData();
                    this.docDetails.cpProgramId = this.cpProgram.programId;
                    this.docDetails.docExtension = app.models.DOCEXTENSION.PDF;
                    this.docDetails.docStatus = app.models.DOCSTATUS.SIGNED_BY_ISSUER;
                    this.docDetails.docSubType = app.models.DOCTYPE.DEPOSITORY_DOCS;
                    this.docDetails.modifiedBy = "Ritu";
                }
                ISINGenerationController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                ISINGenerationController.prototype.generateDocument = function (file) {
                    this.isinFile = file;
                    this.isinFileUrl = this.$sce.trustAsResourceUrl(URL.createObjectURL(file));
                };
                ISINGenerationController.prototype.sign = function () {
                    var _this = this;
                    var httpUploadRequestParams = {
                        url: "http://52.172.46.253:8182/indiacp/indiacpdocuments/signDoc/ISINDocument",
                        data: { file: this.isinFile },
                        method: "POST"
                    };
                    this.Upload.upload(httpUploadRequestParams).
                        then(function (response) {
                        _this.growl.success("ISIN document signed succesfully", { title: "ISIN Signed!" });
                        _this.isinSignedData = response.data;
                        var url = "data:application/pdf;base64," + _this.isinSignedData;
                        _this.isinFileUrl = _this.$sce.trustAsResourceUrl(url);
                    }, function (error) {
                        _this.growl.error("Document signing unsuccesful", { title: "Signing Failed!" });
                    });
                };
                ISINGenerationController.prototype.verify = function () {
                };
                ISINGenerationController.prototype.upload = function () {
                    var _this = this;
                    var tempFile = new File([new Blob([window.atob(this.isinSignedData)], { type: "application/pdf" })], "isinDoc.pdf");
                    this.issuerService.addDoc(this.cpProgram.programId, this.docDetails, tempFile).
                        then(function (response) {
                        console.log(response);
                        _this.growl.success("ISIN document uploaded succesfully", { title: "ISIN Document Uploaded." });
                    }, function (error) {
                        var errorMssg = error.data;
                        console.log(errorMssg.source + "-" + errorMssg.message);
                        _this.growl.error(errorMssg.message, { title: "Upload Failed - " + errorMssg.source });
                    });
                };
                return ISINGenerationController;
            }());
            ISINGenerationController.$inject = ["$sce",
                "$uibModalInstance",
                "app.services.IssuerService",
                "Upload",
                "growl",
                "cpProgram"];
            angular
                .module("app.dashboard.isingeneration")
                .controller("app.dashboard.isingeneration.ISINGenerationController", ISINGenerationController);
        })(isingeneration = dashboard.isingeneration || (dashboard.isingeneration = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=isingeneration.controller.js.map
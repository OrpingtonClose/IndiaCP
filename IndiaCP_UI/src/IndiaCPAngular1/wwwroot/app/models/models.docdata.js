var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var DocData = (function () {
            function DocData() {
            }
            return DocData;
        }());
        models.DocData = DocData;
        var DOCTYPE;
        (function (DOCTYPE) {
            DOCTYPE[DOCTYPE["DEPOSITORY_DOCS"] = "DEPOSITORY_DOCS"] = "DEPOSITORY_DOCS";
        })(DOCTYPE = models.DOCTYPE || (models.DOCTYPE = {}));
        var DOCSTATUS;
        (function (DOCSTATUS) {
            DOCSTATUS[DOCSTATUS["SIGNED_BY_ISSUER"] = "SIGNED_BY_ISSUER"] = "SIGNED_BY_ISSUER";
        })(DOCSTATUS = models.DOCSTATUS || (models.DOCSTATUS = {}));
        var DOCEXTENSION;
        (function (DOCEXTENSION) {
            DOCEXTENSION[DOCEXTENSION["PDF"] = "pdf"] = "PDF";
            DOCEXTENSION[DOCEXTENSION["DOCX"] = "docx"] = "DOCX";
            DOCEXTENSION[DOCEXTENSION["DOC"] = "doc"] = "DOC";
            DOCEXTENSION[DOCEXTENSION["ZIP"] = "zip"] = "ZIP";
        })(DOCEXTENSION = models.DOCEXTENSION || (models.DOCEXTENSION = {}));
        var DocRefData = (function () {
            function DocRefData() {
                this.cp = new CPDocData();
                this.investor = new InvestorDocData();
                this.nsdl = new NSDLDOcData();
                this.ipa = new IPADocData();
            }
            return DocRefData;
        }());
        models.DocRefData = DocRefData;
        var CPDocData = (function () {
            function CPDocData() {
            }
            return CPDocData;
        }());
        models.CPDocData = CPDocData;
        var InvestorDocData = (function () {
            function InvestorDocData() {
            }
            return InvestorDocData;
        }());
        models.InvestorDocData = InvestorDocData;
        var IPADocData = (function () {
            function IPADocData() {
            }
            return IPADocData;
        }());
        models.IPADocData = IPADocData;
        var NSDLDOcData = (function () {
            function NSDLDOcData() {
            }
            return NSDLDOcData;
        }());
        models.NSDLDOcData = NSDLDOcData;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.docdata.js.map
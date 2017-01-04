var app;
(function (app) {
    var dashboard;
    (function (dashboard) {
        var cpprogramcreate;
        (function (cpprogramcreate) {
            "use strict";
            var CPProgramCreateController = (function () {
                function CPProgramCreateController($sce, issuerService) {
                    this.$sce = $sce;
                    this.issuerService = issuerService;
                }
                CPProgramCreateController.prototype.createCPProgram = function () {
                    this.issuerService.issueCPProgram(this.cpprogram).then(function () {
                        console.log("CPProgram created");
                    }, function (error) {
                        console.log("CPProgram not created." + error);
                    });
                };
                return CPProgramCreateController;
            }());
            CPProgramCreateController.$inject = ["$sce", "app.services.IssuerService"];
            angular
                .module("app.dashboard.cpprogramcreate")
                .controller("app.dashboard.cpprogramcreate.CPProgramCreateController", CPProgramCreateController);
        })(cpprogramcreate = dashboard.cpprogramcreate || (dashboard.cpprogramcreate = {}));
    })(dashboard = app.dashboard || (app.dashboard = {}));
})(app || (app = {}));
//# sourceMappingURL=cpprogramcreate.controller.js.map
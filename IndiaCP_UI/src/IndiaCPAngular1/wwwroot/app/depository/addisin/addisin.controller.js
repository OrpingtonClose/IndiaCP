var app;
(function (app) {
    var depository;
    (function (depository) {
        var addisin;
        (function (addisin) {
            "use strict";
            var AddISINController = (function () {
                function AddISINController($uibModalInstance, depositoryService, uuid4, growl, cpProgram) {
                    this.$uibModalInstance = $uibModalInstance;
                    this.depositoryService = depositoryService;
                    this.uuid4 = uuid4;
                    this.growl = growl;
                    this.cpProgram = cpProgram;
                }
                AddISINController.prototype.addISIN = function () {
                    var _this = this;
                    this.depositoryService.addISIN(this.cpProgram.programId, this.isin).then(function () {
                        console.log("ISIN added");
                        _this.growl.success("ISIN added suceesfully.", { title: "Success!" });
                        _this.$uibModalInstance.close();
                    }, function (error) {
                        console.log("ISIN not added.");
                        _this.growl.error("ISIN not added.", { title: "Error!" });
                    });
                };
                AddISINController.prototype.cancel = function () {
                    this.$uibModalInstance.close();
                };
                return AddISINController;
            }());
            AddISINController.$inject = ["$uibModalInstance", "app.services.DepositoryService", "uuid4", "growl", "cpProgram"];
            angular
                .module("app.depository.addisin")
                .controller("app.depository.addisin.AddISINController", AddISINController);
        })(addisin = depository.addisin || (depository.addisin = {}));
    })(depository = app.depository || (app.depository = {}));
})(app || (app = {}));
//# sourceMappingURL=addisin.controller.js.map
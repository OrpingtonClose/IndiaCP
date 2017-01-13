var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var WorkflowStates = (function () {
            function WorkflowStates() {
                this.states = new Array();
                this.states = [new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER]),
                    new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", [NODETYPE.NSDL]),
                    new WorkflowState("ISIN_ADDED", "ISSUECP", [NODETYPE.ISSUER]),
                    new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", [NODETYPE.ISSUER, NODETYPE.INVESTOR]),
                    new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]),
                ];
            }
            return WorkflowStates;
        }());
        models.WorkflowStates = WorkflowStates;
        var WorkflowState = (function () {
            function WorkflowState(status, nextAction, nodeType) {
                this.status = status;
                this.nextAction = nextAction;
                this.nodeType = nodeType;
            }
            return WorkflowState;
        }());
        models.WorkflowState = WorkflowState;
        var NODETYPE;
        (function (NODETYPE) {
            NODETYPE[NODETYPE["ISSUER"] = 0] = "ISSUER";
            NODETYPE[NODETYPE["INVESTOR"] = 1] = "INVESTOR";
            NODETYPE[NODETYPE["NSDL"] = 2] = "NSDL";
            NODETYPE[NODETYPE["IPA"] = 3] = "IPA";
        })(NODETYPE = models.NODETYPE || (models.NODETYPE = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.workflowstates.js.map
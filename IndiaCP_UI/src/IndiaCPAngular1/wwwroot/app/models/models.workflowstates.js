var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var WorkflowStates = (function () {
            function WorkflowStates() {
                this.states = new Array();
                this.states = [
                    new WorkflowState("CP_PROGRAM_CREATED", new Action("ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER])),
                    new WorkflowState("ISIN_GEN_DOC_ADDED", new Action("ADD_ISIN", [NODETYPE.DEPOSITORY])),
                    new WorkflowState("ISIN_ADDED", new Action("ISSUECP", [NODETYPE.ISSUER])),
                    new WorkflowState("CP_ISSUEED", new Action("ADD_IPA_VERI_DOC", [NODETYPE.ISSUER])),
                    new WorkflowState("IPA_VERI_DOC_ADDED", new Action("ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]))
                ];
                // this.states = [
                // new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER]),
                // new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", [NODETYPE.NSDL]),
                // new WorkflowState("ISIN_ADDED", "ISSUECP", [NODETYPE.ISSUER]),
                // new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", [NODETYPE.ISSUER, NODETYPE.INVESTOR]),
                // new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]),
                // ];
            }
            return WorkflowStates;
        }());
        models.WorkflowStates = WorkflowStates;
        var WorkflowState = (function () {
            function WorkflowState(status, nextAction) {
                this.status = status;
                this.nextAction = nextAction;
            }
            return WorkflowState;
        }());
        models.WorkflowState = WorkflowState;
        var Action = (function () {
            function Action(name, allowedNodes) {
                this.name = name;
                this.allowedNodes = allowedNodes;
            }
            return Action;
        }());
        models.Action = Action;
        var NODETYPE;
        (function (NODETYPE) {
            NODETYPE[NODETYPE["ISSUER"] = "ISSUER"] = "ISSUER";
            NODETYPE[NODETYPE["INVESTOR"] = "INVESTOR"] = "INVESTOR";
            NODETYPE[NODETYPE["DEPOSITORY"] = "DEPOSITORY"] = "DEPOSITORY";
            NODETYPE[NODETYPE["IPA"] = "IPA"] = "IPA";
        })(NODETYPE = models.NODETYPE || (models.NODETYPE = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.workflowstates.js.map
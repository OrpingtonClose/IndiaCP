var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var WorkflowStates = (function () {
            function WorkflowStates() {
                this.states = new Array();
                this.states = [new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", "ISSUER"),
                    new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", "NSDL"),
                    new WorkflowState("ISIN_ADDED", "ISSUECP", "ISSUER"),
                    new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", "ISSUER"),
                    new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", "IPA"),
                ];
            }
            return WorkflowStates;
        }());
        models.WorkflowStates = WorkflowStates;
        //    new WorkflowState("IPA_CERT_DOC_ADDED", "", ""),
        //    new WorkflowState("CORP_ACT_FORM_DOC_ADDED", "", ""),
        //    new WorkflowState("ALLOT_LETTER_DOC_ADDED", "", ""),
        //    new WorkflowState("DEAL_CONFIRMATION_ADDED", "", ""),
        //    new WorkflowState("CP_PROGRAM_CLOSED", "", ""),
        //  new WorkflowState("", "", "")
        var WorkflowState = (function () {
            function WorkflowState(status, nextAction, nodeType) {
                this.status = status;
                this.nextAction = nextAction;
                this.nodeType = nodeType;
            }
            return WorkflowState;
        }());
        models.WorkflowState = WorkflowState;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=model.workflowstates.js.map
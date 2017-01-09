module app.models {
    "use strict";
    export class WorkflowStates {
        public states: Array<WorkflowState>;
        constructor() {
            this.states = new Array<WorkflowState>();

            this.states = [new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", "ISSUER"),
            new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", "NSDL"),
            new WorkflowState("ISIN_ADDED", "ISSUECP", "ISSUER"),
            new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", "ISSUER"),
            new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", "IPA"),
            ];
        }
    }

    //    new WorkflowState("IPA_CERT_DOC_ADDED", "", ""),
    //    new WorkflowState("CORP_ACT_FORM_DOC_ADDED", "", ""),
    //    new WorkflowState("ALLOT_LETTER_DOC_ADDED", "", ""),
    //    new WorkflowState("DEAL_CONFIRMATION_ADDED", "", ""),
    //    new WorkflowState("CP_PROGRAM_CLOSED", "", ""),
    //  new WorkflowState("", "", "")

    export class WorkflowState {
        constructor(public status: string,
            public nextAction: string,
            public nodeType: String) { }
    }
}
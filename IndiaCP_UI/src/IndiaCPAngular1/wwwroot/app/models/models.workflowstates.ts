module app.models {
    "use strict";
    export class WorkflowStates {
        public states: Array<WorkflowState>;
        constructor() {
            this.states = new Array<WorkflowState>();

            this.states = [new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER]),
            new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", [NODETYPE.NSDL]),
            new WorkflowState("ISIN_ADDED", "ISSUECP", [NODETYPE.ISSUER]),
            new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", [NODETYPE.ISSUER, NODETYPE.INVESTOR]),
            new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]),
            ];
        }
    }

    export class WorkflowState {
        constructor(public status: string,
            public nextAction: string,
            public nodeType: Array<NODETYPE>) { }
    }

    export enum NODETYPE {
        ISSUER, INVESTOR, NSDL, IPA
    }
}
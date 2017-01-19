module app.models {
    "use strict";
    export class WorkflowStates {
        public states: Array<WorkflowState>;
        constructor() {
            this.states = new Array<WorkflowState>();

            this.states = [
                new WorkflowState("CP_PROGRAM_CREATED", new Action("ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER])),
                new WorkflowState("ISIN_GEN_DOC_ADDED", new Action("ADD_ISIN", [NODETYPE.DEPOSITORY])),
                new WorkflowState("ISIN_ADDED", new Action("ISSUECP", [NODETYPE.ISSUER])),
                new WorkflowState("CP_ISSUEED", new Action("ADD_IPA_VERI_DOC", [NODETYPE.ISSUER])),
                new WorkflowState("IPA_VERI_DOC_ADDED", new Action("ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]))];


            // this.states = [
            // new WorkflowState("CP_PROGRAM_CREATED", "ADD_ISIN_GEN_DOC", [NODETYPE.ISSUER]),
            // new WorkflowState("ISIN_GEN_DOC_ADDED", "ADD_ISIN", [NODETYPE.NSDL]),
            // new WorkflowState("ISIN_ADDED", "ISSUECP", [NODETYPE.ISSUER]),
            // new WorkflowState("CP_ISSUED", "ADD_IPA_VERI_DOC", [NODETYPE.ISSUER, NODETYPE.INVESTOR]),
            // new WorkflowState("IPA_VERI_DOC_ADDED", "ADD_IPA_CERT_DOC", [NODETYPE.ISSUER]),
            // ];
        }
    }

    export class WorkflowState {
        constructor(public status: string,
            public nextAction: Action,
        ) { }
    }

    export class Action {
        constructor(public name: string,
            public allowedNodes: Array<NODETYPE>) { }
    }
    export enum NODETYPE {
        ISSUER = <any>"ISSUER",
        INVESTOR = <any>"INVESTOR",
        DEPOSITORY = <any>"DEPOSITORY",
        IPA = <any>"IPA"
    }
}
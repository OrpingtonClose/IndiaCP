module app.models {
    "use strict";
    export class NodeMapping {
        public mappings: Array<NodeMap>;
        constructor() {
            this.mappings = new Array<NodeMap>();

            this.mappings = [new NodeMap("issuer1", "Barclays INvestments and Loans Ltd."),
            new NodeMap("nsdl1", "NSDL"),
            new NodeMap("ipa1", "HDFC"),
            new NodeMap("investor1", "Barclays Shared Services")];

        }
    }

    export class NodeMap {
        constructor(public username: string,
            public nodeName: string) { }
    }
}
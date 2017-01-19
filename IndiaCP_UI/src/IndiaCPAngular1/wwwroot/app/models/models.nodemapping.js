var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var NodeMapping = (function () {
            function NodeMapping() {
                this.mappings = new Array();
                this.mappings = [new NodeMap("issuer1", "Barclays INvestments and Loans Ltd."),
                    new NodeMap("nsdl1", "NSDL"),
                    new NodeMap("ipa1", "HDFC"),
                    new NodeMap("investor1", "Barclays Shared Services")];
            }
            return NodeMapping;
        }());
        models.NodeMapping = NodeMapping;
        var NodeMap = (function () {
            function NodeMap(username, nodeName) {
                this.username = username;
                this.nodeName = nodeName;
            }
            return NodeMap;
        }());
        models.NodeMap = NodeMap;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.nodemapping.js.map
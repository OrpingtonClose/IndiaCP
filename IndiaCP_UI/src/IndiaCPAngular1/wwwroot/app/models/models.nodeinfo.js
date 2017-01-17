var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var NodeInfo = (function () {
            function NodeInfo(nodeType, host, port, nodeName, dlNodeName) {
                this.nodeType = nodeType;
                this.host = host;
                this.port = port;
                this.nodeName = nodeName;
                this.dlNodeName = dlNodeName;
            }
            return NodeInfo;
        }());
        models.NodeInfo = NodeInfo;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.nodeinfo.js.map
var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var NodeInfo = (function () {
            function NodeInfo(nodeType, host, port) {
                this.nodeType = nodeType;
                this.host = host;
                this.port = port;
            }
            return NodeInfo;
        }());
        models.NodeInfo = NodeInfo;
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.nodeinfo.js.map
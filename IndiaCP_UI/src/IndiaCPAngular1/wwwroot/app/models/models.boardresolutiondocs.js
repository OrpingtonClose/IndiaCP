var app;
(function (app) {
    var models;
    (function (models) {
        "use strict";
        var BoardResolutionDocs = (function () {
            function BoardResolutionDocs() {
            }
            return BoardResolutionDocs;
        }());
        models.BoardResolutionDocs = BoardResolutionDocs;
        var StatusEnum;
        (function (StatusEnum) {
            StatusEnum[StatusEnum["ACTIVE"] = "ACTIVE"] = "ACTIVE";
            StatusEnum[StatusEnum["OBSOLETE"] = "OBSOLETE"] = "OBSOLETE";
        })(StatusEnum = models.StatusEnum || (models.StatusEnum = {}));
    })(models = app.models || (app.models = {}));
})(app || (app = {}));
//# sourceMappingURL=models.boardresolutiondocs.js.map
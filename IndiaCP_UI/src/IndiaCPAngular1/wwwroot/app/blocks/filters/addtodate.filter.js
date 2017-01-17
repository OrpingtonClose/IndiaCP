var app;
(function (app) {
    var blocks;
    (function (blocks) {
        "use strict";
        //addtodate.$inject = ["isoDateString","days"];
        function addtodate() {
            return function (isoDate, days) {
                var parts;
                var isoTime;
                var date;
                var isoDateString = isoDate.toISOString();
                isoDateString = isoDateString || "";
                days = days || 0;
                parts = isoDateString.match(/\d+/g);
                isoTime = Date.UTC(parts[0], parts[1] - 1, parts[2], parts[3], parts[4], parts[5]);
                date = new Date(isoTime);
                if (days) {
                    date.setDate(date.getDate() + days);
                }
                return date;
            };
        }
        blocks.addtodate = addtodate;
        angular
            .module("app.blocks")
            .filter("addtodate", addtodate);
    })(blocks = app.blocks || (app.blocks = {}));
})(app || (app = {}));
//# sourceMappingURL=addtodate.filter.js.map
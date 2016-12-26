var app;
(function (app) {
    var services;
    (function (services) {
        'use strict';
        var PendingPostNotifyService = (function () {
            function PendingPostNotifyService($timeout) {
                this.$timeout = $timeout;
                this.unpublishedCount = 3;
            }
            PendingPostNotifyService.prototype.run = function () {
                var _this = this;
                this.$timeout(function () {
                    console.log(_this.unpublishedCount);
                }, 100);
            };
            return PendingPostNotifyService;
        }());
        PendingPostNotifyService.$inject = ['$timeout'];
        angular
            .module('app.services')
            .service('app.services.PendingPostNotifyService', PendingPostNotifyService);
    })(services = app.services || (app.services = {}));
})(app || (app = {}));
//# sourceMappingURL=pendingpostnotify.service.js.map
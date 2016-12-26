module app.services {
    'use strict';

    export interface IPendingPostNotifyService {
        run(): void;
    }

    class PendingPostNotifyService implements IPendingPostNotifyService {
        unpublishedCount = 3;

        static $inject = ['$timeout'];
        constructor(private $timeout: ng.ITimeoutService) { }

        run(): void {
            this.$timeout((): void => {
                console.log(this.unpublishedCount);
            }, 100);
        }
    }

    angular
        .module('app.services')
        .service('app.services.PendingPostNotifyService',
        PendingPostNotifyService);
}
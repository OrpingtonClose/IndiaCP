module app.blocks {
    "use strict";

    export class ApiCallInterceptor implements ng.IHttpInterceptor {
        // @ngInject
        static factory($q: ng.IQService, localStorageService: ng.local.storage.ILocalStorageService): ApiCallInterceptor {
            return new ApiCallInterceptor($q, localStorageService);
        }

        constructor(private $q: ng.IQService, private localStorageService: ng.local.storage.ILocalStorageService) {
        }

        // created as instance method using arrow function (see notes)
        request = (config: ng.IRequestConfig): ng.IRequestConfig => {
            // console.info("Request:", config);

            if (this.localStorageService.get("accessToken") !== "") {
                config.headers["Authorization"] = `Bearer ${this.localStorageService.get("accessToken")}`;
            }
            return config;
        };

        // created as instance method using arrow function (see notes)
        response = <T>(
            response: ng.IHttpPromiseCallbackArg<T>
        ): ng.IPromise<T> => {
            // console.info("Response:", response);
            return this.$q.when(response);
        };
    }

    angular
        .module("app.services")
        .factory("app.blocks.ApiCallInterceptor",
        ApiCallInterceptor);
}
var app;
(function (app) {
    var blocks;
    (function (blocks) {
        "use strict";
        var ApiCallInterceptor = (function () {
            function ApiCallInterceptor($q, localStorageService) {
                var _this = this;
                this.$q = $q;
                this.localStorageService = localStorageService;
                // created as instance method using arrow function (see notes)
                this.request = function (config) {
                    // console.info("Request:", config);
                    if (_this.localStorageService.get("accessToken") !== "") {
                        config.headers["Authorization"] = "Bearer " + _this.localStorageService.get("accessToken");
                    }
                    return config;
                };
                // created as instance method using arrow function (see notes)
                this.response = function (response) {
                    // console.info("Response:", response);
                    return _this.$q.when(response);
                };
            }
            // @ngInject
            ApiCallInterceptor.factory = function ($q, localStorageService) {
                return new ApiCallInterceptor($q, localStorageService);
            };
            return ApiCallInterceptor;
        }());
        blocks.ApiCallInterceptor = ApiCallInterceptor;
        angular
            .module("app.services")
            .factory("app.blocks.ApiCallInterceptor", ApiCallInterceptor);
    })(blocks = app.blocks || (app.blocks = {}));
})(app || (app = {}));
//# sourceMappingURL=httpinterceptor.factory.js.map
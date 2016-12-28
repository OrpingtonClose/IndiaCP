var app;
(function (app) {
    var services;
    (function (services) {
        "use strict";
        var AuthenticationService = (function () {
            function AuthenticationService($http, $q, $sessionStorage) {
                this.$http = $http;
                this.$q = $q;
                this.$sessionStorage = $sessionStorage;
                this.basePath = "http://localhost:35222/api";
                this.defaultHeaders = {};
            }
            AuthenticationService.prototype.login = function (userInfo) {
                var deferred = this.$q.defer();
                this.$http.post("/account/login", JSON.stringify(userInfo))
                    .success(function (response) {
                    deferred.resolve(response);
                })
                    .error(function (data, status) {
                    deferred.reject({
                        data: data,
                        status: status
                    });
                });
                return deferred.promise;
            };
            AuthenticationService.prototype.authenticate = function () {
                //this.$sessionStorage.isAuthenticated = true;
            };
            AuthenticationService.prototype.clear = function () {
                //this.$sessionStorage.isAuthenticated = false;
            };
            AuthenticationService.prototype.isAuthenticated = function () {
                // if (this.$sessionStorage.isAuthenticated) {
                //     return true;
                // }
                // return false;
                return false;
            };
            return AuthenticationService;
        }());
        AuthenticationService.$inject = ["$http", "$q", "$sessionStorage"];
        angular
            .module("app.services")
            .service("app.services.AuthenticationService", AuthenticationService);
    })(services = app.services || (app.services = {}));
})(app || (app = {}));
//# sourceMappingURL=auth.service.js.map
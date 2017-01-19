var app;
(function (app) {
    var services;
    (function (services) {
        "use strict";
        var AuthenticationService = (function () {
            function AuthenticationService($http, $q, growl, localStorageService, $sessionStorage) {
                this.$http = $http;
                this.$q = $q;
                this.growl = growl;
                this.localStorageService = localStorageService;
                this.$sessionStorage = $sessionStorage;
                this.basePath = "/api";
                this.defaultHeaders = {};
                this.accessToken = "";
                this.isAuthenticated = false;
                this.localStorageService.set("accessToken", "");
            }
            AuthenticationService.prototype.login = function (userInfo) {
                var _this = this;
                var deferred = this.$q.defer();
                this.$http.post(this.basePath + "/authentication", JSON.stringify(userInfo))
                    .then(function (response) {
                    _this.isAuthenticated = true;
                    _this.currentUser = userInfo;
                    _this.localStorageService.set("accessToken", response.data.accessToken);
                    _this.localStorageService.set("nodeInfo", response.data.nodeInfo);
                    deferred.resolve(response);
                }, function (error) {
                    _this.growl.error("Incorrect credentials. Try again.", { title: "Error!" });
                    console.log("Incorrect credentials. Try again.");
                    _this.isAuthenticated = false;
                    _this.localStorageService.set("accessToken", "");
                    deferred.reject({
                        data: error
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
            AuthenticationService.prototype.logout = function () {
                this.isAuthenticated = false;
            };
            return AuthenticationService;
        }());
        AuthenticationService.$inject = ["$http", "$q", "growl", "localStorageService", "$sessionStorage"];
        angular
            .module("app.services")
            .service("app.services.AuthenticationService", AuthenticationService);
    })(services = app.services || (app.services = {}));
})(app || (app = {}));
//# sourceMappingURL=auth.service.js.map
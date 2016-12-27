var app;
(function (app) {
    var services;
    (function (services) {
        "use strict";
        var AuthenticationSerivice = (function () {
            function AuthenticationSerivice($http, $sessionStorage, basePath) {
                this.$http = $http;
                this.$sessionStorage = $sessionStorage;
                this.basePath = "http://finwizui.azurewebsites.net/api";
                this.defaultHeaders = {};
                if (basePath !== undefined) {
                    this.basePath = basePath;
                }
            }
            AuthenticationSerivice.prototype.login = function (userInfo, callback) {
                this.$http.post("/api/account/login", JSON.stringify(userInfo))
                    .success(function (response) {
                    callback(response);
                });
            };
            AuthenticationSerivice.prototype.authenticate = function () {
                //this.$sessionStorage.isAuthenticated = true;
            };
            AuthenticationSerivice.prototype.clear = function () {
                //this.$sessionStorage.isAuthenticated = false;
            };
            AuthenticationSerivice.prototype.isAuthenticated = function () {
                // if (this.$sessionStorage.isAuthenticated) {
                //     return true;
                // }
                // return false;
                return true;
            };
            return AuthenticationSerivice;
        }());
        AuthenticationSerivice.$inject = ["$http", "$sessionStorage"];
    })(services = app.services || (app.services = {}));
})(app || (app = {}));
// (function () {
//     "use strict";
//     angular
//         .module("app")
//         .factory("auth0Service", auth0Service);
//     auth0Service.$inject = ["$http", "$sessionStorage"];
//     function auth0Service($http, $sessionStorage) {
//         var service = {};
//         service.login = login;
//         service.authenticate = authenticate;
//         service.clear = clear;
//         service.isAuthenticated = isAuthenticated;
//         return service;
//         function login(userInfo, callback) {
//             $http.post("/api/account/login", JSON.stringify(userInfo))
//                 .success(function (response) {
//                     callback(response);
//                 });
//         }
//         function authenticate() {
//             $sessionStorage.isAuthenticated = true;
//         }
//         function clear() {
//             $sessionStorage.isAuthenticated = false;
//         }
//         function isAuthenticated() {
//             if ($sessionStorage.isAuthenticated) {
//                 return true;
//             }
//             return false;
//         }
//     }
// })(); 
//# sourceMappingURL=auth.service.js.map
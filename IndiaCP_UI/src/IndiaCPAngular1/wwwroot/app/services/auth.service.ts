module app.services {
    "use strict";

    export interface IAuthenticationService {
        login(userInfo:ICurrentUser, callback:any):void;
        authenticate():void;
        clear():void;
        isAuthenticated():boolean;
    }

    class AuthenticationSerivice implements IAuthenticationService {
        protected basePath = "http://finwizui.azurewebsites.net/api";
        public defaultHeaders : any = {};

        static $inject: string[] = ["$http", "$sessionStorage"];

        constructor(protected $http: ng.IHttpService, protected $sessionStorage?: (d: any) => any, basePath?: string) {
            if (basePath !== undefined) {
                this.basePath = basePath;
            }
        }

        public login(userInfo:ICurrentUser, callback):void {
            this.$http.post("/api/account/login", JSON.stringify(userInfo))
                .success(function (response) {
                    callback(response);
                });
        }

        public authenticate():void {
            //this.$sessionStorage.isAuthenticated = true;
        }

        public clear():void {
            //this.$sessionStorage.isAuthenticated = false;
        }

        public isAuthenticated():boolean {
            // if (this.$sessionStorage.isAuthenticated) {
            //     return true;
            // }
            // return false;
            return true;
        }
       
    }

}


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
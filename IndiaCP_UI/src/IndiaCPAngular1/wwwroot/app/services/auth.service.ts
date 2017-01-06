module app.services {
    "use strict";

    export interface IAuthenticationService {
        login(user:app.models.CurrentUser):ng.IHttpPromise<any>;
        authenticate():void;
        clear():void;
        isAuthenticated():boolean;
    }

    class AuthenticationService implements IAuthenticationService {
        protected basePath = "/api";
        public defaultHeaders : any = {};

        static $inject: string[] = ["$http", "$q", "$sessionStorage"];

        constructor(protected $http: ng.IHttpService, protected $q:ng.IQService, protected $sessionStorage?: (d: any) => any) {
        }

        public login(userInfo:app.models.CurrentUser):ng.IHttpPromise<any> {
            var deferred:ng.IDeferred<any> = this.$q.defer();
            this.$http.post(this.basePath + "/authentication", JSON.stringify(userInfo))
                .success(function (response:any):void {
                    deferred.resolve(response);
                })
                .error(function(data:any, status:any):void{
                    deferred.reject({
                   data: data,
                   status: status
                 });
                });
            return deferred.promise;
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
            return false;
        }
    }

    angular
        .module("app.services")
        .service("app.services.AuthenticationService", AuthenticationService);
}

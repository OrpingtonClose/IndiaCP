module app.services {
    "use strict";

    export interface IAuthenticationService {
        login(user: app.models.CurrentUser): ng.IHttpPromise<any>;
        authenticate(): void;
        clear(): void;
        isAuthenticated: boolean;
        logout(): void;
        currentUser:app.models.CurrentUser;
        
    }

    class AuthenticationService implements IAuthenticationService {
        protected basePath = "/api";
        public defaultHeaders: any = {};
        isAuthenticated: boolean;
        accessToken: string = "";
        currentUser:app.models.CurrentUser;
   
        static $inject: string[] = ["$http", "$q", "growl",  "localStorageService", "$sessionStorage"];

        constructor(protected $http: ng.IHttpService, protected $q: ng.IQService, protected growl, protected localStorageService:ng.local.storage.ILocalStorageService, protected $sessionStorage?: (d: any) => any) {
            this.isAuthenticated = false;
            this.localStorageService.set("accessToken","");
        }

        public login(userInfo: app.models.CurrentUser): ng.IHttpPromise<any> {
            var deferred: ng.IDeferred<any> = this.$q.defer();
            this.$http.post(this.basePath + "/authentication", JSON.stringify(userInfo))
                .then((response: any): void => {
                    this.isAuthenticated = true;
                    this.currentUser = userInfo;
                    this.localStorageService.set("accessToken",response.data.accessToken);
                    this.localStorageService.set("nodeInfo",response.data.nodeInfo);
                    deferred.resolve(response);
                }, (error: any): void => {
                    this.growl.error("Incorrect credentials. Try again.", { title: "Error!" });
                    console.log("Incorrect credentials. Try again.");
                    this.isAuthenticated = false;
                    this.localStorageService.set("accessToken","");
                    deferred.reject({
                        data: error
                    });
                });
            return deferred.promise;
        }

        public authenticate(): void {
            //this.$sessionStorage.isAuthenticated = true;
        }

        public clear(): void {
            //this.$sessionStorage.isAuthenticated = false;
        }

        public logout(): void {
            this.isAuthenticated = false;
        }
        // public isAuthenticated():boolean {
        //     // if (this.$sessionStorage.isAuthenticated) {
        //     //     return true;
        //     // }
        //     // return false;
        //     return false;
        // }
    }

    angular
        .module("app.services")
        .service("app.services.AuthenticationService", AuthenticationService);
}

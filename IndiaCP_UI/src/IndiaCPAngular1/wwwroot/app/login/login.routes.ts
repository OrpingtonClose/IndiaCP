(function(): void {
    "use strict";
    angular
    .module("app.login")
    .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void { 
        $stateProvider
            .state("login", {
                url: "login",
                templateUrl: "app/login/login.html",
                controller: "app.login.LoginController",
                controllerAs: "vm",
                resolve :{
                }
            });
    }
})();
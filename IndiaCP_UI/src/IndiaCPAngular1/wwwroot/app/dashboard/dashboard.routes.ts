(function(): void {
    "use strict";
    angular
    .module("app.dashboard")
    .config(config)

    config.$inject = ['$routeProvider'];
    function config($routeProvider: ng.route.IRouteProvider):void{
        $routeProvider
         .when('/dashboard', {
                templateUrl: 'app/dashboard/dashboard.html',
                controller: 'app.dashboard.DashboardController',
                controllerAs: 'vm',
                resolve: {
                    blogPosts: ():void =>{}
                }
            });
    }

    // resolveCPPrograms.$inject = ["app.services.CPProgramService"];
    // function resolveCPPrograms(cpprogramService):
    // {}

})();
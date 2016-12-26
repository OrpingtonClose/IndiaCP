(function () {
    "use strict";
    angular
        .module("app.dashboard")
        .config(config);
    config.$inject = ['$routeProvider'];
    function config($routeProvider) {
        $routeProvider
            .when('/dashboard', {
            templateUrl: 'app/dashboard/dashboard.html',
            controller: 'app.dashboard.DashboardController',
            controllerAs: 'vm',
            resolve: {
                blogPosts: function () { }
            }
        });
    }
    // resolveCPPrograms.$inject = ["app.services.CPProgramService"];
    // function resolveCPPrograms(cpprogramService):
    // {}
})();
//# sourceMappingURL=dashboard.routes.js.map
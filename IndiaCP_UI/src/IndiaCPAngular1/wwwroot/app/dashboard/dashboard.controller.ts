module app.dashboard {
    "use strict";

    interface IDashboardScope {
        fetchAllCPPrograms(): void;
    }

    class DashboardController implements IDashboardScope {
        loggedinUser: app.users.IUser;

        static $inject = ["$http","$scope","app.services.IssuerService"];
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected issuerService: app.services.IIssuerService) {
            this.fetchAllCPPrograms();
        }
        public fetchAllCPPrograms(): void {
            this.issuerService.fetchAllCPProgram("groggy").then(function (response) {
                this.$scope.cpprograms = response;
            });
        }

    }

    angular
        .module("app.dashboard")
        .controller("app.dashboard.DashboardController",
        DashboardController);
} 
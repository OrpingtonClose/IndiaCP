module app.dashboard {
    "use strict";

    interface IDashboardScope {
        fetchAllCPPrograms(): void;
    }

    class DashboardController implements IDashboardScope {
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected issuerService: app.services.IIssuerService,
            protected loggedinUser: app.users.IUser) {
            this.fetchAllCPPrograms();
        }
        public fetchAllCPPrograms(): void {
            this.issuerService.fetchAllCPProgram(this.loggedinUser.email).then(function (response) {
                this.$scope.cpprograms = response;
            });
        }

    }

    angular
        .module("app.dashboard")
        .controller("app.dashboard.DashboardController",
        DashboardController);
} 
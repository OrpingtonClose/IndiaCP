module app.dashboard {
    "use strict";

    interface IDashboardScope {
        fetchAllCPPrograms(): void;
        generateISINDocs(): void;
    }

    class DashboardController implements IDashboardScope {
        loggedinUser: app.users.IUser;

        static $inject = ["$http", "$scope", "$uibModal","app.services.IssuerService"];
        constructor(protected $http: ng.IHttpService,
            protected $scope: ng.IScope,
            protected $uibModal: ng.ui.bootstrap.IModalService,
            protected issuerService: app.services.IIssuerService) {
            this.fetchAllCPPrograms();
        }
        public fetchAllCPPrograms(): void {
            // this.issuerService.fetchAllCPProgram("groggy").then(function (response) {
            //     this.$scope.cpprograms = response;
            // });
        }
        public generateISINDocs(): void {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.dashboard.isingeneration.ISINGenerationController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/dashboard/isingeneration/isingeneration.html"
            });
        }
    }

    angular
        .module("app.dashboard")
        .controller("app.dashboard.DashboardController",
        DashboardController);
} 
module app.main {
    "use strict";

    interface IMainScope {
        logout(): void;
        nodeType: string;
    }

    class MainController implements IMainScope {
        static $inject = ["$state",
            "app.services.AuthenticationService",
            "localStorageService",
            "$uibModal"];
        nodeType: string;
        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected $uibModal: ng.ui.bootstrap.IModalService) {
            this.nodeType = (this.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
        }
        public logout(): void {
            this.authService.logout();
            this.$state.go("login");
        }

        public uploadBRDoc(): void {
            var uploadBRModal: ng.ui.bootstrap.IModalServiceInstance = this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.legalentity.LegalEntityController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/legalentity/uploadbr.html"
            });

            uploadBRModal.closed.then(() => {
                this.$state.transitionTo("main.dashboard");
            });
        }
        public uploadCRDoc(): void {
            // this.$uibModal.open({
            //     animation: true,
            //     ariaLabelledBy: "modal-title",
            //     ariaDescribedBy: "modal-body",
            //     controller: "app.dashboard.isingeneration.ISINGenerationController",
            //     controllerAs: "vm",
            //     size: "lg",
            //     backdrop: "static",
            //     templateUrl: "app/dashboard/isingeneration/isingeneration.html"
            // });
            var uploadCRModal: ng.ui.bootstrap.IModalServiceInstance = this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.legalentity.LegalEntityController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/legalentity/uploadcr.html",
            });

            uploadCRModal.closed.then(() => {
                this.$state.transitionTo("main.dashboard");
            });
        }
    }

    angular
        .module("app.main")
        .controller("app.main.MainController",
        MainController);
} 
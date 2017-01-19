module app.main {
    "use strict";

    interface IMainScope {
        logout(): void;
        nodeInfo: app.models.NodeInfo;
    }

    class MainController implements IMainScope {
        static $inject = ["$state",
            "app.services.AuthenticationService",
            "localStorageService",
            "$uibModal"];
        nodeInfo: app.models.NodeInfo;
        currentUser: app.models.CurrentUser;
        constructor(protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected $uibModal: ng.ui.bootstrap.IModalService) {
            this.nodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;
            this.currentUser = this.authService.currentUser;
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
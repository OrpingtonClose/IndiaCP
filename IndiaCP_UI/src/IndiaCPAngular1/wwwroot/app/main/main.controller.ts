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

        public showBRDoc(): void {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.legalentity.LegalEntityController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/legalentity/uploadbr.html"
            });
        }
        public showCRDoc(): void {
            this.$uibModal.open({
                animation: true,
                ariaLabelledBy: "modal-title",
                ariaDescribedBy: "modal-body",
                controller: "app.legalentity.LegalEntityController",
                controllerAs: "vm",
                size: "lg",
                backdrop: "static",
                templateUrl: "app/legalentity/uploadcr.html"
            });
        }
    }

    angular
        .module("app.main")
        .controller("app.main.MainController",
        MainController);
} 
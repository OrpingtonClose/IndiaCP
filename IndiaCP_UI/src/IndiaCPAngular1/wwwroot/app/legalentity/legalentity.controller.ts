module app.legalentity {
    "use strict";

    interface ILegalEntityScope {
        nodeType: string;
        brDetails: app.models.BoardResolutionDocs;
        crDetails: app.models.LegalEntityCRDocs;
        uploadBR(): void;
        uploadCR(): void;
    }

    class LegalEntityController implements ILegalEntityScope {
        static $inject = ["$scope", "$sce", "$state", "app.services.AuthenticationService", "localStorageService", "Upload"];
        nodeType: string;
        brDetails: app.models.BoardResolutionDocs;
        crDetails: app.models.LegalEntityCRDocs;
        brFileUrl: string;
        constructor(protected $scope: ng.IScope,
            protected $sce: ng.ISCEService,
            protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected Upload: ng.angularFileUpload.IUploadService) {
            this.nodeType = (this.localStorageService.get("nodeInfo") as app.models.NodeInfo).nodeType;
        }

        public displayBR(file): void {
            var ctrl = this;
            var r: FileReader = new FileReader();
            var data: ArrayBuffer;
            r.onloadend = function (e) {
                data = (e.target as FileReader).result;
                var fileBlob: Blob = new Blob([data], { type: "application/pdf" });
                ctrl.brFileUrl = ctrl.$sce.trustAsResourceUrl(URL.createObjectURL(fileBlob));
                ctrl.$scope.$apply();
            };
            console.log("working");
            // check out readAsDataUrl - https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
            r.readAsArrayBuffer(file);
        }

        public uploadBR(): void {
            console.log("working");
        }

        public uploadCR(): void {
            console.log("working");
        }

    }

    angular
        .module("app.legalentity")
        .controller("app.legalentity.LegalEntityController",
        LegalEntityController);
} 
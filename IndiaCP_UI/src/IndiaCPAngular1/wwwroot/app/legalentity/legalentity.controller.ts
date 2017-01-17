module app.legalentity {
    "use strict";

    interface ILegalEntityScope {
        brDetails: app.models.BoardResolutionDocs;
        crDetails: app.models.CreditRatingDocs;
        signCR(): void;
        signBR(): void;
        uploadBR(): void;
        uploadCR(): void;
    }

    class LegalEntityController implements ILegalEntityScope {
        static $inject = ["$sce",
            "$state",
            "app.services.AuthenticationService",
            "app.services.IssuerService",
            "localStorageService",
            "Upload",
            "growl",
            "$uibModalInstance"];
        nodeInfo: app.models.NodeInfo;
        brDetails: app.models.BoardResolutionDocs;
        crDetails: app.models.CreditRatingDocs;
        brFileUrl: string;
        crFileUrl: string;
        brFile: File;
        crFile: File;
        constructor(protected $sce: ng.ISCEService,
            protected $state: ng.ui.IStateService,
            protected authService: app.services.IAuthenticationService,
            protected issuerService: app.services.IIssuerService,
            protected localStorageService: ng.local.storage.ILocalStorageService,
            protected Upload: ng.angularFileUpload.IUploadService,
            protected growl: ng.growl.IGrowlService,
            protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance) {
            this.nodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;

            // br details setup
            this.brDetails = new app.models.BoardResolutionDocs();
            this.brDetails.legalEntityId = this.nodeInfo.dlNodeName;
            this.brDetails.boardResolutionBorrowingLimit = 10000;
            this.brDetails.boardResolutionIssuanceDate = new Date(); // Todays date
            this.brDetails.boardResolutionExpiryDate = new Date(2022, 12, 08); //5 years from now
            this.brDetails.modifiedBy = "Ritu";
            this.brDetails.currency = "INR";
            this.brDetails.docHash = "XXXXXXXXXXX";

            // cr detsils setup
            this.crDetails = new app.models.CreditRatingDocs();
            this.crDetails.legalEntityId = this.nodeInfo.dlNodeName;
            this.crDetails.creditRatingAgencyName = "ICRA";
            this.crDetails.creditRatingAmount = 10000;
            this.crDetails.creditRating = "AAA";
            this.crDetails.creditRatingIssuanceDate = new Date();
            this.crDetails.creditRatingEffectiveDate = new Date();
            this.crDetails.creditRatingExpiryDate = new Date(2022, 12, 08); //5 years from now
            this.crDetails.modifiedBy = "Ritu";
            this.crDetails.currency = "INR";
            this.crDetails.docHash = "XXXXXXXXXXX";

            //legal entity setup


        }

        public displayBR(file): void {
            this.brFile = file;
            this.brFileUrl = this.$sce.trustAsResourceUrl(URL.createObjectURL(file));
        }

        public signBR(): void {
            let httpUploadRequestParams: any = {
                url: "http://52.172.46.253:8182/indiacp/indiacpdocuments/signDoc/BoardResolution",
                data: { file: this.brFile },
                method: "POST"
            }
            this.Upload.upload(httpUploadRequestParams).
                then((response: any) => {
                    this.growl.success("BR document signed succesfully", { title: "BR Signed!" });
                    let streamData = response.data;
                    var url: string = "data:application/pdf;base64," + streamData;
                    this.crFileUrl = this.$sce.trustAsResourceUrl(url);

                }, (error: any) => {
                    this.growl.error("Document signing unsuccesful", { title: "Signing Failed!" });
                });
        }

        public uploadBR(): void {
            this.issuerService.issueBoardResolution(this.brDetails, this.brFile).
                then((response: any): void => {
                    console.log(response);
                    this.growl.success("Board Resolution document uploaded succesfully", { title: "BR Uploaded" });
                },
                (error: any) => {
                    let errorMssg: app.models.Error = error.data;
                    console.log(`${errorMssg.source}-${errorMssg.message}`);
                    this.growl.error(errorMssg.message, { title: `Upload Failed - ${errorMssg.source}` });
                });
        }

        public displayCR(file): void {
            this.crFile = file;
            this.crFileUrl = this.$sce.trustAsResourceUrl(URL.createObjectURL(file));

            // var r: FileReader = new FileReader();
            // var data: ArrayBuffer;
            // r.onloadend = function (e) {
            //     data = (e.target as FileReader).result;
            //     var fileBlob: Blob = new Blob([data], { type: "application/pdf" });
            //     ctrl.signedCR = fileBlob;
            //     ctrl.crFileUrl = ctrl.$sce.trustAsResourceUrl(URL.createObjectURL(fileBlob));
            //     ctrl.$scope.$apply();
            // };
            // console.log("working");
            // // check out readAsDataUrl - https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
            // r.readAsArrayBuffer(file);
        }

        public signCR(): void {
            let httpUploadRequestParams: any = {
                url: "http://52.172.46.253:8182/indiacp/indiacpdocuments/signDoc/CreditRating",
                data: { file: this.crFile },
                method: "POST"
            }
            this.Upload.upload(httpUploadRequestParams).
                then((response: any) => {
                    this.growl.success("Credit Details document signed succesfully", { title: "CR Signed!" });
                    let streamData = response.data;
                    var url: string = "data:application/pdf;base64," + streamData;
                    this.crFileUrl = this.$sce.trustAsResourceUrl(url);

                }, (error: any) => {
                    this.growl.error("Document signing unsuccesful", { title: "Signing Failed!" });
                });
        }

        public uploadCR(): void {
            this.issuerService.issueCreditRating(this.crDetails, this.crFile).
                then((response: any): void => {
                    console.log(response);
                    this.growl.success("Credit Details document uploaded succesfully", { title: "CR Uploaded" });
                },
                (error: any) => {
                    let errorMssg: app.models.Error = error.data;
                    console.log(`${errorMssg.source}-${errorMssg.message}`);
                    this.growl.error(errorMssg.message, { title: `Upload Failed - ${errorMssg.source}` });
                });
        }

        public close() {
            this.$uibModalInstance.close();
        }

    }

    angular
        .module("app.legalentity")
        .controller("app.legalentity.LegalEntityController",
        LegalEntityController);
} 
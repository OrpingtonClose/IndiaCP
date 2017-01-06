module app.dashboard.cpissue {
    "use strict";

    interface ICPIssueScope {
        issueCP(): void;
    }

    class CPIssueController implements ICPIssueScope {
        isinDocPDF: string;
        cpissue: app.models.IndiaCPIssue;
        maturityValue: number;
        issueValue: number;
        static $inject = ["$uibModalInstance","app.services.IssuerService", "uuid4", "cpProgram"]
        constructor(protected $uibModalInstance: ng.ui.bootstrap.IModalServiceInstance,
                    protected issuerService: app.services.IIssuerService,
                    protected uuid4: any,
                    protected cpProgram: app.models.IndiaCPProgram) {
        this.cpissue = new app.models.IndiaCPIssue();
        this.cpissue.cpProgramId = this.cpProgram.programId;
        this.cpissue.bookId = "book1";
        // this.cpissue.isin = this.cpProgram.isin;
        this.cpissue.isin = "INE123456";
        this.cpissue.traderId = "trader1";
        this.cpissue.issuerId = this.cpProgram.issuerId;
        this.cpissue.issuerName = this.cpProgram.issuerName;
        this.cpissue.cpTradeId = this.cpissue.issuerName + "-" + this.uuid4.generate();
        this.cpissue.beneficiaryId = "Issuer";
        this.cpissue.beneficiaryName = "Issuer";
        this.cpissue.ipaId = this.cpProgram.ipaId;
        this.cpissue.ipaName = this.cpProgram.ipaName;
        this.cpissue.depositoryId = this.cpProgram.depositoryId;
        this.cpissue.depositoryName = this.cpProgram.depositoryName;
        this.cpissue.tradeDate = new Date();
        this.cpissue.valueDate = new Date();
        this.cpissue.valueDate.setDate(new Date().getDate() + 2);
        this.cpissue.maturityDays = this.cpProgram.maturityDays;
        this.cpissue.currency = this.cpProgram.programCurrency;
        this.cpissue.facevaluePerUnit = 100;
        this.cpissue.noOfUnits = 10;
        this.cpissue.rate = 7;
     }
        public issueCP(): void {
           this.issuerService.issueCP(this.cpissue).then(():void => { 
               console.log("CP Issue created");
           },(error:any):void => {
               console.log("CP Issue not created" +  error);
           });
        }
         public cancel():void{
            this.$uibModalInstance.close();
        }

        public updateTradeId(): void {
            this.cpissue.cpTradeId = this.uuid4.generate();
        }

        public updateMaturityandIssueVal():void{
            this.maturityValue = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
            this.issueValue = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
        }

        public updateRate():void{
            this.cpissue.rate = this.cpissue.facevaluePerUnit * this.cpissue.noOfUnits;
        }
    }

    angular
        .module("app.dashboard.cpissue")
        .controller("app.dashboard.cpissue.CPIssueController",
        CPIssueController);
} 
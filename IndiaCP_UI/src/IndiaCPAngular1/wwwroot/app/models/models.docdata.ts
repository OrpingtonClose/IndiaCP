module app.models {
    "use strict";

    export class DocData {
        cpProgramId: string;
        docType: DOCTYPE;
        docSubType: DOCTYPE;
        docExtension: DOCEXTENSION;
        docStatus: DOCSTATUS;
        modifiedBy: string;
    }

    export enum DOCTYPE {
        DEPOSITORY_DOCS = <any>"DEPOSITORY_DOCS"
    }

    export enum DOCSTATUS {
        SIGNED_BY_ISSUER = <any>"SIGNED_BY_ISSUER"
    }

    export enum DOCEXTENSION {
        PDF = <any>"pdf",
        DOCX = <any>"docx",
        DOC = <any>"doc",
        ZIP = <any>"zip"
    }



    export class DocRefData {

        constructor() {
            this.cp = new CPDocData();
            this.investor = new InvestorDocData();
            this.nsdl = new NSDLDOcData();
            this.ipa = new IPADocData();
        }
        /**
         * Unique Id to identify the type of error. For e.g. CP Program Creation, ISIN Generation, etc.
         */
        "cp"?: CPDocData;

        /**
         * Source of the error identifying the layer where the error originated. For e.g. DL Integration Layer, DL (Corda/Ethereum), Reference Data Service
         */
        "investor"?: InvestorDocData;

        /**
         * Short message describing the error
         */
        "ipa"?: IPADocData;

        /**
         * Details of the error. This could also be the complete stack trace. It would be useful for debugging
         */
        "nsdl"?: NSDLDOcData;
    }


    export class CPDocData {
        "issuerName"?: string;
        "secondParty"?: string;
        "desc"?: string;
        "boardResolutionDate"?: Date;
        "valueDate"?: Date;
        "tradeDate"?: Date;
        "dateOfContract"?: Date;
        "allotDate"?: Date;
        "matDate"?: Date;
        "issueValue"?: number;
        "maturityValue"?: number;
        "redemValue"?: number;
        "totalAmount"?: number;
        "tax"?: number;
        "CIN"?: string;
        "NSDLHistory"?: string;
        "issuerAddress"?: string;
        "officialContactNum"?: string;
        "officialFaxNum"?: string;
        "officialEmail"?: string;
        "officialWebsite"?: string;
        "entityType"?: string;
        "price"?: string;
        "discRate"?: number;
        "currency"?: string;
        "issueRef"?: string;
        "isinCode"?: string;
        "creditRating"?: string;
        "ratingIssuedBy"?: string;
        "dateOfRating"?: Date;
        "ratingValidityDate"?: Date;
        "effectiveDateOfRating"?: Date;
        "contactPerson"?: string;
        "complianceOfficer"?: string;
        "complianceOfficerDept"?: string;
        "complianceOfficerAddress"?: string;
        "complianceOfficerContactNo"?: string;
        "complianceOfficerEmail"?: string;
        "investorRelationsOfficer"?: string;
        "investorRelationsOfficerDept"?: string;
        "investorRelationsOfficerAddress"?: string;
        "investorRelationsOfficerContactNo"?: string;
        "investorRelationsOfficerEmail"?: string;
        "operationsOfficer"?: string;
        "operationsOfficerDept"?: string;
        "operationsOfficerAddress"?: string;
        "operationsOfficerContactNo"?: string;
        "operationsOfficerEmail"?: string;
        "conditions"?: string;
        "creditSupport"?: string;
        "creditInstructions"?: string;
        "marketConventions"?: string;
        "businessActivity"?: string;
        "stockExchange"?: string;
        "netWorth"?: number;
        "workingCapitalLimit"?: number;
        "outstandingBankBorrowing"?: number;
        "faceValue"?: number;
        "amountOfCPOutstanding"?: number;
    }

    export class InvestorDocData {
        "investorName"?: string;
        "dpName"?: string;
        "clientId"?: string;
        "dpId"?: string;
        "contactPerson"?: string;
    }

    export class IPADocData {
        "ipaName"?: string;
        "ipaContactPerson": string;
        "ipaContactDept": string;
        "ipaContactAddress": string;
        "ipaContactTelephone": string;
        "ipaContactEmail": string;
        "ipaAddress"?: string;
        "ipaAllotmentDPID"?: string;
        "ipaAllotmentClientID"?: string;
        "ipaRedemptionDPID"?: string;
        "ipaRedemptionClientID"?: string;
    }

    export class NSDLDOcData {
        "nsdlContactPerson"?: string;
        "nsdlAddress"?: string;
    }
}
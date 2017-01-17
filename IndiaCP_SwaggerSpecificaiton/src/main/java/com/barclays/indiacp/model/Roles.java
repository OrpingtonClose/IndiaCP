package com.barclays.indiacp.model;

import java.util.Date;

/**
 * Created by Electrania.com on 12/4/2016.
 */
public class Roles {

    public IPA ipa;
    public CP cp;
    public Investor investor;
    public NSDL nsdl;


    public Roles() {
    }

    public class IPA {
        public String ipaName;
        public String ipaAddress;
        public String ipaDPName;
        public String ipaAllotmentDPID;
        public String ipaAllotmentClientID;
        public String ipaRedemptionDPID;
        public String ipaRedemptionClientID;


        public IPA() {
        }

        public IPA(String ipaName, String ipaAddress, String ipaDPName, String ipaAllotmentDPID, String ipaAllotmentClientID,
                   String ipaRedemptionDPID, String ipaRedemptionClientID ) {
            this.ipaName = ipaName;
            this.ipaAddress = ipaAddress;
            this.ipaDPName = ipaDPName;
            this.ipaAllotmentDPID = ipaAllotmentDPID;
            this.ipaAllotmentClientID = ipaAllotmentClientID;
            this.ipaRedemptionDPID = ipaRedemptionDPID;
            this.ipaRedemptionClientID = ipaRedemptionClientID;
        }
    }

    public class Investor {
        public String investorName;
        public String dpName;
        public String clientId;
        public String dpId;
        public String contactPerson;

        public Investor() {
        }

        public Investor(String investorName, String dpName, String clientId, String dpId, String contactPerson) {
            this.investorName = investorName;
            this.dpName = dpName;
            this.clientId = clientId;
            this.dpId = dpId;
            this.contactPerson = contactPerson;
        }
    }

    public class NSDL {
        public String nsdlContactPerson;
        public String nsdlAddress;

        public NSDL() {
        }

        public NSDL(String nsdlContactPerson, String nsdlAddress) {
            this.nsdlContactPerson = nsdlContactPerson;
            this.nsdlAddress = nsdlAddress;
        }
    }

    public class CP {
        public String issuerName;
        public String secondParty;
        public String boardResolutionDate;
        public String valueDate;
        public String tradeDate;
        public String dateOfContract;
        public String allotDate;
        public String desc;
        public String matDate;
        public String issueValue;
        public String maturityValue;
        public String redemValue;
        public String totalAmount;
        public String tax;
        public String CIN;
        public String NSDLHistory;
        public String issuerAddress;
        public String officialContactNum;
        public String officialFaxNum;
        public String officialEmail;
        public String officialWebsite;
        public String entityType;
        public String price;
        public String discRate;
        public String currency;
        public String issueRef;
        public String isinCode;
        public String creditRating;
        public String ratingIssuedBy;
        public String dateOfRating;
        public String ratingValidityDate;
        public String effectiveDateOfRating;
        public String issuerContactPerson;
        public String complianceOfficer;
        public String complianceOfficerDept;
        public String complianceOfficerAddress;
        public String complianceOfficerContactNo;
        public String complianceOfficerEmail;
        public String investorRelationsOfficer;
        public String investorRelationsOfficerDept;
        public String investorRelationsOfficerAddress;
        public String investorRelationsOfficerContactNo;
        public String investorRelationsOfficerEmail;
        public String operationsOfficer;
        public String operationsOfficerDept;
        public String operationsOfficerAddress;
        public String operationsOfficerContactNo;
        public String operationsOfficerEmail;
        public String conditions;
        public String creditSupport;
        public String creditTo;
        public String currentAccNo;
        public String IFSCCode;
        public String marketConventions;
        public String businessActivity;
        public String stockExchange;
        public String netWorth;
        public String workingCapitalLimit;
        public String outstandingBankBorrowing;
        public String faceValue;
        public String amountOfCPOutstanding;

        public CP() {
        }

        public CP(String issuerName, String secondParty, String desc, String boardResolutionDate, String valueDate, String tradeDate, String dateOfContract, String allotDate, String matDate, String issueValue, String maturityValue, String redemValue, String totalAmount, String tax, String CIN, String NSDLHistory, String issuerAddress,
                  String officialContactNum, String officialFaxNum, String officialEmail, String officialWebsite, String entityType, String price, String discRate, String currency, String issueRef, String isinCode, String creditRating, String ratingIssuedBy, String dateOfRating, String ratingValidityDate, String effectiveDateOfRating, String issuerContactPerson, String complianceOfficer, String complianceOfficerDept, String complianceOfficerAddress, String complianceOfficerContactNo, String complianceOfficerEmail, String investorRelationsOfficer, String investorRelationsOfficerDept, String investorRelationsOfficerAddress, String investorRelationsOfficerContactNo, String investorRelationsOfficerEmail, String operationsOfficer, String operationsOfficerDept, String operationsOfficerAddress, String operationsOfficerContactNo, String operationsOfficerEmail, String conditions, String creditSupport,  String creditTo,
                  String currentAccNo, String IFSCCode, String marketConventions, String businessActivity, String stockExchange, String netWorth, String workingCapitalLimit, String outstandingBankBorrowing, String faceValue, String amountOfCPOutstanding) {
            this.issuerName = issuerName;
            this.secondParty = secondParty;
            this.desc = desc;
            this.boardResolutionDate = boardResolutionDate;
            this.valueDate = valueDate;
            this.tradeDate = tradeDate;
            this.dateOfContract = dateOfContract;
            this.allotDate = allotDate;
            this.matDate = matDate;
            this.issueValue = issueValue;
            this.maturityValue = maturityValue;
            this.redemValue = redemValue;
            this.totalAmount = totalAmount;
            this.tax = tax;
            this.CIN = CIN;
            this.NSDLHistory = NSDLHistory;
            this.issuerAddress = issuerAddress;
            this.officialContactNum = officialContactNum;
            this.officialFaxNum = officialFaxNum;
            this.officialEmail = officialEmail;
            this.officialWebsite = officialWebsite;
            this.entityType = entityType;
            this.price = price;
            this.discRate = discRate;
            this.currency = currency;
            this.issueRef = issueRef;
            this.isinCode = isinCode;
            this.creditRating = creditRating;
            this.ratingIssuedBy = ratingIssuedBy;
            this.dateOfRating = dateOfRating;
            this.ratingValidityDate = ratingValidityDate;
            this.effectiveDateOfRating = effectiveDateOfRating;
            this.complianceOfficer = complianceOfficer;
            this.complianceOfficerDept = complianceOfficerDept;
            this.complianceOfficerAddress = complianceOfficerAddress;
            this.complianceOfficerContactNo = complianceOfficerContactNo;
            this.complianceOfficerEmail = complianceOfficerEmail;
            this.investorRelationsOfficer = investorRelationsOfficer;
            this.investorRelationsOfficerDept = investorRelationsOfficerDept;
            this.investorRelationsOfficerAddress = investorRelationsOfficerAddress;
            this.investorRelationsOfficerContactNo = investorRelationsOfficerContactNo;
            this.investorRelationsOfficerEmail = investorRelationsOfficerEmail;
            this.operationsOfficer = operationsOfficer;
            this.operationsOfficerDept = operationsOfficerDept;
            this.operationsOfficerAddress = operationsOfficerAddress;
            this.operationsOfficerContactNo = operationsOfficerContactNo;
            this.operationsOfficerEmail = operationsOfficerEmail;
            this.issuerContactPerson = issuerContactPerson;
            this.conditions = conditions;
            this.creditSupport = creditSupport;
            this.creditTo = creditTo;
            this.currentAccNo = currentAccNo;
            this.IFSCCode = IFSCCode;
            this.marketConventions = marketConventions;
            this.businessActivity = businessActivity;
            this.stockExchange = stockExchange;
            this.netWorth = netWorth;
            this.workingCapitalLimit = workingCapitalLimit;
            this.outstandingBankBorrowing = outstandingBankBorrowing;
            this.faceValue = faceValue;
            this.amountOfCPOutstanding = amountOfCPOutstanding;
        }
    }

    /*public class Container {
        public int type;
        public Object object;

        public Container() {
        }
    }*/
}
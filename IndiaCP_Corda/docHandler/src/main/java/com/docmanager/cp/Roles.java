package com.docmanager.cp;

/**
 * Created by Electrania.com on 12/10/2016.
 */

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Electrania.com on 12/4/2016.
 */
public class Roles extends ArrayList<Roles.Container> {

    public Roles()
    {}

    public class Container {
        public int type;
        public Object object;
    }

    public class CP {
        public String issuerName;
        public String secondParty;
        public Date boardResolutionDate;
        public Date valueDate;
        public Date tradeDate;
        public Date dateOfContract;
        public Date allotmentDate;
        public String description;
        public Date maturityDate;
        public String issueValue;
        public String maturityValue;
        public String redemptionValue;
        public String totalAmount;
        public String tax;
        public String CIN;
        public String NSDLHistory;
        public String issuerAddress;
        public String entityType;
        public String price;
        public String discRate;
        public String issueRef;
        public String isinCode;
        public String creditRating;
        public String ratingIssuedBy;
        public Date dateOfRating;
        public Date ratingValidityDate;
        public Date effectiveDateOfRating;
        public String contactPerson;
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
        public String creditInstructions;
        public String marketConventions;
        public String businessActivity;
        public String stockExchange;
        public String netWorth;
        public String workingCapitalLimit;
        public String outstandingBankBorrowing;
        public String faceValue;
        public String amountOfCPOutstanding;

        public CP()
        {}

        public CP(
                String issuerName,
                String secondParty,
                String description,
                Date boardResolutionDate,
                Date valueDate,
                Date tradeDate,
                Date dateOfContract,
                Date allotmentDate,
                Date maturityDate,
                String issueValue,
                String maturityValue,
                String redemptionValue,
                String totalAmount,
                String tax,
                String CIN,
                String NSDLHistory,
                String issuerAddress,
                String entityType,
                String price,
                String discRate,
                String issueRef,
                String isinCode,
                String creditRating,
                String ratingIssuedBy,
                Date dateOfRating,
                Date ratingValidityDate,
                Date effectiveDateOfRating,
                String contactPerson,
                String complianceOfficer,
                String complianceOfficerDept,
                String complianceOfficerAddress,
                String complianceOfficerContactNo,
                String complianceOfficerEmail,
                String investorRelationsOfficer,
                String investorRelationsOfficerDept,
                String investorRelationsOfficerAddress,
                String investorRelationsOfficerContactNo,
                String investorRelationsOfficerEmail,
                String operationsOfficer,
                String operationsOfficerDept,
                String operationsOfficerAddress,
                String operationsOfficerContactNo,
                String operationsOfficerEmail,
                String conditions,
                String creditSupport,
                String creditInstructions,
                String marketConventions,
                String businessActivity,
                String stockExchange,
                String netWorth,
                String workingCapitalLimit,
                String outstandingBankBorrowing,
                String faceValue,
                String amountOfCPOutstanding

        )
        {
            this.issuerName = issuerName;
            this.secondParty = secondParty;
            this.description = description;
            this.boardResolutionDate = boardResolutionDate;
            this.valueDate = valueDate;
            this.tradeDate = tradeDate;
            this.dateOfContract = dateOfContract;
            this.allotmentDate = allotmentDate;
            this.maturityDate = maturityDate;
            this.issueValue = issueValue;
            this.maturityValue = maturityValue;
            this.redemptionValue = redemptionValue;
            this.totalAmount = totalAmount;
            this.tax = tax;
            this.CIN = CIN;
            this.NSDLHistory = NSDLHistory;
            this.issuerAddress = issuerAddress;
            this.entityType = entityType;
            this.price = price;
            this.discRate = discRate;
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
            this.contactPerson = contactPerson;
            this.conditions = conditions;
            this.creditSupport = creditSupport;
            this.creditInstructions = creditInstructions;
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

    public class Investor {
        public String investorName;
        public String dpName;
        public String clientId;
        public String dpId;
        public String contactPerson;

        public Investor()
        {}

        public Investor(
                String investorName,
                String dpName,
                String clientId,
                String dpId,
                String contactPerson
        )
        {
            this.investorName = investorName;
            this.dpName = dpName;
            this.clientId = clientId;
            this.dpId = dpId;
            this.contactPerson = contactPerson;
        }


    }
    public class IPA {

        public String ipaName;
        public String ipaAddress;
        public IPA()
        {}
        public IPA(String ipaName, String ipaAddress)
        {
            this.ipaName = ipaName;
            this.ipaAddress = ipaAddress;
        }
    }



}

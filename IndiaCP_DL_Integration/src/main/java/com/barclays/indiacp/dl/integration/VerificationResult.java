package com.barclays.indiacp.dl.integration;

/**
 * Created by Electrania.com on 1/14/2017.
 */
public class VerificationResult {

    public String result;
    public String issuerOrganisation;
    public String serialNumber;
    public String signerName;
    public String organization;
    public String signStatus;
    public String trustStatus;
    public String expiryStatus;

    public VerificationResult()
    {}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIssuerOrganisation() {
        return issuerOrganisation;
    }

    public void setIssuerOrganisation(String issuerOrganisation) {
        this.issuerOrganisation = issuerOrganisation;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSignerName() {
        return signerName;
    }

    public void setSignerName(String signerName) {
        this.signerName = signerName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        if(signStatus.equals("1000"))
        {
            this.signStatus = "Success";
        }
        else
        {
            this.signStatus = "Failure";
        }
    }

    public String getTrustStatus() {
        return trustStatus;
    }

    public void setTrustStatus(String trustStatus) {

        if(trustStatus.equals("1000"))
        {
            this.trustStatus = "Success";
        }
        else
        {
            this.trustStatus = "Failure";
        }
    }

    public String getExpiryStatus() {
        return expiryStatus;
    }

    public void setExpiryStatus(String expiryStatus) {

        if(expiryStatus.equals("1000"))
        {
            this.expiryStatus = "Success";
        }
        else
        {
            this.expiryStatus = "Failure";
        }
    }



    public VerificationResult(String result, String issuerOrganisation, String serialNumber, String signerName, String organization, String signStatus, String trustStatus,
                              String expiryStatus) {

        this.result = result;
        this.issuerOrganisation = issuerOrganisation;
        this.serialNumber = serialNumber;
        this.signerName = signerName;
        this.organization = organization;
        this.signStatus = signStatus;
        this.trustStatus = trustStatus;
        this.expiryStatus = expiryStatus;
    }








}

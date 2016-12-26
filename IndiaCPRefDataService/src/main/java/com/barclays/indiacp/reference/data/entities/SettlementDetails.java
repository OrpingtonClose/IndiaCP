package com.barclays.indiacp.reference.data.entities;

import javax.persistence.*;

/**
 * Created by chaos on 22/12/16.
 */


@Entity
@Table(name = "settlement_details_ref_data")
public class SettlementDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_key")
    Integer settlement_key;

    @Column(name = "creditorName")
    String creditorName;

    @Column(name = "bankAccountDetails")
    String bankAccountDetails;

    @Column(name = "bankName")
    String bankName;

    @Column(name = "rtgsCode")
    String rtgsCode;

    @Column(name = "dpName")
    String dpName;

    @Column(name = "allocationClientId")
    String allocationClientId;

    @Column(name = "allocationDpID")
    String allocationDpID;

    @Column(name = "redemptionClientId")
    String redemptionClientId;

    @Column(name = "redemptionDpID")
    String redemptionDpID;

    @Column(name = "legal_entity_id")
    Integer legal_entity_id;

    public SettlementDetails() {
    }

    public SettlementDetails(Integer legal_entity_id, String creditorName, String bankAccountDetails, String bankName, String rtgsCode, String dpName, String allocationClientId, String allocationDpID, String redemptionClientId, String redemptionDpID)
    {
        this.legal_entity_id = legal_entity_id;
        this.creditorName = creditorName;
        this.bankAccountDetails = bankAccountDetails;
        this.bankName = bankName;
        this.rtgsCode = rtgsCode;
        this.dpName = dpName;
        this.allocationClientId = allocationClientId;
        this.allocationDpID = allocationDpID;
        this.redemptionClientId = redemptionClientId;
        this.redemptionDpID = redemptionDpID;
    }


    public Integer getSettlement_key() {
        return settlement_key;
    }




    public String getCreditorName() {
        return creditorName;
    }

    public String getBankAccountDetails() {
        return bankAccountDetails;
    }

    public String getBankName() {
        return bankName;
    }

    public String getRtgsCode() {
        return rtgsCode;
    }

    public String getDpName() {
        return dpName;
    }






    public void setSettlement_key(Integer settlement_key) {
        this.settlement_key = settlement_key;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public void setBankAccountDetails(String bankAccountDetails) {
        this.bankAccountDetails = bankAccountDetails;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setRtgsCode(String rtgsCode) {
        this.rtgsCode = rtgsCode;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }





    public String getAllocationClientId() {
        return allocationClientId;
    }

    public void setAllocationClientId(String allocationClientId) {
        this.allocationClientId = allocationClientId;
    }

    public String getAllocationDpID() {
        return allocationDpID;
    }

    public void setAllocationDpID(String allocationDpID) {
        this.allocationDpID = allocationDpID;
    }

    public String getRedemptionClientId() {
        return redemptionClientId;
    }

    public void setRedemptionClientId(String redemptionClientId) {
        this.redemptionClientId = redemptionClientId;
    }

    public String getRedemptionDpID() {
        return redemptionDpID;
    }

    public void setRedemptionDpID(String redemptionDpID) {
        this.redemptionDpID = redemptionDpID;
    }

    public Integer getLegal_entity_id() {
        return legal_entity_id;
    }

    public void setLegal_entity_id(Integer legal_entity_id) {
        this.legal_entity_id = legal_entity_id;
    }

    @Override
    public String toString() {
        return "SettlementDetails{" +
                "settlement_key=" + settlement_key +
                ", creditorName='" + creditorName + '\'' +
                ", bankAccountDetails='" + bankAccountDetails + '\'' +
                ", bankName='" + bankName + '\'' +
                ", rtgsCode='" + rtgsCode + '\'' +
                ", dpName='" + dpName + '\'' +
                ", allocationClientId='" + allocationClientId + '\'' +
                ", allocationDpID='" + allocationDpID + '\'' +
                ", redemptionClientId='" + redemptionClientId + '\'' +
                ", redemptionDpID='" + redemptionDpID + '\'' +
                '}';
    }
}

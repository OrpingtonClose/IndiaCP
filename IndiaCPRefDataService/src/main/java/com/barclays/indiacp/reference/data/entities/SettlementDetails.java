package com.barclays.indiacp.reference.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chaos on 22/12/16.
 */

@Entity
@Table(name = "settlement_details_ref_data")
public class SettlementDetails
{

    @Id
    @Column(name = "settlement_key")
    String settlement_key;

    @Column(name = "party_type")
    String party_type;

//    @Column(name = "cp_program_id")
//    String cpProgramID;
//
//    @Column(name = "cp_trade_id")
//    String cpTradeID;

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

    @Column(name = "clientId")
    String clientId;

    @Column(name = "dpID")
    String dpID;

    public SettlementDetails() {
    }

    public SettlementDetails(String settlement_key, String party_type, String creditorName, String bankAccountDetails, String bankName, String rtgsCode, String dpName, String clientId, String dpID) {
        this.settlement_key = settlement_key;
        this.party_type = party_type;
        this.creditorName = creditorName;
        this.bankAccountDetails = bankAccountDetails;
        this.bankName = bankName;
        this.rtgsCode = rtgsCode;
        this.dpName = dpName;
        this.clientId = clientId;
        this.dpID = dpID;
    }


    public String getSettlement_key() {
        return settlement_key;
    }

    public String getParty_type() {
        return party_type;
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

    public String getClientId() {
        return clientId;
    }

    public String getDpID() {
        return dpID;
    }

    public void setSettlement_key(String settlement_key) {
        this.settlement_key = settlement_key;
    }

    public void setParty_type(String party_type) {
        this.party_type = party_type;
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

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setDpID(String dpID) {
        this.dpID = dpID;
    }
}

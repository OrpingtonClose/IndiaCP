/*
 * IndiaCP API
 * This API will drive the UI
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.barclays.indiacp.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * IndiaCPIssue
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-01-10T12:15:59.809Z")
public class IndiaCPIssue   {
  @JsonProperty("cpProgramId")
  private String cpProgramId = null;

  @JsonProperty("cpTradeId")
  private String cpTradeId = null;

  @JsonProperty("bookId")
  private String bookId = null;

  @JsonProperty("isin")
  private String isin = null;

  @JsonProperty("traderId")
  private String traderId = null;

  @JsonProperty("issuerId")
  private String issuerId = null;

  @JsonProperty("issuerName")
  private String issuerName = null;

  @JsonProperty("beneficiaryId")
  private String beneficiaryId = null;

  @JsonProperty("beneficiaryName")
  private String beneficiaryName = null;

  @JsonProperty("ipaId")
  private String ipaId = null;

  @JsonProperty("ipaName")
  private String ipaName = null;

  @JsonProperty("depositoryId")
  private String depositoryId = null;

  @JsonProperty("depositoryName")
  private String depositoryName = null;

  @JsonProperty("tradeDate")
  private Date tradeDate = null;

  @JsonProperty("valueDate")
  private Date valueDate = null;

  @JsonProperty("maturityDays")
  private Integer maturityDays = null;

  @JsonProperty("currency")
  private String currency = null;

  @JsonProperty("facevaluePerUnit")
  private Integer facevaluePerUnit = null;

  @JsonProperty("noOfUnits")
  private Integer noOfUnits = null;

  @JsonProperty("rate")
  private Float rate = null;

  @JsonProperty("issuerSettlementDetails")
  private SettlementDetails issuerSettlementDetails = null;

  @JsonProperty("investorSettlementDetails")
  private SettlementDetails investorSettlementDetails = null;

  @JsonProperty("ipaSettlementDetails")
  private SettlementDetails ipaSettlementDetails = null;

  @JsonProperty("dealConfirmationDocId")
  private String dealConfirmationDocId = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("version")
  private Integer version = null;

  @JsonProperty("modifiedBy")
  private String modifiedBy = null;

  @JsonProperty("lastModifiedDate")
  private Date lastModifiedDate = null;

  public IndiaCPIssue cpProgramId(String cpProgramId) {
    this.cpProgramId = cpProgramId;
    return this;
  }

   /**
   * Unique identifier representing a specific CP Program raised by an Issuer. This CP Issue is allotted under this umbrella program
   * @return cpProgramId
  **/
  @ApiModelProperty(value = "Unique identifier representing a specific CP Program raised by an Issuer. This CP Issue is allotted under this umbrella program")
  public String getCpProgramId() {
    return cpProgramId;
  }

  public void setCpProgramId(String cpProgramId) {
    this.cpProgramId = cpProgramId;
  }

  public IndiaCPIssue cpTradeId(String cpTradeId) {
    this.cpTradeId = cpTradeId;
    return this;
  }

   /**
   * Unique identifier representing a specific CP Issue under the umbrella CP Program
   * @return cpTradeId
  **/
  @ApiModelProperty(value = "Unique identifier representing a specific CP Issue under the umbrella CP Program")
  public String getCpTradeId() {
    return cpTradeId;
  }

  public void setCpTradeId(String cpTradeId) {
    this.cpTradeId = cpTradeId;
  }

  public IndiaCPIssue bookId(String bookId) {
    this.bookId = bookId;
    return this;
  }

   /**
   * Internal Book Id that this trade is booked under
   * @return bookId
  **/
  @ApiModelProperty(value = "Internal Book Id that this trade is booked under")
  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public IndiaCPIssue isin(String isin) {
    this.isin = isin;
    return this;
  }

   /**
   * Unique CP Security Identifier No. In India this is issued by NSDL for Commercial Paper type of securities.
   * @return isin
  **/
  @ApiModelProperty(value = "Unique CP Security Identifier No. In India this is issued by NSDL for Commercial Paper type of securities.")
  public String getIsin() {
    return isin;
  }

  public void setIsin(String isin) {
    this.isin = isin;
  }

  public IndiaCPIssue traderId(String traderId) {
    this.traderId = traderId;
    return this;
  }

   /**
   * Unique identifier of the trader booking this trade
   * @return traderId
  **/
  @ApiModelProperty(value = "Unique identifier of the trader booking this trade")
  public String getTraderId() {
    return traderId;
  }

  public void setTraderId(String traderId) {
    this.traderId = traderId;
  }

  public IndiaCPIssue issuerId(String issuerId) {
    this.issuerId = issuerId;
    return this;
  }

   /**
   * Unique identifier of the Issuer
   * @return issuerId
  **/
  @ApiModelProperty(value = "Unique identifier of the Issuer")
  public String getIssuerId() {
    return issuerId;
  }

  public void setIssuerId(String issuerId) {
    this.issuerId = issuerId;
  }

  public IndiaCPIssue issuerName(String issuerName) {
    this.issuerName = issuerName;
    return this;
  }

   /**
   * Display name of the Issuer
   * @return issuerName
  **/
  @ApiModelProperty(value = "Display name of the Issuer")
  public String getIssuerName() {
    return issuerName;
  }

  public void setIssuerName(String issuerName) {
    this.issuerName = issuerName;
  }

  public IndiaCPIssue beneficiaryId(String beneficiaryId) {
    this.beneficiaryId = beneficiaryId;
    return this;
  }

   /**
   * Unique identifier of the Investor. This also uniquely identifies the Investor DL Node
   * @return beneficiaryId
  **/
  @ApiModelProperty(value = "Unique identifier of the Investor. This also uniquely identifies the Investor DL Node")
  public String getBeneficiaryId() {
    return beneficiaryId;
  }

  public void setBeneficiaryId(String beneficiaryId) {
    this.beneficiaryId = beneficiaryId;
  }

  public IndiaCPIssue beneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
    return this;
  }

   /**
   * Display name of the Investor
   * @return beneficiaryName
  **/
  @ApiModelProperty(value = "Display name of the Investor")
  public String getBeneficiaryName() {
    return beneficiaryName;
  }

  public void setBeneficiaryName(String beneficiaryName) {
    this.beneficiaryName = beneficiaryName;
  }

  public IndiaCPIssue ipaId(String ipaId) {
    this.ipaId = ipaId;
    return this;
  }

   /**
   * Unique identifier of the IPA
   * @return ipaId
  **/
  @ApiModelProperty(value = "Unique identifier of the IPA")
  public String getIpaId() {
    return ipaId;
  }

  public void setIpaId(String ipaId) {
    this.ipaId = ipaId;
  }

  public IndiaCPIssue ipaName(String ipaName) {
    this.ipaName = ipaName;
    return this;
  }

   /**
   * Display name of the IPA
   * @return ipaName
  **/
  @ApiModelProperty(value = "Display name of the IPA")
  public String getIpaName() {
    return ipaName;
  }

  public void setIpaName(String ipaName) {
    this.ipaName = ipaName;
  }

  public IndiaCPIssue depositoryId(String depositoryId) {
    this.depositoryId = depositoryId;
    return this;
  }

   /**
   * Unique identifier of the Depository (NSDL)
   * @return depositoryId
  **/
  @ApiModelProperty(value = "Unique identifier of the Depository (NSDL)")
  public String getDepositoryId() {
    return depositoryId;
  }

  public void setDepositoryId(String depositoryId) {
    this.depositoryId = depositoryId;
  }

  public IndiaCPIssue depositoryName(String depositoryName) {
    this.depositoryName = depositoryName;
    return this;
  }

   /**
   * Display name of the Depository
   * @return depositoryName
  **/
  @ApiModelProperty(value = "Display name of the Depository")
  public String getDepositoryName() {
    return depositoryName;
  }

  public void setDepositoryName(String depositoryName) {
    this.depositoryName = depositoryName;
  }

  public IndiaCPIssue tradeDate(Date tradeDate) {
    this.tradeDate = tradeDate;
    return this;
  }

   /**
   * Date on which the trade was captured
   * @return tradeDate
  **/
  @ApiModelProperty(value = "Date on which the trade was captured")
  public Date getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(Date tradeDate) {
    this.tradeDate = tradeDate;
  }

  public IndiaCPIssue valueDate(Date valueDate) {
    this.valueDate = valueDate;
    return this;
  }

   /**
   * Date on which the trade was settled and the Cash and CP securities were swapped between the Issuer and the Investor
   * @return valueDate
  **/
  @ApiModelProperty(value = "Date on which the trade was settled and the Cash and CP securities were swapped between the Issuer and the Investor")
  public Date getValueDate() {
    return valueDate;
  }

  public void setValueDate(Date valueDate) {
    this.valueDate = valueDate;
  }

  public IndiaCPIssue maturityDays(Integer maturityDays) {
    this.maturityDays = maturityDays;
    return this;
  }

   /**
   * Tenor of the CP maturity calculated from value date
   * @return maturityDays
  **/
  @ApiModelProperty(value = "Tenor of the CP maturity calculated from value date")
  public Integer getMaturityDays() {
    return maturityDays;
  }

  public void setMaturityDays(Integer maturityDays) {
    this.maturityDays = maturityDays;
  }

  public IndiaCPIssue currency(String currency) {
    this.currency = currency;
    return this;
  }

   /**
   * Currency of the issued CP Notes
   * @return currency
  **/
  @ApiModelProperty(value = "Currency of the issued CP Notes")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public IndiaCPIssue facevaluePerUnit(Integer facevaluePerUnit) {
    this.facevaluePerUnit = facevaluePerUnit;
    return this;
  }

   /**
   * Face Value per Unit * NoOfUnits is the amount that will be paid by the Issuer to the Investor on redemption
   * @return facevaluePerUnit
  **/
  @ApiModelProperty(value = "Face Value per Unit * NoOfUnits is the amount that will be paid by the Issuer to the Investor on redemption")
  public Integer getFacevaluePerUnit() {
    return facevaluePerUnit;
  }

  public void setFacevaluePerUnit(Integer facevaluePerUnit) {
    this.facevaluePerUnit = facevaluePerUnit;
  }

  public IndiaCPIssue noOfUnits(Integer noOfUnits) {
    this.noOfUnits = noOfUnits;
    return this;
  }

   /**
   * No. of CP Units Issued
   * @return noOfUnits
  **/
  @ApiModelProperty(value = "No. of CP Units Issued")
  public Integer getNoOfUnits() {
    return noOfUnits;
  }

  public void setNoOfUnits(Integer noOfUnits) {
    this.noOfUnits = noOfUnits;
  }

  public IndiaCPIssue rate(Float rate) {
    this.rate = rate;
    return this;
  }

   /**
   * Rate at which the yield is calculated
   * @return rate
  **/
  @ApiModelProperty(value = "Rate at which the yield is calculated")
  public Float getRate() {
    return rate;
  }

  public void setRate(Float rate) {
    this.rate = rate;
  }

  public IndiaCPIssue issuerSettlementDetails(SettlementDetails issuerSettlementDetails) {
    this.issuerSettlementDetails = issuerSettlementDetails;
    return this;
  }

   /**
   * Get issuerSettlementDetails
   * @return issuerSettlementDetails
  **/
  @ApiModelProperty(value = "")
  public SettlementDetails getIssuerSettlementDetails() {
    return issuerSettlementDetails;
  }

  public void setIssuerSettlementDetails(SettlementDetails issuerSettlementDetails) {
    this.issuerSettlementDetails = issuerSettlementDetails;
  }

  public IndiaCPIssue investorSettlementDetails(SettlementDetails investorSettlementDetails) {
    this.investorSettlementDetails = investorSettlementDetails;
    return this;
  }

   /**
   * Get investorSettlementDetails
   * @return investorSettlementDetails
  **/
  @ApiModelProperty(value = "")
  public SettlementDetails getInvestorSettlementDetails() {
    return investorSettlementDetails;
  }

  public void setInvestorSettlementDetails(SettlementDetails investorSettlementDetails) {
    this.investorSettlementDetails = investorSettlementDetails;
  }

  public IndiaCPIssue ipaSettlementDetails(SettlementDetails ipaSettlementDetails) {
    this.ipaSettlementDetails = ipaSettlementDetails;
    return this;
  }

   /**
   * Get ipaSettlementDetails
   * @return ipaSettlementDetails
  **/
  @ApiModelProperty(value = "")
  public SettlementDetails getIpaSettlementDetails() {
    return ipaSettlementDetails;
  }

  public void setIpaSettlementDetails(SettlementDetails ipaSettlementDetails) {
    this.ipaSettlementDetails = ipaSettlementDetails;
  }

  public IndiaCPIssue dealConfirmationDocId(String dealConfirmationDocId) {
    this.dealConfirmationDocId = dealConfirmationDocId;
    return this;
  }

   /**
   * Unique identifier of the deal confirmation document signed by both the Issuer and the Investor
   * @return dealConfirmationDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the deal confirmation document signed by both the Issuer and the Investor")
  public String getDealConfirmationDocId() {
    return dealConfirmationDocId;
  }

  public void setDealConfirmationDocId(String dealConfirmationDocId) {
    this.dealConfirmationDocId = dealConfirmationDocId;
  }

  public IndiaCPIssue status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Current status of the CP Issue
   * @return status
  **/
  @ApiModelProperty(value = "Current status of the CP Issue")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public IndiaCPIssue version(Integer version) {
    this.version = version;
    return this;
  }

   /**
   * Current version of the CP Issue
   * @return version
  **/
  @ApiModelProperty(value = "Current version of the CP Issue")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public IndiaCPIssue modifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
    return this;
  }

   /**
   * Unique identifier of the Logged-in User that performed the action. This is required for Audit History
   * @return modifiedBy
  **/
  @ApiModelProperty(value = "Unique identifier of the Logged-in User that performed the action. This is required for Audit History")
  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public IndiaCPIssue lastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
    return this;
  }

   /**
   * Last Modified Date for this CPIssue. This is required for Audit History
   * @return lastModifiedDate
  **/
  @ApiModelProperty(value = "Last Modified Date for this CPIssue. This is required for Audit History")
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndiaCPIssue indiaCPIssue = (IndiaCPIssue) o;
    return Objects.equals(this.cpProgramId, indiaCPIssue.cpProgramId) &&
        Objects.equals(this.cpTradeId, indiaCPIssue.cpTradeId) &&
        Objects.equals(this.bookId, indiaCPIssue.bookId) &&
        Objects.equals(this.isin, indiaCPIssue.isin) &&
        Objects.equals(this.traderId, indiaCPIssue.traderId) &&
        Objects.equals(this.issuerId, indiaCPIssue.issuerId) &&
        Objects.equals(this.issuerName, indiaCPIssue.issuerName) &&
        Objects.equals(this.beneficiaryId, indiaCPIssue.beneficiaryId) &&
        Objects.equals(this.beneficiaryName, indiaCPIssue.beneficiaryName) &&
        Objects.equals(this.ipaId, indiaCPIssue.ipaId) &&
        Objects.equals(this.ipaName, indiaCPIssue.ipaName) &&
        Objects.equals(this.depositoryId, indiaCPIssue.depositoryId) &&
        Objects.equals(this.depositoryName, indiaCPIssue.depositoryName) &&
        Objects.equals(this.tradeDate, indiaCPIssue.tradeDate) &&
        Objects.equals(this.valueDate, indiaCPIssue.valueDate) &&
        Objects.equals(this.maturityDays, indiaCPIssue.maturityDays) &&
        Objects.equals(this.currency, indiaCPIssue.currency) &&
        Objects.equals(this.facevaluePerUnit, indiaCPIssue.facevaluePerUnit) &&
        Objects.equals(this.noOfUnits, indiaCPIssue.noOfUnits) &&
        Objects.equals(this.rate, indiaCPIssue.rate) &&
        Objects.equals(this.issuerSettlementDetails, indiaCPIssue.issuerSettlementDetails) &&
        Objects.equals(this.investorSettlementDetails, indiaCPIssue.investorSettlementDetails) &&
        Objects.equals(this.ipaSettlementDetails, indiaCPIssue.ipaSettlementDetails) &&
        Objects.equals(this.dealConfirmationDocId, indiaCPIssue.dealConfirmationDocId) &&
        Objects.equals(this.status, indiaCPIssue.status) &&
        Objects.equals(this.version, indiaCPIssue.version) &&
        Objects.equals(this.modifiedBy, indiaCPIssue.modifiedBy) &&
        Objects.equals(this.lastModifiedDate, indiaCPIssue.lastModifiedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cpProgramId, cpTradeId, bookId, isin, traderId, issuerId, issuerName, beneficiaryId, beneficiaryName, ipaId, ipaName, depositoryId, depositoryName, tradeDate, valueDate, maturityDays, currency, facevaluePerUnit, noOfUnits, rate, issuerSettlementDetails, investorSettlementDetails, ipaSettlementDetails, dealConfirmationDocId, status, version, modifiedBy, lastModifiedDate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndiaCPIssue {\n");
    
    sb.append("    cpProgramId: ").append(toIndentedString(cpProgramId)).append("\n");
    sb.append("    cpTradeId: ").append(toIndentedString(cpTradeId)).append("\n");
    sb.append("    bookId: ").append(toIndentedString(bookId)).append("\n");
    sb.append("    isin: ").append(toIndentedString(isin)).append("\n");
    sb.append("    traderId: ").append(toIndentedString(traderId)).append("\n");
    sb.append("    issuerId: ").append(toIndentedString(issuerId)).append("\n");
    sb.append("    issuerName: ").append(toIndentedString(issuerName)).append("\n");
    sb.append("    beneficiaryId: ").append(toIndentedString(beneficiaryId)).append("\n");
    sb.append("    beneficiaryName: ").append(toIndentedString(beneficiaryName)).append("\n");
    sb.append("    ipaId: ").append(toIndentedString(ipaId)).append("\n");
    sb.append("    ipaName: ").append(toIndentedString(ipaName)).append("\n");
    sb.append("    depositoryId: ").append(toIndentedString(depositoryId)).append("\n");
    sb.append("    depositoryName: ").append(toIndentedString(depositoryName)).append("\n");
    sb.append("    tradeDate: ").append(toIndentedString(tradeDate)).append("\n");
    sb.append("    valueDate: ").append(toIndentedString(valueDate)).append("\n");
    sb.append("    maturityDays: ").append(toIndentedString(maturityDays)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    facevaluePerUnit: ").append(toIndentedString(facevaluePerUnit)).append("\n");
    sb.append("    noOfUnits: ").append(toIndentedString(noOfUnits)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
    sb.append("    issuerSettlementDetails: ").append(toIndentedString(issuerSettlementDetails)).append("\n");
    sb.append("    investorSettlementDetails: ").append(toIndentedString(investorSettlementDetails)).append("\n");
    sb.append("    ipaSettlementDetails: ").append(toIndentedString(ipaSettlementDetails)).append("\n");
    sb.append("    dealConfirmationDocId: ").append(toIndentedString(dealConfirmationDocId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    modifiedBy: ").append(toIndentedString(modifiedBy)).append("\n");
    sb.append("    lastModifiedDate: ").append(toIndentedString(lastModifiedDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


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
 * IndiaCPProgram
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-01-03T07:18:23.010Z")
public class IndiaCPProgram   {
  @JsonProperty("program_id")
  private String programId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("purpose")
  private String purpose = null;

  @JsonProperty("isin")
  private String isin = null;

  @JsonProperty("issuer_id")
  private String issuerId = null;

  @JsonProperty("issuer_name")
  private String issuerName = null;

  @JsonProperty("issue_commencement_date")
  private Date issueCommencementDate = null;

  @JsonProperty("program_size")
  private Integer programSize = null;

  @JsonProperty("program_allocated_value")
  private Double programAllocatedValue = null;

  @JsonProperty("program_currency")
  private String programCurrency = null;

  @JsonProperty("maturity_days")
  private Integer maturityDays = null;

  @JsonProperty("ipa_id")
  private String ipaId = null;

  @JsonProperty("ipa_name")
  private String ipaName = null;

  @JsonProperty("depository_id")
  private String depositoryId = null;

  @JsonProperty("depository_name")
  private String depositoryName = null;

  @JsonProperty("isin_generation_request_doc_id")
  private String isinGenerationRequestDocId = null;

  @JsonProperty("ipa_verification_request_doc_id")
  private String ipaVerificationRequestDocId = null;

  @JsonProperty("ipa_certificate_doc_id")
  private String ipaCertificateDocId = null;

  @JsonProperty("corporate_action_form_doc_id")
  private String corporateActionFormDocId = null;

  @JsonProperty("allotment_letter_doc_id")
  private String allotmentLetterDocId = null;

  @JsonProperty("status")
  private String status = null;

  @JsonProperty("version")
  private Integer version = null;

  @JsonProperty("modified_by")
  private String modifiedBy = null;

  @JsonProperty("last_modified")
  private Date lastModified = null;

  public IndiaCPProgram programId(String programId) {
    this.programId = programId;
    return this;
  }

   /**
   * Unique identifier representing a specific CP Program raised by an Issuer
   * @return programId
  **/
  @ApiModelProperty(value = "Unique identifier representing a specific CP Program raised by an Issuer")
  public String getProgramId() {
    return programId;
  }

  public void setProgramId(String programId) {
    this.programId = programId;
  }

  public IndiaCPProgram name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the CP Program
   * @return name
  **/
  @ApiModelProperty(value = "Name of the CP Program")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public IndiaCPProgram type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Type of the CP Program
   * @return type
  **/
  @ApiModelProperty(value = "Type of the CP Program")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public IndiaCPProgram purpose(String purpose) {
    this.purpose = purpose;
    return this;
  }

   /**
   * Purpose of the CP Program
   * @return purpose
  **/
  @ApiModelProperty(value = "Purpose of the CP Program")
  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public IndiaCPProgram isin(String isin) {
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

  public IndiaCPProgram issuerId(String issuerId) {
    this.issuerId = issuerId;
    return this;
  }

   /**
   * Unique identifier of the Issuer. This will also uniquely map to the DL Node of the Issuer
   * @return issuerId
  **/
  @ApiModelProperty(value = "Unique identifier of the Issuer. This will also uniquely map to the DL Node of the Issuer")
  public String getIssuerId() {
    return issuerId;
  }

  public void setIssuerId(String issuerId) {
    this.issuerId = issuerId;
  }

  public IndiaCPProgram issuerName(String issuerName) {
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

  public IndiaCPProgram issueCommencementDate(Date issueCommencementDate) {
    this.issueCommencementDate = issueCommencementDate;
    return this;
  }

   /**
   * Date when the CP Program was initiated. India regulations mandate that a program should be fully allocated within 2 weeks of commencement.
   * @return issueCommencementDate
  **/
  @ApiModelProperty(value = "Date when the CP Program was initiated. India regulations mandate that a program should be fully allocated within 2 weeks of commencement.")
  public Date getIssueCommencementDate() {
    return issueCommencementDate;
  }

  public void setIssueCommencementDate(Date issueCommencementDate) {
    this.issueCommencementDate = issueCommencementDate;
  }

  public IndiaCPProgram programSize(Integer programSize) {
    this.programSize = programSize;
    return this;
  }

   /**
   * Total borrowing amount under this CP Program
   * @return programSize
  **/
  @ApiModelProperty(value = "Total borrowing amount under this CP Program")
  public Integer getProgramSize() {
    return programSize;
  }

  public void setProgramSize(Integer programSize) {
    this.programSize = programSize;
  }

  public IndiaCPProgram programAllocatedValue(Double programAllocatedValue) {
    this.programAllocatedValue = programAllocatedValue;
    return this;
  }

   /**
   * Amount already allocated to the CP program
   * @return programAllocatedValue
  **/
  @ApiModelProperty(value = "Amount already allocated to the CP program")
  public Double getProgramAllocatedValue() {
    return programAllocatedValue;
  }

  public void setProgramAllocatedValue(Double programAllocatedValue) {
    this.programAllocatedValue = programAllocatedValue;
  }

  public IndiaCPProgram programCurrency(String programCurrency) {
    this.programCurrency = programCurrency;
    return this;
  }

   /**
   * Currency of the issue of CP notes
   * @return programCurrency
  **/
  @ApiModelProperty(value = "Currency of the issue of CP notes")
  public String getProgramCurrency() {
    return programCurrency;
  }

  public void setProgramCurrency(String programCurrency) {
    this.programCurrency = programCurrency;
  }

  public IndiaCPProgram maturityDays(Integer maturityDays) {
    this.maturityDays = maturityDays;
    return this;
  }

   /**
   * No of days to maturity from the value date
   * @return maturityDays
  **/
  @ApiModelProperty(value = "No of days to maturity from the value date")
  public Integer getMaturityDays() {
    return maturityDays;
  }

  public void setMaturityDays(Integer maturityDays) {
    this.maturityDays = maturityDays;
  }

  public IndiaCPProgram ipaId(String ipaId) {
    this.ipaId = ipaId;
    return this;
  }

   /**
   * Unique identifier of the IPA. This will also uniquely map to the DL Node of the IPA
   * @return ipaId
  **/
  @ApiModelProperty(value = "Unique identifier of the IPA. This will also uniquely map to the DL Node of the IPA")
  public String getIpaId() {
    return ipaId;
  }

  public void setIpaId(String ipaId) {
    this.ipaId = ipaId;
  }

  public IndiaCPProgram ipaName(String ipaName) {
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

  public IndiaCPProgram depositoryId(String depositoryId) {
    this.depositoryId = depositoryId;
    return this;
  }

   /**
   * Unique identifier of the Depository (NSDL). This will also uniquely map to the DL Node of the Depository
   * @return depositoryId
  **/
  @ApiModelProperty(value = "Unique identifier of the Depository (NSDL). This will also uniquely map to the DL Node of the Depository")
  public String getDepositoryId() {
    return depositoryId;
  }

  public void setDepositoryId(String depositoryId) {
    this.depositoryId = depositoryId;
  }

  public IndiaCPProgram depositoryName(String depositoryName) {
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

  public IndiaCPProgram isinGenerationRequestDocId(String isinGenerationRequestDocId) {
    this.isinGenerationRequestDocId = isinGenerationRequestDocId;
    return this;
  }

   /**
   * Unique identifier of the documents sent to the depository to generate ISIN
   * @return isinGenerationRequestDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the documents sent to the depository to generate ISIN")
  public String getIsinGenerationRequestDocId() {
    return isinGenerationRequestDocId;
  }

  public void setIsinGenerationRequestDocId(String isinGenerationRequestDocId) {
    this.isinGenerationRequestDocId = isinGenerationRequestDocId;
  }

  public IndiaCPProgram ipaVerificationRequestDocId(String ipaVerificationRequestDocId) {
    this.ipaVerificationRequestDocId = ipaVerificationRequestDocId;
    return this;
  }

   /**
   * Unique identifier of the documents sent to the IPA to certify the CP Program
   * @return ipaVerificationRequestDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the documents sent to the IPA to certify the CP Program")
  public String getIpaVerificationRequestDocId() {
    return ipaVerificationRequestDocId;
  }

  public void setIpaVerificationRequestDocId(String ipaVerificationRequestDocId) {
    this.ipaVerificationRequestDocId = ipaVerificationRequestDocId;
  }

  public IndiaCPProgram ipaCertificateDocId(String ipaCertificateDocId) {
    this.ipaCertificateDocId = ipaCertificateDocId;
    return this;
  }

   /**
   * Unique identifier of the IPA certificate issued by the IPA on verification of the CP Program and supporting Issuer documents
   * @return ipaCertificateDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the IPA certificate issued by the IPA on verification of the CP Program and supporting Issuer documents")
  public String getIpaCertificateDocId() {
    return ipaCertificateDocId;
  }

  public void setIpaCertificateDocId(String ipaCertificateDocId) {
    this.ipaCertificateDocId = ipaCertificateDocId;
  }

  public IndiaCPProgram corporateActionFormDocId(String corporateActionFormDocId) {
    this.corporateActionFormDocId = corporateActionFormDocId;
    return this;
  }

   /**
   * Unique identifier of the Corporate Action Form generated by the Issuer to allot CP
   * @return corporateActionFormDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the Corporate Action Form generated by the Issuer to allot CP")
  public String getCorporateActionFormDocId() {
    return corporateActionFormDocId;
  }

  public void setCorporateActionFormDocId(String corporateActionFormDocId) {
    this.corporateActionFormDocId = corporateActionFormDocId;
  }

  public IndiaCPProgram allotmentLetterDocId(String allotmentLetterDocId) {
    this.allotmentLetterDocId = allotmentLetterDocId;
    return this;
  }

   /**
   * Unique identifier of the Allotment Letter generated by IPA for CP transfer to Investor DP account
   * @return allotmentLetterDocId
  **/
  @ApiModelProperty(value = "Unique identifier of the Allotment Letter generated by IPA for CP transfer to Investor DP account")
  public String getAllotmentLetterDocId() {
    return allotmentLetterDocId;
  }

  public void setAllotmentLetterDocId(String allotmentLetterDocId) {
    this.allotmentLetterDocId = allotmentLetterDocId;
  }

  public IndiaCPProgram status(String status) {
    this.status = status;
    return this;
  }

   /**
   * Current status of the CP Program
   * @return status
  **/
  @ApiModelProperty(value = "Current status of the CP Program")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public IndiaCPProgram version(Integer version) {
    this.version = version;
    return this;
  }

   /**
   * Current version of the CP Program
   * @return version
  **/
  @ApiModelProperty(value = "Current version of the CP Program")
  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public IndiaCPProgram modifiedBy(String modifiedBy) {
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

  public IndiaCPProgram lastModified(Date lastModified) {
    this.lastModified = lastModified;
    return this;
  }

   /**
   * Last Modified Date for this CPIssue. This is required for Audit History
   * @return lastModified
  **/
  @ApiModelProperty(value = "Last Modified Date for this CPIssue. This is required for Audit History")
  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndiaCPProgram indiaCPProgram = (IndiaCPProgram) o;
    return Objects.equals(this.programId, indiaCPProgram.programId) &&
        Objects.equals(this.name, indiaCPProgram.name) &&
        Objects.equals(this.type, indiaCPProgram.type) &&
        Objects.equals(this.purpose, indiaCPProgram.purpose) &&
        Objects.equals(this.isin, indiaCPProgram.isin) &&
        Objects.equals(this.issuerId, indiaCPProgram.issuerId) &&
        Objects.equals(this.issuerName, indiaCPProgram.issuerName) &&
        Objects.equals(this.issueCommencementDate, indiaCPProgram.issueCommencementDate) &&
        Objects.equals(this.programSize, indiaCPProgram.programSize) &&
        Objects.equals(this.programAllocatedValue, indiaCPProgram.programAllocatedValue) &&
        Objects.equals(this.programCurrency, indiaCPProgram.programCurrency) &&
        Objects.equals(this.maturityDays, indiaCPProgram.maturityDays) &&
        Objects.equals(this.ipaId, indiaCPProgram.ipaId) &&
        Objects.equals(this.ipaName, indiaCPProgram.ipaName) &&
        Objects.equals(this.depositoryId, indiaCPProgram.depositoryId) &&
        Objects.equals(this.depositoryName, indiaCPProgram.depositoryName) &&
        Objects.equals(this.isinGenerationRequestDocId, indiaCPProgram.isinGenerationRequestDocId) &&
        Objects.equals(this.ipaVerificationRequestDocId, indiaCPProgram.ipaVerificationRequestDocId) &&
        Objects.equals(this.ipaCertificateDocId, indiaCPProgram.ipaCertificateDocId) &&
        Objects.equals(this.corporateActionFormDocId, indiaCPProgram.corporateActionFormDocId) &&
        Objects.equals(this.allotmentLetterDocId, indiaCPProgram.allotmentLetterDocId) &&
        Objects.equals(this.status, indiaCPProgram.status) &&
        Objects.equals(this.version, indiaCPProgram.version) &&
        Objects.equals(this.modifiedBy, indiaCPProgram.modifiedBy) &&
        Objects.equals(this.lastModified, indiaCPProgram.lastModified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(programId, name, type, purpose, isin, issuerId, issuerName, issueCommencementDate, programSize, programAllocatedValue, programCurrency, maturityDays, ipaId, ipaName, depositoryId, depositoryName, isinGenerationRequestDocId, ipaVerificationRequestDocId, ipaCertificateDocId, corporateActionFormDocId, allotmentLetterDocId, status, version, modifiedBy, lastModified);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndiaCPProgram {\n");
    
    sb.append("    programId: ").append(toIndentedString(programId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    isin: ").append(toIndentedString(isin)).append("\n");
    sb.append("    issuerId: ").append(toIndentedString(issuerId)).append("\n");
    sb.append("    issuerName: ").append(toIndentedString(issuerName)).append("\n");
    sb.append("    issueCommencementDate: ").append(toIndentedString(issueCommencementDate)).append("\n");
    sb.append("    programSize: ").append(toIndentedString(programSize)).append("\n");
    sb.append("    programAllocatedValue: ").append(toIndentedString(programAllocatedValue)).append("\n");
    sb.append("    programCurrency: ").append(toIndentedString(programCurrency)).append("\n");
    sb.append("    maturityDays: ").append(toIndentedString(maturityDays)).append("\n");
    sb.append("    ipaId: ").append(toIndentedString(ipaId)).append("\n");
    sb.append("    ipaName: ").append(toIndentedString(ipaName)).append("\n");
    sb.append("    depositoryId: ").append(toIndentedString(depositoryId)).append("\n");
    sb.append("    depositoryName: ").append(toIndentedString(depositoryName)).append("\n");
    sb.append("    isinGenerationRequestDocId: ").append(toIndentedString(isinGenerationRequestDocId)).append("\n");
    sb.append("    ipaVerificationRequestDocId: ").append(toIndentedString(ipaVerificationRequestDocId)).append("\n");
    sb.append("    ipaCertificateDocId: ").append(toIndentedString(ipaCertificateDocId)).append("\n");
    sb.append("    corporateActionFormDocId: ").append(toIndentedString(corporateActionFormDocId)).append("\n");
    sb.append("    allotmentLetterDocId: ").append(toIndentedString(allotmentLetterDocId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    modifiedBy: ").append(toIndentedString(modifiedBy)).append("\n");
    sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
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


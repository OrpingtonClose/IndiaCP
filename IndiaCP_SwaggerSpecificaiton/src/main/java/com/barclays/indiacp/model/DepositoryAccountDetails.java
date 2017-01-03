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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * DepositoryAccountDetails
 */
@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2017-01-03T07:18:23.010Z")
public class DepositoryAccountDetails   {
  @JsonProperty("dp_id")
  private String dpId = null;

  @JsonProperty("dp_name")
  private String dpName = null;

  /**
   * Depository Account Type. For now only the IPA has two depository accounts one for Allotment and one for Redemption
   */
  public enum DpTypeEnum {
    ALLOTTMENT("ALLOTTMENT"),
    
    REDEMPTION("REDEMPTION");

    private String value;

    DpTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DpTypeEnum fromValue(String text) {
      for (DpTypeEnum b : DpTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("dp_type")
  private DpTypeEnum dpType = null;

  @JsonProperty("client_id")
  private String clientId = null;

  public DepositoryAccountDetails dpId(String dpId) {
    this.dpId = dpId;
    return this;
  }

   /**
   * Unique identifier of the DP Account at the Depository
   * @return dpId
  **/
  @ApiModelProperty(value = "Unique identifier of the DP Account at the Depository")
  public String getDpId() {
    return dpId;
  }

  public void setDpId(String dpId) {
    this.dpId = dpId;
  }

  public DepositoryAccountDetails dpName(String dpName) {
    this.dpName = dpName;
    return this;
  }

   /**
   * Name under which the DP Account is registered. For e.g. Barclays Securities India Pvt Ltd
   * @return dpName
  **/
  @ApiModelProperty(value = "Name under which the DP Account is registered. For e.g. Barclays Securities India Pvt Ltd")
  public String getDpName() {
    return dpName;
  }

  public void setDpName(String dpName) {
    this.dpName = dpName;
  }

  public DepositoryAccountDetails dpType(DpTypeEnum dpType) {
    this.dpType = dpType;
    return this;
  }

   /**
   * Depository Account Type. For now only the IPA has two depository accounts one for Allotment and one for Redemption
   * @return dpType
  **/
  @ApiModelProperty(value = "Depository Account Type. For now only the IPA has two depository accounts one for Allotment and one for Redemption")
  public DpTypeEnum getDpType() {
    return dpType;
  }

  public void setDpType(DpTypeEnum dpType) {
    this.dpType = dpType;
  }

  public DepositoryAccountDetails clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

   /**
   * Unique identifier of the Client at the Depository
   * @return clientId
  **/
  @ApiModelProperty(value = "Unique identifier of the Client at the Depository")
  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DepositoryAccountDetails depositoryAccountDetails = (DepositoryAccountDetails) o;
    return Objects.equals(this.dpId, depositoryAccountDetails.dpId) &&
        Objects.equals(this.dpName, depositoryAccountDetails.dpName) &&
        Objects.equals(this.dpType, depositoryAccountDetails.dpType) &&
        Objects.equals(this.clientId, depositoryAccountDetails.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dpId, dpName, dpType, clientId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DepositoryAccountDetails {\n");
    
    sb.append("    dpId: ").append(toIndentedString(dpId)).append("\n");
    sb.append("    dpName: ").append(toIndentedString(dpName)).append("\n");
    sb.append("    dpType: ").append(toIndentedString(dpType)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
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


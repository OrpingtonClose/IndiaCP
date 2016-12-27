/**
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

// /// <reference path="api.d.ts" />

/* tslint:disable:no-unused-variable member-ordering */

module app.services {
    "use strict";

    export interface IDepositoryAccountDetails {
        /**
         * Unique identifier of the DP Account at the Depository
         */
        "dpId"?: string;

        /**
         * Name under which the DP Account is registered. For e.g. Barclays Securities India Pvt Ltd
         */
        "dpName"?: string;

        /**
         * Unique identifier of the Client at the Depository
         */
        "clientId"?: string;

    }

    export interface IPaymentAccountDetails {
        /**
         * Name in which the payment has to be made. For e.g. Barclays Investments & Loans (India) Ltd CP
         */
        "creditorName"?: string;

        /**
         * Bank account no. at the IPA Bank
         */
        "bankAccountNo"?: string;

        /**
         * Bank account type. For e.g. current account
         */
        "bankAccountType"?: string;

        /**
         * Name of the IPA Bank
         */
        "bankName"?: string;

        /**
         * RTGS IFSC code of the IPA Bank to receive payments
         */
        "rtgsIfscCode"?: string;
    }

    export interface ISettlementDetails {
        "paymentAccountDetails"?: IPaymentAccountDetails;

        "depositoryAccountDetails"?: IDepositoryAccountDetails;

    }

    export interface ICPIssue {
        /**
         * Unique identifier representing a specific CP Program raised by an Issuer. This CP Issue is allotted under this umbrella program
         */
        "cpProgramId"?: string;

        /**
         * Unique identifier representing a specific CP Issue under the umbrella CP Program
         */
        "cpTradeId"?: string;

        /**
         * Internal Book Id that this trade is booked under
         */
        "bookId"?: string;

        /**
         * Unique CP Security Identifier No. In India this is issued by NSDL for Commercial Paper type of securities.
         */
        "isin"?: string;

        /**
         * Unique identifier of the trader booking this trade
         */
        "traderId"?: string;

        /**
         * Unique identifier of the Issuer
         */
        "issuerId"?: string;

        /**
         * Display name of the Issuer
         */
        "issuerName"?: string;

        /**
         * Unique identifier of the Investor. This also uniquely identifies the Investor DL Node
         */
        "investorId"?: string;

        /**
         * Display name of the Investor
         */
        "investorName"?: string;

        /**
         * Unique identifier of the IPA
         */
        "ipaId"?: string;

        /**
         * Display name of the IPA
         */
        "ipaName"?: string;

        /**
         * Unique identifier of the Depository (NSDL)
         */
        "depositoryId"?: string;

        /**
         * Display name of the Depository
         */
        "depositoryName"?: string;

        /**
         * Date on which the trade was captured
         */
        "tradeDate"?: Date;

        /**
         * Date on which the trade was settled and the Cash and CP securities were swapped between the Issuer and the Investor
         */
        "valueDate"?: Date;

        /**
         * Date on which the CP will be matured and redeemed
         */
        "maturityDate"?: Date;

        /**
         * Currency of the issued CP Notes
         */
        "currency"?: string;

        /**
         * This is the amount that will be paid by the Issuer to the Investor on redemption
         */
        "notionalAmount"?: Date;

        /**
         * Rate at which the yield is calculated
         */
        "rate"?: number;

        "issuerSettlementDetails"?: ISettlementDetails;

        "investorSettlementDetails"?: ISettlementDetails;

        /**
         * Unique identifier of the deal confirmation document signed by both the Issuer and the Investor
         */
        "dealConfirmationDocId"?: string;
    }

    export interface IInvestorService{
        fetchAllCP(entity: string, extraHttpRequestParams?: any): ng.IHttpPromise<Array<ICPIssue>>;
        fetchCP(entity: string, cpIssueId: string, extraHttpRequestParams?: any): ng.IHttpPromise<ICPIssue>;
    }

    export class InvestorService implements IInvestorService {
        protected basePath = "http://finwizui.azurewebsites.net/api";
        public defaultHeaders: any = {};

        static $inject: string[] = ["$http", "$httpParamSerializer", "basePath"];

        constructor(protected $http: ng.IHttpService, protected $httpParamSerializer?: (d: any) => any, basePath?: string) {
            if (basePath !== undefined) {
                this.basePath = basePath;
            }
        }

        private extendObj<T1, T2>(objA: T1, objB: T2) {
            for (let key in objB) {
                if (objB.hasOwnProperty(key)) {
                    objA[key.toString()] = objB[key.toString()];
                }
            }
            return <T1 & T2>objA;
        }

        /**
         * Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature
         * This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call 
         * @param entity issuer or investor id that uniquely maps to the DL node
         */
        public fetchAllCP(entity: string, extraHttpRequestParams?: any): ng.IHttpPromise<Array<ICPIssue>> {
            const localVarPath = this.basePath + "/cpissues/open/{entity}"
                .replace("{" + "entity" + "}", String(entity));

            let queryParameters: any = {};
            let headerParams: any = this.extendObj({}, this.defaultHeaders);
            // verify required parameter "entity" is not null or undefined
            if (entity === null || entity === undefined) {
                throw new Error("Required parameter entity was null or undefined when calling fetchAllCP.");
            }
            let httpRequestParams: any = {
                method: "GET",
                url: localVarPath,
                json: true,
                params: queryParameters,
                headers: headerParams
            };

            if (extraHttpRequestParams) {
                httpRequestParams = this.extendObj(httpRequestParams, extraHttpRequestParams);
            }

            return this.$http(httpRequestParams);
        }
        /**
         * Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature
         * This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call 
         * @param entity issuer or investor id that uniquely maps to the DL node
         * @param cpIssueId Unique identifier of the CP Issue to be fetched
         */
        public fetchCP(entity: string, cpIssueId: string, extraHttpRequestParams?: any): ng.IHttpPromise<ICPIssue> {
            const localVarPath = this.basePath + "/cpissue/{entity}/{cpIssueId}"
                .replace("{" + "entity" + "}", String(entity))
                .replace("{" + "cpIssueId" + "}", String(cpIssueId));

            let queryParameters: any = {};
            let headerParams: any = this.extendObj({}, this.defaultHeaders);
            // verify required parameter "entity" is not null or undefined
            if (entity === null || entity === undefined) {
                throw new Error("Required parameter entity was null or undefined when calling fetchCP.");
            }
            // verify required parameter "cpIssueId" is not null or undefined
            if (cpIssueId === null || cpIssueId === undefined) {
                throw new Error("Required parameter cpIssueId was null or undefined when calling fetchCP.");
            }
            let httpRequestParams: any = {
                method: "GET",
                url: localVarPath,
                json: true,
                params: queryParameters,
                headers: headerParams
            };

            if (extraHttpRequestParams) {
                httpRequestParams = this.extendObj(httpRequestParams, extraHttpRequestParams);
            }

            return this.$http(httpRequestParams);
        }
    }
}
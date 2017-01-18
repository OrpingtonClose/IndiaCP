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

    export interface IInvestorService{
        fetchAllCP(entity: string, extraHttpRequestParams?: any): ng.IHttpPromise<Array<app.models.IndiaCPIssue>>;
        fetchAllCPOnThisNode (extraHttpRequestParams?: any ) : ng.IHttpPromise<Array<app.models.IndiaCPIssue>>;
        fetchCP(entity: string, cpIssueId: string, extraHttpRequestParams?: any): ng.IHttpPromise<app.models.IndiaCPIssue>;
    }

    export class InvestorService implements IInvestorService {
         protected basePath:string;
        public defaultHeaders: any = {};

        static $inject: string[] = ["$http", "localStorageService","$httpParamSerializer"];

        constructor(protected $http: ng.IHttpService,protected localStorageService:ng.local.storage.ILocalStorageService, protected $httpParamSerializer?: (d: any) => any) {
              var nodeInfo:app.models.NodeInfo = this.localStorageService.get("nodeInfo") as app.models.NodeInfo;
            var host:string = nodeInfo.host;
            var port:number = nodeInfo.port;
            this.basePath = `http://${host}:${port}/indiacp`;
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
        public fetchAllCP (entity: string, extraHttpRequestParams?: any ) : ng.IHttpPromise<Array<app.models.IndiaCPIssue>> {
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
         * This returns all the Open CP Issues for the given DL Node
         */
        public fetchAllCPOnThisNode (extraHttpRequestParams?: any ) : ng.IHttpPromise<Array<app.models.IndiaCPIssue>> {
            const localVarPath = this.basePath + "/fetchAllCP";

            let queryParameters: any = {};
            let headerParams: any = this.extendObj({}, this.defaultHeaders);
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
        public fetchCP (entity: string, cpIssueId: string, extraHttpRequestParams?: any ) : ng.IHttpPromise<app.models.IndiaCPIssue> {
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

    angular
        .module("app.services")
        .service("app.services.InvestorService", InvestorService);
}

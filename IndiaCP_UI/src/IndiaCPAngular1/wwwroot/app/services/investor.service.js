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
var app;
(function (app) {
    var services;
    (function (services) {
        "use strict";
        var InvestorService = (function () {
            function InvestorService($http, $httpParamSerializer) {
                this.$http = $http;
                this.$httpParamSerializer = $httpParamSerializer;
                this.basePath = "http://finwizui.azurewebsites.net/api";
                this.defaultHeaders = {};
            }
            InvestorService.prototype.extendObj = function (objA, objB) {
                for (var key in objB) {
                    if (objB.hasOwnProperty(key)) {
                        objA[key.toString()] = objB[key.toString()];
                    }
                }
                return objA;
            };
            /**
             * Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature
             * This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call
             * @param entity issuer or investor id that uniquely maps to the DL node
             */
            InvestorService.prototype.fetchAllCP = function (entity, extraHttpRequestParams) {
                var localVarPath = this.basePath + "/cpissues/open/{entity}"
                    .replace("{" + "entity" + "}", String(entity));
                var queryParameters = {};
                var headerParams = this.extendObj({}, this.defaultHeaders);
                // verify required parameter "entity" is not null or undefined
                if (entity === null || entity === undefined) {
                    throw new Error("Required parameter entity was null or undefined when calling fetchAllCP.");
                }
                var httpRequestParams = {
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
            };
            /**
             * Get All Open CP Issues for the given Issuer/Investor. Open CP Issues refers to the Issues that are yet to mature
             * This returns all the CP Issues under the umbrella CP Program identified by an Id provided by the call
             * @param entity issuer or investor id that uniquely maps to the DL node
             * @param cpIssueId Unique identifier of the CP Issue to be fetched
             */
            InvestorService.prototype.fetchCP = function (entity, cpIssueId, extraHttpRequestParams) {
                var localVarPath = this.basePath + "/cpissue/{entity}/{cpIssueId}"
                    .replace("{" + "entity" + "}", String(entity))
                    .replace("{" + "cpIssueId" + "}", String(cpIssueId));
                var queryParameters = {};
                var headerParams = this.extendObj({}, this.defaultHeaders);
                // verify required parameter "entity" is not null or undefined
                if (entity === null || entity === undefined) {
                    throw new Error("Required parameter entity was null or undefined when calling fetchCP.");
                }
                // verify required parameter "cpIssueId" is not null or undefined
                if (cpIssueId === null || cpIssueId === undefined) {
                    throw new Error("Required parameter cpIssueId was null or undefined when calling fetchCP.");
                }
                var httpRequestParams = {
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
            };
            return InvestorService;
        }());
        InvestorService.$inject = ["$http", "$httpParamSerializer"];
        services.InvestorService = InvestorService;
        angular
            .module("app.services")
            .service("app.services.InvestorService", InvestorService);
    })(services = app.services || (app.services = {}));
})(app || (app = {}));
//# sourceMappingURL=investor.service.js.map
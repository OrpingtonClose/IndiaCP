using IdentityModel.Client;
using IndiaCPAngular1.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Net.Http;

namespace IndiaCPAngular1.Controllers
{
    [Route("api/[controller]")]
    public class AuthenticationController
    {
        private readonly ILogger _logger;

        public AuthenticationController(ILogger<AuthenticationController> logger)
        {
            _logger = logger;
        }

        [HttpGet]
        public void Test()
        {
            int i = 1;
        }

        [HttpPost(Name = "Post")]
        public ActionResult Post([FromBody] UserCredentials credentials)
        {
            var disco = DiscoveryClient.GetAsync("http://indiacpidentityserver.azurewebsites.net").GetAwaiter().GetResult();
            var tokenClient = new TokenClient(disco.TokenEndpoint, "ro.client", "secret");
            var tokenResponse = tokenClient.RequestResourceOwnerPasswordAsync(credentials.Username, credentials.Password, "api1").GetAwaiter().GetResult();

            if (tokenResponse.IsError)
            {
                var resp = new HttpResponseMessage();
                resp.StatusCode = System.Net.HttpStatusCode.Unauthorized;
                _logger.LogError(tokenResponse.Error + " " + tokenResponse.ErrorDescription);
                return new UnauthorizedResult();
            }
            else
            {
                NodeInfo nodeInfo = new NodeInfo();


 
                switch (credentials.Username)
                {
                    case "issuer1":
                        nodeInfo.NodeType = "ISSUER";
                        nodeInfo.NodeName = "Barclays Investments and Loans (India) Ltd";
                        nodeInfo.Host = "52.172.46.253";
                        nodeInfo.Port = 8181;
                        nodeInfo.DLNodeName = "BILL_ISSUER";
                        break;
                    case "investor1":
                        nodeInfo.NodeType = "INVESTOR";
                        nodeInfo.NodeName = "Barclays Shared Services";
                        nodeInfo.Host = "52.172.46.253";
                        nodeInfo.Port = 8182;
                        nodeInfo.DLNodeName = "BSS_INVESTOR";
                        break;
                    case "nsdl1":
                        nodeInfo.NodeType = "DEPOSITORY";
                        nodeInfo.NodeName = "NSDL";
                        nodeInfo.Host = "52.172.46.253";
                        nodeInfo.Port = 8183;
                        nodeInfo.DLNodeName = "NSDL_DEPOSITORY";
                        break;
                    default:
                        nodeInfo.NodeType = "IPA";
                        nodeInfo.NodeName = "HDFC";
                        nodeInfo.Host = "52.172.46.253";
                        nodeInfo.Port = 8184;
                        nodeInfo.DLNodeName = "HDFC_IPA";
                        break;
                }


//#if DEBUG                 
//#else
//                nodeInfo.NodeType = Environment.GetEnvironmentVariable("NODETYPE");
//#endif
//                nodeInfo.Host = Environment.GetEnvironmentVariable("HOST");
//                nodeInfo.NodeName = Environment.GetEnvironmentVariable("NODENAME");
//                nodeInfo.Port = Int32.Parse(Environment.GetEnvironmentVariable("PORT"));


                Dictionary<string, object> info = new Dictionary<string, object>();
                info["nodeInfo"] = nodeInfo;
                info["accessToken"] = tokenResponse.AccessToken;
                _logger.LogInformation(tokenResponse.Json.ToString());

                return new OkObjectResult(info);
            }
        }
    }
}

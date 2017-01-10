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



#if DEBUG
                if (credentials.Username == "investor1")
                {
                    nodeInfo.NodeType = "INVESTOR";
                }
                else
                    nodeInfo.NodeType = "ISSUER";
#else
                nodeInfo.NodeType = Environment.GetEnvironmentVariable("NODETYPE");
#endif
                nodeInfo.Host = Environment.GetEnvironmentVariable("HOST");
                nodeInfo.Port = Int32.Parse(Environment.GetEnvironmentVariable("PORT"));
                Dictionary<string, object> info = new Dictionary<string, object>();
                info["nodeInfo"] = nodeInfo;
                info["accessToken"] = tokenResponse.AccessToken;
                _logger.LogInformation(tokenResponse.Json.ToString());

                return new OkObjectResult(info);
            }
        }
    }
}

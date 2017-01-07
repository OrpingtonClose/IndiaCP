using IdentityModel.Client;
using IndiaCPAngular1.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
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

        [HttpPost(Name ="Post")]
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
                nodeInfo.NodeType = Environment.GetEnvironmentVariable("NODETYPE");
                nodeInfo.Host = Environment.GetEnvironmentVariable("HOST");
                nodeInfo.Port = Int32.Parse(Environment.GetEnvironmentVariable("PORT"));
                _logger.LogInformation(tokenResponse.Json.ToString());
                return new OkObjectResult(nodeInfo);
            }
        }
    }
}

using IdentityModel.Client;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;

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
        public HttpResponseMessage Post([FromBody] UserCredentials credentials)
        {
            var disco = DiscoveryClient.GetAsync("http://indiacpidentityserver.azurewebsites.net").GetAwaiter().GetResult();
            var tokenClient = new TokenClient(disco.TokenEndpoint, "ro.client", "secret");
            var tokenResponse = tokenClient.RequestResourceOwnerPasswordAsync(credentials.Username, credentials.Password, "api1").GetAwaiter().GetResult();

            if (tokenResponse.IsError)
            {
                var resp = new HttpResponseMessage();
                resp.StatusCode = System.Net.HttpStatusCode.Unauthorized;
                _logger.LogError(tokenResponse.Error + " " + tokenResponse.ErrorDescription);
                return resp;
            }
            else
            {
                var resp = new HttpResponseMessage();
                resp.StatusCode = System.Net.HttpStatusCode.OK;
                _logger.LogInformation(tokenResponse.Json.ToString());
                return resp;
            }
        }
    }

    public class UserCredentials
    {
        public String Username { get; set; }
        public String Password { get; set; }
    }
}

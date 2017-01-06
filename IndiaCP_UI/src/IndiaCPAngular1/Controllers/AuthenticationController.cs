using IdentityModel.Client;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
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
        public void Post([FromBody] UserCredentials credentials)
        {
            var disco = DiscoveryClient.GetAsync("http://localhost:30114").GetAwaiter().GetResult();
            var tokenClient = new TokenClient(disco.TokenEndpoint, "ro.client", "secret");
            var tokenResponse = tokenClient.RequestResourceOwnerPasswordAsync("issuer1", "password", "api1").GetAwaiter().GetResult();

            if (tokenResponse.IsError)
            {
                _logger.LogError(tokenResponse.Error + " " + tokenResponse.ErrorDescription);
            }
            else
            {
                _logger.LogInformation(tokenResponse.Json.ToString());
            }
        }
    }

    public class UserCredentials
    {
        public String Username { get; set; }
        public String Password { get; set; }
    }
}

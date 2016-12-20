using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.AspNetCore.Identity;
using AspNetCore.Identity.MongoDB;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using System.IO;
using Microsoft.AspNetCore.DataProtection;
using System.Security.Claims;
using FinWizUI.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection.Extensions;
using System.Reflection;

namespace FinWizUI
{
    public class MongoDbSettings
    {
        public string ConnectionString { get; set; }
        public string DatabaseName { get; set; }
    }

    public class Startup
    {
        private readonly IHostingEnvironment _env;

        public Startup(IHostingEnvironment env)
        {
            var builder = new ConfigurationBuilder()
                .SetBasePath(env.ContentRootPath)
                .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
                .AddJsonFile($"appsettings.{env.EnvironmentName}.json", optional: true)
                .AddEnvironmentVariables();
            Configuration = builder.Build();

            _env = env;
        }

        public IConfigurationRoot Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            // Add framework services.
            services.Configure<MongoDbSettings>(Configuration.GetSection("MongoDb"));
            services.AddSingleton<IUserStore<MongoIdentityUser>>(provider =>
            {
                var options = provider.GetService<IOptions<MongoDbSettings>>();
                var client = new MongoClient(options.Value.ConnectionString);
                var database = client.GetDatabase(options.Value.DatabaseName);
                var loggerFactory = provider.GetService<ILoggerFactory>();

                return new MongoUserStore<MongoIdentityUser>(database, loggerFactory);
            });

            services.Configure<IdentityOptions>(options =>
            {
                var dataProtectionPath = Path.Combine(_env.WebRootPath, "identity-artifacts");
                options.Cookies.ApplicationCookie.AuthenticationScheme = "ApplicationCookie";
                options.Cookies.ApplicationCookie.DataProtectionProvider = DataProtectionProvider.Create(dataProtectionPath);
                options.Lockout.AllowedForNewUsers = true;
            });

            // Services used by identity
            services.AddAuthentication(options =>
            {
                // This is the Default value for ExternalCookieAuthenticationScheme
                options.SignInScheme = new IdentityCookieOptions().ExternalCookieAuthenticationScheme;
            });

            // Hosting doesn't add IHttpContextAccessor by default
            services.TryAddSingleton<IHttpContextAccessor, HttpContextAccessor>();

            services.AddOptions();
            services.AddDataProtection();

            services.TryAddSingleton<IdentityMarkerService>();
            services.TryAddSingleton<IUserValidator<MongoIdentityUser>, UserValidator<MongoIdentityUser>>();
            services.TryAddSingleton<IPasswordValidator<MongoIdentityUser>, PasswordValidator<MongoIdentityUser>>();
            services.TryAddSingleton<IPasswordHasher<MongoIdentityUser>, PasswordHasher<MongoIdentityUser>>();
            services.TryAddSingleton<ILookupNormalizer, UpperInvariantLookupNormalizer>();
            services.TryAddSingleton<IdentityErrorDescriber>();
            services.TryAddSingleton<ISecurityStampValidator, SecurityStampValidator<MongoIdentityUser>>();
            services.TryAddSingleton<IUserClaimsPrincipalFactory<MongoIdentityUser>, UserClaimsPrincipalFactory<MongoIdentityUser>>();
            services.TryAddSingleton<UserManager<MongoIdentityUser>, UserManager<MongoIdentityUser>>();
            services.TryAddScoped<SignInManager<MongoIdentityUser>, SignInManager<MongoIdentityUser>>();

            AddDefaultTokenProviders(services);

            services.AddMvc();

            // Add application services.
            services.AddTransient<IEmailSender, AuthMessageSender>();
            services.AddTransient<ISmsSender, AuthMessageSender>();

            //Adding Swagger Gen
            // Inject an implementation of ISwaggerProvider with defaulted settings applied
            services.AddSwaggerGen();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, ILoggerFactory loggerFactory)
        {




            loggerFactory.AddConsole(Configuration.GetSection("Logging"));
            loggerFactory.AddDebug();

            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseBrowserLink();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
            }

            app.UseStaticFiles();
            app.UseIdentity();

            app.UseMvc(routes =>
            {
                routes.MapRoute(
                    name: "default",
                    template: "{controller=Home}/{action=Index}/{id?}");
            });

            
            // Enable middleware to serve generated Swagger as a JSON endpoint
            app.UseSwagger();

            // Enable middleware to serve swagger-ui assets (HTML, JS, CSS etc.)
            app.UseSwaggerUi();
        }


        private void AddDefaultTokenProviders(IServiceCollection services)
        {
            var dataProtectionProviderType = typeof(DataProtectorTokenProvider<>).MakeGenericType(typeof(MongoIdentityUser));
            var phoneNumberProviderType = typeof(PhoneNumberTokenProvider<>).MakeGenericType(typeof(MongoIdentityUser));
            var emailTokenProviderType = typeof(EmailTokenProvider<>).MakeGenericType(typeof(MongoIdentityUser));
            AddTokenProvider(services, TokenOptions.DefaultProvider, dataProtectionProviderType);
            AddTokenProvider(services, TokenOptions.DefaultEmailProvider, emailTokenProviderType);
            AddTokenProvider(services, TokenOptions.DefaultPhoneProvider, phoneNumberProviderType);
        }

        private void AddTokenProvider(IServiceCollection services, string providerName, Type provider)
        {
            services.Configure<IdentityOptions>(options =>
            {
                options.Tokens.ProviderMap[providerName] = new TokenProviderDescriptor(provider);
            });

            services.AddSingleton(provider);
        }

        public class UserClaimsPrincipalFactory<TUser> : Microsoft.AspNetCore.Identity.IUserClaimsPrincipalFactory<TUser>
            where TUser : class
        {
            public UserClaimsPrincipalFactory(
                UserManager<TUser> userManager,
                IOptions<IdentityOptions> optionsAccessor)
            {
                if (userManager == null)
                {
                    throw new ArgumentNullException(nameof(userManager));
                }
                if (optionsAccessor == null || optionsAccessor.Value == null)
                {
                    throw new ArgumentNullException(nameof(optionsAccessor));
                }

                UserManager = userManager;
                Options = optionsAccessor.Value;
            }

            public UserManager<TUser> UserManager { get; private set; }

            public IdentityOptions Options { get; private set; }

            public virtual async Task<ClaimsPrincipal> CreateAsync(TUser user)
            {
                if (user == null)
                {
                    throw new ArgumentNullException(nameof(user));
                }

                var userId = await UserManager.GetUserIdAsync(user);
                var userName = await UserManager.GetUserNameAsync(user);
                var id = new ClaimsIdentity(Options.Cookies.ApplicationCookieAuthenticationScheme,
                    Options.ClaimsIdentity.UserNameClaimType,
                    Options.ClaimsIdentity.RoleClaimType);
                id.AddClaim(new Claim(Options.ClaimsIdentity.UserIdClaimType, userId));
                id.AddClaim(new Claim(Options.ClaimsIdentity.UserNameClaimType, userName));
                if (UserManager.SupportsUserSecurityStamp)
                {
                    id.AddClaim(new Claim(Options.ClaimsIdentity.SecurityStampClaimType,
                        await UserManager.GetSecurityStampAsync(user)));
                }
                if (UserManager.SupportsUserRole)
                {
                    var roles = await UserManager.GetRolesAsync(user);
                    foreach (var roleName in roles)
                    {
                        id.AddClaim(new Claim(Options.ClaimsIdentity.RoleClaimType, roleName));
                    }
                }
                if (UserManager.SupportsUserClaim)
                {
                    id.AddClaims(await UserManager.GetClaimsAsync(user));
                }

                return new ClaimsPrincipal(id);
            }
        }

    }
}

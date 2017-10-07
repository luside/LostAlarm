using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.Entity;
using System.Web.Http;
using Microsoft.Azure.Mobile.Server;
using Microsoft.Azure.Mobile.Server.Authentication;
using Microsoft.Azure.Mobile.Server.Config;
using LostAlarmService.DataObjects;
using LostAlarmService.Models;
using Owin;

namespace LostAlarmService
{
    public partial class Startup
    {
        public static void ConfigureMobileApp(IAppBuilder app)
        {
            HttpConfiguration config = new HttpConfiguration();

            //For more information on Web API tracing, see http://go.microsoft.com/fwlink/?LinkId=620686 
            config.EnableSystemDiagnosticsTracing();

            new MobileAppConfiguration()
                .UseDefaultConfiguration()
                .ApplyTo(config);

            // Use Entity Framework Code First to create database tables based on your DbContext
            Database.SetInitializer(new LostAlarmInitializer());

            // To prevent Entity Framework from modifying your database schema, use a null database initializer
            // Database.SetInitializer<LostAlarmContext>(null);

            MobileAppSettingsDictionary settings = config.GetMobileAppSettingsProvider().GetMobileAppSettings();

            if (string.IsNullOrEmpty(settings.HostName))
            {
                // This middleware is intended to be used locally for debugging. By default, HostName will
                // only have a value when running in an App Service application.
                app.UseAppServiceAuthentication(new AppServiceAuthenticationOptions
                {
                    SigningKey = ConfigurationManager.AppSettings["SigningKey"],
                    ValidAudiences = new[] { ConfigurationManager.AppSettings["ValidAudience"] },
                    ValidIssuers = new[] { ConfigurationManager.AppSettings["ValidIssuer"] },
                    TokenHandler = config.GetAppServiceTokenHandler()
                });
            }
            app.UseWebApi(config);
        }
    }

    public class LostAlarmInitializer : CreateDatabaseIfNotExists<LostAlarmContext>
    {
        protected override void Seed(LostAlarmContext context)
        {
            List<CoordinatesRSSI> coordinatesRSSIItems = new List<CoordinatesRSSI>
            {
                new CoordinatesRSSI { Id = Guid.NewGuid().ToString(), FingerPrintId = 0, XCoordinates = 0, YCoordinates = 0, HashString = "{AAA_qq=-18}", Complete = false },
            };

            foreach (CoordinatesRSSI coordinatesRSSIItem in coordinatesRSSIItems)
            {
                context.Set<CoordinatesRSSI>().Add(coordinatesRSSIItem);
            }

            base.Seed(context);
        }
    }
}


using Microsoft.Owin;
using Owin;

[assembly: OwinStartup(typeof(LostAlarmService.Startup))]

namespace LostAlarmService
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureMobileApp(app);
        }
    }
}
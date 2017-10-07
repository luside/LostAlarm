using System.Linq;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Controllers;
using System.Web.Http.OData;
using Microsoft.Azure.Mobile.Server;
using LostAlarmService.DataObjects;
using LostAlarmService.Models;

namespace LostAlarmService.Controllers
{
    public class TodoItemController : TableController<CoordinatesRSSI>
    {
        protected override void Initialize(HttpControllerContext controllerContext)
        {
            base.Initialize(controllerContext);
            LostAlarmContext context = new LostAlarmContext();
            DomainManager = new EntityDomainManager<CoordinatesRSSI>(context, Request);
        }

        // GET tables/TodoItem
        public IQueryable<CoordinatesRSSI> GetAllTodoItems()
        {
            return Query();
        }

        // GET tables/TodoItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public SingleResult<CoordinatesRSSI> GetTodoItem(string id)
        {
            return Lookup(id);
        }

        // PATCH tables/TodoItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task<CoordinatesRSSI> PatchTodoItem(string id, Delta<CoordinatesRSSI> patch)
        {
            return UpdateAsync(id, patch);
        }

        // POST tables/TodoItem
        public async Task<IHttpActionResult> PostTodoItem(CoordinatesRSSI item)
        {
            CoordinatesRSSI current = await InsertAsync(item);
            return CreatedAtRoute("Tables", new { id = current.Id }, current);
        }

        // DELETE tables/TodoItem/48D68C86-6EA6-4C25-AA33-223FC9A27959
        public Task DeleteTodoItem(string id)
        {
            return DeleteAsync(id);
        }
    }
}
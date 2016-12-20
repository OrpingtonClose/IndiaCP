using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace FinWizUI.Controllers
{
    public class HomeController : Controller
    {
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

        public IActionResult Index()
        {
            return View();
        }

        
        public IEnumerable<String> GetAll()
        {
            return new List<String>();
        }

        public IActionResult About()
        {
            ViewData["Message"] = "Your application description page.";

            return View();
        }

        public IActionResult Contact()
        {
            ViewData["Message"] = "Your contact page.";

            return View();
        }

        public IActionResult Error()
        {
            return View();
        }
    }
}

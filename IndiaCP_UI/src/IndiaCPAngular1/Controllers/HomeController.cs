﻿using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit http://go.microsoft.com/fwlink/?LinkID=397860

namespace IndiaCPAngular1.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
            return View("index");
        }
    }
}

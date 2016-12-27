module app.main {
    "use strict";

    interface IMainScope {
    }

    class MainController implements IMainScope {
        constructor() {
        }
    }

    angular
        .module("app.main")
        .controller("app.main.MainController",
        MainController);
} 
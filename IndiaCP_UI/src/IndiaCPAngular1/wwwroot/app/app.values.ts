
interface ICurrentUser {
    userId: string;
    password: string;
}

((): void => {
    "use strict";
    var currentUser: ICurrentUser = {
        userId: "",
        password: ""

    };

    angular
        .module("app")
        .value("currentUser", currentUser);
})();
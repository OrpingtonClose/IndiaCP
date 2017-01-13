(function (): void {
    "use strict";
    angular
        .module("app.dashboard")
        .config(config);

    config.$inject = ["$stateProvider", "$urlRouterProvider"];
    function config($stateProvider: ng.ui.IStateProvider, $urlRouterProvider: ng.ui.IUrlRouterProvider): void {
        $stateProvider
            .state("main.dashboard", {
                cache: false,
                url: "dashboard",
                component: "app.dashboard.DashboardComponent",
                resolve: { isLegalEntitydocValid: DashboardControllerResolve }
                // templateUrl: "app/dashboard/dashboard.html",
                // controller: "app.dashboard.DashboardController",
                // controllerAs: "vm"
            })
            .state("index.playgames", {
                url: "playgames",
                templateUrl: "/templates/admin/templates/playgames.html",
                params: {
                    seldate: null,
                },
                resolve: {
                    games: function ($http, $stateParams) {
                        return $http.post("/api/appmain/GetGamesForDate", $stateParams.seldate).then(function (response) {
                            return response.data;
                        });
                    }
                },
                controller: function ($scope, games) {
                    $scope.gridOptions.data = games;
                }
            });
    }

    // https://github.com/angular-ui/ui-router/issues/1737
    // https://ui-router.github.io/guide/ng1/migrate-to-1_0
    // https://toddmotto.com/resolve-promises-in-angular-routes/ 
    DashboardControllerResolve.$inject = ["$timeout", "$state", "$q", "app.services.IssuerService"];
    function DashboardControllerResolve($timeout: ng.ITimeoutService, $state: ng.ui.IStateService,
        $q: ng.IQService,
        issuerService: app.services.IIssuerService): ng.IPromise<any> {
        var brPromise: ng.IPromise<any> = issuerService.fetchBoardResolution().then((response: any) => {
            if (response.data === "No Board Resolution Documents Uploaded") {
                return $q.reject("No Board Resolution Documents Uploaded.");
            } else {
                return $q.resolve();
            }
        });
        var crPromise: ng.IPromise<any> = issuerService.fetchCreditRating().then((response: any) => {
            if (response.data === "No Credit Rating Documents Uploaded") {
                return $q.reject("No Credit Rating Documents Uploaded.");
            } else {
                return $q.resolve();
            }

        });
        return $q.all([brPromise, crPromise]).then(() => { return true; }, () => {
            $timeout(function () { $state.go("main.uploaddocs"); });
        });
    }
})();
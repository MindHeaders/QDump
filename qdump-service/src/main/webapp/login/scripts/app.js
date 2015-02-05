module = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ngAnimate',
    'qdumpApp.services', 'qdumpApp.controllers', 'qdumpApp.directives']).
    config(function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $routeProvider.
            when('/user-list', {
                templateUrl: 'login/user-list.html',
                controller: 'UserListCtrl'
            }).
            when('/auth', {
                templateUrl: 'login/auth.html',
                controller: 'AuthCtrl'
            }).
            when('/reg', {
                templateUrl: 'login/registration.html',
                controller: 'RegCtrl'
            }).
            when('/welcome', {
                templateUrl: 'login/welcome.html'
            }).
            when('/account', {
                templateUrl: 'login/account.html',
                controller: 'AccountCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    });


angular.module('qdumpApp', ['ngRoute', 'qdumpApp.services', 'qdumpApp.controllers']).
config(function($routeProvider, $locationProvider) {
    /*$locationProvider.html5Mode(true)*/
    $routeProvider.
    when('/user-list', {
        templateUrl: 'user-list.html',
        controller: 'UserListCtrl'
    });
    $routeProvider.
    when('/auth', {
        templateUrl: 'auth.html',
        controller: 'AuthCtrl'
    });
    $routeProvider.
    when('/reg', {
        templateUrl: 'reg.html',
        controller: 'RegCtrl'
    });
    $routeProvider.
    when('/welcome', {
        templateUrl: 'welcome.html',
        controller: 'AuthCtrl'
    });
});

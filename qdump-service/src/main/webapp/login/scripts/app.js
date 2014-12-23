angular.module('qdumpApp', ['ngRoute', 'qdumpApp.services', 'qdumpApp.controllers']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/user-list', {templateUrl: 'user-list.html', controller: 'UserListCtrl'});
    $routeProvider.
    	when('/auth', {templateUrl: 'auth.html', controller: 'AuthCtrl'});
    $routeProvider.
    	when('/reg', {templateUrl: 'reg.html', controller: 'RegCtrl'})
 }]);
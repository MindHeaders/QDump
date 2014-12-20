'use strict';

var services = angular.module('qdumpApp.services', ['ngResource']);

// var baseUrl = 'http://localhost\\:3330';

services.factory('AuthFactory', function($resource) {
	return $resource('/rest/person/login', {}, {
		auth: {
			method: 'POST',
			method: 'GET',
			isArray: false,
			headers: {'Content-Type':'application/x-www-form-urlencoded'}
		}
	})
});

services.factory('RegFactory', function($resource) {
	return $resource('/rest/person/registration', {}, {
		reg: {
			method: 'POST'
		}
	})
});

services.factory('UsersFactory', function($resource) {
	return $resource('/rest/person/getAll', {}, {
		query: {
			method: 'GET',
			isArray: true
		}
	})
});
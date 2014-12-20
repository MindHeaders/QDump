var services = angular.module('qdumpApp.services', ['ngResource']);

services.factory('AuthFactory', function($resource) {
	return $resource('/rest/person/login', {}, {
		auth: {
			method: 'POST',
			isArray: false,
			headers: {'Content-Type':'application/x-www-form-urlencoded'}
		},
		log: {
			method: 'GET'
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
	return $resource('/rest/person/getAllMin', {}, {
		query: {
			method: 'GET',
			isArray: true
		}
	})
});
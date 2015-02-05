var services = angular.module('qdumpApp.services', ['ngResource']);

services.factory('AuthFactory', function($resource) {
    return $resource('/rest/persons/auth', {}, {
        auth: {
            method: 'POST',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }
    });
});
services.factory('LogoutFactory', function($resource){
   return $resource('/rest/persons/logout',{},{
       logout: {
           method: 'GET'
       }
   })
});

services.factory('RegFactory', function($resource) {
    return $resource('/rest/persons/registration', {}, {
        reg: {
            method: 'POST'
        }
    })
});

services.factory('UsersFactory', function($resource) {
    return $resource('/rest/persons/get/min', {}, {
        query: {
            method: 'GET',
            isArray: true
        }
    })
});
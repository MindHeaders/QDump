/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('account.services', ['ngResource']);

services.factory('PersonFactory', function($resource) {
    return $resource('/rest/persons/:authentication/:logout/:statistic/:verify/:count/:personal/:id',
        {
            authentication: '@authentication',
            logout: '@logout',
            statistic: '@statistic',
            verify: '@verify',
            count: '@count',
            personal: '@personal',
            id: '@id'
        },
        {
            authentication: {
                method: 'POST',
                isArray: false,
                params: {
                    authentication: 'authentication'
                },
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            },
            logout: {
                method: 'GET',
                params: {
                    logout: 'logout'
                }
            }
        })
});
services.factory('ResetPassword', function($resource) {
    return $resource('/rest/person/reset/password', {}, {
        resetPassword : {
            method: 'PUT'
        }
    })
});
services.factory('Registration', function($resource) {
    return $resource('/rest/persons/registration', {}, {
        registration: {
            method: 'POST'
        }
    })
});
services.factory('UpdateUserGroup', function($resource) {
    return $resource('/rest/persons/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
});
services.factory('UpdateUserData', function($resource) {
    return $resource('/rest/persons', {}, {
        update: {
            method: 'PUT'
        }
    })
});
services.factory('Authorization', function($resource, $cookieStore, $q) {
    var isAuth = null;
    return {
        isAuth: function() {
            return $resource('/rest/persons/authorized').get();
        },
        resolve: function() {
            var deferred = $q.defer();
            $resource('/rest/persons/authorized').get(
                function(data) {
                    deferred.resolve(data);
                }, function(data) {
                    deferred.reject(data);
                }
            );
            return deferred.promise;
        },
        setIsAuth: function(isAuth) {
            this.isAuth = isAuth;
            $cookieStore.put('isAuth', isAuth);
        },
        getIsAuth: function() {
            return isAuth;
        },
        logout: function() {
            isAuth = null;
            $cookieStore.remove('isAuth');
        }
    }
});
services.factory('Permission', function($resource, $q) {
    return {
        check_permission: function(userRole) {
            return $resource('/rest/persons/check/permission/:role', {role: userRole}).get();
        },
        resolve: function(userRole) {
            var deferred = $q.defer();
            $resource('/rest/persons/check/permission/:role', {role: userRole}).get(
                function(data) {
                    deferred.resolve(data);
                }, function(data) {
                    deferred.reject(data);
                }
            );
            return deferred.promise;
        }
    }
});
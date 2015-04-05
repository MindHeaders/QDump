/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('account.services', ['ngResource']);

services.factory('PersonFactory', function($resource) {
    return $resource('/rest/persons/:count/:authentication/:delete/:logout/:verify/:personal/:update/:reset/:password/:questionnaires/:pagination/:create/:get/:admin/:id',
        {
            count: '@count',
            authentication: '@authentication',
            delete: '@delete',
            logout: '@logout',
            verify: '@verify',
            personal: '@personal',
            reset: '@reset',
            password: '@password',
            questionnaires: '@questionnaires',
            pagination: '@pagination',
            create: '@create',
            get: '@get',
            admin: '@admin',
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
            },
            verify: {
                method: 'GET',
                params: {
                    verify: 'verify'
                }
            },
            page: {
                method: 'GET',
                params: {
                    personal: 'personal'
                }
            },
            resetPassword: {
                method: 'PUT',
                params: {
                    reset: 'reset',
                    password: 'password'
                }
            },
            completed: {
                method: 'GET',
                params: {
                    pagination: 'pagination',
                    questionnaires: 'questionnaires'
                },
                isArray: true
            },
            create: {
                method: 'POST',
                params: {
                    questionnaires: 'questionnaires',
                    create: 'create'
                }
            },
            get_questionnaire: {
                method: 'GET',
                params: {
                    questionnaires: 'questionnaires',
                    get: 'get'
                }
            },
            get_all_questionnaires: {
                method: 'GET',
                params: {
                    get: 'get',
                    admin: 'admin'
                },
                isArray: true
            },
            count: {
                method: 'GET',
                params: {
                    count: 'count'
                }
            },
            get_for_update: {
                method: 'GET',
                params: {
                    personal: 'personal'
                }
            }
        })
});
services.factory('ResetPasswordFactory', function($resource) {
    return $resource('/rest/person/reset/password', {}, {
        resetPassword : {
            method: 'PUT'
        }
    })
});
services.factory('RegistrationFactory', function($resource) {
    return $resource('/rest/persons/registration', {}, {
        registration: {
            method: 'POST'
        }
    })
});
services.factory('UpdateUserGroupFactory', function($resource) {
    return $resource('/rest/persons/update/:id', {id: '@id'}, {
        update: {
            method: 'PUT'
        }
    });
});
services.factory('UpdateUserDataFactory', function($resource) {
    return $resource('/rest/persons/update', {}, {
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
services.factory('PersonQuestionFactory', function($resource) {
    return $resource('/rest/persons/questions/:delete/:get/:count/:checking/:id',
        {
            delete: '@delete',
            get: '@get',
            count: '@count',
            checking: '@checking',
            id: '@id'

        }, {
            get_not_checked: {
                method: 'GET',
                params: {
                    get: 'get',
                    checking: 'checking'
                },
                isArray: true
            }
        })
});
/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('account.services', ['ngResource']);

services.factory('PersonFactory', function($resource) {
    return $resource('/rest/persons/:auth/:logout/:verify/:personal/:update/:reset/:password/:questionnaires/:pagination/:create/:get/:id',
        {
            auth: '@auth',
            logout: '@logout',
            verify: '@verify',
            personal: '@personal',
            update: '@update',
            reset: '@reset',
            password: '@password',
            questionnaires: '@questionnaires',
            pagination: '@pagination',
            create: '@create',
            get: '@get',
            id: '@id'
        },
        {
            auth: {
                method: 'POST',
                isArray: false,
                params: {
                    auth: 'auth'
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
            update: {
                method: 'PUT',
                isArray: false,
                params: {
                    update: 'update'
                },
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
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
services.factory('Authorization', function($resource, $cookieStore) {
    var user = null;
    return {
        getUser: function() {
            if(user == null) {
                this.setUser();
            }
            return user;
        },
        setUser: function() {
            if($cookieStore.get('user') == null) {
                user = $resource('/rest/persons/authorized').get();
                $cookieStore.put('user', user);
            } else {
                user = $cookieStore.get('user');
            }
        },
        logout: function() {
            user = null;
            $cookieStore.remove('user');
        }
    }
});
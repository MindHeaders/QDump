var services = angular.module('qdumpApp.services', ['ngResource']);

services.factory('PersonFactory', function($resource) {
    return $resource('/rest/persons/:authentication/:logout/:registration/:verify/:personal/:update/:reset/:password/:questionnaires/:pagination/:create',
        {
            auth: '@authentication',
            logout: '@logout',
            registration: '@registration',
            verify: '@verify',
            personal: '@personal',
            update: '@update',
            reset: '@reset',
            password: '@password',
            questionnaires: '@questionnaires',
            pagination: '@pagination',
            create: '@create'
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
            }
        })
});

services.factory('AuthFactory', function($resource) {
    return $resource('/rest/persons/authentication', {}, {
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

services.factory('RegistrationFactory', function($resource) {
    return $resource('/rest/persons/registration', {}, {
        registration: {
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
services.factory('VerificationFactory', function($resource) {
    return $resource('/rest/persons/verify', {}, {
        verify : {
            method: 'GET'
        }
    })
});
services.factory('AccountFactory', function($resource) {
    return $resource('/rest/persons/personal', {}, {
        page : {
            method: 'GET'
        }
    })
});
services.factory('UpdateFactory', function($resource) {
    return $resource('/rest/persons/update', {}, {
        update : {
            method: 'PUT',
            isArray: false,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
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
services.factory('CompletedFactory', function($resource) {
    return $resource('/rest/persons/questionnaires/pagination',
        {page: '@page', size: '@size', direction: '@direction', sort: '@sort'})
});
services.factory('CreateQuestionnaireFactory', function($resource) {
    return $resource('/rest/questionnaires/create');
});
services.factory('QuestionnaireFactory', function($resource) {
    return $resource('/rest/questionnaires/:pagination/:get/:id', {
            pagination: '@pagination',
            get: '@get',
            id: '@id'
        },
        {
            query: {
                method: 'GET',
                params: {
                    pagination: 'pagination'
                },
                isArray: true
            },
            get: {
                method: 'GET',
                params: {
                    get: 'get'
                }
            }
        }
    )
});
services.factory('PersonalQuestionnaireFactory', function() {
    var questionnaire = {
        id: ''
    };
    return {
        getQuestionnaireId: function() {
            return questionnaire.id;
        },
        setQuestionnaireId: function(questionnaireId) {
            questionnaire.id = questionnaireId;
        }
    }
});
services.factory('Role', function($http) {
    var role = {
        getRole: function() {
            var promise = $http({
                method: 'GET',
                url: '/rest/persons/check/permission/admin'
            });
            promise.success(function(data, status, headers, conf) {
                return data;
            });
            promise.error(function(data) {
                return data;
            });
            return promise
        }
    };
    return role;
});
services.factory('ErrorFactory', function() {
    var errorMessage = null;
    return {
        getErrorMessage: function() {
            return errorMessage;
        },
        setErrorMessage: function(msg) {
            this.errorMessage = msg;
        }
    }
});
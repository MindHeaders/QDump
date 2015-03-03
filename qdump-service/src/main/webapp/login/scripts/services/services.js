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
   return $resource('/rest/persons/questionnaires/pagination?page=:page&size=:size&direction=:direction&sort=:sort',
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
                method: 'GET'
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
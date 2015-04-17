/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('questionnaire.services', ['ngResource']);

services.factory('PersonQuestionnairesFactory', function($resource) {
    return $resource('/rest/persons/questionnaires/:statistic/:completed/:started/:checking/:count/:type/:id',
        {
            statistic: '@statistic',
            completed: '@completed',
            started: '@started',
            checking: '@checking',
            count: '@count',
            type: '@type',
            id: '@id'
        }, {
            getCompleted: {
                method: 'GET',
                params: {
                    completed: 'completed'
                },
                isArray: true
            },
            getChecking: {
                method: 'GET',
                params: {
                    completed: 'checking'
                },
                isArray: true
            },
            getStarted: {
                method: 'GET',
                params: {
                    completed: 'started'
                },
                isArray: true
            }
        })
});
services.factory('UpdatePersonQuestionnaire', function($resource) {
    return $resource('/rest/persons/questionnaires', {}, {
        update: {
            method: 'PUT'
        }
    })
});
services.factory('QuestionnaireFactory', function($resource) {
    return $resource('/rest/questionnaires/:statistic/:published/:personal/:count/:id', {
            statistic: '@statistic',
            published: '@published',
            personal: '@personal',
            count: '@count',
            id: '@id'
        },
        {
            getPublished: {
                method: 'GET',
                params: {
                    published: 'published'
                },
                isArray: true
            }
        }
    )
});
services.factory('UpdateQuestionnaire', function($resource) {
    return $resource('/rest/questionnaires', {}, {update: {method: 'PUT'}});
});
services.factory('QuestionFactory', function($resource) {
    return $resource('/rest/questions/:id', {
        id: '@id'
    })
});
services.factory('AnswerFactory', function($resource) {
   return $resource('/rest/answers/:id', {
       id: '@id'
   })
});
services.factory('PersonQuestionFactory', function($resource) {
    return $resource('/rest/persons/questions/:checking/:count/:id',
        {
            count: '@count',
            checking: '@checking',
            id: '@id'

        }, {
            getNotChecked: {
                method: 'GET',
                params: {
                    checking: 'checking'
                },
                isArray: true
            }
        })
});
services.factory('PersonAnswerFactory', function($resource) {
   return $resource('/rest/persons/answers/:id', {
       id: '@id'
   })
});
services.factory('PersonalQuestionnaire', function($cookieStore) {
    return {
        getQuestionnaireId: function() {
            return typeof(Storage) != 'undefined' ?
                Number(sessionStorage.getItem('questionnaireId')) :
                $cookieStore.get('questionnaireId');
        },
        setQuestionnaireId: function(id) {
            typeof(Storage) != 'undefined' ?
                sessionStorage.setItem('questionnaireId', id) :
                $cookieStore.put('questionnaireId', id);
        },
        getPersonQuestionnaireId: function() {
            return typeof(Storage) != 'undefined' ?
                Number(sessionStorage.getItem('personQuestionnaireId')) :
                $cookieStore.get('personQuestionnaireId');
        },
        setPersonQuestionnaireId: function(id) {
            typeof(Storage) != 'undefined' ?
                sessionStorage.setItem('personQuestionnaireId', id) :
                $cookieStore.put('personQuestionnaireId', id);
        }
    }
});
services.factory('Questionnaires', function() {
    var questionnaireId = null;
    return {
        getQuestionnaireId: function() {
            return questionnaireId;
        },
        setQuestionnaireId: function(id) {
            questionnaireId = id;
        }
    };
});
services.factory('Questionnaire', function($resource, $cookieStore, ErrorFactory, $location) {
    var questionnaireId = null;
    var questionnaire = {
        getPersonQuestionnaire: function() {
            if(questionnaireId == null) {
                ErrorFactory.setErrorMessage("Person questionnaire id is null");
                $location.path('/error');
            }
            var promise = $resource('/rest/persons/questionnaires/completed/:id', {id: questionnaireId}).get();
            return promise
        },
        setQuestionnaireId: function(id) {
            questionnaireId = id;
        }
    };
    return questionnaire;
});
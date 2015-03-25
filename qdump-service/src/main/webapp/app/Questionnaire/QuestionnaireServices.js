/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('questionnaire.services', ['ngResource']);

services.factory('PersonQuestionnairesFactory', function($resource) {
    return $resource('/rest/persons/questionnaires/:completed/:started/:count/:type',
        {completed: '@completed', started: '@started', count: '@count', type: '@type'},
        {
            completed: {
                method: 'GET',
                params: {
                    completed: 'completed'
                },
                isArray: true
            },
            started: {
                method: 'GET',
                params: {
                    started: 'started'
                },
                isArray: true
            },
            count_completed: {
                method: 'GET',
                params: {
                    count: 'count',
                    type: 'completed'
                }
            },
            count_started: {
                method: 'GET',
                params: {
                    count: 'count',
                    type: 'started'
                }
            }
        })
});
services.factory('CreateQuestionnaireFactory', function($resource) {
    return $resource('/rest/questionnaires/create', {}, {
        save: {
            method: 'POST'
        }
    });
});
services.factory('QuestionnaireFactory', function($resource) {
    return $resource('/rest/questionnaires/:get/:published/:delete/:personal/:id/:all/:count', {
            published: '@published',
            get: '@get',
            id: '@id',
            count: '@count',
            all: '@all',
            delete: '@delete',
            personal: '@personal'
        },
        {
            get_published: {
                method: 'GET',
                params: {
                    get: 'get',
                    published: 'published'
                },
                isArray: true
            },
            get_one: {
                method: 'GET',
                params: {
                    get: 'get'
                },
                isArray: false
            },
            count_published: {
                method: 'GET',
                params: {
                    get: 'get',
                    published: 'published',
                    count: 'count'
                }
            },
            get_all: {
                method: 'GET',
                params: {
                    get: 'get',
                    all: 'all'
                },
                isArray: true
            },
            count_all: {
                method: 'GET',
                params: {
                    get: 'get',
                    all: 'all',
                    count: 'count'
                }
            },
            delete_one: {
                method: 'DELETE',
                params: {
                    delete: 'delete'
                }
            }
        }
    )
});
services.factory('PersonalQuestionnaireFactory', function() {
    var questionnaireId = null;
    var personQuestionnaireId = null;
    return {
        getQuestionnaireId: function() {
            return questionnaireId;
        },
        setQuestionnaireId: function(id) {
            questionnaireId = id;
        },
        getPersonQuestionnaireId: function() {
            return personQuestionnaireId;
        },
        setPersonQuestionnaireId: function(id) {
            personQuestionnaireId = id;
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
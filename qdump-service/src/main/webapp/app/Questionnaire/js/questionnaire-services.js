/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('questionnaire.services', ['ngResource']);

services.factory('PersonQuestionnairesFactory', ["$resource", function($resource) {
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
}]);
services.factory('UpdatePersonQuestionnaire', ["$resource", function($resource) {
    return $resource('/rest/persons/questionnaires', {}, {
        update: {
            method: 'PUT'
        }
    })
}]);
services.factory('QuestionnaireFactory', ["$resource", function($resource) {
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
}]);
services.factory('UpdateQuestionnaire', ["$resource", function($resource) {
    return $resource('/rest/questionnaires', {}, {update: {method: 'PUT'}});
}]);
services.factory('QuestionFactory', ["$resource", function($resource) {
    return $resource('/rest/questions/:id', {
        id: '@id'
    })
}]);
services.factory('AnswerFactory', ["$resource", function($resource) {
    return $resource('/rest/answers/:id', {
        id: '@id'
    })
}]);
services.factory('PersonQuestionFactory', ["$resource", function($resource) {
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
}]);
services.factory('PersonAnswerFactory', ["$resource", function($resource) {
    return $resource('/rest/persons/answers/:id', {
        id: '@id'
    })
}]);
services.factory('PersonalQuestionnaire', ["$cookieStore", function($cookieStore) {
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
}]);
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
services.factory('Questionnaire', ["$resource", "$cookieStore", "ErrorFactory", "$location", function($resource, $cookieStore, ErrorFactory, $location) {
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
}]);
services.factory('Twitter', ["$resource", "$http", "$base64", function($resource, $http, $base64) {
    var consumerKey = encodeURIComponent('U35e0lcpdQ3YKUREqm1uLOSr8');
    var consumerSecret = encodeURIComponent('uIkQImB660qSMdURHEm5336rSIxh3dDXfpqJ5Q3Dx90nCMtynx');
    var credentials = $base64.encode(consumerKey + ':' + consumerSecret);
    var twitterOauthEndpoint = $http.post(
        'https://api.twitter.com/oauth2/token',
        "grant_type=client_credentials",
        {headers: {
            'Authorization': 'Basic ' + credentials,
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'}}

    );
    twitterOauthEndpoint.success(function(response) {
        services.$httpProvider.defaults.headers.common['Authorization'] = "Bearer " + response.access_token;
    }).error(function(data) {
        console.log(data);
    });
    return $resource('https://api.twitter.com/1.1/search/:action',
        {
            action: 'tweets.json',
            q: '#RazborPoletov',
            result_type: 'recent',
            count: 6
        }
    );
}]);
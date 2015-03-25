var app = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ui.bootstrap',
    'account.services', 'account.controllers', 'account.directives',
    'additional.controllers', 'additional.directives', 'additional.services',
    'questionnaire.controllers', 'questionnaire.directives', 'questionnaire.services', 'underscore']).
    config(function($tooltipProvider, $routeProvider, $locationProvider) {
        $tooltipProvider.setTriggers({
            'mouseenter' : 'mouseleave',
            'click' : 'click',
            'focus' : 'blur',
            'never' : 'mouseleave'
        });
        $locationProvider.html5Mode(true);
        $routeProvider.
            when('/qdump', {
                templateUrl: '/app/qdump.html',
                controller: 'MainCtrl'
            }).
            when('/account', {
                redirectTo: '/account/personal'
            }).
            when('/account/auth', {
                templateUrl: '/app/Account/auth.html',
                controller: 'MainCtrl'
            }).
            when('/account/registration', {
                templateUrl: '/app/Account/registration.html',
                controller: 'MainCtrl'
            }).
            when('/account/personal', {
                templateUrl: '/app/Account/personal-page.html',
                controller: 'AccountCtrl'
            }).
            when('/account/change', {
                templateUrl: '/app/Account/change-personal.html',
                controller: 'AccountCtrl'
            }).
            when('/account/questionnaire', {
                templateUrl: '/app/Questionnaire/personal-questionnaire.html',
                controller: 'PersonalQuestionnaireCtrl',
                resolve: {
                    questionnaire_id: function(PersonalQuestionnaireFactory) {
                        return PersonalQuestionnaireFactory.getQuestionnaireId();
                    }
                }
            }).
            when('/success', {
                templateUrl: '/app/Additional/success.html',
                controller: 'SuccessCtrl'
            }).
            when('/error', {
                templateUrl: '/app/Additional/error.html',
                controller: 'ErrorCtrl',
                resolve: {
                    error: function(ErrorFactory) {
                        return ErrorFactory.getErrorMessage();
                    }
                }
            }).
            when('/questionnaires/create', {
                templateUrl: '/app/Questionnaire/create-questionnaire.html',
                controller: 'CreateQuestionnaireCtrl',
                resolve: {
                    admin: function(Authorization) {
                        var user = Authorization.getUser();
                        return (user.authorized && _.isEqual(user.role, 'ADMIN')) ? user : null;
                    },
                    questionnaire: function(QuestionnaireFactory, PersonalQuestionnaireFactory) {
                        var id = PersonalQuestionnaireFactory.getQuestionnaireId();
                        return _.isNull(id) ? new Object({question_entities: []}) : QuestionnaireFactory.get({get: 'get', id: id}, function(data) { return data});
                    }
                }
            }).
            when('/account/questionnaires/show/completed', {
                templateUrl: '/app/Questionnaire/show-complete.html',
                controller: 'ShowCompletedPersonQuestionnaireCtrl',
                resolve: {
                    person_questionnaire: function(Questionnaire) {
                        return Questionnaire.getPersonQuestionnaire();
                    }
                }
            }).
            when('/questionnaires', {
                templateUrl: '/app/Questionnaire/questionnaires.html',
                controller: 'QuestionnaireCtrl',
                resolve: {
                    questionnaire_type: function() {
                        return 'published'
                    }
                }
            }).
            when('/account/questionnaires/completed', {
                templateUrl: '/app/Questionnaire/completed.html',
                controller: 'QuestionnaireCtrl',
                resolve: {
                    questionnaire_type: function() {
                        return 'completed'
                    }
                }
            }).
            when('/account/questionnaires/started', {
                templateUrl: '/app/Questionnaire/started.html',
                controller: 'QuestionnaireCtrl',
                resolve: {
                    questionnaire_type: function() {
                        return 'started'
                    }
                }
            }).
            when('/account/questionnaires/admin', {
                templateUrl: '/app/Questionnaire/questionnaires-admin.html',
                controller: 'QuestionnaireCtrl',
                resolve: {
                    admin: function(Authorization) {
                        var user = Authorization.getUser();
                        return (user.authorized && _.isEqual(user.role, 'ADMIN')) ? user : null;
                    },
                    questionnaire_type: function() {
                        return 'all'
                    }
                }
            }).
            when('/contacts', {
                templateUrl: '/app/Additional/contacts.html',
                controller: 'ContactsCtrl'
            }).
            otherwise({
                redirectTo: '/qdump'
            });
    });
app.run(['$rootScope', '$cookieStore', 'ErrorFactory', '$location', function($root, $cookieStore, ErrorFactory, $location) {
    $root.$on('$routeChangeStart', function(event, next, current) {
        if(next.$$route.originalPath != '/error' && next.$$route.originalPath != '/success') {
            $cookieStore.remove('error');
            $cookieStore.remove('success')
        }
        if(!angular.equals(next.$$route.originalPath, '/account/questionnaire')
            && !angular.equals(next.$$route.originalPath, '/account/questionnaires/show/completed')) {
            $cookieStore.remove('person_questionnaire_id');
            $cookieStore.remove('questionnaire_id')
        }
    });
    $root.$on('$routeChangeSuccess', function(event, current, previous) {
    });
    $root.$on('$routeChangeError', function(event, current, previous, reject) {
        if(current.$$route.originalPath == '/questionnaires/create') {
            $cookieStore.put('error', reject.data.error);
            $location.path('/error');
        }
    });
}]);

var app = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ui.bootstrap', 'base64',
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
                templateUrl: '/app/qdump.html'
            }).
            when('/account', {
                redirectTo: '/account/personal'
            }).
            when('/account/authentication', {
                templateUrl: '/app/Account/authentication.html',
                controller: 'MainCtrl'
            }).
            when('/account/registration', {
                templateUrl: '/app/Account/registration.html',
                controller: 'MainCtrl'
            }).
            when('/account/personal', {
                templateUrl: '/app/Account/personal-page.html',
                controller: 'AccountCtrl',
                resolve: {
                    isAuth: function(Authorization) {
                        return Authorization.resolve();
                    }
                }
            }).
            when('/account/change', {
                templateUrl: '/app/Account/change-personal.html',
                controller: 'AccountCtrl'
            }).
            when('/account/questionnaire', {
                templateUrl: '/app/Questionnaire/personal-questionnaire.html',
                controller: 'PersonalQuestionnaireCtrl',
                resolve: {
                    questionnaireId: function(PersonalQuestionnaire) {
                        return PersonalQuestionnaire.getQuestionnaireId();
                    },
                    personQuestionnaireId: function(PersonalQuestionnaire) {
                        return PersonalQuestionnaire.getPersonQuestionnaireId();
                    }
                }
            }).
            when('/account/admin', {
                templateUrl: '/app/Account/admin-panel.html',
                controller: 'AdminPanelCtrl',
                resolve: {
                    isAuth: function(Authorization) {
                        return Authorization.resolve();
                    },
                    admin: function(Permission) {
                        return Permission.resolve('ADMIN');
                    },
                    page_type: function() {
                        return 'edit_questionnaires'
                    },
                    tab_number: function() {
                        if(typeof(Storage) != "undefined") {
                            var tab = sessionStorage.getItem("admin_panel_tab");
                            return  tab == null ? 0 : Number(tab);
                        } else {
                            return 0;
                        }
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
                    isAuth: function(Authorization) {
                        return Authorization.resolve();
                    },
                    admin: function(Permission) {
                        return Permission.resolve('ADMIN');
                    },
                    questionnaire: function(QuestionnaireFactory, PersonalQuestionnaire) {
                        var id = PersonalQuestionnaire.getQuestionnaireId();
                        return (_.isNull(id) || id == 0) ? new Object({question_entities: []}) : QuestionnaireFactory.get({id: id}, function(data) { return data});
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
        if(next != null) {
            if(next.$$route) {
                if(next.$$route.originalPath != '/error' && next.$$route.originalPath != '/success') {
                    $cookieStore.remove('error');
                    $cookieStore.remove('success')
                }
                if(!angular.equals(next.$$route.originalPath, '/account/questionnaire')
                    && !angular.equals(next.$$route.originalPath, '/account/questionnaires/show/completed')) {
                    $cookieStore.remove('personQuestionnaireId');
                    $cookieStore.remove('questionnaireId')
                }
                if(angular.equals(next.$$route.originalPath, '/account/registration')) {
                    if($cookieStore.get('isAuth') == true) {
                        $location.path('/');
                    }
                }
            }
        }
    });
    $root.$on('$routeChangeSuccess', function(event, current, previous) {
    });
    $root.$on('$routeChangeError', function(event, current, previous, reject) {
        ErrorFactory.setErrorMessage(reject.data.error);
        $location.path('/error');
    });
}]);

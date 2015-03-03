module = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ui.bootstrap',
    'qdumpApp.services', 'qdumpApp.controllers', 'qdumpApp.directives']).
    config(function($tooltipProvider, $routeProvider, $locationProvider) {
        $tooltipProvider.setTriggers({
            'mouseenter' : 'mouseleave',
            'click' : 'click',
            'focus' : 'blur',
            'never' : 'mouseleave'
        });
        $locationProvider.html5Mode(true);
        $routeProvider.
            when('/user-list', {
                templateUrl: 'login/user-list.html',
                controller: 'UserListCtrl'
            }).
            when('/auth', {
                templateUrl: 'login/auth.html',
                controller: 'AuthCtrl'
            }).
            when('/reg', {
                templateUrl: 'login/registration.html',
                controller: 'RegCtrl'
            }).
            when('/welcome', {
                templateUrl: 'login/welcome.html'
            }).
            when('/account', {
                templateUrl: 'login/account.html',
                controller: 'AccountCtrl'
            }).
            when('/error', {
                templateUrl: 'login/error.html'
            }).
            when('/verify', {
                templateUrl: 'login/success.html'
            }).
            when('/success', {
                templateUrl: 'login/success.html'
            }).
            when('/account/personal', {
                templateUrl: 'login/personal-page.html',
                controller: 'AccountCtrl'
            }).
            when('/account/change', {
                templateUrl: 'login/change-personal.html',
                controller: 'AccountCtrl'
            }).
            when('/account/completed', {
                templateUrl: 'login/completed.html',
                controller: 'AccountCtrl'
            }).
            when('/account/questionnaire', {
                templateUrl: 'login/personal-questionnaire.html',
                controller: 'PersonalQuestionnaireCtrl'
            }).
            when('/questionnaires/create', {
                templateUrl: 'login/create-questionnaire.html',
                controller: 'CreateQuestionnaireCtrl'
            }).
            when('/test', {
                templateUrl: 'login/test.html',
                controller: 'TestCtrl'
            }).
            when('/questionnaires', {
                templateUrl: 'login/questionnaires.html',
                controller: 'QuestionnaireCtrl'

            }).
            when('/contacts', {
                templateUrl: 'login/contacts.html'
            }).
            otherwise({
                redirectTo: '/'
            });
    });


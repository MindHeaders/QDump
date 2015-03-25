var app = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ui.bootstrap',
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
            when('/qdump', {
                templateUrl: 'login/partials/qdump.html',
                controller: 'MainCtrl'
            }).
            when('/account', {
                templateUrl: 'login/partials/account.html',
                controller: 'AccountCtrl'
            }).
            when('/account/auth', {
                templateUrl: 'login/partials/auth.html',
                controller: 'AuthCtrl'
            }).
            when('/account/registration', {
                templateUrl: 'login/partials/registration.html',
                controller: 'RegistrationCtrl'
            }).
            when('/account/personal', {
                templateUrl: 'login/partials/personal-page.html',
                controller: 'AccountCtrl'
            }).
            when('/account/change', {
                templateUrl: 'login/partials/change-personal.html',
                controller: 'AccountCtrl'
            }).
            when('/account/completed', {
                templateUrl: 'login/partials/completed.html',
                controller: 'CompletedCtrl'
            }).
            when('/account/questionnaire', {
                templateUrl: 'login/partials/personal-questionnaire.html',
                controller: 'PersonalQuestionnaireCtrl'
            }).
            when('/welcome', {
                templateUrl: 'login/partials/welcome.html'
            }).
            when('/verify', {
                templateUrl: 'login/partials/success.html'
            }).
            when('/success', {
                templateUrl: 'login/partials/success.html',
                controller: 'SuccessCtrl'
            }).
            when('/error', {
                templateUrl: 'login/partials/error.html',
                controller: 'ErrorCtrl'
            }).
            when('/questionnaires/create', {
                templateUrl: 'login/partials/create-questionnaire.html',
                controller: 'CreateQuestionnaireCtrl',
                resolve: {
                    admin: function(Role) {
                        return Role.getRole();
                    }
                }
            }).
            when('/questionnaires', {
                templateUrl: 'login/partials/questionnaires.html',
                controller: 'QuestionnaireCtrl'
            }).
            when('/contacts', {
                templateUrl: 'login/partials/contacts.html'
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
    });
    $root.$on('$routeChangeSuccess', function(event, current, previous) {
    });
    $root.$on('$routeChangeError', function(event, current, previous, reject) {
        if(current.$$route.originalPath == '/questionnaires/create') {
            $cookieStore.put('error', reject.data.error);
            $location.path('/error');
        }
    });
}]);var app = angular.module('qdumpApp', ['ngRoute', 'ngMessages', 'ui.bootstrap',
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
            when('/qdump', {
                templateUrl: 'login/partials/qdump.html',
                controller: 'MainCtrl'
            }).
            when('/account', {
                templateUrl: 'login/partials/account.html',
                controller: 'AccountCtrl'
            }).
            when('/account/auth', {
                templateUrl: 'login/partials/auth.html',
                controller: 'AuthCtrl'
            }).
            when('/account/registration', {
                templateUrl: 'login/partials/registration.html',
                controller: 'RegistrationCtrl'
            }).
            when('/account/personal', {
                templateUrl: 'login/partials/personal-page.html',
                controller: 'AccountCtrl'
            }).
            when('/account/change', {
                templateUrl: 'login/partials/change-personal.html',
                controller: 'AccountCtrl'
            }).
            when('/account/completed', {
                templateUrl: 'login/partials/completed.html',
                controller: 'CompletedCtrl'
            }).
            when('/account/questionnaire', {
                templateUrl: 'login/partials/personal-questionnaire.html',
                controller: 'PersonalQuestionnaireCtrl'
            }).
            when('/welcome', {
                templateUrl: 'login/partials/welcome.html'
            }).
            when('/verify', {
                templateUrl: 'login/partials/success.html'
            }).
            when('/success', {
                templateUrl: 'login/partials/success.html',
                controller: 'SuccessCtrl'
            }).
            when('/error', {
                templateUrl: 'login/partials/error.html',
                controller: 'ErrorCtrl'
            }).
            when('/questionnaires/create', {
                templateUrl: 'login/partials/create-questionnaire.html',
                controller: 'CreateQuestionnaireCtrl',
                resolve: {
                    admin: function(Role) {
                        return Role.getRole();
                    }
                }
            }).
            when('/questionnaires', {
                templateUrl: 'login/partials/questionnaires.html',
                controller: 'QuestionnaireCtrl'
            }).
            when('/contacts', {
                templateUrl: 'login/partials/contacts.html'
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

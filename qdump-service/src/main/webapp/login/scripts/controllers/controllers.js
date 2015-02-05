var app = angular.module('qdumpApp.controllers', ['ngCookies']);

app.controller('AuthCtrl', ['$scope', '$cookieStore', '$location', '$window', 'AuthFactory',
    function($scope, $cookieStore, $location, $window, AuthFactory) {
        $scope.isFormSubmitted = false
        $scope.auth = function() {
            $scope.isFormSubmitted = true
            AuthFactory.auth($.param({
                login: $scope.user.login,
                password: $scope.user.password
            }), function() {
                $window.location.assign('index.html')
                $cookieStore.put('login', $scope.user.login)
            }, function(data) {
                $scope.errorData = data.data
                $scope.user.password = ''
                console.log(data)
            });
        }
    }
]);
app.controller('LogOutCtrl', ['$scope', '$cookieStore', '$window', 'LogoutFactory',
    function($scope, $cookieStore, $window, LogoutFactory) {
        $scope.logout = function() {
            LogoutFactory.logout()
            $cookieStore.remove('login')
            $window.location.reload()
        }
    }
])
app.controller('RegCtrl', ['$scope', 'RegFactory',
    function($scope, RegFactory) {
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
        $scope.submitted = false;
        $scope.reg = function() {
            $scope.submitted = true;
            RegFactory.reg($scope.user);
        }
        $scope.passwordSize = function() {
            console.log($scope.user.password.length)
            return $scope.user.password.length
        }
    }
]);
app.controller('UserListCtrl', ['$scope', 'UsersFactory',
    function($scope, UsersFactory) {
        $scope.users = UsersFactory.query(
            function(data) {
                console.log(data);
            },
            function(data) {
                console.log(data);
            }
        );
    }
]);
app.controller('BasicCtrl', ['$scope', '$cookieStore',
    function ($scope, $cookieStore) {
        $scope.login = $cookieStore.get('login')
        if($scope.login == null) {
            $scope.isAuth = false
        } else {
            $scope.isAuth = true
        }
    }
])
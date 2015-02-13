var app = angular.module('qdumpApp.controllers', ['ngCookies']);

app.controller('AuthCtrl', ['$scope', '$cookieStore', '$location', '$window', 'AuthFactory',
    function($scope, $cookieStore, $location, $window, AuthFactory) {
        $scope.isFormSubmitted = false;
        $scope.auth = function() {
            $scope.isFormSubmitted = true
            AuthFactory.auth($.param({
                login_or_email: $scope.user.login_or_email,
                password: $scope.user.password,
                rememberMe: $scope.rememberMe
            }), function() {
                $cookieStore.put('userAuth', true);
                $window.location.assign('login/index.html');
            }, function(data) {
                $cookieStore.put('userAuth', false);
                $scope.errorData = data.data;
                $scope.user.password = '';
            });
        }
    }
]);
app.controller('LogOutCtrl', ['$scope', '$cookieStore', '$window', 'LogoutFactory',
    function($scope, $cookieStore, $window, LogoutFactory) {
        $scope.logout = function() {
            LogoutFactory.logout()
            $cookieStore.remove('userAuth')
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
        $scope.userAuth = $cookieStore.get('userAuth');
    }
]);
app.controller('AccountCtrl', ['$scope', 'AccountFactory', 'UpdateFactory', 'ResetPasswordFactory',
    function($scope, AccountFactory, UpdateFactory, ResetPasswordFactory) {
        $scope.user = AccountFactory.page(
            function() {
                $scope.isAuth = true;
                $scope.changedUser = angular.copy($scope.user);
                $scope.reset = function() {
                    $scope.changedUser = angular.copy($scope.user);
                }
                $scope.userGender = function() {
                    if($scope.user.gender == 1) return 'Male';
                    else if($scope.user.gender == 2) return 'Female';
                    else return 'Not Signed';
                }
            },
            function(data) {
                $scope.isAuth = false;
                $scope.errorData = data.data;
            }
        );
        $scope.templates = [{
            template: { menu: 'login/float-menu.html'}
        }];
        $scope.template = $scope.templates[0].template;
        $scope.update = function(changedUser, user) {
            if(changedUser.email != user.email) {
                alert("You change your email. This changes will disabled your account. " +
                    "If you confirm update your personal data you should to enabled " +
                    "your account you may confirm it with url that we send to your new email.")
            }
            var isUpdate = confirm("Do you want to continue update user?")
            if(isUpdate) UpdateFactory.update(changedUser);
        }
        $scope.resetPassword = function() {
            var reset = confirm("Reset password: We will generate new password and send to your email. " +
                "Do you want to continue?")
           if(reset) ResetPasswordFactory.resetPassword();
        }
    }
]);
app.controller('CheckerCtrl', ['$scope',
    function($scope) {
        var checkValid = null;
        var checkInvalid = null;
        var successUnique = null;
        var newUserDataNotNull = null;
        var isEquals = null;
        var dirty = null;
        var lengthMoreThanZero = null;
        var checkers = [{
            dirtyValid: 'dirtyValid',
            dirtyInvalid: 'dirtyInvalid'
        }]
        $scope.check = function(formType, checker) {
            if(checker === checkers[0].dirtyValid) {
                return formType.$valid && formType.$dirty
            }
            else if(checker === checkers[0].dirtyInvalid)
                return formType.$invalid && formType.$dirty
        }
        $scope.objectEquals = function(oldUser, newUser) {
            return angular.equals(oldUser, newUser);
        }
        $scope.isEquals = function(newUserData, oldUserData) {
            return angular.equals(newUserData, oldUserData);
        }
        $scope.setData = function(formType, oldUserData, newUserData) {
            if(newUserData != null) {
                lengthMoreThanZero = newUserData.length > 0;
                isEquals = $scope.isEquals(oldUserData, newUserData);
                newUserDataNotNull = newUserData != '';
            }
            checkValid = $scope.check(formType, 'dirtyValid');
            checkInvalid = $scope.check(formType, 'dirtyInvalid')
            successUnique = formType.$$success.unique != null;
            dirty = formType.$dirty;
        }
        $scope.checkState = function(type) {
            switch (type) {
                case $scope.state.okIconUnique: return checkValid && successUnique && newUserDataNotNull && !isEquals;
                case $scope.state.errorContainer: return checkInvalid && !isEquals;
                case $scope.state.notEquals: return !isEquals;
                case $scope.state.warnIconUpdate: return isEquals && dirty;
                case $scope.state.okIcon: return checkValid && lengthMoreThanZero && newUserDataNotNull && !isEquals;
            }
        }
        var states = [{
            okIconUnique: 'okIconUnique',
            errorContainer: 'errorContainer',
            notEquals: 'notEquals',
            warnIconUpdate: 'warnIconUpdate',
            okIcon: 'okIcon'
        }]
        $scope.state = states[0];
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
    }
])
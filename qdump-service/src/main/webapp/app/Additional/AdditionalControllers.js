/**
 * Created by artemvlasov on 17/03/15.
 */
var app = angular.module('additional.controllers', ['ngCookies']);

app.controller('MainCtrl', ['$scope', '$cookieStore', '$http', '$location', '$window', 'PersonFactory', 'Authorization',
    function($scope, $cookieStore, $http, $location, $window, PersonFactory, Authorization) {
        $scope.userIsAuth = $cookieStore.get('userAuth');
        $scope.adminPermission = $cookieStore.get('role') == 'ADMIN';
        $scope.checkPermission = function(role) {
            return _.isEqual(Authorization.getUser().role, role);
        };
        //Authorization
        $scope.isFormSubmitted = false;
        $scope.auth = function() {
            $scope.isFormSubmitted = true;
            PersonFactory.auth($.param({
                login_or_email: $scope.user.login_or_email,
                password: $scope.user.password,
                rememberMe: $scope.rememberMe
            }), function(data) {
                console.log(data);
                $cookieStore.put('userAuth', true);
                $cookieStore.put('role', data.role);
                $window.location.assign('/');
            }, function(data) {
                $cookieStore.put('userAuth', false);
                $scope.errorData = data.data;
                $scope.user.password = '';
            });
        };
        Authorization.setUser();
        //Logout
        $scope.logout = function() {
            PersonFactory.logout();
            $cookieStore.remove('userAuth');
            $cookieStore.remove('role');
            $location.path('/qdump');
            $window.location.reload()
        };
        Authorization.logout();
        //Registration
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
        $scope.user = {};
        $scope.registration = function() {
            RegistrationFactory.registration($scope.user,
                function(){
                    $cookieStore.put('success', 'You successfully registered on QDump Project.\n We send you verification link to your email. Life time of this link is 24 hours');
                    $location.path('/success');
                },
                function(data){
                    $location.path('/error');
                    $scope.error = data;
                });
        };
        $scope.passwordSize = function() {
            console.log($scope.user.password.length);
            return $scope.user.password.length;
        };
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
        $scope.successUnique = angular.copy(successUnique);
        var checkers = [{
            dirtyValid: 'dirtyValid',
            dirtyInvalid: 'dirtyInvalid'
        }];
        $scope.check = function(formType, checker) {
            if(formType == null || checker == null) {
                throw {
                    message: 'Error with formType or checker',
                    code: 404
                }
            }
            if(checker === checkers[0].dirtyValid) {
                return formType.$valid && formType.$dirty
            }
            else if(checker === checkers[0].dirtyInvalid)
                return formType.$invalid && formType.$dirty
        };
        $scope.objectEquals = function(oldUser, newUser) {
            return angular.equals(oldUser, newUser);
        };
        $scope.isEquals = function(newUserData, oldUserData) {
            return angular.equals(newUserData, oldUserData);
        };
        $scope.setData = function(formType, oldUserData, newUserData) {
            if(newUserData != null) {
                lengthMoreThanZero = newUserData.length > 0;
                newUserDataNotNull = newUserData != '';
                isEquals = $scope.isEquals(oldUserData, newUserData);
            }
            checkValid = $scope.check(formType, 'dirtyValid');
            checkInvalid = $scope.check(formType, 'dirtyInvalid');
            successUnique = formType.$$success.unique != null;
            dirty = formType.$dirty;
        };
        $scope.checkState = function(type) {
            switch (type) {
                case $scope.state.okIconUnique: return checkValid && successUnique && newUserDataNotNull && !isEquals;
                case $scope.state.errorContainer: return checkInvalid && !isEquals;
                case $scope.state.notEquals: return !isEquals;
                case $scope.state.warnIconUpdate: return isEquals && dirty;
                case $scope.state.okIcon: return checkValid && lengthMoreThanZero && newUserDataNotNull && !isEquals;
                case $scope.state.okIconUniqueReg: return checkValid && successUnique;
            }
        };
        var states = [{
            okIconUnique: 'okIconUnique',
            errorContainer: 'errorContainer',
            notEquals: 'notEquals',
            warnIconUpdate: 'warnIconUpdate',
            okIcon: 'okIcon',
            okIconUniqueReg: 'okIconUniqueReg'
        }];
        $scope.state = states[0];
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
    }
]);
app.controller('ErrorCtrl', ['$scope', 'error',
    function($scope, error) {
        $scope.error = error;
    }
]);
app.controller('SuccessCtrl', ['$scope', '$cookieStore',
    function($scope, $cookieStore) {
        $scope.success = $cookieStore.get('success');
    }
]);
app.controller('ContactsCtrl', ['$scope',
    function($scope) {
        $scope.contactsDevelopers = [
            {
                name: 'Artem',
                surname: 'Vlasov',
                development: 'Back-end, Front-end',
                languages: 'Java, AngularJS',
                email: 'vlasovartem21@gmail.com',
                skype: 'martens121087',
                linkedin: 'https://www.linkedin.com/in/vlasovartem',
                github: 'https://github.com/VlasovArtem',
                icon: '/style/icons/personal/vlasovartem.jpg'
            },
            {
                name: 'Evgeniy',
                surname: 'Golovko',
                development: 'Back-end',
                languages: 'Java',
                email: '',
                skype: 'esudreame',
                linkedin: '',
                github: '',
                icon: '/style/icons/personal/esudreame.png'
            },
            {
                name: 'Anastasia',
                surname: 'Grankina',
                development: 'Back-end',
                languages: 'Java',
                email: '',
                skype: 'klava.prekrasnaia',
                linkedin: '',
                github: '',
                icon: ''
            },
            {
                name: 'Elena',
                surname: 'Bugayeva',
                development: 'Front-end',
                languages: 'HTML, CSS, AngularJS, JavaScript',
                email: '',
                skype: 'rain_b1ack',
                linkedin: '',
                github: '',
                icon: '/style/icons/personal/b1ackrain.png'
            },
            {
                name: 'Aleksandr',
                surname: '',
                development: 'Back-end',
                languages: 'Java',
                email: '',
                skype: 'ebonhawk007',
                linkedin: '',
                github: '',
                icon: ''
            },
            {
                name: 'Irina',
                surname: 'Brinchak',
                development: 'Back-end (DBA)',
                languages: 'Hibernate, JPA, Oracle',
                email: '',
                skype: 'i.brichak',
                linkedin: '',
                github: '',
                icon: ''
            },
            {
                name: 'Viola',
                surname: '',
                development: 'Front-end (QA)',
                languages: '',
                email: '',
                skype: 'cherry_wave7',
                linkedin: '',
                github: '',
                icon: ''
            }
        ];
        $scope.checkField = function(fieldName, index) {
            if($scope.contactsDevelopers[index][fieldName] == null || $scope.contactsDevelopers[index][fieldName] == '') {
                return true;
            } else {
                return false;
            }
        }
    }
]);
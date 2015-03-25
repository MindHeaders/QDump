var app = angular.module('account.controllers', ['ngCookies']);

app.controller('AccountCtrl', ['$scope', '$http', '$cookieStore', '$location', '$window', 'PersonFactory',
    function($scope, $http, $cookieStore, $location, $window, PersonFactory) {
        $scope.user = PersonFactory.page(
            function() {
                $scope.changedUser = angular.copy($scope.user);
                $scope.reset = function() {
                    $scope.changedUser = angular.copy($scope.user);
                };
                $scope.userGender = function() {
                    if($scope.user.gender == 1) return 'Male';
                    else if($scope.user.gender == 2) return 'Female';
                    else return 'Not Signed';
                }
            },
            function(data) {
                $cookieStore.put('error', data.error);
                $location.path('/error')
            }
        );
        $scope.templates = [{
            template: { menu: '/app/Account/float-menu.html'}
        }];
        $scope.template = $scope.templates[0].template;
        $scope.update = function(changedUser, user) {
            if(changedUser.email != user.email) {
                alert("You change your email. This changes will disabled your account. " +
                "If you confirm update your personal data you should to enabled " +
                "your account you may confirm it with url that we send to your new email.")
            }
            var isUpdate = confirm("Do you want to continue update user?");
            if(isUpdate) PersonFactory.update(changedUser);
        };
        $scope.resetPassword = function() {
            var reset = confirm("Reset password: We will generate new password and send to your email. " +
            "Do you want to continue?");
            if(reset) PersonFactory.resetPassword();
        };
    }
]);
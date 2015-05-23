/**
 * Created by artemvlasov on 05/02/15.
 */
var app = angular.module('account.directives',[]);
app.directive('ensureUnique', ["$http", function($http) {
    var toId;
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elem, attr, ctrl) {
            //when the scope changes, check the email.
            scope.$watch(attr.ngModel, function(value) {
                // if there was a previous attempt, stop it.
                ctrl.$setValidity('unique', null);
                if(toId) clearTimeout(toId);
                // start a new attempt with a delay to keep it from
                // getting too "chatty".
                if(!ctrl.$pristine) {
                    toId = setTimeout(function () {
                        // call to some API that returns { isValid: true } or { isValid: false }
                        $http.get('/rest/persons/check/' + attr.name
                        + '?' + attr.name + '=' + value)
                            .success(function () {
                                ctrl.$setValidity('unique', true);
                            }).error(function() {
                                ctrl.$setValidity('unique', false);
                            });

                    }, 1000);
                }
            })
        }
    }
}]);

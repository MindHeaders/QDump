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
            scope.$watch(attr.ngModel, function(value) {
                ctrl.$setValidity('unique', null);
                if(toId) clearTimeout(toId);
                if(!ctrl.$pristine) {
                    toId = setTimeout(function () {
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

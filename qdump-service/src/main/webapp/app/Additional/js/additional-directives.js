/**
 * Created by artemvlasov on 17/03/15.
 */
/**
 * Created by artemvlasov on 05/02/15.
 */
var app = angular.module('additional.directives',[]);
app.directive('showErrors', ["$timeout", function($timeout){
    return {
        restrict: 'A',
        require: '^form',
        link: function (scope, el, attrs, formCtrl) {
            var inputEl = el[0].querySelector("[name]");
            var inputNgEl = angular.element(inputEl);
            var inputName = inputNgEl.attr('name');
            var blurred = false;
            inputNgEl.bind('blur', function() {
                blurred = true;
                el.toggleClass('has-error', formCtrl[inputName].$invalid);
            });
            scope.$watch(function() {
                return formCtrl[inputName].$invalid;
            }, function(invalid) {
                if(!blurred && invalid) {
                    return
                }
                el.toggleClass('has-error', invalid);
            });
            scope.$on('show-errors-check-validity', function() {
                el.toggleClass('has-error', formCtrl[inputName].$invalid);
            });

            scope.$on('show-errors-reset', function() {
                $timeout(function() {
                    el.removeClass('has-error');
                }, 0, false);
            });
        }
    }
}]);
app.directive('ngFocus', [function() {
    var FOCUS_CLASS = "ng-focused";
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, ctrl) {
            ctrl.$focused = false;
            element.bind('focus', function(evt) {
                element.addClass(FOCUS_CLASS);
                scope.$apply(function() {ctrl.$focused = true;});
            }).bind('blur', function(evt) {
                element.removeClass(FOCUS_CLASS);
                scope.$apply(function() {ctrl.$focused = false;});
            });
        }
    }
}]);
app.directive('validationMessages', function () {
    return {
        scope: {
            modelController: '='
        },
        restrict: 'EA',
        link: function (scope, elm, attrs) {
            if (!scope.modelController) {
                console.log('Requires a html attribute data-model-controller. This should point to the input field model controller.');
            }
            scope.$watch('modelController.$error', function (newValue) {
                if (newValue) {
                    scope.errorMessages = [];
                    angular.forEach(newValue, function (value, key) {
                        if (value && attrs[key + 'Error']) {
                            scope.errorMessages.push(attrs[key + 'Error']);
                        }
                    });
                }
            }, true);
        },
        template: '<small class="help-block test" ng-repeat="message in errorMessages" ng-show= "!modelController.$pristine && $first" class="warning">{{message}}</small>'
    }
});
app.directive('entitySorting', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var dataSorting = [
                {type: 'createdDate', direction: 'DESC'},
                {type: 'modifiedDate', direction: 'DESC'}
            ];
            scope.$watch("dataSorting", function() {
                Array.prototype.push.apply(dataSorting, scope.dataSorting);
            });
            var queryParams = null;
            var setParams = function() {
                queryParams = {
                    page: scope.currentPage - 1,
                    size: scope.currentPageSize.value,
                    direction: scope.currentDataSorting.direction,
                    sort: scope.currentDataSorting.type
                };
            };
            scope.currentPage = 1;
            scope.maxSize = 5;
            scope.currentDataSorting = dataSorting[0];
            scope.pageSize = [
                {label: '15', value: 15},
                {label: '25', value: 25},
                {label: '50', value: 50},
                {label: '100', value: 100}
            ];
            scope.currentPageSize = scope.pageSize[0];
            scope.$watch(
                "currentPageSize.value",
                function(newValue, oldValue) {
                    if(newValue === oldValue) return;
                    setParams();
                    scope.getData(queryParams);
                }
            );
            scope.sorting = function(type){
                if(scope.currentDataSorting.type == type) {
                    scope.currentDataSorting.direction = scope.currentDataSorting.direction == 'ASC' ? 'DESC' : 'ASC';
                } else {
                    for(var i = 0; i < dataSorting.length; i++){
                        if(dataSorting[i].type == type) {
                            scope.currentDataSorting.direction = 'DESC';
                            scope.currentDataSorting = dataSorting[i];
                            break;
                        }
                    }
                }
                setParams();
                scope.getData(queryParams);
            };
        }
    }
});
app.directive('sorting', function() {
    return {
        restrict: 'E',
        replace: true,
        compile: function(element, attrs) {
            var attribute = attrs['attribute'];
            var name = attrs['name'];
            element.html('<a href="#" ng-click="sorting(' + attribute + ')">' + name +
            '   <span ng-show="currentDataSorting.type == ' + attribute + '"> ' +
            '       <span style="font-size: small" class="glyphicon glyphicon-sort-by-alphabet" ng-show="currentDataSorting.direction == \'ASC\'"></span> ' +
            '       <span style="font-size: small" class="glyphicon glyphicon-sort-by-alphabet-alt" ng-show="currentDataSorting.direction == \'DESC\'"></span>' +
            '   </span> ' +
            '</a>')
        }
    }
});
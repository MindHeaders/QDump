/**
 * Created by artemvlasov on 05/02/15.
 */
var app = angular.module('qdumpApp.directives',[]);
app.directive('showErrors', function($timeout){
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
});
app.directive('ensureUnique', function($http) {
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
});
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
app.directive('putAnswer', function() {
    return {
        restrict: 'E',
        scope: {template : '@'},
        replace: true,
        template: '<div ng-include="template"></div>'
    }
});
app.directive('bsTooltip', function() {
    return function(scope, element, attrs) {
        element.find('[data-toggle="tooltip"]').tooltip();
    }
});
app.directive('booleanGridRadioPerson', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, element, attrs, controller) {
            scope.$watch(attrs.ngModel, function() {
                var radioSelected = scope.$eval(attrs.ngModel);
                if(radioSelected) {
                    var selectedAnswer = scope.personQuestionnaireEntity.person_question_entities[scope.index].person_answer_entities[scope.$index];
                    _.each(scope.personQuestionnaireEntity.person_question_entities[scope.index].person_answer_entities, function(answer) {
                        _.isEqual(selectedAnswer, answer) ? answer['marked'] = true : answer['marked'] = false;
                    })
                }
            })
        }
    }
});
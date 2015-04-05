/**
 * Created by artemvlasov on 05/02/15.
 */
var app = angular.module('questionnaire.directives',[]);
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
app.directive('createQuestionnaire', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            scope.$watch(attrs.ngShow, function() {
                if(scope.$eval(attrs.ngShow) == false) {
                    scope.questionnaire.published = false;
                }
            });
        }
    }
})
/**
 * Created by artemvlasov on 17/03/15.
 */
var app = angular.module('questionnaire.controllers', ['ngCookies']);

app.controller('CreateQuestionnaireCtrl', ['questionnaire', '$scope', '$location', '$cookieStore', '$modal', 'PersonalQuestionnaire', 'QuestionnaireFactory', 'QuestionFactory', 'AnswerFactory', 'UpdateQuestionnaire',
    function(questionnaire, $scope, $location, $cookieStore, $modal, PersonalQuestionnaire, QuestionnaireFactory, QuestionFactory, AnswerFactory, UpdateQuestionnaire) {
        $scope.options = [
            {label: 'Select type of question...', value: ''},
            {label: 'One answer question', value: 'RADIO'},
            {label: 'Multiple answer question', value: 'CHECKBOX'},
            {label: 'Open question', value: 'TEXTAREA'},
            {label: 'Select answer question', value: 'SELECT'}
        ];
        $scope.type = $scope.options[0];
        $scope.questionsLimit = 15;
        $scope.answersLimit = 4;
        $scope.templates = [{
            template: {
                checkbox: '/app/Questionnaire/checkbox-question.html',
                radio: '/app/Questionnaire/radio-question.html',
                textarea: '/app/Questionnaire/textarea-question.html',
                select: '/app/Questionnaire/select-question.html'
            }
        }];
        $scope.template = $scope.templates[0].template;
        $scope.questionnaire = questionnaire;
        if($scope.questionnaire instanceof QuestionnaireFactory) {
            $scope.questionnaire.$promise.then(function(data) {
                $scope.questionnaire = data;
                if($scope.questionnaire.question_entities.length == 0) {
                    $scope.questionnaire.published = false;
                }
            });
        }

        $scope.checkQuestionnaire = function () {
            if($scope.questionnaire.question_entities == null || $scope.questionnaire.question_entities.length < 1) {
                return false;
            } else {
                return $scope.questionnaire.question_entities.every(function(element) {
                    if(element.question_type == 'TEXTAREA'){
                        return true;
                    } else if (element.answer_entities == null)  {
                        return false;
                    } else {
                        return element.answer_entities.some(function (element) {
                            return element.correct == true;
                        })
                    }
                })
            }
        };
        $scope.deleteAnswer = function(answerIndex, questionIndex) {
            if(questionnaire instanceof QuestionnaireFactory) {
                AnswerFactory.delete(
                    {
                        id: $scope.questionnaire.question_entities[questionIndex].answer_entities[answerIndex].answer_id
                    })
            }
            $scope.questionnaire.question_entities[questionIndex].answer_entities.splice(answerIndex, 1);
        };
        $scope.deleteQuestion = function(questionIndex) {
            if(questionnaire instanceof QuestionnaireFactory) {
                QuestionFactory.delete(
                    {
                        id: $scope.questionnaire.question_entities[questionIndex].question_id
                    })
            }
            $scope.questionnaire.question_entities.splice(questionIndex, 1);
        };
        $scope.addQuestion = function() {
            if($scope.questionnaire.question_entities == null) {
                $scope.questionnaire.question_entities = [];
            } else if ($scope.questionsLimit == $scope.questionnaire.question_entities.length) {
                alert("You can add only 15 questions");
            } else {
                $scope.questionnaire.question_entities.push({
                    question_type: $scope.type.value,
                    answer_entities: []
                });
            }
        };
        $scope.addAnswer = function(questionIndex) {
            $scope.questionnaire.question_entities[questionIndex].answer_entities.push({});
            if(_.isEqual($scope.questionnaire.question_entities[questionIndex].question_type, 'CHECKBOX')
                && $scope.questionnaire.question_entities[questionIndex].answer_entities.length == 1) {
                $scope.addAnswer(questionIndex);
            }
        };
        $scope.changeRadioAnswers = function(newAnswerIndex, questionIndex) {
            _.each($scope.questionnaire.question_entities[questionIndex].answer_entities, function(answer, index) {
                index == newAnswerIndex ? answer.correct = true : answer.correct = false;
            });
        };
        $scope.createQ = function() {
            if($scope.questionnaire instanceof QuestionnaireFactory) {
                PersonalQuestionnaire.setQuestionnaireId(null);
                UpdateQuestionnaire.update($scope.questionnaire);
            } else {
                QuestionnaireFactory.save($scope.questionnaire,
                    function() {
                        alert('Questionnaire was create successfully');
                        location.reload();
                    }
                );
            }
        };
        $scope.preview = function() {
            var modalInstance = $modal.open({
                templateUrl: '/app/Questionnaire/questionnaire-preview.html',
                controller: 'PreviewPersonQuestionnaireCtrl',
                size: 'lg',
                resolve: {
                    questionnaire: function() {
                        return $scope.questionnaire
                    }
                }
            });
        };
        $scope.toConsole = function() {
            console.clear();
            console.log(angular.toJson($scope.questionnaire, true));
        }
    }
]);
app.controller('StartQuestionnaireCtrl', ['$scope', '$location', '$cookieStore', 'PersonalQuestionnaire',
    function($scope, $location, $cookieStore, PersonalQuestionnaire) {
        $scope.startQDump = function(id) {
            if(_.isNull($cookieStore.get('user'))) {
                $location.path('');
            }
            PersonalQuestionnaire.setQuestionnaireId(id);
            $location.path('/account/questionnaire');
        };
    }
]);
app.controller('PersonalQuestionnaireCtrl', ['questionnaireId', 'personQuestionnaireId', '$scope', '$location', '$cookieStore', 'QuestionnaireFactory', 'PersonFactory', 'PersonQuestionnairesFactory', 'UpdatePersonQuestionnaire',
    function(questionnaireId, personQuestionnaireId, $scope, $location, $cookieStore, QuestionnaireFactory, PersonFactory, PersonQuestionnairesFactory, UpdatePersonQuestionnaire) {
        $scope.personQuestionnaireEntity = {};
        if (!personQuestionnaireId) {
            $scope.personQuestionnaireEntity = {
                questionnaire_entity: {},
                person_question_entities: []
            };
            $scope.question_index = [];
            QuestionnaireFactory.get({personal: 'personal', id: questionnaireId},
                function (data) {
                    $scope.questionnaire = data;
                    $scope.updatePersonQuestionnaireEntity = function () {
                        $scope.personQuestionnaireEntity.questionnaire_entity.id = $scope.questionnaire.id;
                        for (var i = 0; i < $scope.questionnaire.question_entities.length; i++) {
                            var question = $scope.questionnaire.question_entities[i];
                            var personQuestions = $scope.personQuestionnaireEntity.person_question_entities;
                            $scope.question_index.push(new Object({index: i, id: question.id}));
                            personQuestions.push(new Object({question_entity: question}));
                            personQuestions[i].person_answer_entities = [];
                            for (var j = 0; j < $scope.questionnaire.question_entities[i].answer_entities.length; j++) {
                                var answer = question.answer_entities[j];
                                var personAnswers = personQuestions[i].person_answer_entities;
                                personAnswers.push(new Object({answer_entity: answer}));
                                personAnswers[j].person_answer = null;
                                if (question.question_type == 'TEXTAREA') {
                                    personAnswers.push(new Object({person_answer: null}))
                                }
                            }
                        }
                    };
                    $scope.updatePersonQuestionnaireEntity();

                }, function (data) {
                    $cookieStore.put('error', data.data.error);
                    $location.path('/error');
                }
            );
        } else {
            PersonFactory.get({id: questionnaireId},
                function(data) {
                    $scope.personQuestionnaireEntity = data;
                })
        }
        $scope.selectedItem = {};
        $scope.selectChange = function (parentIndex) {
            var selectedAnswer = $scope.personQuestionnaireEntity.person_question_entities[parentIndex].person_answer_entities[$scope.selectedItem.answer];
            _.each($scope.personQuestionnaireEntity.person_question_entities[parentIndex].person_answer_entities, function (answer) {
                _.isEqual(selectedAnswer, answer) ? answer['marked'] = true : answer['marked'] = false;
            })
        };
        $scope.textareaQuestion = function (questionIndex) {
            var answer = $scope.personQuestionnaireEntity.person_question_entities[questionIndex].person_answer_entities[0];
            (answer.person_answer == null || answer.person_answer == '') ? answer.marked = false : answer.marked = true;

        };
        $scope.complete = function () {
            var complete = confirm("If you complete questionnaire you cannot change it. Do you want to continue?");
            if(complete) {
                if(!personQuestionnaireId) {
                    $scope.personQuestionnaireEntity.status = 'in checking process';
                    PersonQuestionnairesFactory.save($scope.personQuestionnaireEntity);
                    $location.path('/');
                } else {
                    UpdatePersonQuestionnaire.update($scope.personQuestionnaireEntity);
                    $location.path('/')
                }
            }
        };
        $scope.save = function () {
            $scope.personQuestionnaireEntity.status = 'in progress';
            if(!personQuestionnaireId) {
                PersonFactory.save($scope.personQuestionnaireEntity);
            } else {
                UpdatePersonQuestionnaire.update($scope.personQuestionnaireEntity);
            }
        };
        $scope.close = function() {
            $location.path('/questionnaires')
        };
        $scope.toConsole = function () {
            console.clear();
            console.log(angular.toJson($scope.personQuestionnaireEntity, true))
        }
    }
]);
app.controller('CompletedPersonQuestionnaireCtrl', ['$scope', '$location', 'Questionnaire',
    function($scope, $location, Questionnaire) {
        $scope.show = function(id) {
            Questionnaire.setQuestionnaireId(id);
            $location.path('/account/questionnaires/show/completed');
        }
    }]);
app.controller('StartedPersonQuestionnaireCtrl', ['$scope', '$location', 'PersonalQuestionnaire',
    function($scope, $location, PersonalQuestionnaire) {
        $scope.continue = function(id) {
            PersonalQuestionnaire.setPersonQuestionnaireId(id);
            $location.path('/account/questionnaire');
        }
    }]);
app.controller('PreviewPersonQuestionnaireCtrl', ['$scope', 'questionnaire',
    function($scope, questionnaire) {
        $scope.questionnaire = questionnaire;
        _.each($scope.questionnaire.question_entities, function(question) {
            if(question.question_type == 'SELECT') {
                $scope.selectAnswers = angular.fromJson(question.answer_entities);
                console.log($scope.selectAnswers);
            }
        })
    }]);
app.controller('ShowCompletedPersonQuestionnaireCtrl', ['$scope', 'person_questionnaire',
    function($scope, person_questionnaire) {
        $scope.person_questionnaire = person_questionnaire;
    }
]);
app.controller('QuestionnaireCtrl', ['questionnaire_type', '$scope', '$location', '$cookieStore', 'PersonQuestionnairesFactory', 'QuestionnaireFactory',
    function(questionnaire_type, $scope, $location, $cookieStore, PersonQuestionnairesFactory, QuestionnaireFactory) {
        var type = questionnaire_type.toUpperCase();
        if(angular.equals(type, 'PUBLISHED')) {
            $scope.dataSorting = [{type: 'description', direction: 'DESC'}, {type: 'name', direction: 'DESC'}];
        } else if(angular.equals(type, 'COMPLETED')) {
            $scope.dataSorting= [{type: 'status', direction: 'DESC'}];
        }
        $scope.getData = function(queryParams) {
            switch (type) {
                case 'COMPLETED':
                    getCompleted(queryParams);
                    break;
                case 'STARTED':
                    getStarted(queryParams);
                    break;
                case 'PUBLISHED':
                    getPublished(queryParams);
                    break;
            }
        };
        var getCompleted = function(queryParams) {
            PersonQuestionnairesFactory.getCompleted(queryParams, function(questionnaires) {
                $scope.questionnaires = questionnaires;
            });
            PersonQuestionnairesFactory.get({completed: 'completed', count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            });
        };
        var getStarted = function(queryParams) {
            $scope.questionnaires = PersonQuestionnairesFactory.getStarted(queryParams);
            PersonQuestionnairesFactory.get({started: 'started', count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            });
        };
        var getPublished = function(queryParams) {
            $scope.questionnaires = QuestionnaireFactory.getPublished(queryParams);
            QuestionnaireFactory.get({published: 'published', count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            })
        };
        $scope.getData();
    }
]);
app.controller('AdminQuestionnaireCtrl', ['$scope', '$location', '$window', 'QuestionnaireFactory', 'PersonalQuestionnaire',
    function($scope, $location, $window, QuestionnaireFactory, PersonalQuestionnaire) {
        $scope.delete = function(id) {
            alert('You really want to delete this questionnaire?');
            QuestionnaireFactory.delete({id: id}).$promise.then($window.location.reload());
        };
        $scope.edit = function(id) {
            PersonalQuestionnaire.setQuestionnaireId(id);
            $location.path('/questionnaires/create');
        }
    }
]);

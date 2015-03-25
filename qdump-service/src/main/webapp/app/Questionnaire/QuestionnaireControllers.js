/**
 * Created by artemvlasov on 17/03/15.
 */
var app = angular.module('questionnaire.controllers', ['ngCookies']);

app.controller('CreateQuestionnaireCtrl', ['questionnaire', '$scope', '$location', '$cookieStore', '$modal', 'CreateQuestionnaireFactory', 'PersonalQuestionnaireFactory',
    function(questionnaire, $scope, $location, $cookieStore, $modal, CreateQuestionnaireFactory, PersonalQuestionnaireFactory) {
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
        if($scope.questionnaire.question_entities == null) {
            $scope.questionnaire.$promise.then(function(data) {
                $scope.questionnaire = data;
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
        $scope.createQ = function() {
            PersonalQuestionnaireFactory.setQuestionnaireId(null);
            CreateQuestionnaireFactory.save($scope.questionnaire,
                function() {
                    alert('Questionnaire was create successfully');
                    location.reload();
                },
                function(data) {
                    console.log(data)
                }
            );
        };
        $scope.deleteAnswer = function(answerIndex, questionIndex) {
            $scope.questionnaire.question_entities[questionIndex].answer_entities.splice(answerIndex, 1);
        };
        $scope.deleteQuestion = function(questionIndex) {
            $scope.questionnaire.question_entities.splice(questionIndex, 1);
        };
        $scope.addQuestion = function() {
            if ($scope.questionsLimit == $scope.questionnaire.question_entities.length) {
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
app.controller('StartQuestionnaireCtrl', ['$scope', '$location', '$cookieStore', 'PersonalQuestionnaireFactory',
    function($scope, $location, $cookieStore, PersonalQuestionnaireFactory) {
        $scope.startQDump = function(id) {
            if(_.isNull($cookieStore.get('user'))) {
                $location.path('');
            }
            PersonalQuestionnaireFactory.setQuestionnaireId(id);
            $location.path('/account/questionnaire');
        };
    }
]);
app.controller('PersonalQuestionnaireCtrl', ['$scope', '$location', '$cookieStore', 'PersonalQuestionnaireFactory', 'QuestionnaireFactory', 'PersonFactory',
    function($scope, $location, $cookieStore, PersonalQuestionnaireFactory, QuestionnaireFactory, PersonFactory) {
        $scope.personQuestionnaireEntity = {};
        if (PersonalQuestionnaireFactory.getPersonQuestionnaireId() == null) {
            $scope.personQuestionnaireEntity = {
                questionnaire_entity: {},
                person_question_entities: []
            };
            $scope.question_index = [];
            QuestionnaireFactory.get({personal: 'personal', id: PersonalQuestionnaireFactory.getQuestionnaireId()},
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
            PersonFactory.get_questionnaire({id: PersonalQuestionnaireFactory.getPersonQuestionnaireId()})
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
                $scope.personQuestionnaireEntity.status = 'in checking process';
                PersonFactory.create($scope.personQuestionnaireEntity);
                $location.path('/');
            }
        };
        $scope.save = function () {
            $scope.personQuestionnaireEntity.status = 'in progress';
            PersonFactory.create($scope.personQuestionnaireEntity);
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
app.controller('StartedPersonQuestionnaireCtrl', ['$scope', '$location', 'PersonQuestionnairesFactory',
    function($scope, $location, PersonQuestionnairesFactory) {
        $scope.continue = function(id) {
            PersonQuestionnairesFactory.setPersonQuestionnaireId(id);
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
        var type = questionnaire_type;
        var queryParams = null;
        $scope.questionnaires = [];
        $scope.getQuestionnaires = function() {
            switch (type.toUpperCase()) {
                case 'COMPLETED':
                    getCompleted();
                    break;
                case 'STARTED':
                    getStarted();
                    break;
                case 'PUBLISHED':
                    getPublished();
                    break;
                case 'ALL':
                    getAll();
                    break;
            }
        };
        var setParams = function() {
            queryParams = {
                page: $scope.currentPage - 1,
                size: $scope.currentPageSize.value,
                direction: $scope.currentQuestionnairesSorting.direction,
                sort: $scope.currentQuestionnairesSorting.type
            };
        };
        var getCompleted = function() {
            setParams();
            $scope.questionnaires = PersonQuestionnairesFactory.completed(queryParams);
            PersonQuestionnairesFactory.count_completed(function(data) {
                $scope.totalItems = data.count;
            });
        };
        var getStarted = function() {
            setParams();
            $scope.questionnaires = PersonQuestionnairesFactory.started(queryParams);
            PersonQuestionnairesFactory.count_started(function(data) {
                $scope.totalItems = data.count;
            });
        };
        var getPublished = function() {
            setParams();
            $scope.questionnaires = QuestionnaireFactory.get_published(queryParams);
            QuestionnaireFactory.count_published(function(data) {
                $scope.totalItems = data.count;
            })
        };
        var getAll = function() {
            setParams();
            $scope.questionnaires = QuestionnaireFactory.get_all(queryParams);
            QuestionnaireFactory.count_all(function(data) {
                $scope.totalItems = data.count;
            })
        };
        $scope.currentPage = 1;
        $scope.maxSize = 5;
        var questionnairesSorting = [
            {type: 'name', direction: 'DESC'},
            {type: 'createdDate', direction: 'DESC'}
        ];
        if(angular.equals(type.toUpperCase(), 'PUSBLISHED')) {
            questionnairesSorting.push({type: 'description', direction: 'DESC'});

        } else if(_.isEqual(type.toUpperCase(), 'ALL')) {
            questionnairesSorting.push({type: 'modifiedDate', direction: 'DESC'});
        } else {
            questionnairesSorting.push({type: 'status', direction: 'DESC'});
            questionnairesSorting.push({type: 'modifiedDate', direction: 'DESC'});

        }
        $scope.currentQuestionnairesSorting = questionnairesSorting[1];
        $scope.pageSize = [
            {label: '15', value: 15},
            {label: '25', value: 25},
            {label: '50', value: 50},
            {label: '100', value: 100}
        ];
        $scope.currentPageSize = $scope.pageSize[0];
        if($scope.currentPage == 1) {
            $scope.getQuestionnaires();
        }
        $scope.$watch(
            "currentPageSize.value",
            function(newValue, oldValue) {
                if(newValue === oldValue) return;
                $scope.getQuestionnaires();
            }
        );
        $scope.sorting = function(type){
            $scope.questionnairesSorting.forEach(function(element, index) {
                if(element.type == type) {
                    var previousQuestionnairesSorting = angular.copy($scope.currentQuestionnairesSorting);
                    $scope.currentQuestionnairesSorting = $scope.questionnairesSorting[index];
                    if(angular.equals($scope.currentQuestionnairesSorting, previousQuestionnairesSorting)) {
                        if($scope.currentQuestionnairesSorting.direction == 'ASC')
                            $scope.currentQuestionnairesSorting.direction = 'DESC';
                        else
                            $scope.currentQuestionnairesSorting.direction = 'ASC'
                    }
                }
            });
            $scope.getQuestionnaires();
        };
    }
]);
app.controller('AdminQuestionnaireCtrl', ['$scope', '$location', 'QuestionnaireFactory', 'PersonalQuestionnaireFactory',
    function($scope, $location, QuestionnaireFactory, PersonalQuestionnaireFactory) {
        $scope.delete = function(id) {
            alert('You really want to delete this questionnaire?');
            QuestionnaireFactory.delete_one({id: id});
        };
        $scope.edit = function(id) {
            PersonalQuestionnaireFactory.setQuestionnaireId(id);
            $location.path('/questionnaires/create');
        }
    }
]);

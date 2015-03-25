var app = angular.module('qdumpApp.controllers', ['ngCookies']);

app.controller('MainCtrl', ['$scope', '$cookieStore',
    function($scope, $cookieStore) {
        $scope.userIsAuth = $cookieStore.get('userAuth');
        $scope.adminPermission = $cookieStore.get('role') == 'ADMIN';
    }
]);
app.controller('AuthCtrl', ['$scope', '$cookieStore', '$location', '$window', 'AuthFactory', 'PersonFactory',
    function($scope, $cookieStore, $location, $window, AuthFactory, PersonFactory) {
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
        }
    }
]);
app.controller('LogOutCtrl', ['$scope', '$cookieStore', '$window', '$location', 'LogoutFactory', 'PersonFactory',
    function($scope, $cookieStore, $window, $location, LogoutFactory, PersonFactory) {
        $scope.logout = function() {
            PersonFactory.logout();
            $cookieStore.remove('userAuth');
            $cookieStore.remove('role');
            $location.path('/qdump');
            $window.location.reload()
        }
    }
])
app.controller('RegistrationCtrl', ['$scope', '$cookieStore', 'RegistrationFactory', '$location',
    function($scope, $cookieStore, RegistrationFactory, $location) {
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
        $scope.user = {}
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
app.controller('AccountCtrl', ['$scope', '$http', 'PersonFactory',
    function($scope, $http, PersonFactory) {
        $scope.user = PersonFactory.page(
            function() {
                $scope.isAuth = true;
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
                $scope.isAuth = false;
                $scope.errorData = data.data;
            }
        );
        $scope.templates = [{
            template: { menu: 'login/partials/float-menu.html'}
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
app.controller('CompletedCtrl', ['$scope', '$http', 'PersonFactory', 'CompletedFactory',
    function($scope, $http, PersonFactory, CompletedFactory) {
        $scope.completedQs = [];
        $scope.getCompleted = function(direction, sort) {
            var queryParams = {
                page: $scope.currentPage - 1,
                size: $scope.elementPerPage
            };
            if(direction != null) {
                queryParams['direction'] = direction;
            }
            if(sort != null) {
                queryParams['sort'] = sort;
            }
            $scope.completedQs = CompletedFactory.query(queryParams);
        };
        $http.get('/rest/questionnaires/pagination/count').success(
            function(data) {
                $scope.totalItems = data
            }).error(
            function(data) {
                console.log(data);
            });
        $scope.currentPage = 1;
        $scope.elementPerPage = 15;
        $scope.maxSize = 5;
        $scope.isEmpty = true;
        if($scope.currentPage = 1) {
            $scope.getCompleted(null, null);
            $scope.isEmpty = $scope.completedQs < 1
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
app.controller('CreateQuestionnaireCtrl', ['admin', '$scope', '$location', '$rootScope', '$cookieStore', '$compile', '$filter', '$document', 'CreateQuestionnaireFactory', 'ErrorFactory',
    function(admin, $scope, $compile, $rootScope, $cookieStore, $location, $filter, $document, QuestionnaireFactory, ErrorFactory) {
        console.log("Inside Controller");
        $scope.options = [
            {label: 'Select type of question...', value: ''},
            {label: 'One answer question.', value: 'RADIO'},
            {label: 'Multiple answer question.', value: 'CHECKBOX'},
            {label: 'Open question.', value: 'TEXTAREA'},
            {label: 'Select answer question.', value: 'SELECT'}
        ];
        $scope.type = $scope.options[0];
        $scope.questionsLimit = 15;
        $scope.answersLimit = 4;
        $scope.questionsCounter = 0;
        $scope.questionAnswersCounter = {};
        $scope.templates = [{
            template: {
                checkbox: 'login/partials/checkbox-question.html',
                radio: 'login/partials/radio-question.html',
                textarea: 'login/partials/textarea-question.html',
                select: 'login/partials/select-question.html'
            }
        }];
        $scope.template = $scope.templates[0].template;
        $scope.questionnaire = {
            question_entities: []
        };
        $scope.formQuestions = [];
        $scope.addAllInputs = function() {
            if ($scope.questionsLimit == $scope.questionsCounter) {
                alert("You can add only 15 questions");
            } else {
                switch ($scope.type.value) {
                    case 'RADIO' :
                        $scope.addQuestionEntities();
                        $scope.addQuestion($scope.template.radio);
                        break;
                    case 'CHECKBOX' :
                        $scope.addQuestionEntities();
                        $scope.addQuestion($scope.template.checkbox);
                        break;
                    case 'TEXTAREA' :
                        $scope.addQuestionEntities();
                        $scope.addQuestion($scope.template.textarea);
                        break;
                    case 'SELECT' :
                        $scope.addQuestionEntities();
                        $scope.addQuestion($scope.template.select);
                        break;
                    default :
                        break;
                }
            }
        };
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
        $scope.addQuestionEntities = function() {
            $scope.questionsCounter++;
            $scope.questionnaire.question_entities.push({
                question_id : $scope.questionsCounter - 1,
                question_type : $scope.type.value
            });
        };
        $scope.createQ = function() {
            QuestionnaireFactory.save($scope.questionnaire,
                function() {
                    alert('Questionnaire was create successfully');
                    location.reload();
                },
                function(data) {
                    console.log(data)
                }
            );
        };
        $scope.deleteAnswer = function(answerId, questionId) {
            $scope.questionnaire.question_entities.forEach(function(element, questionIndex) {
                if(element.question_id == questionId) {
                    element.answer_entities.forEach(function(element, answerIndex) {
                        if(element.answer_id == answerId) {
                            $scope.questionnaire.question_entities[questionIndex].answer_entities.splice(answerIndex, 1);
                        }
                    })
                }
            });
            $scope.formQuestions.forEach(function(element, questionIndex) {
                if(element.id == questionId) {
                    element.answers.forEach(function(element, answerIndex) {
                        if(element.id == answerId) {
                            $scope.formQuestions[questionIndex].answers.splice(answerIndex, 1);
                        }
                    })
                }
            });
            $scope.questionAnswersCounter[questionId]--;
        };
        $scope.deleteQuestion = function(questionId) {
            $scope.questionnaire.question_entities.forEach(function(element, index) {
                if(element.question_id == questionId) {
                    $scope.questionnaire.question_entities.splice(index, 1);
                }
            });
            $scope.formQuestions.forEach(function(element, index) {
                if(element.id == questionId) {
                    $scope.formQuestions.splice(index, 1);
                }
            });
            $scope.questionsCounter--;
        };
        $scope.addQuestion = function(questionAddress) {
            $scope.formQuestions.push({
                idType : 'question' + $scope.type.value + $scope.questionsCounter,
                idQuestion : 'question' + $scope.questionsCounter,
                id : $scope.questionsCounter-1,
                counter : $scope.questionsCounter,
                questionAddress : questionAddress
            });
            $scope.questionAnswersCounter[$scope.questionsCounter-1] = 0;
        };
        $scope.addAnswer = function(questionId) {
            if($scope.formQuestions[questionId].answers == null) {
                $scope.formQuestions[questionId]['answers'] = [];
            }
            $scope.formQuestions[questionId].answers.push({
                idAnswer : 'answer' + ($scope.questionAnswersCounter[$scope.formQuestions[questionId].id] + 1),
                id : $scope.questionAnswersCounter[$scope.formQuestions[questionId].id]
            });
            if($scope.questionnaire.question_entities[questionId].answer_entities == null) {
                $scope.questionnaire.question_entities[questionId]['answer_entities'] = []
            }
            $scope.questionnaire.question_entities[questionId].answer_entities.push({
                answer_id : $scope.questionAnswersCounter[$scope.formQuestions[questionId].id]
            });
            $scope.questionAnswersCounter[$scope.formQuestions[questionId].id]++;
            if($scope.questionnaire.question_entities[questionId].question_type == 'CHECKBOX'
                && $scope.questionAnswersCounter[questionId] == 1) {
                $scope.addAnswer(questionId);
            }
        };
        $scope.changeRadioAnswers = function(newCheckedId, questionId) {
            $scope.questionnaire.question_entities[questionId].answer_entities.forEach(
                function(element) {
                    if(element.answer_id == newCheckedId) element.correct = true;
                    else element.correct = false;
                }
            );
        };

        $scope.toConsole = function() {
            console.log(angular.toJson($scope.questionnaire, true));
            console.log(angular.toJson($scope.formQuestions, true));
        };
    }
]);
app.controller('QuestionnaireCtrl', ['$scope', '$http', '$location', '$cookieStore', 'QuestionnaireFactory', 'PersonalQuestionnaireFactory', 'PersonFactory',
    function($scope, $http, $location, $cookieStore, QuestionnaireFactory, PersonalQuestionnaireFactory, PersonFactory) {
        $scope.getQuestionnaires = function() {
            var queryParams = {
                page: $scope.currentPage - 1,
                size: $scope.currentPageSize.value,
                direction: $scope.currentQuestionnairesSorting.direction,
                sort: $scope.currentQuestionnairesSorting.type
            };
            $scope.questionnaires = QuestionnaireFactory.query(
                queryParams,
                function(data) {
                    $scope.questionnaires = data;
                }, function(data) {
                    console.log(data);
                });
        };
        $scope.questionnaires = [];
        //$scope.fillQuestionnaires = function() {
        //    for(var i = 1; i <= 100; i++) {
        //        $scope.questionnaires.push({
        //            name: 'Questionnaire name #'+i,
        //            description: 'Questionnaire description #'+i
        //        });
        //    }
        //};
        //$scope.fillQuestionnaires();
        //$scope.$watch('currentPage + maxSize', function() {
        //    var begin = (($scope.currentPage - 1) * $scope.maxSize),
        //        end = begin + $scope.maxSize;
        //    $scope.filteredQuestionnaires = $scope.questionnaires.slice(begin, end);
        //});
        $scope.startQDump = function(index) {
            if(!$cookieStore.get('userAuth')) {
                $location.path('');
            }
            PersonalQuestionnaireFactory.setQuestionnaireId($scope.questionnaires[index].id);
            $cookieStore.put('person-questionnaire-id', $scope.questionnaires[index].id);
            $location.path('/account/questionnaire');
        };
        $scope.deleteQDump = function(index) {

        };
        $http.get('/rest/questionnaires/pagination/count').success(
            function(data) {
                $scope.totalItems = data
            }).error(
            function(data) {
                console.log(data);
            });

        $scope.currentPage = 1;
        $scope.maxSize = 5;
        $scope.questionnairesSorting = [
            {type: 'name', direction: 'DESC'},
            {type: 'description', direction: 'DESC'},
            {type: 'createdDate', direction: 'DESC'}
        ];
        $scope.currentQuestionnairesSorting = $scope.questionnairesSorting[2];
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
app.controller('PersonalQuestionnaireCtrl', ['$scope', '$location', '$cookieStore', 'PersonalQuestionnaireFactory', 'QuestionnaireFactory', 'PersonFactory',
    function($scope, $location, $cookieStore, PersonalQuestionnaireFactory, QuestionnaireFactory, PersonFactory) {
        $scope.personQuestionnaireEntity = {
            questionnaire_entity: {
            },
            person_question_entities: []
        };
        $scope.question_index = [];
        QuestionnaireFactory.get(new Object({id: $cookieStore.get('person-questionnaire-id')}),
            function(data) {
                $scope.questionnaire = data;
                $scope.updatePersonQuestionnaireEntity = function() {
                    $scope.personQuestionnaireEntity.questionnaire_entity.id = $scope.questionnaire.id;
                    for(var i = 0; i < $scope.questionnaire.question_entities.length; i++) {
                        var question = $scope.questionnaire.question_entities[i];
                        var personQuestions = $scope.personQuestionnaireEntity.person_question_entities;
                        $scope.question_index.push(new Object({index: i, id: question.id}));
                        personQuestions.push(new Object({question_entity: question}));
                        personQuestions[i].person_answer_entities = [];
                        for(var j = 0; j < $scope.questionnaire.question_entities[i].answer_entities.length; j++) {
                            var answer = question.answer_entities[j];
                            var personAnswers = personQuestions[i].person_answer_entities;
                            personAnswers.push(new Object({answer_entity: answer}));
                            personAnswers[j].person_answer = null;
                            if(question.question_type == 'TEXTAREA') {
                                personAnswers.push(new Object({person_answer: null}))
                            }

                        }
                    }
                };
                $scope.updatePersonQuestionnaireEntity();

            }, function(data) {
                $cookieStore.put('error', data.data.error);
                $location.path('/error');
            }

        );
        $scope.selectedItem = {};
        $scope.selectChange = function(parentIndex) {
            var selectedAnswer = $scope.personQuestionnaireEntity.person_question_entities[parentIndex].person_answer_entities[$scope.selectedItem.answer];
            _.each($scope.personQuestionnaireEntity.person_question_entities[parentIndex].person_answer_entities, function(answer) {
                _.isEqual(selectedAnswer, answer) ? answer['marked'] = true : answer['marked'] = false;
            })
        };
        $scope.textareaQuestion = function(questionIndex) {
            var answer = $scope.personQuestionnaireEntity.person_question_entities[questionIndex].person_answer_entities[0];
            (answer.person_answer == null || answer.person_answer == '') ? answer.marked = false : answer.marked = true;

        };
        $scope.complete = function() {
            $scope.personQuestionnaireEntity.status = 'in checking process';
            PersonFactory.create($scope.personQuestionnaireEntity);
        };
        $scope.save = function() {
            $scope.personQuestionnaireEntity.status = 'in progress';
            PersonFactory.create($scope.personQuestionnaireEntity);
        };

        $scope.toConsole = function() {
            console.log(angular.toJson($scope.personQuestionnaireEntity, true))
        }
    }
]);
app.controller('ErrorCtrl', ['$scope', '$cookieStore',
    function($scope, $cookieStore) {
        $scope.error = $cookieStore.get('error');
    }
]);
app.controller('SuccessCtrl', ['$scope', '$cookieStore',
    function($scope, $cookieStore) {
        $scope.success = $cookieStore.get('success');
    }
]);

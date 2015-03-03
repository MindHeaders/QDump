var app = angular.module('qdumpApp.controllers', ['ngCookies']);

app.controller('AuthCtrl', ['$scope', '$cookieStore', '$location', '$window', 'AuthFactory',
    function($scope, $cookieStore, $location, $window, AuthFactory) {
        $scope.isFormSubmitted = false;
        $scope.auth = function() {
            $scope.isFormSubmitted = true;
            AuthFactory.auth($.param({
                login_or_email: $scope.user.login_or_email,
                password: $scope.user.password,
                rememberMe: $scope.rememberMe
            }), function() {
                $cookieStore.put('userAuth', true);
                $window.location.assign('login/index.html');
            }, function(data) {
                $cookieStore.put('userAuth', false);
                $scope.errorData = data.data;
                $scope.user.password = '';
            });
        }
    }
]);
app.controller('LogOutCtrl', ['$scope', '$cookieStore', '$window', 'LogoutFactory',
    function($scope, $cookieStore, $window, LogoutFactory) {
        $scope.logout = function() {
            LogoutFactory.logout();
            $cookieStore.remove('userAuth');
            $window.location.reload()
        }
    }
])
app.controller('RegCtrl', ['$scope', 'RegFactory', '$location',
    function($scope, RegFactory, $location) {
        $scope.firstnamePattern = /^[A-Z][a-zA-Z]*/i;
        $scope.lastnamePattern = /[a-zA-z]+([ '-][a-zA-Z]+)*/i;
        $scope.reg = function() {
            RegFactory.reg($scope.user,
                function(){
                    $location.redirectTo('/success');
                },
                function(data){
                    $location.redirectTo('/error');
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
app.controller('AccountCtrl', ['$scope', 'AccountFactory', 'UpdateFactory', 'ResetPasswordFactory',
    function($scope, AccountFactory, UpdateFactory, ResetPasswordFactory) {
        $scope.user = AccountFactory.page(
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
            template: { menu: 'login/float-menu.html'}
        }];
        $scope.template = $scope.templates[0].template;
        $scope.update = function(changedUser, user) {
            if(changedUser.email != user.email) {
                alert("You change your email. This changes will disabled your account. " +
                "If you confirm update your personal data you should to enabled " +
                "your account you may confirm it with url that we send to your new email.")
            }
            var isUpdate = confirm("Do you want to continue update user?");
            if(isUpdate) UpdateFactory.update(changedUser);
        };
        $scope.resetPassword = function() {
            var reset = confirm("Reset password: We will generate new password and send to your email. " +
            "Do you want to continue?");
            if(reset) ResetPasswordFactory.resetPassword();
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
app.controller('CompletedCtrl', ['$scope', 'CompletedFactory',
    function($scope, CompletedFactory) {
        $scope.getPage = function(page, size, direction, sort) {
            if(direction != $scope.directions[0].ASC && direction != $scope.directions[0].DESC) {
                direction = undefined;
            }
            if(sort == 'modifiedDate') return;
            else sort = undefined;
            $scope.completedQs = CompletedFactory.query(
                {page: page, size: size, direction: direction, sort: sort},
                function(data) {
                    $scope.empty = data.data.length > 0;
                    console.log(data);
                }, function(data) {
                    console.log(data);
                });
        };
        $scope.directions = [{
            ASC: 'ASC',
            DESC: 'DESC'
        }];
    }
]);
app.controller('CreateQuestionnaireCtrl', ['$scope', '$compile', '$filter', '$document', 'CreateQuestionnaireFactory',
    function($scope, $compile, $filter, $document, QuestionnaireFactory) {
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
                checkbox: 'login/checkbox-question.html',
                radio: 'login/radio-question.html',
                textarea: 'login/textarea-question.html',
                select: 'login/select-question.html'
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
                    if(element.answer_entities == null) {
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
            QuestionnaireFactory.save($scope.questionnaire);
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
            })
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
            })
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
                idAnswer : 'answer' + ($scope.questionAnswersCounter[questionId] + 1),
                id : $scope.questionAnswersCounter[questionId]
            });
            if($scope.questionnaire.question_entities[questionId].answer_entities == null) {
                $scope.questionnaire.question_entities[questionId]['answer_entities'] = []
            }
            $scope.questionnaire.question_entities[questionId].answer_entities.push({
                answer_id : $scope.questionAnswersCounter[questionId]
            });
            $scope.questionAnswersCounter[questionId]++;
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
app.controller('QuestionnaireCtrl', ['$scope', '$http', '$location', 'QuestionnaireFactory', 'PersonalQuestionnaireFactory',
    function($scope, $http, $location, QuestionnaireFactory, PersonalQuestionnaireFactory) {
        $scope.getQuestionnaires = function(direction, sort) {
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
            PersonalQuestionnaireFactory.setQuestionnaireId($scope.questionnaires[index].id);
            $location.path('/account/questionnaire');
            //QuestionnaireFactory.get({get: 'get', id: $scope.questionnaires[index].id},
            //    function(data) {
            //        console.log(data);
            //    }, function(data) {
            //        console.log(data);
            //    }
            //);
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
        if($scope.currentPage == 1) {
            $scope.getQuestionnaires(null, null);
        }
    }
]);
app.controller('PersonalQuestionnaireCtrl', ['$scope', 'QuestionnaireFactory', 'PersonalQuestionnaireFactory',
    function($scope, QuestionnaireFactory, PersonalQuestionnaireFactory) {
        console.log(PersonalQuestionnaireFactory.getQuestionnaireId());
    }
]);

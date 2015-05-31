var app = angular.module('account.controllers', ['ngCookies']);

app.controller('AccountCtrl', ['$modal', '$scope', '$http', '$cookieStore', '$location', '$window', 'PersonFactory', 'ErrorFactory', 'UpdateUserData', 'ResetPassword',
    function($modal, $scope, $http, $cookieStore, $location, $window, PersonFactory, ErrorFactory, UpdateUserData, ResetPassword) {
        $scope.user = PersonFactory.get({personal: 'personal'},
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
                ErrorFactory.setErrorMessage(data.data.error);
                $location.path('/error')
            }
        );
        $scope.templates = [{
            template: { menu: '/app/Account/float-menu.html'}
        }];
        $scope.template = $scope.templates[0].template;
        $scope.updateUser = function(user_id) {
            var modalInstance = $modal.open({
                templateUrl: '/app/Account/update-user-data.html',
                controller: 'UpdateUserCtrl',
                resolve: {
                    user: function() {
                        return PersonFactory.get({id: user_id});
                    }
                }
            });
            modalInstance.result.then(function(data) {
                if(_.isEqual(data, "success")) {
                    $route.reload();
                    alert('User data successfully updated');
                }
            }, function(data) {
                ErrorFactory.setErrorMessage(data.data.error);
            })
        };
        $scope.resetPassword = function() {
            var reset = confirm("Reset password: We will generate new password and send to your email. " +
            "Do you want to continue?");
            if(reset) ResetPassword.resetPassword();
        };
    }
]);
app.controller('AdminPanelCtrl', ['page_type', 'tab_number', '$timeout', '$route', '$resource', '$scope', '$location', '$cookieStore', '$modal', 'QuestionnaireFactory', 'PersonFactory', 'PersonQuestionnairesFactory', 'ErrorFactory', 'PersonQuestionFactory',
    function(page_type, tab_number, $timeout, $route, $resource, $scope, $location, $cookieStore, $modal, QuestionnaireFactory, PersonFactory, PersonQuestionnairesFactory, ErrorFactory, PersonQuestionFactory) {
        $scope.page_type = page_type;
        $timeout(function() {
            PersonQuestionFactory.get({get: 'get', checking: 'checking', count: 'count'}, function(data) {
                $scope.questionBadge = data.count;
            })
        }, 1800000);
        $scope.tabs = [
            {
                title: 'Edit Q`s',
                label: 'edit_questionnaires',
                template: '/app/Questionnaire/questionnaires-admin.html',
                name: 'Questionnaires Editor',
                dataSorting: null,
                active: false
            },
            {
                title: 'Users',
                label: 'edit_users',
                template: '/app/Account/users.html',
                name: 'User Editor',
                dataSorting: [
                    {type: 'login', direction: 'DESC'},
                    {type: 'email', direction: 'DESC'},
                    {type: 'personGroup', direction: 'DESC'},
                    {type: 'enabled', direction: 'DESC'}
                ],
                active: false
            },
            {
                title: 'Questions Ver.',

                label: 'questions_verification',
                template: '/app/Questionnaire/question-verification.html',
                name: 'Questions verification',
                dataSorting: null,
                active: false,
                badge: $scope.questionBadge

            },
            {
                title: 'Statistics',
                label: 'statistics',
                template: '/app/Additional/statistics.html',
                name: 'Project Statistics',
                active: false
            }
        ];
        $scope.tabs[tab_number].active = true;
        $scope.change_type = function(tab, index) {
            $scope.page_type = tab.label;
            $scope.currentTabName = tab.name;
            $scope.dataSorting = tab.dataSorting;
            sessionStorage.setItem("admin_panel_tab", index);
            $scope.getData();
        };
        $scope.dataSorting = $scope.tabs[0].dataSorting;
        $scope.getData = function(queryParams) {
            switch ($scope.page_type) {
                case $scope.tabs[0].label:
                    if($scope.questionnaires == null) {
                        getQuestionnaires(queryParams);
                    }
                    break;
                case $scope.tabs[1].label:
                    if($scope.users == null) {
                        getUsers(queryParams);
                    }
                    break;
                case $scope.tabs[2].label:
                    if($scope.personQuestions == null) {
                        getQuestion(queryParams);
                    }
                    break;
                case $scope.tabs[3].label:
                    if($scope.personQuestions == null) {
                        getStatistics();
                    }
            }
        };
        var getQuestionnaires = function(queryParams) {
            $scope.questionnaires = QuestionnaireFactory.query(queryParams);
            QuestionnaireFactory.get({count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            })
        };
        var getUsers = function(queryParams) {
            $scope.users = PersonFactory.query(queryParams);
            PersonFactory.get({count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            });
        };
        var getQuestion = function(queryParams) {
            $scope.personQuestions = PersonQuestionFactory.getNotChecked(queryParams);
            PersonQuestionFactory.get({checking: 'checking', count: 'count'}, function(data) {
                $scope.totalItems = data.count;
            })
        };
        var getStatistics = function() {
            PersonFactory.get({statistic: 'statistic'}, function(data) {
                $scope.personsStatistic = data;
            });
            QuestionnaireFactory.get({statistic: 'statistic'}, function(data) {
                console.log(data);
                $scope.questionnairesStatistic = data;
            });
            PersonQuestionnairesFactory.get({statistic: 'statistic'}, function(data) {
                $scope.personQuestionnairesStatistic = data;
            });
        };
        $scope.updateGroup = function(user) {
            var modalInstance = $modal.open({
                templateUrl: '/app/Account/update-user-group.html',
                controller: 'UpdateGroupCtrl',
                resolve: {
                    user: function() {
                        return user;
                    }
                }
            });
            modalInstance.result.then(function(data) {
                if(_.isEqual(data, "success")) {
                    $route.reload();
                    alert('User group successfully updated');
                }
            }, function(data) {
                ErrorFactory.setErrorMessage(data.data.error);
            })
        };
        $scope.updateUser = function(user_id) {
            var modalInstance = $modal.open({
                templateUrl: '/app/Account/update-user-data.html',
                controller: 'UpdateUserCtrl',
                resolve: {
                    user: function() {
                        return PersonFactory.get({id: user_id});
                    }
                }
            });
            modalInstance.result.then(function(data) {
                if(_.isEqual(data, "success")) {
                    $route.reload();
                    alert('User data successfully updated');
                }
            }, function(data) {
                ErrorFactory.setErrorMessage(data.data.error);
            })
        };
        $scope.delete = function(user) {
            var confirmed = confirm('Do you want to delete user with next credentials: \nlogin - ' + user.login + '\nemail - ' + user.email);
            if(confirmed) PersonFactory.delete({id: user.id}).$promise.then(
                function() {
                    $route.reload();
                }, function(data) {
                    ErrorFactory.setErrorMessage(data.data.error);
                });
        };
        $scope.verify = function(personQuestionId, personQuestionCorrect) {
            $resource('/rest/persons/questions/verify/:id',
                {id: personQuestionId, correct: personQuestionCorrect},
                {verify: {method: 'PUT'}}).verify(function() {
                    $route.reload();
                }, function(data) {
                    ErrorFactory.setErrorMessage(data.data.error);
                })
        };
        var checkAllStatus = true;
        $scope.checkAll = function() {
            _.each($scope.personQuestions, function(personQuestion) {
                personQuestion.correct = checkAllStatus;
            });
            checkAllStatus = !checkAllStatus;
        };
        $scope.verifyAll = function() {
            var confirmed = confirm('Are you sure you want to verify all persons questions?');
            if (confirmed) $resource('/rest/persons/questions/verify', {}, {verifyAll: {method: 'PUT', isArray: true}})
                .verifyAll($scope.personQuestions);
            $route.reload();
        }
    }
]);
app.controller('UpdateGroupCtrl', ['user', '$scope', '$modalInstance', '$resource',
    function(user, $scope, $modalInstance, $resource) {
        $scope.user = user;
        $scope.groups = ['ADMIN', 'USER'];
        $scope.currentGroup = $scope.user.person_group;
        $scope.newGroup = $scope.currentGroup;
        $scope.update = function() {
            if($scope.currentGroup != $scope.newGroup) {
                $resource('/rest/persons/update/:id', {id: $scope.user.id, group: $scope.newGroup},
                    {update: {method: 'PUT'}}).update(function() {
                        $modalInstance.close("success");
                    }, function(data) {
                        $modalInstance.dismiss(data);
                    });
            }
        };
        $scope.close = function() {
            $modalInstance.close("closed");
        }

    }
]);
app.controller('UpdateUserCtrl', ['user', '$scope', '$modalInstance', '$location', 'UpdateUserData', 'ErrorFactory',
    function(user, $scope, $modalInstance, $location, UpdateUserData, ErrorFactory) {
        user.$promise.then(
            function(user) {
                $scope.user = user;
                $scope.changedUser = angular.copy($scope.user);
            }, function(data) {
                ErrorFactory.setErrorMessage(data.data.error);
                $location.path('/error');
            });
        $scope.update = function() {
            if($scope.changedUser.email != $scope.user.email) {
                confirm("You change your email. This changes will disabled your account. " +
                "If you confirm update your personal data you should to enabled " +
                "your account with confirming it with url that we send to your new email.")
            }
            var isUpdate = confirm("Do you want to continue update user?");
            if(isUpdate) UpdateUserData.update($scope.changedUser, function() {
                $modalInstance.close("success");
            }, function(data) {
                $modalInstance.dismiss(data);
            });
        };
        $scope.reset = function() {
            $scope.changedUser = angular.copy($scope.user);
        };
        $scope.close = function() {
            $modalInstance.close();
        }
    }
]);


var app = angular.module('qdumpApp.controllers', []);

app.controller('AuthCtrl', ['$scope','AuthFactory', 
	function($scope, AuthFactory) {
		$scope.auth = function() {
			AuthFactory.auth($.param({
				login : $scope.user.login,
				password : $scope.user.password
			}), function(data, status, headers, config) {
				console.log('You log in')
				console.log(data)
				console.log(status)
				console.log(headers)
				console.log(config)
			}, function() {
				console.log('There is a problem with login or password')
			});
		}
}]);
app.controller('RegCtrl', ['$scope', 'RegFactory',
	function($scope, RegFactory) {
		$scope.reg = function() {
			RegFactory.reg($scope.user);
		}
}]);
app.controller('UserListCtrl', ['$scope', 'UsersFactory',
	function($scope, UsersFactory) {
		$scope.users = UsersFactory.query();
}]);
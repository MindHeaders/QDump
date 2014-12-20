var app = angular.module('qdumpApp.controllers', []);

app.controller('AuthCtrl', ['$scope', '$location', 'AuthFactory', 
	function($scope, $location, AuthFactory) {
		$scope.auth = function() {
			AuthFactory.auth($.param({
				login : $scope.user.login,
				password : $scope.user.password
			}), function() {
				$location.url('/welcome');
				console.log('You log in')
			}, function() {
				console.log(AuthFactory.log())
			});
		}
}]);
app.controller('RegCtrl', ['$scope', 'RegFactory',
	function($scope, RegFactory) {
		$scope.reg = function() {
			RegFactory.reg($scope.user, 
				function(data) {
					console.log(data)
			}, function(data) {
					console.log(data)
			});
		}
}]);
app.controller('UserListCtrl', ['$scope', 'UsersFactory',
	function($scope, UsersFactory) {
		$scope.users = UsersFactory.query();
}]);
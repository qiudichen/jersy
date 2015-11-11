var app = angular.module('myApp', []);

app.controller('TestCtrl', function($scope) {
	$scope.title = 'My Title';
	$scope.hello = 'world';
	$scope.item = {
		name: 'My Name',
		description: 'This description'
	};
});
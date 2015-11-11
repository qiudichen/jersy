var app = angular.module('myApp', []);

app.controller('TestCtrl', function($scope) {
	$scope.myData = {};
 	$scope.myData.showIt = true;
 	$scope.myData.switch = 3;
});
//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

//Create the controller and register to the angular module using the controller.
app.controller('MainCtrl', function($scope) {
	$scope.message = 'hello main';

	//updateMessage
	$scope.updateMessage = function(message){
	    $scope.message = message;
  	};
});

//Create a child controller
app.controller('childCtrl', function($scope) {
	$scope.message = 'hello child';

	//updateMessage
	$scope.updateMessage = function(message){
	    $scope.message = message;
  	};
});
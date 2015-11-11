//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

//Create the controller and register to the angular module using the controller.
app.controller('firstController', function($scope) {
	// Initialize the model variables
	  $scope.firstName = "John";
	  $scope.lastName = "Doe";

	  // Define utility functions
	  $scope.getFullName = function ()
	  {
		return $scope.firstName + " " + $scope.lastName;
	  };
});

//Create a child controller
app.controller('secondController', function($scope) {
// Initialize the model variables
  $scope.firstName = "Bob";
  $scope.middleName = "Al";
  $scope.lastName = "Smith";

  // Define utility functions
  $scope.getFullName = function ()
  {
    return $scope.firstName + " " + $scope.middleName + " " + $scope.lastName;
  };
});
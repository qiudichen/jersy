//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

//Create the controller and register to the angular module using the controller.
app.controller('MainCtrl', function($scope) {
	$scope.myData = {};
	$scope.myData.items = [{ v: "1"}, { v: "2"}, { v : "3"} ];

	//
	$scope.myData.doClick = function() {
		alert("clicked");
	};

	$scope.myData.doClickEvent = function(event) {
		alert("clicked: " + event.clientX + ", " + event.clientY);
	};

    $scope.onSecondBtnClick = function (value, event) {
     	$scope.onSecondBtnClickResult = "you typed '" + value + "', the position is {" + event.clientX + ", " + event.clientY + "}";
    };

	$scope.myData.doClickItem = function(item, event) {
		alert("clicked: " + item.v + " @ " + event.clientX + ": " + event.clientY);
	};

});
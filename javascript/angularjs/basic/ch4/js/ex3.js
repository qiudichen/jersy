//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

//Create the controller and register to the angular module using the controller.
app.controller('MainCtrl', function($scope) {
// Initialization
    $scope.onKeyDownResult = "";
    $scope.onKeyUpResult = "";
    $scope.onKeyPressResult = "";

    // Utility functions

    var getKeyboardEventResult = function (keyEvent, keyEventDesc)
    {
		var msg = keyEventDesc + " keyEvent : {" +
			"altKey : " + keyEvent.altKey + ", " +
		"ctrlKey : " + keyEvent.ctrlKey + ", " +
		"shiftKey : " + keyEvent.shiftKey + ", " +
		"repeat : " + keyEvent.repeat + ", " +
		"type : " + keyEvent.type + ", " +
		"which : " + keyEvent.which + ", " +
		"charCode : " + keyEvent.charCode + ", " +
		"code : " + keyEvent.code + ", " +
		"key : " + keyEvent.key + ", " +
		"keyCode : " + keyEvent.keyCode + "}";
		return msg;
    };

    // Event handlers
    $scope.onKeyDown = function ($event) {
      $scope.onKeyDownResult = getKeyboardEventResult($event, "Key down");
    };

    $scope.onKeyUp = function ($event) {
      $scope.onKeyUpResult = getKeyboardEventResult($event, "Key up");
    };

    $scope.onKeyPress = function ($event) {
      $scope.onKeyPressResult = getKeyboardEventResult($event, "Key press");
    };

});
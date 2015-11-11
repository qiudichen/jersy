var app = angular.module('app', []);

//using controller as syntax, replace $scope with this
app.controller('MainCtrl', function() {
	this.message = 'hello main';

	this.changeMessage  = function(message){
	    this.message = message;
  	};
});


app.controller('childCtrl', function() {
	this.message = 'hello child';

	this.changeMessage  = function(message){
	    this.message = message;
  	};
});
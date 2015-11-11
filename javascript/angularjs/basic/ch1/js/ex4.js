var app = angular.module('myApp', []);

app.controller('MainCtrl', function($scope) {
	$scope.setCurrentItem = function(item) {
		$scope.currentItem = item;
	};

	$scope.hello = 'world';
	$scope.item = {
		name: 'Single Item',
		description: 'Some description'
	};

	$scope.items = [
       {
         name: 'First Item',
         description: 'Some description'
       },
       {
         name: 'Second Item',
         description: 'Some description'
       },
       {
         name: 'Third Item',
         description: 'Some description'
       }
     ];
});
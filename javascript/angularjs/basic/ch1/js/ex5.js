var app = angular.module('myApp', []);

app.controller('MainCtrl', function($scope) {
	$scope.toggleVisibility = function(item) {
		item.visible = !item.visible;
	};

	$scope.items = [
       {
         name: 'First Item',
         description: 'Some description',
		 visible: true
       },
       {
         name: 'Second Item',
         description: 'Some description',
		 visible: true
       },
       {
         name: 'Third Item',
         description: 'Some description',
		 visible: true
       }
     ];
});
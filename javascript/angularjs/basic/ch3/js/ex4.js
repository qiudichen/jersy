var app = angular.module('app', []);

//Angular does not use eval(), alert(), and confirm(). And Angular expressions do not have access to global
//variables like window, document or location.
//Instead use service like $window And $location
app.controller('MainCtrl', ['$window', '$scope', function($window, $scope) {
  $scope.name = 'World';

  $scope.greet = function() {
    $window.alert('Hello ' + $scope.name);
  };
}]);
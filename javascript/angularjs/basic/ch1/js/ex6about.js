var app = angular.module('myApp');

app.controller('aboutCtrl',
    function aboutCtrl ($scope) {
      'use strict';
      $scope.about = {
        title: "About Me - ui-router"
      };
	});
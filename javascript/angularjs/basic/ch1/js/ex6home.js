var app = angular.module('myApp');

app.controller('homeCtrl',
    function homeCtrl ($scope) {
      'use strict';
      $scope.home = {
        title: "My Home - ui-router"
      };
	});
var app = angular.module('myApp');

app.controller('contactCtrl',
    function contactCtrl ($scope) {
      'use strict';
      $scope.contact = {
        title: "My Contact - ui-router"
      };
	});
var app = angular.module('myApp', ['ngRoute']);

app.config(function($routeProvider) {
		'use strict';
	  $routeProvider
		.when('/home', {
		  templateUrl: 'home.tmpl.html',
		  controller: 'homeCtrl'
		})
		.when('/about', {
		  templateUrl: 'about.tmpl.html',
		  controller: 'aboutCtrl'
		})
		.when('/contact', {
		  templateUrl: 'contact.tmpl.html',
		  controller: 'contactCtrl'
		})
		.otherwise({
		  redirectTo: '/home'
		});
});
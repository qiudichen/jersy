var app = angular.module('myApp', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider
		.state('home', {
			url: "/home",
			templateUrl: "home.tmpl.html",
			controller: 'homeCtrl'
		})
		.state('about', {
			url: "/about",
			templateUrl: "about.tmpl.html",
			controller: 'aboutCtrl'
		})
		.state('contact', {
			url: "/contact",
			templateUrl: "contact.tmpl.html",
			controller: 'contactCtrl'
		});

	 $urlRouterProvider.otherwise("/home");
});
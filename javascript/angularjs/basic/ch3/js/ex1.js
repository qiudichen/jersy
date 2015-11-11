var app = angular.module('app', []);

//create a service called messageSvc
app.factory('messageSvc', function() {
	//create an empty object
	var messages = {};

	//create a property list on messages, and set it to an empty array
	messages.list = [{id: 0, text: 'init'}];

	//create a function add on messages that will add provided messages to the list
	messages.add = function(message) {
		messages.list.push({id: messages.list.length, text: message});
	};
	return messages;
});

//create a service called batchLog with its own dependencies
app.factory('batchLog', ['$interval', '$log', function($interval, $log) {
  var messageQueue = [];

  function log() {
    if (messageQueue.length) {
      $log.log('batchLog messages: ', messageQueue);
      messageQueue = [];
    }
  }

  // start periodic checking
  $interval(log, 50000);

  return function(message) {
    messageQueue.push(message);
  }
}]);


app.config(['$provide', function($provide) {
  $provide.factory('serviceId', function(batchLog) {
    var shinyNewServiceInstance = {};

    shinyNewServiceInstance.add = function(message) {
		batchLog(message);
	};
    return shinyNewServiceInstance;
  });
}]);

//Create a controller called ListCtrl that injects messageSvc,
//and exposes the list from our service to the view.
app.controller('ListCtrl', function (messageSvc){
  var self = this;
  self.messages = messageSvc.list;
});

//Create a controller named PostCtrl that also injects our messages service.
//This controller will also contain an addMessage function that uses the add
//function we made in our service.
app.controller('PostCtrl', function (messageSvc, batchLog, serviceId){
  var self = this;

  self.addMessage = function(message){
	//batchLog(message);
	serviceId.add(message);
    messageSvc.add(message);
  };
});

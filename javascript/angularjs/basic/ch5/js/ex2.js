//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

function getData($timeout, $q) {
  return function() {
      // simulated async function
      return $q(function(resolve, reject) {
        $timeout(function() {
          resolve(Math.floor(Math.random() * 10))
        }, 2000)
      });
  }
}


app.factory('getData', getData).run(function(getData) {
  var promise = getData().then(function(num) {
		console.log(num)
		console.log('Finished at:', new Date());
		return num * 2
    }).then(function(num) {
		console.log(num) // = random number * 2;
		console.log('Finished at:', new Date());
	})
})


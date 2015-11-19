
//The first parameter of angular.module define the name of the module.
//The second parameter is an array that declares the module dependencies that the module uses.
var app = angular.module('app', []);

function getData($timeout, $q) {
  return function() {
    var defer = $q.defer()
    // simulated async function
    $timeout(function() {
      if(Math.round(Math.random())) {
        defer.resolve('data received!')
      } else {
        defer.reject('oh no an error! try again')
      }
    }, 2000)
    return defer.promise
  }
}


app.factory('getData', getData).run(function(getData) {
  var promise = getData().then(function(string) {
      console.log(string);
    }, function(error) {
      console.log(error);
    }).finally(function() {
      console.log('Finished at:', new Date());
    });
})

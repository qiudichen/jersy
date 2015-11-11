var app = angular.module('myApp', []);

app.filter('myFilter', function() {
		return function(stringValue) {
			return stringValue.substring(0,3);
		}
});

app.controller('TestCtrl', function($scope) {
	$scope.title = 'My Title';
	$scope.hello = 'world';

	$scope.itemFilter = function(item) {
		if(item.info == 'info two')
			return false;
		return true;
	};

	$scope.filterArray = function(item) {
		if(item.info == 'info two') return false;
			return true;
	}


	$scope.items = [
       {
         name: 'First Item',
         info: 'info one',
         description: 'Some description'
       },
       {
         name: 'Second Item',
         info: 'info two',
         description: 'Some description'
       },
       {
         name: 'Third Item',
         info: 'info three',
         description: 'Some description'
       },
       {
         name: 'Four Item',
         info: 'info four',
         description: 'Some description'
       },
       {
         name: 'Five Item',
         info: 'info five',
         description: 'Some description'
       }
     ];

     $scope.orders = [
            {
                UserName: 'John.Doe@test.ca',
                Users: [
                    {
                        MarketID: 'TOR-TVX',
                        Limits: [
                            {
                              SingleOrderLimit: '7,500,000',
                              DoubleOrderLimit: '1,000,000'
                            },
                            {
                              SingleOrderLimit: '2,000,000',
                              DoubleOrderLimit: '3,000,000'
                            }
                        ]
                    },
                    {
                        MarketID: 'TOR-TSX',
                        Limits: [
                            {
                              SingleOrderLimit: '7,500,000',
                              DoubleOrderLimit: '1,000,000'
                            },
                            {
                              SingleOrderLimit: '2,000,000',
                              DoubleOrderLimit: '3,000,000'
                            }
                        ]
                    }
                ]
            },
            {
                UserName: 'Jane.Doe@test.ca',
                Users: [
                    {
                        MarketID: 'TOR-TSU',
                        Limits: [
                            {
                              SingleOrderLimit: '7,500,000',
                              DoubleOrderLimit: '1,000,000'
                            },
                            {
                              SingleOrderLimit: '2,000,000',
                              DoubleOrderLimit: '3,000,000'
                            }
                        ]
                    },
                    {
                        MarketID: 'TOR-TVU',
                        Limits: [
                            {
                              SingleOrderLimit: '7,500,000',
                              DoubleOrderLimit: '1,000,000'
                            },
                            {
                              SingleOrderLimit: '2,000,000',
                              DoubleOrderLimit: '3,000,000'
                            }
                        ]
                    }
                ]
            }
        ];
});
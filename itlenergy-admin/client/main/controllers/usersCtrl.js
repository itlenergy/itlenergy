(function(angular){
  "use strict";

  angular.module('main')
    .config([
      '$routeProvider',
      function ($routeProvider) {
        $routeProvider
          .when('/users', {controller: 'usersCtrl', templateUrl: 'main/users'});
      }
    ])

    .controller('usersCtrl', ['$scope', 'apUserCollection',
      function($scope, apUserCollection){

        apUserCollection.find().success(function (data) {
          $scope.users = data;
        });

      }])

    ;
})(angular);
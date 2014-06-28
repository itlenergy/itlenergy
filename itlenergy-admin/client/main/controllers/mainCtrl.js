(function(angular){
  "use strict";

  angular.module('main')
    .config([
      '$routeProvider',
      function ($routeProvider) {
        $routeProvider
          .when('/', {controller: 'mainCtrl', templateUrl: 'main/main'});
      }
    ])

    .controller('mainCtrl', ['$scope',
      function($scope){

      }])

    ;
})(angular);
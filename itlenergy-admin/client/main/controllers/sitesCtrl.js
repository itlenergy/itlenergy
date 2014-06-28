(function(angular){
  "use strict";

  angular.module('main')
    .config([
      '$routeProvider',
      function ($routeProvider) {
        $routeProvider
          .when('/sites', {controller: 'sitesCtrl', templateUrl: 'main/sites'});
      }
    ])

    .controller('sitesCtrl', ['$scope', 'apSiteCollection',
      function($scope, apSiteCollection){

        apSiteCollection.find().success(function (data) {
          $scope.sites = data;
        });

      }])

    ;
})(angular);
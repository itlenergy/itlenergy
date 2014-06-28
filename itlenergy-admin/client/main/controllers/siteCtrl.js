(function(angular){
  "use strict";

  angular.module('main')
    .config([
      '$routeProvider',
      function ($routeProvider) {
        $routeProvider
          .when('/sites/:id', {controller: 'siteCtrl', templateUrl: 'main/site'});
      }
    ])

    .controller('siteCtrl', ['$scope', '$routeParams', 'apSiteCollection', '$location', 'messagebox',
      function($scope, $routeParams, apSiteCollection, $location, messagebox){
        var id = $routeParams.id;

        $scope.save = function () {
          if ($scope.isnew) {
            apSiteCollection.add($scope.site).success(function () {
              $location.path('/sites');
            });
          } else {
            apSiteCollection.update($scope.site).success(function () {
              $location.path('/sites');
            });
          }
        };

        $scope.del = function () {
          var d = messagebox.open('Are you sure you want to delete this site?', 'Confirm');
          d.result.then(function (result) {
            if (result === true) {
              apSiteCollection.remove(id).then(function () {
                $location.path('/sites');
              });
            }
          });
        };

        $scope.cancel = function () {
          $location.path('/sites');
        };

        if (!id) {
          $scope.title = 'Add site';
          $scope.isnew = true;
        } else {
          $scope.title = 'Edit site';
          $scope.isnew = false;
          apSiteCollection.get(+id).success(function (data) {
            $scope.site = data;
          });
        }
      }])

    ;
})(angular);
(function(angular){
  "use strict";

  angular.module('main')
    .config([
      '$routeProvider',
      function ($routeProvider) {
        $routeProvider
          .when('/users/:id', {controller: 'userCtrl', templateUrl: 'main/user'});
      }
    ])

    .controller('userCtrl', ['$scope', '$routeParams', 'apUserCollection', '$location', 'messagebox',
      function($scope, $routeParams, apUserCollection, $location, messagebox){

        $scope.roleHints = [
          'admin',
          'hub',
          'weather-reporter',
          'reader',
          'home-user'
        ];

        var id = $routeParams.id;

        $scope.save = function () {
          if ($scope.isnew) {
            apUserCollection.add($scope.user).success(function () {
              $location.path('/users');
            });
          } else {
            apUserCollection.update($scope.user).success(function () {
              $location.path('/users');
            });
          }
        };

        $scope.del = function () {
          var d = messagebox.open('Are you sure you want to delete this user?', 'Confirm');
          d.result.then(function (result) {
            if (result === true) {
              apUserCollection.remove(id).then(function () {
                $location.path('/users');
              });
            }
          });
        };

        $scope.pwd = function () {
          var d = messagebox.open('Please type a new password:', 'Change password for ' + $scope.user.username, true, true);
          d.result.then(function (result) {
            apUserCollection.changePassword(id, result);
          });
        };

        $scope.cancel = function () {
          $location.path('/users');
        };

        if (!id) {
          $scope.title = 'Add user';
          $scope.isnew = true;
        } else {
          $scope.title = 'Edit user';
          $scope.isnew = false;
          apUserCollection.get(+id).success(function (data) {
            $scope.user = data;
          });
        }
      }])

    ;
})(angular);
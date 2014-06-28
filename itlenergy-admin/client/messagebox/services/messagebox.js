(function(angular){
  "use strict";

  var messageboxCtrl = ['$scope', '$modalInstance', 'config', function ($scope, $modalInstance, config) {
    $scope.title = config.title;
    $scope.text = config.text;
    $scope.input = !!config.input;
    $scope.inputType = config.password ? 'password' : 'text';
    $scope.form = {};

    $scope.ok = function () {
      if ($scope.input) {
        $modalInstance.close($scope.form.inputValue);
      } else {
        $modalInstance.close(true);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss(false);
    };
  }];

  angular.module('messagebox')
    .factory('messagebox', ['$modal', '$q', function ($modal) {
      return {
        open: function messagebox_open(text, title, input, password) {
          return $modal.open({
            templateUrl: 'messagebox/messagebox',
            controller: messageboxCtrl,
            resolve: {
              config: function () {
                return {
                  text: text,
                  title: title,
                  input: input,
                  password: password
                };
              }
            }
          });
        }
      };
    }]);
})(angular);
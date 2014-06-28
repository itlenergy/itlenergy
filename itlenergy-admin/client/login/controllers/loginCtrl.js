(function(angular){
  "use strict";

  angular.module('login')
    .controller('loginCtrl', ['$scope', '$rootScope', 'dialog', 'apAuthentication'],
      function ($scope, $rootScope, dialog, apAuthentication)
      {
        $scope.errorMessage = '';

        $scope.login = function ()
        {
          apAuthentication.newTicket($scope.username, $scope.password)
            .success(function (data, status, error, config)
            {
              if (data.success === true)
              {
                $rootScope.$broadcast('event:loginSuccess');
                dialog.close();
              }
              else
              {
                $scope.errorMessage = 'Invalid username or password.';
              }
            })
            .error(function (data, status, error, config)
            {
              $scope.errorMessage = 'Error logging in.';
            });
        };
      });
})(angular);
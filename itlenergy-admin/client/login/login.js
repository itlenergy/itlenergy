(function(angular){
  "use strict";

  // based on https://github.com/stewartml/angular-auth by Stewart Mackenzie-Leigh

  var loginCtrl = ['$scope', '$rootScope', '$modalInstance', 'apAuthentication',
    function ($scope, $rootScope, $modalInstance, apAuthentication)
    {
      $scope.errorMessage = '';
      $scope.form = {};

      $scope.login = function ()
      {
        apAuthentication.newTicket($scope.form.username, $scope.form.password)
          .success(function (data, status, error, config)
          {
            $rootScope.$broadcast('loginSuccess', data.ticket);
            $modalInstance.close();
          })
          .error(function (data, status, error, config)
          {
            if (status === 403) {
              $scope.errorMessage = 'Invalid username or password.';
            } else {
              $scope.errorMessage = 'Error logging in (' + status + ')';
            }
          });
      };
    }];

  angular
    .module('login', ['ui.bootstrap'])
    .config(['$httpProvider', function ($httpProvider)
    {
      $httpProvider.responseInterceptors.push([
        '$rootScope', '$q',

        function (scope, $q)
        {
          function login_success(response)
          {
            return response;
          }

          function login_error(response)
          {
            // don't match log in attempts
            if (response.status === 403  && response.config.url.indexOf('auth/login') < 0)
            {
              var deferred = $q.defer();

              scope.failedRequests.push({
                config: response.config,
                deferred: deferred
              });

              scope.$broadcast('loginRequired');
              return deferred.promise;
            }
            else
            {
              return $q.reject(response);
            }
          }

          return function (promise)
          {
            return promise.then(login_success, login_error);
          };
        }
      ]);
    }])
    .run(['$rootScope', '$modal', '$http', 'apAuthentication', function login_run($rootScope, $modal, $http, apAuthentication)
    {
      $rootScope.failedRequests = [];
      $rootScope.loggedIn = false;
      $rootScope.user = '';

      $rootScope.$on('loginRequired', function login_onLoginRequired()
      {
        $modal.open({
          templateUrl: 'login/dialog',
          controller: loginCtrl,
          backdrop: 'static'
        });
      });

      $rootScope.$on('authRenewRequired', function login_onAuthRenewRequired() {
        apAuthentication.renewTicket();
      });

      $rootScope.$on('loginSuccess', function login_onLoginSuccess(e, ticket)
      {
        function resolve(request) {
          return function (response) {
            request.deferred.resolve(response);
          };
        }

        for(var i = 0; i < $rootScope.failedRequests.length; i++)
        {
          var request = $rootScope.failedRequests[i];
          if (request.config.params === undefined) { request.config.params = {}; }
          request.config.params.sgauth = ticket;
          $http(request.config).then(resolve(request));
        }

        $rootScope.failedRequests = [];
      });
    }]);
})(angular);
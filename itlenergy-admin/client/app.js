(function(angular){
  "use strict";

  angular.module('app', ['ui.bootstrap', 'app-templates', 'main', 'login', 'apatsche-api'])
    .config(['apConnectionProvider', function (apConnectionProvider) {
      apConnectionProvider.endpoint = 'http://itl.stugo.co.uk/api';
    }]);
})(angular);

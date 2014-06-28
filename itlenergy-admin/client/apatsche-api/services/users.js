(function(angular){
  "use strict";

  function createService(RestService) {

    function ApUserCollection(connection) {
      this.connection = connection;
      this.url = 'users';
    }

    ApUserCollection.prototype = RestService.prototype;

    ApUserCollection.prototype.changePassword = function (id, password) {
      return this.connection.post([this.url, id, 'password'], {password: password});
    };

    return ApUserCollection;
  }

  angular.module('apatsche-api')
    .factory('apUserCollection', ['apConnection', 'RestService', function (apConnection, RestService){
      var Service = createService(RestService);
      return new Service(apConnection);
    }]);

})(angular);
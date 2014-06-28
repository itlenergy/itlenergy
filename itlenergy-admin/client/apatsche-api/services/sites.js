(function(angular){
  "use strict";

  function createService(RestService) {

    function ApSiteCollection(connection) {
      this.connection = connection;
      this.url = 'sites';
    }


    ApSiteCollection.prototype = RestService.prototype;

    return ApSiteCollection;
  }

  angular.module('apatsche-api')
    .factory('apSiteCollection', ['apConnection', 'RestService', function (apConnection, RestService){
      var Service = createService(RestService);
      return new Service(apConnection);
    }]);

})(angular);
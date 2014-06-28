(function(angular){
  "use strict";

  function RestService() {
  }


  /**
   * Gets all values from a resource.
   */
  RestService.prototype.find = function () {
    var promise = this.connection.get(this.url);
    var fn = promise.success;
    // monkey patch success function
    promise.success = function (callback) {
      fn.call(promise, function (data, status, headers, config) {
        callback(data.items, status, headers, config);
      });
    };
    return promise;
  };


  /**
   * Gets one value from a resource.
   */
  RestService.prototype.get = function (id) {
    return this.connection.get([this.url, id]);
  };


  /**
   * Updates a value in a resource.
   */
  RestService.prototype.update = function (data) {
    return this.connection.put([this.url], data);
  };


  /**
   * Adds a new value or values to a resource.
   */
  RestService.prototype.add = function (data) {
    if (Array.isArray(data)) {
      return this.connection.put([this.url], { items: data });
    } else {
      return this.connection.post([this.url], data);
    }
  };


  /**
   * Removes one value from a resource.
   */
  RestService.prototype.remove = function (id) {
    return this.connection['delete']([this.url, id]);
  };


  angular.module('apatsche-api')
    .factory('RestService', ['apConnection', '$q', function (apConnection, $q){
      return RestService;
    }]);

})(angular);
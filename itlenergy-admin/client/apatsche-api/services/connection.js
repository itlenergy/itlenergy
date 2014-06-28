(function(angular){
  "use strict";

  /**
   * Abstracts connecting to the web service.
   */
  function ApConnection(endpoint, $http, cookies, $rootScope) {
    // normalise to end with a '/'
    this.endpoint = endpoint.replace(/\/?$/, '/');
    this.$http = $http;
    this.ticket = false;
    this.cookies = cookies;
    this.$rootScope = $rootScope;
  }


  /*function isHttpError(status) {
    var str = "" + status;
    if (str.length !== 3) { return null; } // falsey; don't know what this code is
    return +str[0] < 4;
  }*/


  /**
   * Performs a request to a REST resource.
   */
  ApConnection.prototype.sendRequest = function (method, resource, data, headers) {
    var self = this;
    if (Array.isArray(resource)) {
      resource = resource.join('/');
    }
    resource = self.endpoint + resource;
    var config = {method: method, url: resource};
    if (data !== undefined) { config.data = data; }
    if (headers !== undefined) { config.headers = headers; }
    self.configTicket(config);

    // returns promise object.
    return self.$http(config).success(
      function (data, status) {
        self.$rootScope.connectionError = false;
      }).error(
      function (data, status) {
        self.$rootScope.connectionError = true;
      });
  };


  /**
   * Sets the ticket in the config object if available.
   */
  ApConnection.prototype.configTicket = function (config) {
    if (config.params === undefined) {
      config.params = {};
    }
    if (this.ticket) {
      config.params.sgauth = this.ticket;
    } else {
      this.ticket = config.params.sgauth = this.cookies('sgauth');
    }
    if (this.expires && this.expires instanceof Date) {
      var now = new Date();
      if (now.getTime() - this.expires.getTime() >= 10*60*1000) {
        this.$rootScope.broadcast('authRenewRequired');
      }
    }
  };


  /**
   * Performs a GET request to a REST resource.
   */
  ApConnection.prototype.get = function (resource, headers) {
    return this.sendRequest("GET", resource, headers);
  };


  /**
   * Performs a POST request to a REST resource.
   */
  ApConnection.prototype.post = function (resource, data, headers) {
    return this.sendRequest("POST", resource, data, headers);
  };


  /**
   * Performs a PUT request to a REST resource.
   */
  ApConnection.prototype.put = function (resource, data, headers) {
    return this.sendRequest("PUT", resource, data, headers);
  };


  /**
   * Performs a DELETE request to a REST resource.
   */
  ApConnection.prototype['delete'] = function (resource, headers) {
    return this.sendRequest("DELETE", resource, headers);
  };


  /**
   * Sets the authentication ticket value.
   */
  ApConnection.prototype.setTicket = function (ticket, expiry) {
    this.cookies('sgauth', ticket);
    this.ticket = ticket;
    this.expiry = expiry;
  };


  /**
   * Gets the value of the authentication ticket.
   */
  ApConnection.prototype.getTicket = function () {
    return this.ticket;
  };


  // make the service available.
  angular.module('apatsche-api')
    .provider('apConnection', [function (){
      this.endpoint = '/api/';

      this.$get = ['$http', 'apCookies', '$rootScope', function ($http, apCookies, $rootScope) {
        return new ApConnection(this.endpoint, $http, apCookies, $rootScope);
      }];
    }]);
})(angular);
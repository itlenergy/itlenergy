(function(angular){
  "use strict";

  function ApAuthentication(connection, $q) {
    this.connection = connection;
    this.$q = $q;
  }


  /**
   * Sends authentication info to the server and gets a new ticket.
   */
  ApAuthentication.prototype.newTicket = function (username, password) {
    var self = this;
    return self.connection.post(['auth', 'login'], {username: username, password: password})
      .success(function (data) {
        self.connection.setTicket(data.ticket, data.expires);
      });
  };


  /**
   * Sends authentication info to the server and gets a new ticket.
   */
  ApAuthentication.prototype.renewTicket = function () {
    var self = this;
    return self.connection.get(['auth', 'renew'])
      .success(function (data) {
        self.connection.setTicket(data.ticket, data.expires);
      });
  };


  angular.module('apatsche-api')
    .factory('apAuthentication', ['apConnection', '$q', function (apConnection, $q){
      return new ApAuthentication(apConnection, $q);
    }]);

})(angular);
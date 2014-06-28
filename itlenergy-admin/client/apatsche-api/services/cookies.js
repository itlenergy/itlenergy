/*global document:false*/
(function(angular){
  "use strict";


  angular.module('apatsche-api')
    .factory('apCookies', [function (){
      return function (name, value, expires, path, domain, secure) {
        // from http://www.sitepoint.com/how-to-deal-with-cookies-in-javascript/
        if (arguments.length === 1) {
          /*** GET COOKIE ***/
          var regexp = new RegExp("(?:^" + name + "|;\\s*"+ name + ")=(.*?)(?:;|$)", "g");
          var result = regexp.exec(document.cookie);
          return (result === null) ? null : result[1];
        } else {
          /*** SET COOKIE ***/
          var cookie = '';

          if (typeof value === 'object') {
            cookie += angular.toJson(value);
          } else {
            cookie += value;
          }

          cookie += ';';

          if (expires && expires instanceof Date && !isNaN(expires.getTime())) {
            cookie += 'expires=' + expires.toUTCString() + ';';
          }

          // default to all paths
          cookie += 'path=' + (path ? path : '/') + ';';

          if (domain) {
            cookie += 'domain=' + domain + ';';
          }

          if (secure) {
            cookie += 'secure;';
          }

          document.cookie = name + '=' + cookie;
        }
      };
    }]);

})(angular);
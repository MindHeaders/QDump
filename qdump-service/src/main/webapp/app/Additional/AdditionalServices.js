/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('additional.services', ['ngResource']);

services.factory('ErrorFactory', function() {
    var errorMessage = null;
    return {
        getErrorMessage: function() {
            return errorMessage;
        },
        setErrorMessage: function(msg) {
            errorMessage = msg;
        }
    }
});
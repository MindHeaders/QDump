/**
 * Created by artemvlasov on 17/03/15.
 */
var services = angular.module('additional.services', ['ngResource']);

services.factory('ErrorFactory', function($location) {
    var errorMessage;
    return {
        getErrorMessage: function() {
            return errorMessage;
        },
        setErrorMessage: function(msg) {
            errorMessage = msg;
            $location.path('/error')
        }
    }
});
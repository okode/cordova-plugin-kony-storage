var exec = require('cordova/exec');

var KonyStorage = (function () {
    function KonyStorage() { }
    KonyStorage.prototype.keySet = function() {
        return new Promise(function (resolve, reject) {
            exec(resolve || function () { }, reject || function () { }, "KonyStorage", "keySet", []);
        });
    };
    KonyStorage.prototype.get = function (key) {
        return new Promise(function (resolve, reject) {
            exec(resolve || function () { }, reject || function () { }, "KonyStorage", "get", [key]);
        });
    };
    return KonyStorage;
}());

module.exports = new KonyStorage();

Cordova Plugin Kony Storage
===========================

Add plugin
---------

```
$ cordova plugin add https://github.com/okode/cordova-plugin-kony-storage --save

```

Remove plugin
-----------

```
$ cordova plugin remove cordova-plugin-kony-storage

```

Remove reference in config.xml file

```
<plugin name="cordova-plugin-kony-storage" spec="https://github.com/okode/cordova-plugin-kony-storage"/>

```

Usage
-----

```
if (typeof konyStore != 'undefined') {
  konyStore.get('pendingServices').then(result => alert('OK: ' + result)).catch(reason => alert('FAIL: ' + reason));
} else {
  alert("konyStore undefined");
}
```

API Documentation
-----------------

See `typings/kony-storage-plugin.d.ts` for documentation of the JavaScript APIs including call signatures and parameter types.

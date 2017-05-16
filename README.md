APPCOL - Kony data migration plugin
===============================


Add plugin
---------

In the project path run this command:

```
$ cordova plugin add ./localPlugins/KonyStorage --save

```

Then, add `kony-storage-plugin.d.ts` file to declarations folder.


Remove plugin
-----------

Run the following command:

```
$ cordova plugin remove kony-storage-plugin

```

Remove reference in config.xml file

```
<plugin name="kony-storage-plugin" spec="./localPlugins/KonyStorage"/>

```

Remove `kony-storage-plugin.d.ts` file from declarations folder.


Usage Example
-------------

```

if (typeof konyStore != 'undefined') {
  konyStore.get('pendingServices').then(result => alert('OK: ' + result)).catch(reason => alert('FAIL: ' + reason));
} else {
  alert("konyStore undefined");
}


```

API Documentation
-------------

See `typings/kony-storage-plugin.d.ts` for documentation of the JavaScript APIs including call signatures and parameter types.

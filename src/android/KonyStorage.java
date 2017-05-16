package kony.storage.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * This class manage kony storage and it's called from JavaScript.
 */
public class KonyStorage extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("KonyStorage", "KonyStorage called with options [action, args]: [" + action + ", " + args + "]");
        if(action.equals("keySet")) {
            this.keySet(callbackContext);
            return true;
        } else if(action.equals("get")) {
            String key = args.getString(0);
            this.get(key, callbackContext);
            return true;
        }
        return false;
    }

    private void keySet(CallbackContext callbackContext) {
        Context context = this.cordova.getActivity().getApplicationContext();
        callbackContext.success((JSONArray)obtaingKonyKeySet(context));
    }

    private void get(String key, CallbackContext callbackContext) {
        if (key != null && key.length() > 0) {
            Context context = this.cordova.getActivity().getApplicationContext();
            callbackContext.success(obtaingKonyValuesByKey(context, key));
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private static JSONArray obtaingKonyKeySet(Context context) {
        Log.d("KonyStorage", "Starting to obtain key set");
        PackageManager m = context.getPackageManager();
        String directoryDBPath = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(directoryDBPath, 0);
            directoryDBPath = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KonyStorage", "Error Package name not found ", e);
        }

        DBHelper mydb = new DBHelper(context, directoryDBPath);
        return mydb.getKeySet();
    }

    private static String obtaingKonyValuesByKey(Context context, String key) {
        Log.d("KonyStorage", "Starting to obtain value");
        PackageManager m = context.getPackageManager();
        String directoryDBPath = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(directoryDBPath, 0);
            directoryDBPath = p.applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KonyStorage", "Error Package name not found ", e);
        }

        DBHelper mydb = new DBHelper(context, directoryDBPath);
        LocalStorage localStorage = mydb.getData(key);
        if(localStorage != null) {
            return localStorage.getValue();
        }
        Log.d("KonyStorage", "Empty result");
        return "";
    }
}

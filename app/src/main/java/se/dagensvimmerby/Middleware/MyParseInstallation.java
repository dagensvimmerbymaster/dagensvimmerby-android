package se.dagensvimmerby.Middleware;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.TimeZone;

import crm.agile.agilecrm.BuildConfig;

public class MyParseInstallation extends ParseTools {

    private static final String TAG = "MyParseInstallation";

    private static final String SENDER_ID = BuildConfig.FCM_SENDERID;
    private static final String TYPE = "fcm";
    private static final String VERSION_NAME = BuildConfig.PARSE_VERSION;

    private static final String KEY_OBJECT_ID = "objectId";
    private static final String KEY_GCMSENDER_ID = "GCMSenderId";
    private static final String KEY_INSTALLATION_ID = "installationId";
    private static final String KEY_DEVICE_TYPE = "deviceType";
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_APP_IDENTIFIER = "appIdentifier";
    private static final String KEY_PARSE_VERSION = "parseVersion";
    private static final String KEY_DEVICE_TOKEN = "deviceToken";
    private static final String KEY_PUSH_TYPE = "pushType";
    private static final String KEY_TIME_ZONE = "timeZone";
    private static final String KEY_LOCALE = "localeIdentifier";
    private static final String KEY_APP_VERSION = "appVersion";

    public static void updateInstallation(Context ctx, String token, final DoneCallback callback) {
        final ParseObject parseObject = ParseInstallation.getCurrentInstallation();
        HashMap<String, Object> data = new HashMap<>();
        final HashMap<String, String> info = getAppInfo(ctx);
        data.put(KEY_OBJECT_ID, parseObject.getObjectId());
        data.put(KEY_GCMSENDER_ID, SENDER_ID);
        data.put(KEY_INSTALLATION_ID, parseObject.get(KEY_INSTALLATION_ID));
        data.put(KEY_DEVICE_TYPE, parseObject.get(KEY_DEVICE_TYPE));
        data.put(KEY_APP_NAME, info.get(KEY_APP_NAME));
        data.put(KEY_APP_IDENTIFIER, info.get(KEY_APP_IDENTIFIER));
        data.put(KEY_PARSE_VERSION, VERSION_NAME);
        data.put(KEY_DEVICE_TOKEN, token);
        data.put(KEY_PUSH_TYPE, TYPE);
        data.put(KEY_TIME_ZONE, info.get(KEY_TIME_ZONE));
        data.put(KEY_LOCALE, info.get(KEY_LOCALE));
        data.put(KEY_APP_VERSION, info.get(KEY_APP_VERSION));

        ParseCloud.callFunctionInBackground("UpdateInstallation", data, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    // Log.d(TAG, "Updated installation");
                    callback.done();
                } else {
                    // Log.d(TAG, "Failed to update installation");
                    callback.error(e);
                }
            }
        });
    }

    private static HashMap<String, String> getAppInfo(Context ctx) {
        HashMap<String, String> info = new HashMap<>();
        if (ctx != null) {
            PackageInfo pinfo;
            try {
                pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
                info.put(KEY_LOCALE, ctx.getResources().getConfiguration().locale.toString());
                info.put(KEY_APP_NAME, getApplicationName(ctx));
                info.put(KEY_APP_IDENTIFIER, pinfo.packageName);
                info.put(KEY_APP_VERSION, pinfo.versionName);
                info.put(KEY_TIME_ZONE, TimeZone.getDefault().getID());
                return info;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    public static String getApplicationName(Context context) {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }
}

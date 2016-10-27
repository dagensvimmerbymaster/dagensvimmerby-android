package se.dagensvimmerby.CloudMessaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.parse.ParseException;

import se.dagensvimmerby.Middleware.MyParseInstallation;
import se.dagensvimmerby.Middleware.ParseTools;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private final String TAG = "FCM";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "Refreshed token: " + refreshedToken);

        MyParseInstallation.updateInstallation(this, refreshedToken, new ParseTools.DoneCallback() {
            @Override
            public void done() {
                Log.d(TAG, "Token stored");
            }

            @Override
            public void error(ParseException error) {
                Log.d(TAG, "Token failed to store");
            }
        });
    }
}


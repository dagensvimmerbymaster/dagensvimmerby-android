package se.dagensvimmerby.Middleware;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;

public class ParseTools {

    private static String appId = ""; // TODO: Add appID and serverUrl
    private static String serverUrl = "";

    public static void setUpParse(Context context) {
        Parse.enableLocalDatastore(context);

        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId(appId)
                .server(serverUrl)
                .build()
        );
    }

    public interface DoneCallback {
        void done();
        void error(ParseException e);
    }

}

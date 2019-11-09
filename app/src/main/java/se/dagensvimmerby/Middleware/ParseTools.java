package se.dagensvimmerby.Middleware;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;

import crm.agile.agilecrm.BuildConfig;

public class ParseTools {

    private static String appId = BuildConfig.PARSE_APPID;
    private static String serverUrl = BuildConfig.PARSE_SERVERID;

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

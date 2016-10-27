package se.dagensvimmerby;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import crm.agile.agilecrm.R;

public class Home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        startActivity(new Intent(Home.this,MainActivity.class));
        finish();
    }

}

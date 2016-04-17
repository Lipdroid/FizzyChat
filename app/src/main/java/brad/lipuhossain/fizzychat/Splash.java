package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

import brad.lipuhossain.fizzychat.Utils.APIUtils;
import brad.lipuhossain.fizzychat.Utils.GlobalUtils;
import brad.lipuhossain.fizzychat.apis.RequestAsyncTask;
import brad.lipuhossain.fizzychat.constants.Constants;
import brad.lipuhossain.fizzychat.interfaces.AsyncCallback;

public class Splash extends Activity {
    private static final String TAG_LOG = "Splash";
    private static int SPLASH_TIME_OUT = 3000;
    private Handler handler;
    private Runnable runnable;
    private RequestAsyncTask mRequestAsync = null;
    private Context mContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = Splash.this;
        getUserCountry();
        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                // Do what ever you want
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, SignUpActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    private void getUserCountry() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        mRequestAsync = new RequestAsyncTask(mContext, Constants.REQUEST_GET_USER_COUNTRY, params, new AsyncCallback() {
            @Override
            public void done(String result) {
                Log.e(TAG_LOG, result);

                HashMap<String, Object> returnHashMap = APIUtils.parseJSON(Constants.REQUEST_GET_USER_COUNTRY, result);

            }

            @Override
            public void progress() {
                GlobalUtils.showLoadingProgress(mContext);
            }

            @Override
            public void onInterrupted(Exception e) {
                GlobalUtils.dismissLoadingProgress();
            }

            @Override
            public void onException(Exception e) {
                GlobalUtils.dismissLoadingProgress();
            }
        });

        mRequestAsync.execute();
    }


}

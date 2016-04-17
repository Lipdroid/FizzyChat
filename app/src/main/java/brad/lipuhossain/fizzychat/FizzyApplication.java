package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lipu Hossain on 17/04/2016.
 */
public class FizzyApplication extends Application {
    private static Context sContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        sContext = FizzyApplication.this;

    }


    public static Context getFizzyContext() {
        return sContext;

    }
}

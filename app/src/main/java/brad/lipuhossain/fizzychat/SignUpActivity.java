package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import brad.lipuhossain.fizzychat.Utils.GlobalUtils;


public class SignUpActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "SignUpActivity";
    private LinearLayout ln_age = null;
    private TextView tv_age = null;
    private LinearLayout ln_country = null;
    private TextView tv_country = null;
    private LinearLayout ln_gender = null;
    private TextView tv_gender = null;
    private DatePickerDialog dpd = null;
    private Calendar now = null;
    private PopupWindow popupWindow = null;
    private EditText mail = null;
    private EditText username = null;
    private EditText password = null;
    private ImageView btn_signup = null;
    private ImageView btn_fb = null;
    private LinearLayout  ln_goto_login = null;

    private String email = null;
    private String pass = null;

    private String name = null;
    private String age = null;

    private String country = null;
    private String gender = null;
    private String birthday = null;

    private Context mContext = null;
    
    // facebook variable
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //init facebook callbackmanager
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_signup);
        printKeyHash(this);
        mContext = this;
        //initialize the views
        ln_age = (LinearLayout) findViewById(R.id.llage);
        tv_age = (TextView) findViewById(R.id.tv_age);
        ln_country = (LinearLayout) findViewById(R.id.llcountry);
        tv_country = (TextView) findViewById(R.id.tv_country);
        ln_gender = (LinearLayout) findViewById(R.id.llgender);
        ln_goto_login = (LinearLayout) findViewById(R.id.login_page_btn);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        mail = (EditText) findViewById(R.id.et_mail);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        btn_signup = (ImageView) findViewById(R.id.signup_mail);
        btn_fb = (ImageView) findViewById(R.id.btn_fb);

        if(GlobalUtils.user_current_country != null)
            tv_country.setText(GlobalUtils.user_current_country);

        //initialize the date picker
        now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                SignUpActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        //set up the listeners
        ln_age.setOnClickListener(this);
        ln_country.setOnClickListener(this);
        ln_goto_login.setOnClickListener(this);
        ln_gender.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_fb.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result != null)
                    tv_country.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llage:
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.llcountry:
                //go to the country menu page
                Intent i = new Intent(this, CountryList.class);
                startActivityForResult(i, 1);
                break;
            case R.id.llgender:
                //go to the country menu page
                popupWindow = popupWindowShow(this);
                popupWindow.showAsDropDown(view, 0, -5);
                break;
            case R.id.signup_mail:
                //sign up by mail
                mailSignUp();
                break;
            case R.id.btn_fb:
                //login facebook
                loginWithfacebook();
                break;
            case R.id.login_page_btn:
                //goto login page
                gotoLogin();
                break;
        }
    }

    /**
     * just for get keyhash
     *
     * @param context
     * @return
     */
    public String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void gotoLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void loginWithfacebook() {
        // logout previous user before login new one
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"  ,"user_birthday"));
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    loginSNS(object);

                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,birthday,gender,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Utils.dismissLoadingProgress();
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
                // Utils.dismissLoadingProgress();
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onError");
                e.printStackTrace();
                // Utils.dismissLoadingProgress();
            }
        });
    }

    private void loginSNS(JSONObject jsonObject) {
        try {
            if(jsonObject.has("email"))
                email = jsonObject.getString("email");
            if(jsonObject.has("gender"))
                gender = jsonObject.getString("gender");
            if(jsonObject.has("name"))
                name = jsonObject.getString("name");

            if(GlobalUtils.user_current_country != null)
                country = GlobalUtils.user_current_country;

            if(jsonObject.has("birthday")) {
                birthday = jsonObject.getString("birthday");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    Date d = dateFormat.parse(birthday);

                    int current_year = now.get(Calendar.YEAR);
                    now.setTime(d);
                    int birth_year = now.get(Calendar.YEAR);
                    if (birth_year < current_year)
                        age = String.valueOf(current_year - birth_year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void mailSignUp() {
        String dialogBody = null;
        String title = getResources().getString(R.string.dialog_error_title);;
        email = mail.getText().toString().trim();
        pass = password.getText().toString().trim();
        name = username.getText().toString().trim();
        age = tv_age.getText().toString().trim();
        country = tv_country.getText().toString().trim();
        gender = tv_gender.getText().toString().trim();

        if (email.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (pass.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_password_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (!GlobalUtils.isEmailValid(mail.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        }else if (name.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_name_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (age.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_age_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        }else if (country.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_country_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (gender.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_gender_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        }

        requestformailSignUp();
    }

    private void requestformailSignUp() {
    }

    public PopupWindow popupWindowShow(Context mActivity) {

        // initialize a pop up window type
        popupWindow = new PopupWindow(mActivity);
        // the drop down list is a list view
        ListView list = new ListView(mActivity);
        list.setVerticalScrollBarEnabled(false);
        list.setHorizontalScrollBarEnabled(false);
        ArrayAdapter<String> adapter = null;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        list.setAdapter(adapter);

        // set our adapter and pass our pop up window contents
        // listViewDogs.setAdapter(dogsAdapter(popUpContents));

        // set the item click listener
        list.setOnItemClickListener(new DropdownOnItemClickListener());

        // some other visual settings
        Drawable d = new ColorDrawable(Color.WHITE);

//        d.setAlpha(130);
        popupWindow.setBackgroundDrawable(d);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(300);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(list);

        return popupWindow;
    }


    public class DropdownOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);
            String value = (String) arg0.getItemAtPosition(arg2);
            tv_gender.setText(value);
            // dismiss the pop up
            popupWindow.dismiss();
        }


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int current_year = now.get(Calendar.YEAR);
        int birth_year = year;
        if (birth_year < current_year)
            tv_age.setText(current_year - birth_year + " years");
        else
            Toast.makeText(SignUpActivity.this, "You have selected the current year", Toast.LENGTH_LONG).show();
    }
}

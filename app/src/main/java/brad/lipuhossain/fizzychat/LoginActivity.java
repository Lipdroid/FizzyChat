package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import brad.lipuhossain.fizzychat.Utils.GlobalUtils;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText username = null;
    private EditText password = null;
    private ImageView btn_signin = null;
    private ImageView btn_fb = null;
    private String mail = null;
    private String pass = null;
    private Context mContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        //initialize the views
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        btn_signin = (ImageView) findViewById(R.id.signin_mail);
        btn_fb = (ImageView) findViewById(R.id.btn_fb);
        //set up the listeners
        btn_signin.setOnClickListener(this);
        btn_fb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_mail:
                //sign up by mail
                loginWithMail();
                break;
            case R.id.btn_fb:
                //login facebook
                loginWithfacebook();
                break;
        }
    }

    private void loginWithMail() {
        String dialogBody = null;
        String title = getResources().getString(R.string.dialog_error_title);;

        mail = username.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (mail.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_email_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (pass.equals("")) {
            dialogBody = getResources().getString(R.string.dialog_body_password_empty_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        } else if (!GlobalUtils.isEmailValid(username.getText().toString())) {
            dialogBody = getResources().getString(R.string.dialog_body_email_invalid_label);
            GlobalUtils.showInfoDialog(mContext, title, dialogBody, null, null);
            return;
        }

        requestLoginMail();

    }

    private void requestLoginMail() {
    }

    private void loginWithfacebook() {

    }
}

package brad.lipuhossain.fizzychat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class SignUpActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //initialize the views
        ln_age = (LinearLayout) findViewById(R.id.llage);
        tv_age = (TextView) findViewById(R.id.tv_age);
        ln_country = (LinearLayout) findViewById(R.id.llcountry);
        tv_country = (TextView) findViewById(R.id.tv_country);
        ln_gender = (LinearLayout) findViewById(R.id.llgender);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        mail = (EditText) findViewById(R.id.et_mail);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        btn_signup = (ImageView) findViewById(R.id.signup_mail);
        btn_fb = (ImageView) findViewById(R.id.btn_fb);
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
        ln_gender.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_fb.setOnClickListener(this);
    }

    public void next_page(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
        }
    }

    private void loginWithfacebook() {
    }

    private void mailSignUp() {
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

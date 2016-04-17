package brad.lipuhossain.fizzychat.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import brad.lipuhossain.fizzychat.FizzyApplication;
import brad.lipuhossain.fizzychat.R;
import brad.lipuhossain.fizzychat.interfaces.DialogCallback;
import brad.lipuhossain.fizzychat.widgets.CustomDialog;
import brad.lipuhossain.fizzychat.widgets.ProgressDialog;

/**
 * Created by Lipu Hossain on 16/04/2016.
 */
public class GlobalUtils {
    private static ProgressDialog sPdLoading = null;
    public  static String user_current_country = null;

    public static void showInfoDialog(Context context, String title, String body, String action, final DialogCallback dialogCallback) {

        final CustomDialog infoDialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_info_dialog, null);

        new MultipleScreen(context);
        MultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            tvBody.setText(body);
        }

        if (action != null) {
            btnOK.setText(action);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }



    /**
     * TODO Function:<br>
     * To show loading progess
     *
     * @author: Munir
     * @date: Mar 9, 2016
     */
    public static void showLoadingProgress(Context context) {
        if (ProgressDialog.sPdCount <= 0) {
            ProgressDialog.sPdCount = 0;
            sPdLoading = null;
            sPdLoading = new ProgressDialog(context, R.style.CustomDialogTheme);
            sPdLoading.show();
            if (Build.VERSION.SDK_INT > 10) {
                View loadingV = LayoutInflater.from(context).inflate(R.layout.layout_pd_loading, null);
                new MultipleScreen(context);
                MultipleScreen.resizeAllView((ViewGroup) loadingV);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(MultipleScreen.getValueAfterResize(340),
                        MultipleScreen.getValueAfterResize(340));
                sPdLoading.addContentView(loadingV, lp);
            } else {
                String message = context.getResources().getString(R.string.common_loading);
                sPdLoading.setMessage(message);
            }
            ProgressDialog.sPdCount++;
        } else {
            ProgressDialog.sPdCount++;
        }
    }

    /**
     * TODO Function:<br>
     * To dismiss loading progess
     *
     * @author: Munir
     * @date: Mar 9, 2016
     */
    public static void dismissLoadingProgress() {
        if (ProgressDialog.sPdCount <= 1) {
            if (sPdLoading != null && sPdLoading.isShowing())
                sPdLoading.dismiss();
            ProgressDialog.sPdCount--;
        } else {
            ProgressDialog.sPdCount--;
        }
    }
}

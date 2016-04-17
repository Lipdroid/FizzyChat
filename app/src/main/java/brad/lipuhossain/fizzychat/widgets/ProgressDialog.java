package brad.lipuhossain.fizzychat.widgets;

import android.content.Context;

/**
 * Custom loading progress dialog will provide closing dialog when click back button
 *
 * @author Phan Tri
 */
public class ProgressDialog extends android.app.ProgressDialog {

    public static int sPdCount = 0;

    public ProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    public ProgressDialog(Context context, int style) {
        super(context, style);
        // TODO Auto-generated constructor stub
        this.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        sPdCount = 0;
        this.dismiss();
    }
}

package brad.lipuhossain.fizzychat.widgets;

import android.app.Dialog;
import android.content.Context;

/**
 * Custom loading progress dialog will provide closing dialog when click back button
 * 
 * @author Lipu
 *
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setCancelable(false);
	}

	public CustomDialog(Context context, int style) {
		super(context, style);
		// TODO Auto-generated constructor stub
		this.setCancelable(false);
	}

	@Override
	public void onBackPressed() {
		this.dismiss();
	}


}

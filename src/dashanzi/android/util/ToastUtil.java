package dashanzi.android.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

	public static void toast(Context context, String warningString,int imageId) {
		Toast toast = Toast.makeText(context.getApplicationContext(), warningString,
				Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(context.getApplicationContext());
		imageCodeProject.setImageResource(imageId);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}
}

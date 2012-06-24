package dashanzi.android.util;

import dashanzi.android.Constants;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastUtil {

	public static void toast(Context context, String warningString, int imageId) {
		Toast toast = Toast.makeText(context.getApplicationContext(),
				warningString, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(
				context.getApplicationContext());
		imageCodeProject.setImageResource(imageId);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	public static void showIdiomToast(Context context, String idiom,
			int playerId) {
		Toast toast = Toast.makeText(context.getApplicationContext(), idiom,
				Toast.LENGTH_SHORT);
		switch (playerId) {
		case Constants.Player.PlAYER_ONE:
			toast.setGravity(Gravity.RIGHT | Gravity.TOP, 30, 40);
			break;
		case Constants.Player.PlAYER_TWO:
			toast.setGravity(Gravity.LEFT , 60, 20);
			break;
		case Constants.Player.PlAYER_THREE:
			toast.setGravity(Gravity.RIGHT , 60, 20);
			break;
		default:
			break;
		}
		toast.show();
	}
}

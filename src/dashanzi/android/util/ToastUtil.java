package dashanzi.android.util;

import dashanzi.android.Constants;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Toast 工具类
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class ToastUtil {

	/**
	 * toast提示框
	 * @param context 上下文
	 * @param warningString 提示内容
	 * @param imageId 图标标识
	 */
	public static void toastAlert(Context context, String warningString, int imageId) {
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

	/**
	 * 成语显示toast
	 * @param context 上下文
	 * @param idiom 成语
	 * @param playerId 玩家标识
	 */
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

package dashanzi.android.util;

public class FormatUtil {
	
	public static int String2Int(String target){
		
		int result = -888;
		try {
			result = Integer.parseInt(target);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

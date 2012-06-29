package dashanzi.android.util;

public class FormatUtil {
	
	/**
	 * String 向 int 转化
	 * @param target 目标字符串
	 * @return 整形结果
	 */
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

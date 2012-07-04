package dashanzi.android.util;

/**
 * 数据类型转换工具类
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

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

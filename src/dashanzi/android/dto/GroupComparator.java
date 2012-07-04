package dashanzi.android.dto;

import java.util.Comparator;
/**
 * 房间信息比较器
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class GroupComparator implements Comparator<Object> {

	@Override
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		
		GroupInfo group0 = (GroupInfo)lhs;
		GroupInfo group1 = (GroupInfo)rhs;
		
		//按gid进行排序
		int flag = group0.getGid().compareTo(group1.getGid());
		return flag;
	}
}

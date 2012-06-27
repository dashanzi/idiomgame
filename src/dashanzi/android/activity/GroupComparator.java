package dashanzi.android.activity;

import java.util.Comparator;

import dashanzi.android.dto.GroupInfo;

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

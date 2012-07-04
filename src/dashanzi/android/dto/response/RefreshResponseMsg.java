package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.GroupInfo;

/**
 * 大厅信息刷新响应
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class RefreshResponseMsg extends ResponseMsg {

	List<GroupInfo> groupInfoList;

	public List<GroupInfo> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<GroupInfo> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}

	@Override
	public String toString() {
		return "RefreshResponseMsg [groupInfoList=" + groupInfoList
				+ ", getType()=" + getType() + ", getStatus()=" + getStatus()
				+ "]";
	}

}

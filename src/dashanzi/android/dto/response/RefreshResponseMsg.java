package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.GroupInfo;

public class RefreshResponseMsg extends ResponseMsg {

	List<GroupInfo> groupInfoList;

	public List<GroupInfo> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<GroupInfo> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}
	}
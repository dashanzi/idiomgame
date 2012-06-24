package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.GroupInfo;

public class LoginResponseMsg extends ResponseMsg{

	private List<GroupInfo> groupInfoList;

	public List<GroupInfo> getGroupInfoList() {
		return groupInfoList;
	}

	public void setGroupInfoList(List<GroupInfo> groupInfoList) {
		this.groupInfoList = groupInfoList;
	}

	@Override
	public String toString() {
		return "LoginResponseMsg [groupInfoList=" + groupInfoList
				+ ", getType()=" + getType() + ", getStatus()=" + getStatus()
				+ "]";
	}

}

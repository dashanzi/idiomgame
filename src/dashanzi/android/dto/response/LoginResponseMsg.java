package dashanzi.android.dto.response;

import java.util.List;

import dashanzi.android.dto.GroupInfo;

/**
 * 登陆响应
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
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

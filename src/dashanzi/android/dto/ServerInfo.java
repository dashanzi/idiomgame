package dashanzi.android.dto;

/**
 * 服务器信息
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */

public class ServerInfo {

	private String ip;
	private int port;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "ServerInfo [ip=" + ip + ", port=" + port + "]";
	}
	
}

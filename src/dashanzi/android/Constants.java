package dashanzi.android;

public class Constants {

	public static class PlayerList {
		public static final String HEADER_IMAGE = "image";
		public static final String NAME = "name";
		public static final String SCORE = "score";
	}

	public static class HouseList {
		public static final String HEADER_IMAGE = "image";
		public static final String HOUSE_NUM = "housenum";
		public static final String HOUSE_REST_PLACE_NUM = "restnum";
	}

	public static class Response {
		public static final String SUCCESS = "OK";
		public static final String FAILED = "FAIL";
	}

	public static class JSON {
		public static final String HEADER = "header";
		public static final String BODY = "body";
	}

	public static class JSON_REQ_HEADER {
		public static final String TYPE = "type";
		public static final String UID = "uid";
		public static final String GID = "gid";
	}

	public static class JSON_RES_HEADER {
		public static final String TYPE = "type";
		public static final String STATUS = "uid";
		public static final String GID = "gid";
	}

	public static class Type {

		public static final String LOGIN_REQ = "LOGIN_REQ";
		public static final String LOGIN_RESP = "LOGIN_RESP";
		public static final String REFRESH_REQ = "REFRESH_REQ";
		public static final String REFRESH_RESP = "REFRESH_RESP";
		public static final String REFRESHROOM_REQ = "REFRESHROOM_REQ";
		public static final String REFRESHROOM_RESP = "REFRESHROOM_RESP";
		public static final String JOIN_REQ = "JOIN_REQ";
		public static final String JOIN_RESP = "JOIN_RESP";
		public static final String ROOM_NOTIFY = "ROOM_NOTIFY";
		public static final String START_REQ = "START_REQ";
		public static final String START_NOTIFY = "START_NOTIFY";
		public static final String INPUT_REQ = "INPUT_REQ";
		public static final String INPUT_RESP = "INPUT_RESP";
		public static final String HELP_REQ = "HELP_REQ";
		public static final String HELP_RESP = "HELP_RESP";
		public static final String TIMEOUT_REQ = "TIMEOUT_REQ";
		public static final String TIMEOUT_RESP = "TIMEOUT_RESP";
		public static final String QUIT_NOTIFY = "QUIT_NOTIFY";
		public static final String LOGOUT_NOTIFY = "LOGOUT_NOTIFY";
	}

	public static class CheckResultType {
		public static final int CORRECT = 100;
		public static final int WORING = 101;
		public static final int TIME_OUT = 102;
		public static final int HELP = 103;
	}

	public static final int PLAYER_NUM = 3;
}

package dashanzi.android;

/**
 * 
 * @author dashanzi
 * @version 1.0
 * @date 20120629
 *
 */
public class Constants {

	public static class Game{
		
		public static final int PLAYER_1_IMAGEBTN_TAG = 0;// 需要与uid的末尾对应
		public static final int PLAYER_2_IMAGEBTN_TAG = 1;
		public static final int PLAYER_3_IMAGEBTN_TAG = 2;
		
		// 倒计时秒数
		public static final int COUNT_DOWN_SECOND = 60;
		public static final int MIN_IDIOM_WORD_NUM = 3;
		public static final int MAX_IDIOM_WORD_NUM = 16;
		
		// gridView
		public static final int EXIT = 0;
		public static final int SUBMIT = 1;
		public static final int HELP = 2;
	}
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


		public static final String REGISTER_REQ = "REGISTER_REQ";
		public static final String REGISTER_RESP = "REGISTER_RESP";
		
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
		public static final String GETUSERINFO_REQ = "GETUSERINFO_REQ";
		public static final String GETUSERINFO_RESP = "GETUSERINFO_RESP";
	}

	public static class CheckResultType {
		public static final int CORRECT = 100;
		public static final int WORING = 101;
		public static final int TIME_OUT = 102;
		public static final int HELP = 103;
	}
	
	public static class Player {
		public static final int PLAYER_NUM = 3;
		public static final int PlAYER_ONE = 0;
		public static final int PlAYER_TWO = 1;
		public static final int PlAYER_THREE = 2;
		
		public static final int FEMALE = 0;
		public static final int MAN = 1;
		
	}
	
	public static class ButtonTag{
		public static final int SERVER_CONFIG_BTN = 1;
		public static final int LOGIN_BTN = 2;
	}
	
	public static class DataBase{
		public static final String DB_NAME = "idiom_game_db";
		public static final int DB_VERSION = 1;
		public static final String SERVER_CONFIG_TABLE = "serverConfigTable"; 
		
		public static final String ID = "id"; 
		public static final String VALUE_IP = "ipValue"; 
		public static final String VALUE_PORT = "portValue"; 
		public static final String DEFAULT_ID = "123456"; 
		public static final String DEFAULT_IP = "175.41.135.231"; 
		public static final int DEFAULT_PORT = 8888; 
	}
	
	public static class HeatBeat{
		public static final int HEARTBEAT_INTERVAl = 10000;
	}
}

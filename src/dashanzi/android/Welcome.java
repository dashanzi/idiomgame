package dashanzi.android;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import dashanzi.android.dto.request.LoginRequestMsg;
import dashanzi.android.login.Login;
import dashanzi.android.util.Beans2JsonUtil;
import dashanzi.android.util.ToastUtil;
public class Welcome extends Activity {  
    private final int SPLASH_DELAY_TIME = 5000 ;  
    private String Tag = "WelcomeActivity" ;  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        Log.d(Tag , "onCreate()" );  
        super.onCreate(savedInstanceState);  
        
        //1. 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.welcome);  
        
        //2. 建立TCP连接 TODO
        
        
        //3. 自动跳转至 Login界面
        new Handler().postDelayed(  
                new Runnable()  
                {  
                    @Override  
                    public void run() {  
                        startActivity(new Intent(Welcome.this , Login.class));  
                        Welcome.this.finish();  
                    }  
                      
                }  
        , SPLASH_DELAY_TIME);
        
//        //test TODO
//        LoginRequestMsg bean = new LoginRequestMsg();
//        bean.setType("ddd");
//        bean.setName("dddsaa");
//        bean.setPassword("123300");
//        
//        try {
//			String jsonStr = Beans2JsonUtil.getJsonStrFromLoginRequest(bean);
//			Log.i(Tag, jsonStr);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }  
} 
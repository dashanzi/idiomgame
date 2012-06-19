package juzm.android;

import juzm.android.login.Login;
import juzm.android.util.TestUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
public class Welcome extends Activity {  
    private final int SPLASH_DELAY_TIME = 5000 ;  
    private String Tag = "WelcomeActivity" ;  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        Log.d(Tag , "onCreate()" );  
        super.onCreate(savedInstanceState);  
        
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.welcome);  
        
        //自动跳转
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
        
        TestUtil.detectJSON();        
        
    }  
} 
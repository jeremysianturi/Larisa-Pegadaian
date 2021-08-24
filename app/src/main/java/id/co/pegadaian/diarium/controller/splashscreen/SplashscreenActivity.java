package id.co.pegadaian.diarium.controller.splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.controller.login.LoginActivity;
import id.co.pegadaian.diarium.util.UserSessionManager;


public class SplashscreenActivity extends AppCompatActivity {

    private UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        session = new UserSessionManager(SplashscreenActivity.this);

//
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_splashscreen);
        //getSupportActionBar().();

        Thread splashTread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("failed di splashscreeen: " + e);
                    // do nothing
                } finally {

                    if(session.isLogin()) {
                        Intent i = new Intent(SplashscreenActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(SplashscreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    }

                }
            }
        };
        splashTread.start();
    }
}

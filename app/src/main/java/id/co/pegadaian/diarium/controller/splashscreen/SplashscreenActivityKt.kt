package id.co.pegadaian.diarium.controller.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.controller.HomeActivity
import id.co.pegadaian.diarium.controller.HomeActivityWithViewPager
import id.co.pegadaian.diarium.controller.login.LoginActivityKt
import id.co.pegadaian.diarium.util.UserSessionManager

class SplashscreenActivityKt : AppCompatActivity() {

    private lateinit var session : UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        session = UserSessionManager(this)


        // Animation
//        val topAnim = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.top_anim)
//        val botAnim = android.view.animation.AnimationUtils.loadAnimation(this,R.anim.bottom_anim)

        // Set Animation


       val splashThread : Thread = object : Thread() {
           override fun run() {
               try {
                   sleep(2000)
               } catch (e : InterruptedException){
                   Log.d("splashscreen", "Splashscreen error : $e")
               } finally {
                   if (session.isLogin){
//                       val i = Intent(this@SplashscreenActivityKt, HomeActivity::class.java)
                       val i = Intent(this@SplashscreenActivityKt, HomeActivity::class.java)
                       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                       i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                       startActivity(i)
                       finish()
                   } else {
                       val i = Intent(this@SplashscreenActivityKt, LoginActivityKt::class.java)
                       startActivity(i)
                       finish()
                   }
               }
           }
       }

        splashThread.start()
    }
}
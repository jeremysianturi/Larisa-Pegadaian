package id.co.pegadaian.diarium.util;

import android.app.Application;

//import com.element.camera.ElementFaceSDK;
//import com.qiscus.sdk.Qiscus;

import id.co.pegadaian.diarium.util.qiscus.util.Constant;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        ElementFaceSDK.initSDK(this);
//        Qiscus.init(this, Constant.QISCUS_APP_ID);

    }
}
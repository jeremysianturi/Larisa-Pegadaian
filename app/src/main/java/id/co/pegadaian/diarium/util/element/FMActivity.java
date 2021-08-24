//package id.co.telkomsigma.Diarium.util.element;
//
//import android.app.Activity;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Base64;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;
////import com.element.camera.Capture;
////import com.element.camera.ElementFaceCaptureActivity;
////import com.element.common.PermissionUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import id.co.telkomsigma.Diarium.BuildConfig;
//import id.co.telkomsigma.Diarium.R;
//import id.co.telkomsigma.Diarium.util.UserSessionManager;
//
//public class FMActivity extends ElementFaceCaptureActivity {
//
//    private ProgressDialogHelper progressDialogHelper;
//
//    public static String USER_ID = "userId";
//    public static String METHOD = "method";
//
//    public static String METHOD_FR = "method_fr";
//    public static String METHOD_ENROLL = "method_enroll";
//
//    public static String METHOD_FR_URL = "https://id-fm.discoverelement.com:9443/api/faceMatching";
//    public static String METHOD_ENROLL_URL = "https://id-fm.discoverelement.com:9443/api/enroll";
//    public static String METHOD_ENROLL_CHECK_URL = "https://id-fm.discoverelement.com:9443/api/checkUser";
//
//    public static String API_KEY = "fPsaykSoCqyVbYr0qdMZRofYY5pFe1SOBSQlXJfOThYG2GkzuKZP5ortGj4Lw5Fu@id-fm";
//
//    public static String RESPONSE = "response";
//    public static String MESSAGE = "message";
//
//    public static String EXTRA_USER_APP_ID = "com.element.EXTRA_USER_APP_ID";
//    public UserSessionManager session;
//
//   // Intent intent ;
//
//    @Override
//    public void onImageCaptured(Capture[] captures, String s) {
//        if (progressDialogHelper == null) {
//            progressDialogHelper = new ProgressDialogHelper();
//        }
//        progressDialogHelper.showProgressDialog(FMActivity.this, getString(R.string.processing));
//
//        String userId = getIntent().getStringExtra(EXTRA_ELEMENT_USER_ID);
//
//        String method = getIntent().getStringExtra(METHOD);
//
//        System.out.println("met "+method);
//        if(method.equals(METHOD_FR)){
//            fmAuthCheck(userId,captures,METHOD_FR);
//        }else if(method.equals(METHOD_ENROLL)){
//            fmAuthCheck(userId,captures,METHOD_ENROLL);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle bundle) {
//        AndroidNetworking.initialize(getApplicationContext());
//
//        String userId = getIntent().getStringExtra(USER_ID);
//
// //       intent = getIntent();
//        session = new UserSessionManager(FMActivity.this);
//
////        if(session.getIntentState()!=null){
////
////        }else{
////            session.setIntentState(getIntent());
////        }
//
//
//        getIntent().putExtra(EXTRA_USER_APP_ID, getPackageName());
//        getIntent().putExtra(EXTRA_ELEMENT_USER_ID, userId);
//
//
//        super.onCreate(bundle);
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!PermissionUtils.isGranted(getBaseContext(), android.Manifest.permission.CAMERA)) {
//            toastMessage("Please grant all permissions in Settings -> Apps");
//            finish();
//        }
//    }
//
//    private void fmAuthCheck(String userId,Capture[] images,String mode){
//
//        String url ;
//        if(mode.equals(METHOD_ENROLL)){
//            url = METHOD_ENROLL_URL;
//        }else{
//            url = METHOD_FR_URL;
//        }
//
//        JSONObject body = new JSONObject();
//        try {
//
//            body.put("userId",userId);
//            body.put("images",captureJSONConvert(images));
//
//            if(mode.equals(METHOD_ENROLL)){
//                body.put("name","Diarium User");
//                body.put("name2","Diarium User");
//            }
//
//
//        }catch (JSONException e){
//            System.out.println(e);
//        }
//        System.out.println(body.toString());
//
//        System.out.println(url);
//        AndroidNetworking.post(url)
//                .addHeaders("apiKey",API_KEY)
//                .addHeaders("appVersion", BuildConfig.VERSION_NAME)
//                .addHeaders("os","ANDROID")
//                .addHeaders("appId",getPackageName())
//                .addHeaders("deviceModel",Build.MODEL)
//                .addHeaders("sdkVersion","1.0")
//                .addJSONObjectBody(body)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        progressDialogHelper.dismissProgressDialog(FMActivity.this);
//                        System.out.println(response);
//
//                       // Intent intent = getIntent();
//                        //Intent intent = session.getIntentState();
////                        Intent intent = new Intent();
////                        intent.putExtra("response", "ok");
////                        intent.putExtra("message", response.toString()+"");
////                        setResult(Activity.RESULT_OK, intent);
//                        session.setFMResponse("ok");
//                        session.setFMMessage(response.toString());
//                        session.setFMResult(Activity.RESULT_OK);
//                        finish();
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(FMActivity.this);
//                        //System.out.println(error.toString());
//                        //Intent intent = getIntent();
//                       // Intent intent = session.getIntentState();
////                        Intent intent = new Intent();
////                        intent.putExtra("response", "ok");
////                        intent.putExtra("message", error.getErrorBody()+"");
////                        setResult(Activity.RESULT_OK, intent);
//                        session.setFMResponse("ok");
//                        session.setFMMessage(error.getErrorBody());
//                        session.setFMResult(Activity.RESULT_OK);
//                        finish();
//                    }
//                });
//    }
//
//    private JSONArray captureJSONConvert(Capture[] images){
//        JSONArray result = new JSONArray();
//
//        int i = 0;
//        for(Capture capture : images){
//            JSONObject obj = new JSONObject();
//            try {
//                obj.put("index",i);
//                obj.put("modality","face");
//                obj.put("mode","gaze");
//                obj.put("data",Base64.encodeToString(capture.data, Base64.DEFAULT));
//                obj.put("tag",capture.tag);
//
//            }catch (Exception e){
//                System.out.println(e);
//            }
//           result.put(obj);
//        }
//
//        return result;
//    }
//
//}
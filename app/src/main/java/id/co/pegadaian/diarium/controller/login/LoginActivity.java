package id.co.pegadaian.diarium.controller.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import id.co.pegadaian.diarium.R;
import id.co.pegadaian.diarium.controller.HomeActivity;
import id.co.pegadaian.diarium.util.PhotoUtil;
import id.co.pegadaian.diarium.util.TakePhotoUtil;
import id.co.pegadaian.diarium.util.UserSessionManager;
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper;
//import com.element.common.PermissionUtils;
//import id.co.telkomsigma.Diarium.util.element.FMActivity;
//import id.co.telkomsigma.Diarium.util.element.FMActivity;

/**
 * A login screen that offers login via nik/password.
 */
public class LoginActivity extends AppCompatActivity implements BiometricCallback {

    private static final int REQUEST_READ_CONTACTS = 0;
    public static final int ENROLL_REQ_CODE = 12800;
    public static final int AUTH_REQ_CODE = 12801;
    private static final int REQUEST_READ_PHONE_STATE = 123;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

//      test uuid
//    private static final int REQUEST_READ_PHONE_STATE = 1 ;
//      test uuid

    // private UserLoginTask mAuthTask = null;
    UserSessionManager session;
    Dialog myDialog;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    private ProgressDialogHelper progressDialogHelper;
    String[] permissionsRequired = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};
    String uuidString, myDeviceModel, fcmToken;

    // UI references.
    private TextInputEditText passwordField, nikField;
    private ImageView ivViewPass;
    private View mProgressView;
    private View mLoginFormView;
    private View mLoginormView;
    LinearLayout rootView;
    private TextInputLayout til_nik_field;

    BiometricManager mBiometricManager;

    private boolean doubleBackToExitPressedOnce = false;
    private String login_usrmn, login_pwrd;

    private ImageView imgLoginfinger;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    private Bitmap bitmapPhoto;
    private String img_data = null, mCurrentPhotoPath;
    private static final int CAPTURE_PHOTO = 2;
    private static final int CAPTURE_GALLERY = 3;
    private PhotoUtil imageUtil;
    private TakePhotoUtil photoUtil;
    private String TAG = "LOGIN";
    private Button btnFr;
    private ImageView ivProfile;

    // FR peruri
//  private SDKPeruri sdkPeruri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(this);
//        sdkPeruri = new SDKPeruri(this);

        fcmToken = FirebaseInstanceId.getInstance().getToken();
        System.out.println(fcmToken + "test token");

        myDeviceModel = android.os.Build.MODEL;
        System.out.println("my device mode;: " + myDeviceModel);

        // Set up the login form.
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        AndroidNetworking.initialize(getApplicationContext());
        nikField = findViewById(R.id.nik);
        passwordField = findViewById(R.id.password);
        til_nik_field = findViewById(R.id.til_nikfield);
//        ivViewPass = findViewById(R.id.iv_view_pass);
        login_usrmn = session.getLoginUsername();
        login_pwrd = session.getLoginPassword();

        imgLoginfinger = findViewById(R.id.img_fingerprint);


        if (login_usrmn != null && login_pwrd != null) {
            System.out.println("masuk Login");
            nikField.setText(login_usrmn);

            nikField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return true;
                    }
                    return false;
                }
            });

            passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return true;
                    }
                    return false;
                }
            });

//            ivViewPass.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("TEST ON CLICK");
//                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }
//            });

//                passwordField.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        final int DRAWABLE_LEFT = 0;
//                        final int DRAWABLE_TOP = 1;
//                        final int DRAWABLE_RIGHT = 2;
//                        final int DRAWABLE_BOTTOM = 3;
//
//                        System.out.println("On touch Listener DR");
//
//                        if(event.getAction() == MotionEvent.ACTION_UP) {
//                            System.out.println("On touch Listener right drawable");
//                            if(event.getRawX() >= (passwordField.getRight() - passwordField.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//                                // your action here
//                                if (passwordField.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
//                                    passwordField.setInputType(129);
//                                    System.out.println("Masuk ke show password");
//                                } else {
//                                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                                    System.out.println("Masuk ke hide password");
//
//                                }
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                });


//            passwordField.setDrawableClickListener(new DrawableClickListener() {
//                public void onClick(DrawablePosition target) {
//                    switch (target) {
//                        case LEFT:
//                            //Do something here
//                            break;
//
//                        default:
//                            break;
//                    }
//                }
//
//            });


//            ivViewPass.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    switch ( event.getAction() ) {
//
//                        case MotionEvent.ACTION_UP:
//                            System.out.println("MASUKSINIGAKKK");
//                            passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                            break;
//
//                        case MotionEvent.ACTION_DOWN:
//                            System.out.println("MASUKSINIGAKKK1");
//                            passwordField.setInputType(InputType.TYPE_CLASS_TEXT);
//                            break;
//
//                    }
//                    return true;
//                }
//            });


//            ivViewPass.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("MASUKONCLICKNYA");
//                    if(passwordField.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD){
//                        System.out.println("MASUKSINIGAKKK");
//                        passwordField.setInputType(InputType.TYPE_CLASS_TEXT);
////                        passwordField.setTransformationMethod(new PasswordTransformationMethod());
//                    } else if (passwordField.getInputType() == InputType.TYPE_CLASS_TEXT){
//                        System.out.println("MASUKSINIGAKKK");
//                        passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
////                        passwordField.setTransformationMethod(null);
//                    }
//                }
//            });
//            COBA


            nikField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    System.out.println("Test On Text Change");
//                    if (!s.toString().equals("")) {
//                        nikField.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_check_login,0);
//                        System.out.println("Tes logo Checklis: " + s.toString());
//                    }
//                    else {
//
//                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    System.out.println("After Text Change " + s.length() + "s.toString : " + s.toString());
                    if (!s.toString().equals("") || s.length() != 0) {
                        System.out.println("cek warna");
                        til_nik_field.setEndIconDrawable(R.drawable.ic_check_login);
                    } else {
                        til_nik_field.setEndIconDrawable(R.drawable.ic_check_login_grey);
                    }
                }
            });

            //            COBA
            passwordField.setText(login_pwrd);
            imgLoginfinger.setVisibility(View.VISIBLE);
            BiometricBuild();
        }

        imgLoginfinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricBuild();
//              sdkPeruri.startVideo();
//              selectAction();
//              PostPeruri();
            }
        });

        if (!hasPermissions(this, PERMISSIONS)) {
            System.out.println("PERMISSION ada disini");
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        //      test uuid
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
//        } else {
//            //TODO
//            TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//            String uuid = tManager.getDeviceId();
//        }

        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        UUID uuid = UUID.randomUUID();
        uuidString = String.valueOf(uuid);
        System.out.println("test uuid string: " + uuidString);
        //      test uuid


        myDialog = new Dialog(LoginActivity.this);

        // populateAutoComplete();
        session = new UserSessionManager(LoginActivity.this);
//        if(ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[0])
//                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[1])
//                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,permissionsRequired[2])){
//                //Show Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                builder.setTitle("Need Multiple Permissions");
//                builder.setMessage("This app needs Camera and Location permissions.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
//                //Previously Permission Request was cancelled with 'Dont Ask Again',
//                // Redirect to Settings after showing Information about why you need the permission
//                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                builder.setTitle("Need Multiple Permissions");
//                builder.setMessage("This app needs Camera and Location permissions.");
//                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//
////                        sentToSettings = true;
////                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                        Uri uri = Uri.fromParts("package", getPackageName(), null);
////                        intent.setData(uri);
////                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
////                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.show();
//            }  else {
//                //just request the permission
//                ActivityCompat.requestPermissions(LoginActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
//            }

//            txtPermissions.setText("Permissions Required");

//            SharedPreferences.Editor editor = permissionStatus.edit();
//            editor.putBoolean(permissionsRequired[0],true);
//            editor.commit();
//        } else {
//            //You already have the permission, just go ahead.
//        }
        progressDialogHelper = new ProgressDialogHelper();
        rootView = findViewById(R.id.lin_login);

//
//        if(session.isLogin()){
//            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//            Animatoo.animateFade(LoginActivity.this);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//            finish();
//        }

        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                attemptLogin();
                Log.d("check login", "test mau masuk ke attemp login ");
                System.out.println("test mau masuk ke attemp login");
//                 Intent i = new Intent(LoginActivity.this, HomeActivity.class);
//
//                Intent i = new Intent(LoginActivity.this, FMActivity.class);
//                i.putExtra(FMActivity.USER_ID,nikField.getText().toString());
//                if(passwordField.getText().toString().equals("passEnroll")){
//                    i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
//                }else{
//                    i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//                }
//
//                startActivityForResult(i,AUTH_REQ_CODE);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void postFcmToken(){

        JSONObject body = new JSONObject();
        try {
            body.put("fcm_token", fcmToken);
        }catch (JSONException e){
            System.out.println(e);

        }
        System.out.println("PARAMLOGIN postFcmToken(): "+body);

        AndroidNetworking.post("https://hcis-auth.pegadaian.co.id/api/fcm/token")
//            AndroidNetworking.post("https://hcis-auth.pegadaian.co.id/api/auth")
//            AndroidNetworking.post("https://main.hc.digitalevent.id/ldap/api/auth")
//          AndroidNetworking.post(session.getServerURL()+"api/auth")
                .addHeaders("Accept","application/json")
                .addHeaders("Authorization","Bearer " + session.getToken())
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response postfcmToken(): "+response);
                        try {
                            if(response.getInt("status")==200){
                                System.out.println("success post fcmtoken: " + uuidString);
                            }else{
                                progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                System.out.println("On response message: "+response.getString("message"));
                                System.out.println("");
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        String jsonError = error.getErrorBody();
                        System.out.println("response token post fcmtoken: "+ jsonError);
                        Snackbar.make(rootView, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void PostPeruri() {
        JSONObject body = null;
        try {
            body = new JSONObject("valueInput");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            assert body != null;
            body.put("base64FileSrc", img_data);
            body.put("inputFormatSrc", "IMG");
            body.put("base64FileTarget", img_data);
            body.put("inputFormatTarget", "IMG");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post("http://apg01.peruri.co.id:9094/gateway/absensiWFH/1.0/checkConfidence/v1")
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addJSONObjectBody(body)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Login", "onResponse_login : " + response);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", "onError_login: " + anError.getErrorBody());
                        Log.e("Login", "onError_login : " + anError.getResponse());

                    }
                });


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("masuk ke has permission   " + permissions);
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void BiometricBuild() {
        mBiometricManager = new BiometricManager.BiometricBuilder(LoginActivity.this)
                .setTitle("LOGIN")
                .setSubtitle("Login to Larisa ")
                .setDescription("Biometric authenticetion to verify your identity")
                .setNegativeButtonText("CANCEL")
                .build();

        mBiometricManager.authenticate(LoginActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      //  SDK Peruri
//        if (requestCode == sdkPeruri.REQUEST_RECORD_VIDEO) {
//            System.out.println("Video Location :" +
//                    data.getStringExtra("filelocation"));
//        }
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == CAPTURE_PHOTO) {
                if (bitmapPhoto != null) {
                    bitmapPhoto.recycle();
                }
                setPic();
            } else if (requestCode == CAPTURE_GALLERY) {
                selectGallery(data);
            }
        } else {
            //Toast.makeText(getActivity().getApplicationContext(), "request code == null", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == AUTH_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
//                Snackbar.make(rootView,"Your face has successfuly registered",Snackbar.LENGTH_LONG).show();
                // User enrolled successfully
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if (dat.getString("result").equals("VERIFIED")) {
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        session.setLoginState(true);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Your face is not valid")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setFMResponse("-");
                                        session.setFMMessage("-");
                                        session.setFMResult(Activity.RESULT_CANCELED);
                                    }

                                })
                                .show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(LoginActivity.this, "Seems like you have connection problem", Toast.LENGTH_SHORT).show();
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Snackbar.make(rootView, "Face registration failed, Try Again!", Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            } else {
                Snackbar.make(rootView, "No activity found", Snackbar.LENGTH_LONG).show();
            }
        }

        if (requestCode == ENROLL_REQ_CODE) {
            if (session.getFMResult() == Activity.RESULT_OK) {
                try {
                    System.out.println(session.getFMResponse());
                    System.out.println(session.getFMMessage());
                    JSONObject dat = new JSONObject(session.getFMMessage());
                    if (dat.getBoolean("isModelReady")) {
                        session.setFMResponse("-");
                        session.setFMMessage("-");
                        session.setFMResult(Activity.RESULT_CANCELED);
                        session.setLoginState(true);
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Your face is not valid, please check your camera and try again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        session.setFMResponse("-");
                                        session.setFMMessage("-");
                                        session.setFMResult(Activity.RESULT_CANCELED);
                                    }

                                })
                                .show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(LoginActivity.this, "Seems like you have connection problem ", Toast.LENGTH_SHORT).show();

                }


            } else if (resultCode == Activity.RESULT_CANCELED) {

                //System.out.println(data.getStringExtra(FMActivity.MESSAGE));
                Snackbar.make(rootView, "Face registration failed, Try Again!", Snackbar.LENGTH_LONG).show();
                // Enrollment cancelled
            } else {
                Snackbar.make(rootView, "No activity found", Snackbar.LENGTH_LONG).show();
            }
        }
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
            System.out.println("permission ada disini");
            //Got Permission
        }
    }


    public void selectAction() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Select Action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File;
                            Log.d("catch", "me");
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(intent, CAPTURE_PHOTO);
                        }
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    // Create intent to Open Image applications like Gallery, Google Photos
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // Start the Intent
                    startActivityForResult(galleryIntent, CAPTURE_GALLERY);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        //String imageFileName = username + "_";
        String imageFileName = session.getUserFullName() + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void selectGallery(Intent data) {
        Bitmap scaledBitmap = null;
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), selectedImageUri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(selectedImagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = imageUtil.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(selectedImagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(selectedImagePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmapPhoto = BitmapFactory.decodeFile(selectedImagePath, options);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap); //hasil base64

//        System.out.println(img_data+" ZAIMjasdajshdjkahsdkjhasdkjas");
        Log.d("GAMBER", "GAMBAR : " + img_data);
        ivProfile.setImageBitmap(scaledBitmap); // set to ImageView imageView imageview
        //img_bg.setImageBitmap(scaledBitmap);
        //img_ic.setVisibility(View.GONE);
    }

    private void setPic() {
        Bitmap scaledBitmap = null;
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        int actualHeight = bmOptions.outHeight;
        int actualWidth = bmOptions.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        bmOptions.inSampleSize = imageUtil.calculateInSampleSize(bmOptions, actualWidth, actualHeight);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inDither = false;
        bmOptions.inPurgeable = true;
        bmOptions.inInputShareable = true;
        bmOptions.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) bmOptions.outWidth;
        float ratioY = actualHeight / (float) bmOptions.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));


        ExifInterface exif;
        try {
            exif = new ExifInterface(mCurrentPhotoPath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        img_data = photoUtil.getStringBase64Bitmap(scaledBitmap);
//        System.out.println(img_data+" ZAIM2222saldlasjdklasjdkljasldjasl");
        ivProfile.setImageBitmap(scaledBitmap); // set to imageView
//        img_bg.setImageBitmap(scaledBitmap);
//        img_ic.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            System.out.println("request code :" + requestCode);
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//               // populateAutoComplete();
//            }
//
//        }

        System.out.println("TES result uuid" +"   "+ requestCode +"   " + permissions + "   "+ grantResults);
//      test uuid
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    //TODO
//
//              }
//              break;
//
//          default:
//              break;
//      }
//      test uuid


      }

    private void postDevice(String buscd, String nik){
        System.out.println("MASUKDEVICE");
//        final String nik = nikField.getText().toString();
//        progressDialogHelper.showProgressDialog(LoginActivity.this, "Logging in..");
//
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String dev_id = "";
//        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            dev_id =  telephonyManager.getDeviceId();
//        }else{
//            PermissionUtils.verifyPermissions(
//                    LoginActivity.this,
//                    Manifest.permission.READ_PHONE_STATE);
//        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat tgl = new SimpleDateFormat("yyyy-MM-dd");
        String tRes = tgl.format(new Date());
        String reg_id = FirebaseInstanceId.getInstance().getToken();

        JSONObject body = new JSONObject();
        try {
            body.put("begin_date",tRes);
            body.put("end_date","9999-12-31");
            body.put("business_code",buscd);
            body.put("personal_number",nik);
            body.put("device_id",nik);
            body.put("device_model","ANDROID");
            body.put("register_id",reg_id);
            body.put("platform","ANDROID");
            body.put("version_code","1");
            body.put("change_user",nik);
        }catch (JSONException e){
            System.out.println(e);

        }
        System.out.println("PARAMDEVICE"+body);

        AndroidNetworking.post(session.getServerURL()+"users/"+nik+"/personaldevice/"+nik+"/deviceid/"+nik)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"RESPONAUTHpostDevice");
                        try {
                            if (response.getInt("status")==200) {
                                System.out.println("suksespostdevice");
                            } else {
                                System.out.println("gagalpost");
                            }
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            System.out.println(e + "postDevice e");
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        Snackbar.make(rootView, error.getResponse().message(), Snackbar.LENGTH_LONG).show();
                        System.out.println("TEST MESSAGE ERROR: " + error.getMessage());
                        System.out.println("Salah password "+error.getMessage());
                        if (error.getResponse().message() == "Unauthorized"){
                            Snackbar.make(rootView,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                        }
                        System.out.println(error);
                    }
                });
    }

    private void attemptLogin(){
        System.out.println("MASUKLOGIN");
        nikField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        final String nik = nikField.getText().toString();
        String password = passwordField.getText().toString();
        session.setLoginUsername(nik);
        session.setLoginPassword(password);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordField.setError(getString(R.string.error_invalid_password));
            focusView = passwordField;
            cancel = true;
        }

        // Check for a valid nik address.
        if (TextUtils.isEmpty(nik)) {
            nikField.setError(getString(R.string.error_field_required));
            focusView = nikField;
            cancel = true;
        } else if (!isNikValid(nik)) {
            nikField.setError(getString(R.string.error_invalid_email));
            focusView = nikField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            System.out.println("masuk kesini kok org udh ada loggin in ...");
            progressDialogHelper.showProgressDialog(LoginActivity.this, "Logging in....");
//            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//            String dev_id = "";
//            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                dev_id =  telephonyManager.getDeviceId();
//            }else{
//                PermissionUtils.verifyPermissions(
//                        LoginActivity.this,
//                        Manifest.permission.READ_PHONE_STATE);
//            }

//            String reg_id = FirebaseInstanceId.getInstance().getToken();
            JSONObject body = new JSONObject();
            try {
                body.put("application_id","6");
                body.put("username",nik);
                body.put("password",password);
                body.put("device_id",uuidString);

                System.out.println("check body value when login : " + body);
            }catch (JSONException e){
                System.out.println("check error when create body : " + e);
                progressDialogHelper.dismissProgressDialog(this);
            }
            System.out.println("PARAMLOGIN"+body);

            AndroidNetworking.post("https://hcis-auth.pegadaian.co.id/api/auth/login")
//            AndroidNetworking.post("https://hcis-auth.pegadaian.co.id/api/auth")
//            AndroidNetworking.post("https://main.hc.digitalevent.id/ldap/api/auth")
//          AndroidNetworking.post(session.getServerURL()+"api/auth")
                  .addHeaders("Accept","application/json")
                    .addHeaders("Content-Type","application/json")
                    .addJSONObjectBody(body)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            System.out.println("attempt login response: "+response);
                            try {
                                if(response.getInt("status")==200){
                                    session.setToken(response.getString("access_token"));
                                    session.setUserNik(nik);
//                                    session.setUserNik("632406");
                                    postFcmToken();
                                    getPersonalData(session.getToken(),session.getUserNIK());
                                    System.out.println("Token diarium" + session.getToken());
//                                    getPersonalData(session.getToken(),"10101010");

                                }else{
                                    progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                    System.out.println("On response message: "+response.getString("message"));
                                    System.out.println("");
                                }
                            }catch (Exception e){
                                progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                System.out.println(e);
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            String jsonError = error.getErrorBody();
                            System.out.println("error di attemp login: " + error);
                            System.out.println("response token error attemp login: "+ jsonError);

                            if (jsonError.contains("Please check your mail for authorize this login session.")){
                                Snackbar.make(rootView, "Please check your mail for authorize this login session.", Snackbar.LENGTH_LONG).show();
                            } else if (jsonError.contains("These credentials do not match our records.")){
                                Snackbar.make(rootView, "These credentials do not match our records.", Snackbar.LENGTH_LONG).show();
                            }

                            System.out.println("RESPONSETOKEN"+error.getErrorBody());
                            if (error.getResponse().message().equals("Unauthorized") ){
                                Snackbar.make(rootView,"Wrong email or password", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean isNikValid(String nik) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private void getPersonalData(String token, String pernr){
        Log.d("PERSONALDATA", token + " " +  pernr);
        progressDialogHelper.changeMessage("Get personal data..");

        System.out.println("get personal data url: " + session.getServerURL()+"users/"+pernr+"/personal/"+pernr);

        AndroidNetworking.get(session.getServerURL()+"users/"+pernr+"/personal/"+pernr)
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization","Bearer "+token)
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println(response+"kjwrhbk4jr");
                        Log.d("check login", " response at get personal data : " + response);
                        try {
                            if(response.getInt("status")==200){
                                session.setUserNik(response.getJSONObject("data").getString("personal_number"));
                                session.setUserBusinessCode(response.getJSONObject("data").getString("business_code"));
                                session.setUserFullName(response.getJSONObject("data").getString("full_name"));
                                session.setUserNickName(response.getJSONObject("data").getString("nickname"));
                                session.setAvatar(response.getJSONObject("data").getString("profile"));
                                session.setBornDate(response.getJSONObject("data").getString("born_date").substring(5));
                                session.setStatusClickBornDate("0");
                                session.setUserFaceCode(session.getUserBusinessCode()+"-"+session.getUserNIK());
                                postDevice(response.getJSONObject("data").getString("business_code"), pernr); // belom

                                JSONArray arrayPosisi = response.getJSONObject("data").getJSONArray("Posisi");
                                for (int i=0;i<arrayPosisi.length();i++) {
                                    JSONObject objPosisi = arrayPosisi.getJSONObject(i);
                                    JSONArray arrayDalemanPosisi = objPosisi.getJSONArray("posisi");
                                    for (int a=0;a<arrayDalemanPosisi.length();a++) {
                                        JSONObject objDaleman = arrayDalemanPosisi.getJSONObject(a);
                                        String organizational_type = objDaleman.getString("organizational_type");
                                        if (organizational_type.equals("S")) {
                                            String organizational_name = objDaleman.getString("organizational_name");
                                            session.setJob(organizational_name);
                                        }
                                    }
                                }
//                                getMyTeam(response.getJSONObject("data").getString("business_code"), response.getJSONObject("data").getString("personal_number"));

//                                checkFaceEnroll(session.getUserFaceCode()); // pake fr

//                                 tanpa fr

                                System.out.println("response getpersonal data di 200: " + response.getString("message") );
                                session.setLoginState(true);
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                              progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            }else{
                                progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                                System.out.println("response getpersonal data di else: " + response.getString("message") );
                                Log.d("check login","response getpersonal data di else: " + response.getString("message") );
                                Snackbar.make(rootView,"Can't get your personal data", Snackbar.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                            Snackbar.make(rootView,"error to get personal data", Snackbar.LENGTH_LONG).show();
                            System.out.println("response getpersonal data di catch: " + e.getMessage() );
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
                        System.out.println("response getpersonal data di catch: " + error.getErrorBody() );
                    }
                });

        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
    }

    private void getMyTeam(String buscd, final String personal_number_param){
        AndroidNetworking.get(session.getServerURL()+"users/"+buscd+"/myteam?")
                .addHeaders("Accept","application/json")
                .addHeaders("Content-Type","application/json")
                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response+"getOurTeamKOKOKWOY");
                        try {
                            if(response.getInt("status")==200){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject object = jsonArray.getJSONObject(a);
                                    String personal_number_get = object.getString("personal_number");
                                    String name = object.getString("name");
                                    String start_date = object.getString("start_date");
                                    String end_date = object.getString("end_date");
                                    String unit = object.getString("unit");
                                    String position = object.getString("position");
                                    String job = object.getString("job");
                                    String relation = object.getString("relation");
                                    String status = object.getString("status");
                                    if (personal_number_get.equals(personal_number_param)) {
                                        session.setStatus(status);
//                                        session.setJob(position);
                                    }
                                }
                            }else{
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        System.out.println(error);
                    }
                });
    }

    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(LoginActivity.this,"SDK is Not Supported",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(LoginActivity.this,"Device is Not Support Biometic Auth",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(LoginActivity.this,"Fingerprint is not registered in device",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(LoginActivity.this,"Permission is not granted by user",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(LoginActivity.this, "Authentication cancelled by user", Toast.LENGTH_LONG).show();
        mBiometricManager.cancelAuthentication();

    }

    @Override
    public void onAuthenticationSuccessful() {
        attemptLogin();

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

    }

//    private void checkFaceEnroll(String faceCode){
//        progressDialogHelper.changeMessage("Checking face data..");
//
//        JSONObject body = new JSONObject();
//        try {
//            body.put("userId",faceCode);
//        }catch (JSONException e){
//            System.out.println(e);
//            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
//        }
//
//        System.out.println(body);
//
//        AndroidNetworking.post(FMActivity.METHOD_ENROLL_CHECK_URL)
//                .addHeaders("apiKey", FMActivity.API_KEY)
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
//                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
//                        System.out.println(response+"kjewbrjkw4");
//                        try {
//                            System.out.println(response);
//                            if(response.getBoolean("isEnrolled")){
//                                Intent i = new Intent(LoginActivity.this, FMActivity.class);
//                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_FR);
//
//                                startActivityForResult(i,AUTH_REQ_CODE);
//                            }else{
//                                new AlertDialog.Builder(LoginActivity.this)
//                                        .setMessage("Your face is not registered yet, please register your face first")
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                Intent i = new Intent(LoginActivity.this, FMActivity.class);
//                                                i.putExtra(FMActivity.USER_ID,session.getUserFaceCode());
//                                                i.putExtra(FMActivity.METHOD,FMActivity.METHOD_ENROLL);
//                                                startActivityForResult(i,ENROLL_REQ_CODE);
//
//                                            }
//
//                                        })
//                                        .show();
//
//                                //Snackbar.make(rootView,"Can't get your personal data",Snackbar.LENGTH_LONG).show();
//                            }
//                        }catch (Exception e){
//                            progressDialogHelper.dismissProgressDialog(LoginActivity.this);
//                            Snackbar.make(rootView,"error to get face recognition", Snackbar.LENGTH_LONG).show();
//                            System.out.println(e);
//                        }
//
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        progressDialogHelper.dismissProgressDialog(LoginActivity.this);
//                        System.out.println(error);
//                    }
//                });
//    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }



}


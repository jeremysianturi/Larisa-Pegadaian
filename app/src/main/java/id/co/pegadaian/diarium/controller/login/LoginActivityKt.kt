package id.co.pegadaian.diarium.controller.login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.an.biometric.BiometricCallback
import com.an.biometric.BiometricManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.iid.FirebaseInstanceId
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.controller.HomeActivity
import id.co.pegadaian.diarium.util.PhotoUtil
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class LoginActivityKt : AppCompatActivity(), BiometricCallback , View.OnClickListener {

    private val tags = LoginActivityKt::class.java.simpleName

    companion object{
        const val ENROLL_REQ_CODE = 12800
        const val AUTH_REQ_CODE = 12801
        const val REQUEST_READ_CONTACTS = 0
        const val REQUEST_READ_PHONE_STATE = 123
        const val CAPTURE_PHOTO = 2
        const val CAPTURE_GALLERY = 3

//        fun hasPermission(context: Context,){

//        }
    }

    // session
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    lateinit var dialog : Dialog
    private lateinit var sharedPreference : SharedPreferences

    var permissionsRequired = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)

    var PERMISSION_ALL : Int = 1
    var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INSTALL_PACKAGES,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    )

    // deviceId
    lateinit var uuidString : String
    lateinit var myDeviceModel : String
    var fcmToken : String? = null
    // deviceId

    //view
    private lateinit var passwordField : TextInputEditText
    private lateinit var nikField : TextInputEditText
    private lateinit var rootView : LinearLayout
    private lateinit var til_nik_field : TextInputLayout
    //view

    // setup image
    private lateinit var bitmapPhoto : Bitmap
    lateinit var mCurrentPhotoPath : String
    lateinit var imageUtil : PhotoUtil

    // in-App Update
    private lateinit var appUpdateManager: AppUpdateManager

    lateinit var mBiometricManager : BiometricManager
    private var doubleBackToExitPressedOnce : Boolean = false

    var loginUsername : String? = null
    var loginPassword : String? = null
    var img_data : String? = null
    var versionName : String? = null
    var versionCode : String? = null
    var PACKAGE_NAME : String? = null
    var osAndroid : String? = null
    var userAgent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        session = UserSessionManager(this)
        progressDialogHelper = ProgressDialogHelper()
        sharedPreference = getSharedPreferences("permissionStatus", MODE_PRIVATE)
        AndroidNetworking.initialize(applicationContext)

        nikField = findViewById(R.id.nik)
        passwordField = findViewById(R.id.password)
        til_nik_field = findViewById(R.id.til_nikfield)

        fcmToken = FirebaseInstanceId.getInstance().token
        myDeviceModel = android.os.Build.MODEL
        var tManager : TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var uuid : UUID = UUID.randomUUID()
        uuidString = uuid.toString()
        Log.d(tags, "check value : \n fcmToken : $fcmToken \n myDeviceModel : $myDeviceModel \n uuid : $uuidString")

        // hit api check version
//        checkVersion()

        // get version name
        try {
            val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
            versionName = pInfo.versionName
            tv_version_login.text = "v $versionName"
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(tags, "catch di get version name : ${e.printStackTrace()}")
        }

        // get package name
        PACKAGE_NAME = getApplicationContext().getPackageName();

        // get version code
        try {
            val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
            versionCode = pInfo.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(tags, "catch di get version code : ${e.printStackTrace()}")
        }

        // os android
        osAndroid = (android.os.Build.VERSION.SDK_INT).toString()

        // check value for user agent
        Log.d(tags, "version name : $versionName \n package name : $PACKAGE_NAME \n " +
                "version code : $versionCode \n android version : $myDeviceModel")

        loginUsername = session.loginUsername
        loginPassword = session.loginPassword

        if (loginUsername != null && loginPassword != null) {
            nikField.setText(loginUsername)

            // hide keyboard when press done button (NIK Field)
            nikField.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    true
                } else {
                    false
                }
            }

            // hide keyboard when press done button (Password Field)
            passwordField.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                    true
                } else {
                    false
                }
            }

            // change icon when user already typing
            nikField.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    til_nik_field.setEndIconDrawable(R.drawable.ic_check_login_grey)
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.toString().equals("") || s?.length != 0) {
                        til_nik_field.setEndIconDrawable(R.drawable.ic_check_login)
                    } else {
                        til_nik_field.setEndIconDrawable(R.drawable.ic_check_login_grey)
                    }
                }
            })

            passwordField.setText(loginPassword)
            img_fingerprint.visibility = View.VISIBLE
            biometricBuild()
        }

        img_fingerprint.setOnClickListener(this)

        // req permission
        if (!hasPermissions(this, PERMISSIONS.toString())) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }

        dialog = Dialog(this)
        passwordField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL){
                progressDialogHelper.showProgressDialog(this, "Logging in...")
                attempLogin("button done")
                true
            }
            false
        }

        // IN-APP UPDATE
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // CARA PERTAMA IN-APP UPDATE
        appUpdateInfoTask.addOnSuccessListener {

            Log.d(tags,"check update in app : ${it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE}" +
                    "\n update type is allowed : ${it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)}")

            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, 999)
            } else {
//                makeToast("Update not available!")
            }

        }
                .addOnFailureListener {
                    Log.d(tags,"masuk ke on failure listener in-app update : $it")
                }
        // CARA PERTAMA IN-APP UPDATE


        email_sign_in_button.setOnClickListener(this)
    }

    private fun attempLogin(text : String){

        Log.d(tags,"masuk kesini kan iya kann masa engga : $text")

        // set user agent
        userAgent = "Diarium/${versionName} (${PACKAGE_NAME};build:${versionCode}; Android ${osAndroid}) FastAndroidNetworking/1.0.2"
        Log.d(tags, "check user agent value : $userAgent")

        nikField.setError(null)
        passwordField.setError(null)

        var nik : String = nikField.text.toString()
        var password : String = passwordField.text.toString()
        session.loginUsername = nik
        session.loginPassword = password

        var cancel : Boolean = false
        var focusView : View? = null

        // Check for a valid password & Check for a valid nik address.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)){
            passwordField.setError(getString(R.string.error_invalid_password))
            focusView = passwordField
            cancel = true
            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)

        } else if (TextUtils.isEmpty(nik)){
            nikField.error = getString(R.string.error_field_required)
            focusView = nikField
            cancel = true
            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)

        } else {

            // for print
            var bodyString : String?
            var urlString : String?
            var responseString : String?
            var anErrorString : String?

            var body : JSONObject = JSONObject()
            try {
                body.put("application_id", "6")
                body.put("username", nik)
                body.put("password", password)
                body.put("device_id", uuidString)
                body.put("fcm_token", fcmToken)
                body.put("app_version", versionName)
                body.put("app_code", versionCode)
                body.put("platform", "ANDROID")
            } catch (e: Exception){
                Log.d(tags, "cannot fill body post login -> error : $e")
            }
            bodyString = body.toString()

            val urlLogin = "${session.serverURLHCISAUTH}api/auth/login"
            urlString = urlLogin


            AndroidNetworking.post(urlLogin)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(body)
                    .setPriority(Priority.MEDIUM)
                    .setUserAgent(userAgent)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {

                            responseString = response.toString()
                            apiResponsePrint("login", bodyString, urlString, responseString)

                            if (response?.getInt("status") == 200) {

                                // set token & nik to session
                                session.token = response?.getString("access_token")
                                session.setUserNik(nik)


                                // get personal data
                                val splashThread: Thread = object : Thread() {
                                    override fun run() {
                                        try {
                                            sleep(2000)
                                        } catch (e: InterruptedException) {
                                            Log.d("splashscreen", "Splashscreen error : $e")
                                        } finally {
                                            progressDialogHelper.changeMessage("Getting personal data..")
                                            getPersonalData(session.token, session.userNIK)
                                        }
                                    }
                                }

                                splashThread.start()


//                                progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)

                                // get personal data
//                                progressDialogHelper.changeMessage("Get personal data..")// suka crash karna attemp to invoke null object reference


//                                    getPersonalData(session.token, session.userNIK)


                            } else {
                                progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                            }
                        }

                        override fun onError(anError: ANError?) {

                            // print error
                            val anErrorBody = anError?.errorBody.toString()
                            val anErrorJsonObj = JSONObject(anErrorBody)
                            val errorMessage = anErrorJsonObj.getString("message")
                            apiResponsePrint("login", bodyString, urlString, errorMessage)
                            makeToast(errorMessage)

//                            // toast error
//                            val notVerified: String? = "Please check your mail for authorize this login session."
//                            val wrongPassword: String? = "These credentials do not match our records."
//                            val serverError: String? = "Internal server error"
//                            val notContainNumeric: String? = "At least one the field password must be contains numeric (0..9)"
//                            val dataInactive: String? = "Your data is inactive"
//                            val notContainPunctuation: String? = "At least one the field password must be contains punctuation (!\$#%@)"
//                            val notContainAlphaNumeric: String? = "The password must be alpha numeric"
//                            val notContainLowerCase: String? = "At least one the field password must be contains lower case letter (a..z)."
//                            val notContainUpperCase: String? = "At least one the field password must be contains upper case letter (A..Z)."
//                            val passDidNotMeetReq: String? = "The password format is invalid. password must be alphanumeric, contains uppercase, lowercase and unicode characters!"
//
//                            val jsonErrorBody = anError?.errorBody
//                            if (jsonErrorBody!!.contains(notVerified.toString())) {
//                                makeToast("$notVerified")
//                            } else if (jsonErrorBody!!.contains(wrongPassword.toString())) {
//                                makeToast("$wrongPassword")
//                            } else if (jsonErrorBody!!.contains(serverError.toString())) {
//                                makeToast(serverError)
//                            } else if (jsonErrorBody!!.contains(notContainNumeric.toString())) {
//                                makeToast(notContainNumeric)
//                            } else if (jsonErrorBody!!.contains(dataInactive.toString())) {
//                                makeToast(dataInactive)
//                            } else if (jsonErrorBody!!.contains(notContainPunctuation.toString())) {
//                                makeToast(notContainPunctuation)
//                            } else if (jsonErrorBody!!.contains(notContainAlphaNumeric.toString())) {
//                                makeToast(notContainAlphaNumeric)
//                            } else if (jsonErrorBody!!.contains(notContainLowerCase.toString())) {
//                                makeToast(notContainLowerCase)
//                            } else if (jsonErrorBody!!.contains(notContainUpperCase.toString())) {
//                                makeToast(notContainUpperCase)
//                            } else if (jsonErrorBody!!.contains(passDidNotMeetReq.toString())) {
//                                makeToast(passDidNotMeetReq)
//                            } else {
//                                makeToast("Error!")
//                            }
                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                        }
                    })
        }
    }

    private fun getPersonalData(token: String?, pernr: String?){

//        progressDialogHelper.showProgressDialog(this,"Get personal data..")

        var bodyString : String?
        var urlString : String?
        var responseString : String?
        var anErrorString : String?
        var responseMessage : String?

        bodyString = null // dont have body

        val urlGetPersonalData = "${session.serverURL}users/$pernr/mypersonal/$pernr"
        urlString = urlGetPersonalData

        AndroidNetworking.get(urlGetPersonalData)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        // response message
                        responseMessage = response?.getString("message").toString()
                        Log.d(tags, "check response message : $responseMessage")

                        responseString = response.toString()
                        apiResponsePrint("get personal data", bodyString, urlString, responseString)

                        if (response?.getInt("status") == 200) {

                            //retrieve data from api
                            val dataObject = response.getJSONObject("data")

                            val userNik = dataObject.getString("personal_number")
                            val buscd = dataObject.getString("business_code")
                            val fullName = dataObject.getString("full_name")
                            val nickName = dataObject.getString("nickname")
                            val avatar = dataObject.getString("profile")
                            val bornDate = dataObject.getString("born_date").substring(5)
                            val statusClickBornDate = "0"
                            val userFaceCode = "$buscd-$userNik"

                            var organizationName = ""
                            var orgNameArray : Array<String> = arrayOf()

                            val arrayPosisi = dataObject.getJSONArray("Posisi")
                            for (i in 0 until arrayPosisi.length()) {
                                val objectPosisi = arrayPosisi.getJSONObject(i)
                                val arrayPosisi2 = objectPosisi.getJSONArray("posisi")
                                for (i in 0 until arrayPosisi2.length()) {
                                    val objectPosisi2 = arrayPosisi2.getJSONObject(i)
                                    val organizationType = objectPosisi2.getString("organizational_type")
                                    if (organizationType.equals("S")) {
                                        organizationName = objectPosisi2.getString("organizational_name")
                                        orgNameArray = arrayOf(organizationName)
                                        Log.d(tags, "array of organization name : $orgNameArray")
                                        Log.d(tags,"check org type : $organizationType \n check org name : $organizationName")
//                                        session.job = organizationName
                                    }
                                }
                            }

                            val splashThread: Thread = object : Thread() {
                                override fun run() {
                                    try {
                                        sleep(2000)
                                    } catch (e: InterruptedException) {
                                        Log.d("splashscreen", "Splashscreen error : $e")
                                    } finally {
                                        progressDialogHelper.changeMessage("Update personal data..")
//                                        saveAndSetData(userNik, buscd, fullName, nickName, avatar, bornDate, statusClickBornDate, userFaceCode, organizationName)
                                        saveAndSetData(userNik, buscd, fullName, nickName, avatar, bornDate, statusClickBornDate, userFaceCode, orgNameArray)
                                    }
                                }
                            }

                            splashThread.start()

//                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
//
//                            val intent = Intent(this@LoginActivityKt, HomeActivity::class.java)
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                            startActivity(intent)
//                            finish()
                        } else {
                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                            makeToast(responseMessage)
                            Log.d(tags, "masuk sini emangnya iya ya ya ya ya")
                        }

                    }

                    override fun onError(anError: ANError?) {
                        anErrorString = anError.toString()
                        apiResponsePrint("get personal data", bodyString, urlString, anErrorString)
                        makeToast("Cannot get personal data")
                        progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                    }
                })

    }

    private fun saveAndSetData(userNik: String?, buscd: String?, userFullName: String?, userNikName: String?, avatar: String?, bornDate: String?,
                               statusBornDate: String?, userFaceCode: String?, orgName: Array<String>){

        // set data
        session.setUserNik(userNik)
        session.userBusinessCode = buscd
        session.userFullName = userFullName
        session.userNickName = userNikName
        session.avatar = avatar
        session.bornDate = bornDate
        session.statusClickBornDate = statusBornDate
        session.userFaceCode = userFaceCode

        for (name in orgName){
            session.job = name
            Log.d(tags,"check assign name value to session : $name")
        }

        session.setLoginState(true)
        progressDialogHelper.dismissProgressDialog(this)

        val intent = Intent(this@LoginActivityKt, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()

    }

    @SuppressLint("SimpleDateFormat")
    private fun postDevice(buscd: String?, pernr: String?){

        val bodyString : String?
        val urlString : String?
        var responseString : String?
        var anErrorString : String?

        // date
        val tgl : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val tRes = tgl.format(Date())
        val regId = FirebaseInstanceId.getInstance().getToken()

        // body
        val body = JSONObject()
        try {
            body.put("begin_date", tRes)
            body.put("end_date", "9999-12-31")
            body.put("business_code", buscd)
            body.put("personal_number", pernr)
            body.put("device_id", pernr)
            body.put("device_model", "ANDROID")
            body.put("register_id", regId)
            body.put("platform", "ANDROID")
            body.put("version_code", "1")
            body.put("change_user", pernr)
        } catch (e: Exception){
            Log.d(tags, "cannot fill body post device -> error : $e")
        }

        bodyString = body.toString()

        val urlPostDevice = "${session.serverURL}users/$pernr/personaldevice/$pernr/deviceid/$pernr"
        urlString = urlPostDevice

        AndroidNetworking.post(urlPostDevice)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        responseString = response.toString()
                        apiResponsePrint("post device", bodyString, urlString, responseString)
                        if (response?.getInt("status") == 200) {


                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)


                        } else {
                            var errorPostDevice = response?.getString("message")
                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                            makeToast(errorPostDevice)

                        }
                    }

                    override fun onError(anError: ANError?) {
                        anErrorString = anError.toString()
                        progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                        makeToast("Cannot update device id")
                        apiResponsePrint("post device", bodyString, urlString, anErrorString)
                    }
                })

    }


    private fun postFcmToken(){

        val bodyString : String?
        val urlString : String?
        var responseString : String?
        var anErrorString : String?

        val body = JSONObject()
        try {
            body.put("fcm_token", fcmToken)
        } catch (e: Exception){
            Log.d(tags, "cannot fill body post fcm token -> error : $e")
        }

        bodyString = body.toString()

        val urlPostFcmToken = "${session.serverURLHCISAUTH}api/fcm/token"
        urlString = urlPostFcmToken

        AndroidNetworking.post(urlPostFcmToken)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer ${session.token}")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        responseString = response.toString()
                        apiResponsePrint("fcm token", bodyString, urlString, responseString)

                        if (response?.getInt("status") == 200) {

//                            val splashThread: Thread = object : Thread() {
//                                override fun run() {
//                                    try {
//                                        sleep(2000)
//                                    } catch (e: InterruptedException) {
//                                        Log.d("splashscreen", "Splashscreen error : $e")
//                                    } finally {
//                                        progressDialogHelper.changeMessage("Getting personal data..")
//                                        getPersonalData(session.token, session.userNIK)
//                                    }
//                                }
//                            }
//
//                            splashThread.start()

                        } else {
                            progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                            Toast.makeText(this@LoginActivityKt, "${response!!.getString("message")}", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onError(anError: ANError?) {

                        anErrorString = anError.toString()
                        apiResponsePrint("fcm token", bodyString, urlString, anErrorString)
                        progressDialogHelper.dismissProgressDialog(this@LoginActivityKt)
                        Toast.makeText(this@LoginActivityKt, "Error creating token", Toast.LENGTH_SHORT).show()

                    }
                })
    }

    private fun checkVersion(){

        Log.d(tags, "masuk ke check version di login")

        var bodyString : String?
        var urlString : String?
        var responseString : String?
        var anErrorString : String?
        var responseMessage : String?

        bodyString = null // dont have body

        var urlCheckVersionLogin = "${session.serverURL}appversion/ANDROID/buscd/5000"
        Log.d(tags,"url check version : $urlCheckVersionLogin")

        urlString = urlCheckVersionLogin


        AndroidNetworking.get(urlCheckVersionLogin)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        responseMessage = response?.getString("message").toString()

                        responseString = response.toString()
                        apiResponsePrint("checking version", bodyString, urlString, responseString)

                        if (response?.getInt("status") == 200) {

                            val pInfo: PackageInfo = this@LoginActivityKt.getPackageManager().getPackageInfo(packageName, 0)
                            val versionApplication = pInfo.versionCode

                            val jsonArray: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArray.length()) {
                                val objCheckVersion: JSONObject = jsonArray.getJSONObject(a)

                                val versionApi = objCheckVersion.getString("version_code")
                                val url = objCheckVersion.getString("version_link")

                                if (versionApi.toInt() > versionApplication) {
                                    popupUpdate("playstore", url)
                                }
                            }
                        } else {
                            makeToast(response?.getString("message"))
                        }
                    }

                    override fun onError(anError: ANError?) {
                        anErrorString = anError.toString()
                        apiResponsePrint("checking version", bodyString, urlString, anErrorString)
                        makeToast("Cant compare version : ${anError?.errorBody}")
                    }
                })
    }

    private fun popupUpdate(type: String, url: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_popup_update)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnYes = dialog.findViewById<View>(R.id.btnYes) as Button
        dialog.show()
        dialog.setCancelable(false)
        btnYes.setOnClickListener {
            if (type == "playstore") {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } else {
                Log.d(tags,"masuk ke gagal update berarti ya")
            }
        }
    }

    private fun apiResponsePrint(post: String?, body: String?, url: String?, responseOrError: String?){
        if (post.equals("fcm token")){
            Log.d(tags, "post fcm value -> \n body : $body \n url : $url \n response/error : $responseOrError")
        } else if (post.equals("login")){
            Log.d(tags, "post login value -> \n body : $body \n url : $url \n response/error : $responseOrError")
        } else if (post.equals("get personal data")){
            Log.d(tags, "get personal data -> \n body : $body \n url : $url \n response/error : $responseOrError")
        } else if (post.equals("post device")){
            Log.d(tags, "posting device -> \n body : $body \n url : $url \n response/error : $responseOrError")
        } else if (post.equals("checking version")){
            Log.d(tags, "api checking version -> \n body : $body \n url : $url \n response/error : $responseOrError")
        }
    }

    private fun isPasswordValid(password: String) : Boolean{
       return password.length > 5
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission!!) != PackageManager.PERMISSION_GRANTED) {
                    println("masuk ke has permission   $permissions")
                    return false
                }
            }
        }
        return true
    }


    // onclick member
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.img_fingerprint -> {
                biometricBuild()
            }

            R.id.email_sign_in_button -> {

                // Login
                progressDialogHelper.showProgressDialog(this, "Logging in...")

                attempLogin("button")

            }
        }
    }

    // biometric dialog
    private fun biometricBuild(){
        mBiometricManager = BiometricManager.BiometricBuilder(this)
                .setTitle("LOGIN")
                .setSubtitle("Login to Larisa")
                .setDescription("Biometric authenticetion to verify your identity")
                .setNegativeButtonText("Cancel")
                .build()

        mBiometricManager.authenticate(this)
    }


    // biometric implemented member
    override fun onSdkVersionNotSupported() { makeToast("SDK is Not Supported") }
    override fun onBiometricAuthenticationNotSupported() { makeToast("Device is Not Support Biometic Auth") }
    override fun onBiometricAuthenticationNotAvailable() { makeToast("Fingerprint is not registered in device") }
    override fun onBiometricAuthenticationPermissionNotGranted() { makeToast("Permission is not granted by user") }
    override fun onBiometricAuthenticationInternalError(error: String?) { makeToast("$error") }
    override fun onAuthenticationFailed() {}
    override fun onAuthenticationCancelled() {
        makeToast("Authentication cancelled by user")
        mBiometricManager.cancelAuthentication()
    }
    override fun onAuthenticationSuccessful() {
        progressDialogHelper.showProgressDialog(this, "Logging in...")
        attempLogin("biometric")
    }
    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {}
    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {}

    // activity result

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(tags, "check on activity result $resultCode")
        // photo
        if (resultCode == AppCompatActivity.RESULT_OK){
            if (requestCode == CAPTURE_PHOTO){

                // camera
                if (bitmapPhoto != null){
                    bitmapPhoto.recycle()
                }
                Log.d(tags, "check flow setPic")
//                setPic()
            }

            else if (requestCode == CAPTURE_GALLERY){
                Log.d(tags, "check flow setPic")
//                selectGallery(data)
            }
        }

        // auth
        else if (requestCode == AUTH_REQ_CODE){
            if (session.fmResult == Activity.RESULT_OK){

                try {
                    val data = JSONObject(session.fmMessage)
                    if (data.getString("result").equals("VERIFIED")){
                        session.fmMessage = "-"
                        session.fmMessage = "-"
                        session.fmResult = Activity.RESULT_CANCELED
                        session.setLoginState(true)

                        val i = Intent(this, HomeActivity::class.java)
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(i)
                        finish()
                    } else {
                        AlertDialog.Builder(this)
                                .setMessage("Your face is not valid")
                                .setPositiveButton("OK"){ dialog, which ->}
                    }


                } catch (e: java.lang.Exception){
                    makeToast("Seems like you have connection problem")
                }

            } else if (resultCode == Activity.RESULT_CANCELED){
                makeToast("Face registration failed, Try Again!")
            } else {
                makeToast("No activity found")
            }
        }

        // IN-APP UPDATE
        else if (requestCode == 999) {
            Log.d(tags,"check masuk ke 999 94k y4")
            // check result in-app update
            if (resultCode == Activity.RESULT_CANCELED){
                finish()
            } else if (resultCode == Activity.RESULT_OK){
                makeToast("Successfully updated")
            } else {
                makeToast("Update failed!")
            }
        }

    }

    // toast
    private fun makeToast(message: String?){
        Toast.makeText(this, "$message", Toast.LENGTH_SHORT).show()
    }


    // back pressed
    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce){
            intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        doubleBackToExitPressedOnce = true
        makeToast("Klik lagi untuk keluar")

        Handler().postDelayed({
            run {
                doubleBackToExitPressedOnce = false
            }
        }, 2000)
    }

    override fun onResume() {

        // IN-APP UPDATE CARA PERTAMA
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, 999)
            } else {
//                makeToast("Update not available! on resume()")
            }
        }
        // IN-APP UPDATE CARA PERTAMA

        super.onResume()

         // hit api check version
//        checkVersion()
                }
    }

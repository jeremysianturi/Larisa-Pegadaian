package id.co.pegadaian.diarium.controller.home.main_menu.presence

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PresenceConfirmationAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.RangeDatePickerCreateEleave
import id.co.pegadaian.diarium.entity.DataSpinnerPresenceConfirmation
import id.co.pegadaian.diarium.model.PresenceConfirmationModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import id.co.pegadaian.diarium.util.photo.PhotoUtil
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil
import kotlinx.android.synthetic.main.activity_create_eleave.*
import kotlinx.android.synthetic.main.activity_presence_confirmation.*
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE = 22
class PresenceConfirmation : AppCompatActivity(), View.OnClickListener {

    private val tags = PresenceConfirmation::class.java.simpleName

    // initialize
    private lateinit var viewModel : PresenceConfirmationModel
    private lateinit var adapter : PresenceConfirmationAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date picker
    private lateinit var dateConfirmation : OnDateSetListener
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var tgl : String

    // for spinner absent type
    private val listDataAbsenType = ArrayList<DataSpinnerPresenceConfirmation>()
    private lateinit var listCode : ArrayList<String>
    private lateinit var dataAbsentType : DataSpinnerPresenceConfirmation
    private var strListType : String? = null
    private var typeChosen : String? = ""

    // for evidence
    private var bitmapPhoto: Bitmap? = null
    private var img_data: String? = null
    private  var mCurrentPhotoPath: String? = null
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    private val CAPTURE_PHOTO = 2
    private val CAPTURE_GALLERY = 3
    private val REQUEST_CODE = 7
    private var imageUtil: PhotoUtil? = null
    private var photoUtil: TakePhotoUtil? = null
    private val userID: String? = null

    // range date
    private lateinit var listDate : ArrayList<String>
    private lateinit var listDateShow : ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence_confirmation)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Create Presence Confirmation")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PresenceConfirmationModel::class.java]

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listCode = ArrayList<String>()
        listDate = ArrayList<String>()
        listDateShow = ArrayList<String>()

        // getting data from intent
        val listDateFromRangeDatePicker = intent.getStringArrayListExtra("listDate")
        val listDateFromRangeDatePickerShow = intent.getStringArrayListExtra("listDateShow")
        Log.d(tags, "check value listDateFromRangeDatePicker : $listDateFromRangeDatePicker \n list show : $listDateFromRangeDatePickerShow")
        if (listDateFromRangeDatePicker!=null){
            listDate = listDateFromRangeDatePicker
            setVisibleSecondll()
            tv_pc_date.text = "${listDateFromRangeDatePickerShow[0]} until ${listDateFromRangeDatePickerShow[listDateFromRangeDatePickerShow.size-1]}"
            iv_datepicker.visibility = View.GONE
        }

        // for evidence
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        photoUtil = TakePhotoUtil()
        imageUtil = PhotoUtil()

        //set text nik & name
        tv_pc_nik.text = session.userNIK
        tv_pc_name.text = session.userFullName

        // date picker
        popUpDateConfirmation()

        //absent type
        getDataAbsentType()

        // on click
        spinner_jenis_konfirmasi.setOnClickListener(this)
        iv_choose_image.setOnClickListener(this)
        btn_submit_presenceconf.setOnClickListener(this)
    }

    // get data spinner
    private fun getDataAbsentType(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val beginDate = tgl
        val objectType = "ABSTY"


        viewModel.setDataSpinner(baseUrl, token, buscd, beginDate, objectType)

        viewModel.getDataSpinner().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listDataAbsenType.clear()
                listCode.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val status = model[i].code
                    val name = model[i].typeName

                    Log.d(tags, "check value getSpinner status surat tugas: \n model size : ${model.size} \n status : $status \n name : $name")

                    dataAbsentType = DataSpinnerPresenceConfirmation(intResponse, status, name)
                    listDataAbsenType.add(dataAbsentType)
                    listCode.add(name)

                    Log.d(tags, "list status number : ${listDataAbsenType} \n list status : $listCode ")
                }
            }
        })

    }

    //  filter date
    private fun popUpDateConfirmation(){

        dateConfirmation = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelDateConfirmation()
        }

        iv_datepicker.setOnClickListener(this)
    }

    private fun updateLabelDateConfirmation(){

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_pc_date.text = sdf.format(myCalendar.time)

        listDate.add("'${sdf.format(myCalendar.time)}'")
        listDateShow.add(sdf.format(myCalendar.time))

        Log.d(tags, "check value list date on update lable : ${listDate.size}")

        if (listDate.size < 100){

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add date presence confirmation")
            builder.setMessage("Do you want to add more date?")
            builder.setCancelable(false)

            builder.setPositiveButton("Add More") { dialog, which ->
                DatePickerDialog(this, dateConfirmation, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            builder.setNegativeButton("No") { dialog, which ->
                iv_datepicker.visibility = View.GONE
                setVisibleSecondll()

                var listDateShowEdit = ""
                for (a in 0 until listDateShow.size) {
                    if (a == listDateShow.size-1){
                        listDateShowEdit += listDateShow[a]
                    } else {
                        listDateShowEdit += "${listDateShow[a]} , "
                    }

                }
                Log.d(tags,"check value listdateshowedit : $listDateShowEdit")
                tv_pc_date.text = listDateShowEdit
            }
            builder.show()

        } else {
            Toast.makeText(this, "You've reach your maximum date selection", Toast.LENGTH_SHORT).show()
            iv_datepicker.visibility = View.GONE
            tv_pc_date.text = listDateShow.toString()
            setVisibleSecondll()
        }

    }

//    private fun checkDate(startDate: String?, endDate: String?): Boolean {
//        var status = true
//        val sdf = SimpleDateFormat("yyyy-MM-dd")
//        try {
//            val sDate = sdf.parse(startDate)
//            val eDate = sdf.parse(endDate)
//            if (sDate.after(eDate)) {
//                status = false
//                println("check end date after start date")
//            }
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//        return status
//    }

    private fun submitPresenceConfirmation(nik: String?, name: String?, buscd: String?, date: ArrayList<String>, jenisKonfirmasi: String, deskripsi: String?){

        progressDialogHelper.showProgressDialog(this,"Submit confirmation..")
        val beginDate = tgl
        val endDate = "9999-12-31"
        val buscd = buscd
        val pernr = nik
        val date = date
        val absentType = jenisKonfirmasi
        val deskripsi = deskripsi
        val status = "01"

        val baseUrl = session.serverURL
        val token = session.token

        // posting presence confirmation

        val postConfUrl = "${baseUrl}users/presensi/absent"
        Log.d(tags, "check url post presence confirmation : $postConfUrl")

        Log.d(tags, "check value-value : \n beginDate : $beginDate \n endDate : $endDate \n buscd : $buscd \n " +
                "pernr : $pernr \n dateAbsent : $date \n absentType : $absentType \n desc : $deskripsi \n approvalStatus : $status " +
                "\n evidence : $img_data")

        AndroidNetworking.upload(postConfUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", token)
                .addMultipartParameter("begin_date", beginDate)
                .addMultipartParameter("end_date", endDate)
                .addMultipartParameter("business_code", buscd)
                .addMultipartParameter("personal_number", pernr)
                .addMultipartParameter("arr_date_absent", date.toString())
                .addMultipartParameter("absent_type", absentType)
                .addMultipartParameter("description", deskripsi)
                .addMultipartParameter("approval_status", status)
                .addMultipartFile("evidence", saveImage(this, img_data))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tags, "check response new presence confirmation : $response")

                        if (response?.getString("message").equals("success")) {
                            Toast.makeText(this@PresenceConfirmation, response?.getString("message"), Toast.LENGTH_SHORT).show()
                            finish()

                        } else {
                            Toast.makeText(this@PresenceConfirmation, "${response?.getString("message")}", Toast.LENGTH_SHORT).show()
                        }
                        progressDialogHelper.dismissProgressDialog(this@PresenceConfirmation)

                    }

                    override fun onError(anError: ANError?) {
                        val anErrorBody = anError?.errorBody.toString()
                        val anErrorJsonObj = JSONObject(anErrorBody)
                        val errorMessage = anErrorJsonObj.getString("message")
                        progressDialogHelper.dismissProgressDialog(this@PresenceConfirmation)
                        Toast.makeText(this@PresenceConfirmation, errorMessage , Toast.LENGTH_SHORT).show()

                        // logging
                        Log.d(tags, "check eror onerror : $anError")
                        Log.d(tags, "check eror onerror body : ${anError?.errorBody}")
                        Log.d(tags, "check eror onerror detail : ${anError?.errorDetail}")
                    }
                })
    }

    private fun responseOrError(body: JSONObject, postUrl: String?, response: String?, onError: String?){

            Log.d(tags, "Submit post data identification info: \n " +
                    "Post body : $body \n" +
                    "Url post : $postUrl \n" +
                    "response post : $response \n" +
                    "error post : $onError \n")


    }

    private fun getAllValue(){

        val nik = session.userNIK
        val name = session.userFullName

        Log.d(tags, "check value nik : $nik & name : $name")
//        val nik = tv_pc_nik.text.toString()
//        val name = et_pc_name.text.toString()
        val buscd = session.userBusinessCode
        val date = listDate
        val jenisKonfirmasi = spinner_jenis_konfirmasi.text.toString()
        var jenisInCode : String = ""
        if (jenisKonfirmasi.equals("Cuti")){
            jenisInCode = "01"
        } else if (jenisKonfirmasi.equals("Sakit")){
            jenisInCode = "02"
        } else if (jenisKonfirmasi.equals("Izin")){
            jenisInCode = "03"
        } else if (jenisKonfirmasi.equals("Maintenance Larisa")){
            jenisInCode = "04"
        } else if (jenisKonfirmasi.equals("Tugas Luar / Dinas")){
            jenisInCode = "05"
        }
        val description = et_pc_deskripsi.text.toString()

        // buat check date
//        if (checkDate(tgl,date)){
//            submitPresenceConfirmation(nik, name, buscd, date, jenisInCode, description)
//        } else {
//            Toast.makeText(this, "Start date cannot be greater than end date", Toast.LENGTH_SHORT).show()
//        }

        if (date.equals("")){
            Toast.makeText(this, "Date empty!", Toast.LENGTH_SHORT).show()
        } else if (jenisKonfirmasi.equals("")){
            Toast.makeText(this, "Please fill presence confirmation type", Toast.LENGTH_SHORT).show()
        } else if (description.equals("")){
            Toast.makeText(this, "Please fill the description", Toast.LENGTH_SHORT).show()
        } else if (img_data.equals("") || img_data.equals(null)){
            Toast.makeText(this, "Please fill the evidence!", Toast.LENGTH_SHORT).show()
        } else {
            submitPresenceConfirmation(nik, name, buscd, date, strListType!! , description)
        }

    }

    // untuk image evidence
    fun saveImage(context: Context, imageData: String?): File? {
        println(imageData + "kqn3kje")
        val imgBytesData: ByteArray
        imgBytesData = if (imageData == "" || imageData == null) {
            Base64.decode("0",
                    Base64.DEFAULT)
        } else {
            Base64.decode(imageData,
                    Base64.DEFAULT)
        }
        val file: File
        val fileOutputStream: FileOutputStream
        try {
            file = File.createTempFile("image", ".jpg", context.cacheDir)  // suffix diganti jadi ".jpg" biar jd jpg
            fileOutputStream = FileOutputStream(file)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val bufferedOutputStream = BufferedOutputStream(
                fileOutputStream)
        try {
            bufferedOutputStream.write(imgBytesData)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                bufferedOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            println("RESULT ok CAMERA")
            if (requestCode == CAPTURE_PHOTO) {
                if (bitmapPhoto != null) {
                    bitmapPhoto!!.recycle()
                }
                setPic()
            } else if (requestCode == CAPTURE_GALLERY) {
                selectGallery(data)
            }
        } else {
            //Toast.makeText(getActivity().getApplicationContext(), "request code == null", Toast.LENGTH_SHORT).show();
        }
    }

    private fun setPic() {
        var scaledBitmap: Bitmap? = null
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)

        // Decode the image file into a Bitmap sized to fill the View
        var actualHeight = bmOptions.outHeight
        var actualWidth = bmOptions.outWidth
        val maxHeight = 816.0f
        val maxWidth = 612.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        bmOptions.inSampleSize = imageUtil!!.calculateInSampleSize(bmOptions, actualWidth, actualHeight)
        bmOptions.inJustDecodeBounds = false
        bmOptions.inDither = false
        bmOptions.inPurgeable = true
        bmOptions.inInputShareable = true
        bmOptions.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / bmOptions.outWidth.toFloat()
        val ratioY = actualHeight / bmOptions.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        val exif: ExifInterface
        try {
            exif = ExifInterface(mCurrentPhotoPath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            println("TESNIHTES")
            e.printStackTrace()
        }
        bitmapPhoto = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
        img_data = TakePhotoUtil.getStringBase64Bitmap(scaledBitmap)
        Log.d("GAMBER", "GAMBAR : $img_data")
        iv_choose_image.setImageBitmap(scaledBitmap)
//        img_bg.setImageBitmap(scaledBitmap);
//        img_ic.setVisibility(View.GONE);
    }


    //
    private fun selectGallery(data: Intent?) {
        var scaledBitmap: Bitmap? = null
        val selectedImageUri = data?.data
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursorLoader = CursorLoader(applicationContext, selectedImageUri, projection, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        val selectedImagePath = cursor.getString(column_index)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(selectedImagePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        val maxHeight = 816.0f
        val maxWidth = 612.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = imageUtil!!.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(selectedImagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        val exif: ExifInterface
        try {
            exif = ExifInterface(selectedImagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        bitmapPhoto = BitmapFactory.decodeFile(selectedImagePath, options)
        img_data = TakePhotoUtil.getStringBase64Bitmap(scaledBitmap)
        Log.d("GAMBER", "GAMBAR : $img_data")
        iv_choose_image.setImageBitmap(scaledBitmap)
        //img_bg.setImageBitmap(scaledBitmap);
        //img_ic.setVisibility(View.GONE);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("Request Code :$requestCode $permissions $grantResults")
    }

    private fun selectAction() {
        val items = arrayOf<CharSequence>("Capture Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Action")
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Capture Photo") {
                val permission = arrayOf(Manifest.permission.CAMERA)
                if (ContextCompat.checkSelfPermission(applicationContext, permission[0])
                        == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(applicationContext.packageManager) != null) {
                        // Create the File where the photo should go
                        var photoFile: File? = null
                        try {
                            println("Masuk ke try")
                            photoFile = createImageFile()
                        } catch (ex: IOException) {
                            println("DISINI " + ex.message)
                            // Error occurred while creating the File;
                            Log.d("catch", "me")
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                            startActivityForResult(intent, CAPTURE_PHOTO)
                        }
                    }
                } else {
                    ActivityCompat.requestPermissions(this, permission, REQUEST_CODE)
                }
                //                    coba2
            } else if (items[item] == "Choose from Gallery") {
                // Create intent to Open Image applications like Gallery, Google Photos
                val galleryIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                // Start the Intent
                startActivityForResult(galleryIntent, CAPTURE_GALLERY)
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        //String imageFileName = username + "_";
        val imageFileName: String = userID + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        println("masih jalan sampe sini")
        val image = File.createTempFile(imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        println("error saat create image file")

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_submit_presenceconf -> {
                getAllValue()

            }

            R.id.iv_datepicker -> {
                popupMenuActivity("asd")
            }

            R.id.spinner_jenis_konfirmasi -> {

                var name: String? = null
                var code: String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listCode, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    code = listDataAbsenType[i].code
                    name = listDataAbsenType[i].typeName


                    spinner_jenis_konfirmasi.text = name
                    strListType = code

                    spinner_jenis_konfirmasi.text = name
                }
                spinnerDialogStatus.showSpinerDialog()
            }

            R.id.iv_choose_image -> {
                selectAction()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    private fun popupMenuActivity(objectIdentifier : String?) {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_approval_eleave_detail)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Presence confirmation")

        val btnSingleDate = dialog.findViewById<View>(R.id.btnApprove_eleavedetail_approval) as Button
        val btnRangeDate = dialog.findViewById<View>(R.id.btnReject_eleavedetail_approval) as Button
        val btnGakepake = dialog.findViewById<View>(R.id.btnDetail_eleavedetail_approval) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle_eleavedetail_approval) as TextView

        tvTitle.text = "Choose your date type"
        dialog.show()
        dialog.setCancelable(true)

        // button range date
        btnRangeDate.text = "Range date"
        btnRangeDate.setOnClickListener {
            val intent = Intent(this, RangeDatePickerCreateEleave::class.java)
            intent.putExtra("from","presence confirmation")
            startActivity(intent)
            dialog.dismiss()
        }

        //button single date
        btnSingleDate.text = "Single date"
        btnSingleDate.setOnClickListener {
            DatePickerDialog(this, dateConfirmation, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            dialog.dismiss()
        }

        // button gakepake
        btnGakepake.visibility = View.GONE
        btnGakepake.setOnClickListener{

        }
    }

    private fun setVisibleSecondll(){
        ll_second_create_presenceconfirmation.visibility = View.VISIBLE
    }

    private fun setGoneSecondll(){
        ll_second_create_presenceconfirmation.visibility = View.GONE
    }
}


//private fun popupMenuSelectEvidence() {
//    val dialog = Dialog(this)
//    dialog.setContentView(R.layout.popup_select_action)
//    val lp = WindowManager.LayoutParams()
//    lp.copyFrom(dialog.window!!.attributes)
//    lp.width = WindowManager.LayoutParams.MATCH_PARENT
//    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//    lp.gravity = Gravity.CENTER
//    dialog.window!!.attributes = lp
//    dialog.setTitle("Input Code Here")
//    val btnTakePhoto = dialog.findViewById<View>(R.id.btnTakePhoto) as Button
//    val btnSelectGallery = dialog.findViewById<View>(R.id.btnSelectGallery) as Button
//    val tvTitle = dialog.findViewById<View>(R.id.tv_title) as TextView
//    tvTitle.text = "Select action"
//    dialog.show()
//    dialog.setCancelable(true)
//
//    // button take photo
//    btnTakePhoto.visibility = View.VISIBLE
//    btnTakePhoto.setOnClickListener {
//
//        dialog.dismiss()
//    }
//
//    //button detail
//    btnSelectGallery.setOnClickListener {
//
//        dialog.dismiss()
//    }
//}




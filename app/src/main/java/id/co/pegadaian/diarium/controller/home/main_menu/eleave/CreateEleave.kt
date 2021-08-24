package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import DataSpinnerEleaveCutiBesar
import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
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
import id.co.pegadaian.diarium.entity.DataSpinnerEleave
import id.co.pegadaian.diarium.model.CreateEleaveModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import id.co.pegadaian.diarium.util.photo.PhotoUtil
import id.co.pegadaian.diarium.util.photo.TakePhotoUtil
import kotlinx.android.synthetic.main.activity_create_eleave.*
import kotlinx.android.synthetic.main.activity_presence_confirmation.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateEleave : AppCompatActivity(),View.OnClickListener {

    private val tags = CreateEleave::class.java.simpleName

    // initialize
    private lateinit var viewModel : CreateEleaveModel
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date picker
    private lateinit var dateEleave : DatePickerDialog.OnDateSetListener
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var tgl : String

    // for spinner eleave type
    private val listDataEleaveType = ArrayList<DataSpinnerEleave>()
    private lateinit var listCode : ArrayList<String>
    private lateinit var dataEleaveType : DataSpinnerEleave
    private var strListType : String? = null
    private var typeChosen : String? = ""
    private var codeCuti : String? = ""

    // for spinner cuti besar eleave type
    private val listDataEleaveTypeCutiBesar = ArrayList<DataSpinnerEleaveCutiBesar>()
    private lateinit var listCodeCutiBesar : ArrayList<String>
    private lateinit var dataEleaveTypeCutiBesar : DataSpinnerEleaveCutiBesar
    private var strListTypeCutiBesar : String? = null
    private var typeChosenCutiBesar : String? = ""

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

    // list for chip bubble eleave date
//    val listDates: MutableList<String> = ArrayList()
    private lateinit var listDate : ArrayList<String>
    private lateinit var listDateShow : ArrayList<String>
//    private lateinit var listDateFromRangeDatePicker : ArrayList<String>



    // checkbox
    var checkBoxTunjangan : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_eleave)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Form Permohonan Cuti")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[CreateEleaveModel::class.java]

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())


        listCode = ArrayList<String>()
        listCodeCutiBesar = ArrayList<String>()
        listDate = ArrayList<String>()
        listDateShow = ArrayList<String>()

//        listDateFromRangeDatePicker = ArrayList<String>()

        // if from range date
        val listDateFromRangeDatePicker = intent.getStringArrayListExtra("listDate")
        val listDateFromRangeDatePickerShow = intent.getStringArrayListExtra("listDateShow")
        val nameTipeCutiIntent = intent.getStringExtra("nameTipeCutiFromRangeDatePicker")
        strListType = intent.getStringExtra("codeTipeCutiFromRangeDatePicker")
        Log.d(tags, "check value listDateFromRangeDatePicker : $listDateFromRangeDatePicker \n " +
                "list show : $listDateFromRangeDatePickerShow" +
                " & name tipe cuti : $nameTipeCutiIntent" +
                "str list type value menghilang : $strListType")
        if (listDateFromRangeDatePicker!=null){
            listDate = listDateFromRangeDatePicker
            setVisibleSecondLl()
            tv_pc_date_eleave.text = "${listDateFromRangeDatePickerShow[0]} until ${listDateFromRangeDatePickerShow[listDateFromRangeDatePickerShow.size-1]}"
            iv_datepicker_eleave.visibility = View.GONE
            spinner_jenis_konfirmasi_eleave.text = nameTipeCutiIntent
            ll_after_date_havevalue.visibility = View.VISIBLE
        }

        // for evidence
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        photoUtil = TakePhotoUtil()
        imageUtil = PhotoUtil()

        //set text nik & name
        tv_pc_nik_eleave.text = session.userNIK
        tv_pc_name_eleave.text = session.userFullName

        // date picker
        popUpDateConfirmation()

        //cuti type
        getDataEleaveType()

        // cuti besar type
        getDataEleaveTypeCutiBesar()

        // on click
        spinner_jenis_konfirmasi_eleave.setOnClickListener(this)
        spinner_jenis_konfirmasi_eleave_cutibesar.setOnClickListener(this)
        iv_choose_image_eleave.setOnClickListener(this)
        btn_submit_eleave.setOnClickListener(this)
        checkBox_tunjangan_eleave.setOnClickListener(this)
    }

    // get data spinner
    private fun getDataEleaveType(){

        val baseUrl = session.serverURLHCIS
        val token = session.token

        viewModel.setDataSpinner(baseUrl, token)

        viewModel.getDataSpinner().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listDataEleaveType.clear()
                listCode.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val name = model[i].typeName
                    val code = model[i].typeCode

                    Log.d(tags, "check value getSpinner status eleave: \n model size : ${model.size} \n name : $name")

                    dataEleaveType = DataSpinnerEleave(intResponse, name, code)
                    listDataEleaveType.add(dataEleaveType)
                    listCode.add(name)

                    Log.d(tags, "list status number : ${listDataEleaveType} \n list status : $listCode \n list code : $code")
                }
            }
        })
    }


    // get data spinner cuti besar
    private fun getDataEleaveTypeCutiBesar(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val buscd = session.userBusinessCode
        val objType = "BSRTY"

        viewModel.setDataSpinnerCutiBesar(baseUrl, token, date, buscd, objType)

        viewModel.getDataSpinnerCutiBesar().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listDataEleaveTypeCutiBesar.clear()
                listCodeCutiBesar.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val name = model[i].typeName
                    val code = model[i].typeCode

                    Log.d(tags, "check value getSpinner status eleave: \n model size : ${model.size} \n name : $name")

                    dataEleaveTypeCutiBesar = DataSpinnerEleaveCutiBesar(intResponse, name, code)
                    listDataEleaveTypeCutiBesar.add(dataEleaveTypeCutiBesar)
                    listCodeCutiBesar.add(name)

                    Log.d(tags, "list status number : ${listDataEleaveTypeCutiBesar} \n " +
                            "list status : $listCodeCutiBesar \n list code : $code")
                }
            }
        })
    }


    //  filter date
    private fun popUpDateConfirmation(){

        dateEleave = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelDateConfirmation()

        }
        iv_datepicker_eleave.setOnClickListener(this)
    }

    private fun updateLabelDateConfirmation(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_pc_date_eleave.text = sdf.format(myCalendar.time)

        listDate.add("'${sdf.format(myCalendar.time)}'")
        listDateShow.add(sdf.format(myCalendar.time))

        Log.d(tags, "check value list date on update lable : ${listDate.size}")

        if (listDate.size < 100){

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add date E-leave")
            builder.setMessage("Do you want to add more date?")
            builder.setCancelable(false)

            builder.setPositiveButton("Add More") { dialog, which ->
                DatePickerDialog(this, dateEleave, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            builder.setNegativeButton("No") { dialog, which ->
                iv_datepicker_eleave.visibility = View.GONE
                ll_after_date_havevalue.visibility = View.VISIBLE

                var listDateShowEdit = ""
                for (a in 0 until listDateShow.size) {
                    if (a == listDateShow.size-1){
                        listDateShowEdit += listDateShow[a]
                    } else {
                        listDateShowEdit += "${listDateShow[a]} , "
                    }

                }
                Log.d(tags,"check value listdateshowedit : $listDateShowEdit")
                tv_pc_date_eleave.text = listDateShowEdit
            }
            builder.show()

        } else {
            Toast.makeText(this, "You've reach your maximum date selection", Toast.LENGTH_SHORT).show()
            iv_datepicker_eleave.visibility = View.GONE
            tv_pc_date_eleave.text = listDateShow.toString()
            ll_after_date_havevalue.visibility = View.VISIBLE
        }
    }

    private fun resetView(){
        ll_after_date_havevalue.visibility = View.GONE
        listDateShow.clear()
        listDate.clear()
        tv_pc_date_eleave.text = ""
        iv_datepicker_eleave.visibility = View.VISIBLE
        et_pc_deskripsi_eleave.text.clear()
        iv_choose_image_eleave.setImageResource(R.drawable.placeholder_gallery)
        checkBoxTunjangan = false
    }

    private fun setVisibilityTunjanganCheckbox(){
        if (strListType.equals("CTBSR") || strListType.equals("CTTHN")){
            checkBox_tunjangan_eleave.visibility = View.VISIBLE
        } else {
            checkBox_tunjangan_eleave.visibility = View.GONE
        }
    }

    private fun setVisibleSecondLl (){
        ll_afterchoose_jeniscuti.visibility = View.VISIBLE
    }

    private fun setViewSpinnerForCutiBesar(){
        // set the view
        tv_jenis_konfirmasi_eleave_cutibesar.visibility = View.VISIBLE
        spinner_jenis_konfirmasi_eleave_cutibesar.visibility = View.VISIBLE
        ll_date_eleave.visibility = View.GONE
        ll_after_date_havevalue.visibility = View.VISIBLE

        Toast.makeText(this, "Please fill cuti besar type", Toast.LENGTH_LONG).show()
    }

    private fun hideSpinnerForCutiBesar(){
        // set the view
        tv_jenis_konfirmasi_eleave_cutibesar.visibility = View.GONE
        spinner_jenis_konfirmasi_eleave_cutibesar.visibility = View.GONE
        ll_date_eleave.visibility = View.VISIBLE

    }

    // get all value
    private fun getAllValue(){

        val nik = session.userNIK
        val name = session.userFullName
        val buscd = session.userBusinessCode
        val date = listDate
        Log.d(tags, "check value arraylist date eleave at getAllValue : ${date.size} \n value : $date")
        Log.d(tags,"code cutinya cyinnn : $codeCuti")
        val jenisCuti = strListType
        Log.d(tags,"jenis cutinya cyinnn : $jenisCuti")
        val description = et_pc_deskripsi_eleave.text.toString()

        // for cuti besar
        val jenisCutiBesar = strListTypeCutiBesar


        Log.d(tags, "test value check box tunjangan : $checkBoxTunjangan \n spinner code eleave : $strListType")
        val checkboxTunjanganString = checkBoxTunjangan.toString()

        Log.d(tags, "check value img_data : $img_data")
        if (img_data==null || img_data==""){
            submitOneEleaveWithoutEvidence(nik, buscd, jenisCuti, jenisCutiBesar, description, checkboxTunjanganString, date)
        } else {
            submitOneEleaveWithEvidence(nik, buscd, jenisCuti, jenisCutiBesar, description, checkboxTunjanganString, date)
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
        iv_choose_image_eleave.setImageBitmap(scaledBitmap)
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
        iv_choose_image_eleave.setImageBitmap(scaledBitmap)
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
            R.id.iv_datepicker_eleave -> {
                codeCuti = strListType
                Log.d(tags,"check value code cuti : $codeCuti")

                Log.d(tags, "check strListType at datepicker create eleave : $strListType")
                if (strListType.equals("CTBRN") || strListType.equals("CTGGR")
                        || strListType.equals("CTHJI") || strListType.equals("CTURH")) {

                    // range date picker

                    val intent = Intent(this, RangeDatePickerCreateEleave::class.java)
                    intent.putExtra("from","create eleave")
                    intent.putExtra("codeTipeCuti",strListType)
                    intent.putExtra("nameTipeCuti",typeChosen)
                    startActivity(intent)


                } else {
                    DatePickerDialog(this, dateEleave, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
                }

            }

            R.id.spinner_jenis_konfirmasi_eleave -> {
                var name: String? = null
                var code: String? = null

                if (!spinner_jenis_konfirmasi_eleave.text.equals("")){
                    Log.d(tags,"reset view, value spinner cuti has changed")
                    resetView()
                }

                val spinnerDialogStatus = SpinnerDialog(
                        this, listCode, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    code = listDataEleaveType[i].typeCode
                    name = listDataEleaveType[i].typeName

                    spinner_jenis_konfirmasi_eleave.text = name
                    strListType = code
                    typeChosen = name
                    Log.d(tags, "check value chosen eleave type in code variable : $code \n strListTypenya : $strListType \n " +
                            "typeChosennya : $typeChosen")

                    setVisibleSecondLl()
                    setVisibilityTunjanganCheckbox()
                    if (code.equals("CTBSR")) {
                        setViewSpinnerForCutiBesar()
                    } else {
                        hideSpinnerForCutiBesar()
                    }

//                    spinner_jenis_konfirmasi_eleave.text = name
                }
                spinnerDialogStatus.showSpinerDialog()
            }

            R.id.spinner_jenis_konfirmasi_eleave_cutibesar -> {
                var name: String? = null
                var code: String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listCodeCutiBesar, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    code = listDataEleaveTypeCutiBesar[i].typeCode
                    name = listDataEleaveTypeCutiBesar[i].typeName


                    spinner_jenis_konfirmasi_eleave_cutibesar.text = name
                    strListTypeCutiBesar = code

                    spinner_jenis_konfirmasi_eleave_cutibesar.text = name
                }
                spinnerDialogStatus.showSpinerDialog()
            }

            R.id.iv_choose_image_eleave -> {
                selectAction()
            }

            R.id.btn_submit_eleave -> {
                Log.d(tags, "check masuk ke button submit eleave \n " + "deskripsi : ${et_pc_deskripsi_eleave.text}")
                if (tv_pc_date_eleave.text.equals("") && strListTypeCutiBesar.equals(null)) {
                    Toast.makeText(this, "You have to pick a date!", Toast.LENGTH_SHORT).show()
                } else if (et_pc_deskripsi_eleave.text == null){
                    Toast.makeText(this, "Please fill the description!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(tags,"masuk ke you ha")
                    getAllValue()
                }
            }

            R.id.checkBox_tunjangan_eleave -> {
                checkBoxTunjangan = checkBox_tunjangan_eleave.isChecked
            }
        }
    }

    private fun intentToEleaveList () {
        val intent = Intent(this, EleaveList::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

    }

    // post eleave with evidence
    private fun submitOneEleaveWithEvidence(nik: String?, buscd: String?, jenisCuti: String?,
                                            jenisCutiBesar: String?, deskripsi: String?, tunjangan: String, date: ArrayList<String>){

        val beginDate = tgl
        val endDate = "9999-12-31"
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val dateToPost = date

        // set cuti besar value
        var jnsCtBsr : String = ""
        Log.d(tags, "jenis cuti besarnya : $jenisCutiBesar")
        if (jenisCutiBesar == null){

            // WITH EVIDENCE BUKAN CUTI BESAR
                jnsCtBsr = ""
            // check value
            val amountOfIndexDate = dateToPost.size
            Log.d(tags, "check date list size w/ evidence bukan cuti besar : ${amountOfIndexDate} \n dateToPost value cuti besar : $dateToPost")
            Log.d(tags,
                    "check value-value : \n " +
                            "beginDate : $beginDate \n " +
                            "endDate : $endDate \n " +
                            "buscd : $buscd \n " +
                            "pernr : $nik \n " +
                            "leavecode/jeniscuti : $jenisCuti \n" +
                            "leaveDescription : $deskripsi \n " +
                            "flagAllowance : $tunjangan \n " +
                            "leaveDate : $date")

            // POST
            val postConfUrl = "${baseUrl}hcis/api/leave"
            Log.d(tags, "check url post eleave : $postConfUrl")

            AndroidNetworking.upload(postConfUrl)
                    .addHeaders("Authorization", "Bearer $token")
                    .addMultipartParameter("begin_date", beginDate)
                    .addMultipartParameter("end_date", endDate)
                    .addMultipartParameter("business_code", buscd)
                    .addMultipartParameter("personnel_number", nik)
                    .addMultipartParameter("leave_code", jenisCuti)
                    .addMultipartParameter("leave_description", deskripsi)
                    .addMultipartParameter("flag_allowance", tunjangan)
                    .addMultipartParameter("cuti_besar_type", jnsCtBsr)
                    .addMultipartFile("document_id", saveImage(this, img_data))
                    .addMultipartParameter("arr_leave_date", dateToPost.toString())
                    .addMultipartParameter("mobile_post","true")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d(tags, "response submit eleave bukan cuti besar : $response")
                            val message = response?.getString("message")
                            try {

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                    finish()
//                                intentToEleaveList()
                                } else {
                                    Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {
                                Log.d(tags, "catch di post create eleave bukan cuti besar : ${e.printStackTrace()}")
                            }
                        }

                        override fun onError(anError: ANError?) {

                            var errorMessage = anError?.errorBody.toString()
                            Log.d(tags, "check value error message create eleave w evidence bukan cuti besar : $errorMessage")

                            val objOnerror = JSONObject(errorMessage)
                            val error = objOnerror.getString("message")
                            Toast.makeText(this@CreateEleave, error, Toast.LENGTH_SHORT).show()

                        }
                    })
            // WITH EVIDENCE BUKAN CUTI BESAR

        } else {

            // WITH EVIDENCE CUTI BESAR
            jnsCtBsr = jenisCutiBesar

            // check value
            Log.d(tags,
                    "check value-value : \n " +
                            "beginDate : $beginDate \n " +
                            "endDate : $endDate \n " +
                            "buscd : $buscd \n " +
                            "pernr : $nik \n " +
                            "leavecode/jeniscuti : $jenisCuti \n" +
                            "jenis cuti besar : $jnsCtBsr \n " +
                            "leaveDescription : $deskripsi \n " +
                            "flagAllowance : $tunjangan \n " +
                            "leaveDate : $date")

            // POST
            val postConfUrl = "${baseUrl}hcis/api/leave"
            Log.d(tags, "check url post eleave : $postConfUrl")

            AndroidNetworking.upload(postConfUrl)
                    .addHeaders("Authorization", "Bearer $token")
                    .addMultipartParameter("begin_date", beginDate)
                    .addMultipartParameter("end_date", endDate)
                    .addMultipartParameter("business_code", buscd)
                    .addMultipartParameter("personnel_number", nik)
                    .addMultipartParameter("leave_code", jenisCuti)
                    .addMultipartParameter("leave_description", deskripsi)
                    .addMultipartParameter("flag_allowance", tunjangan)
                    .addMultipartParameter("cuti_besar_type", jnsCtBsr)
                    .addMultipartFile("document_id", saveImage(this, img_data))
                    .addMultipartParameter("mobile_post","true")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d(tags, "response submit eleave cuti besar : $response")
                            val message = response?.getString("message")
                            try {

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                    finish()
//                                intentToEleaveList()
                                } else {
                                    Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: Exception) {
                                Log.d(tags, "catch di post create eleave cuti besar : ${e.printStackTrace()}")
                            }
                        }

                        override fun onError(anError: ANError?) {

                            val errorBody = anError?.errorBody.toString()
                            Log.d(tags, "check value error message create eleave w evidence cuti besar : $errorBody")

                            val errorObject = JSONObject(errorBody)
                            Toast.makeText(this@CreateEleave, errorObject.getString("message"), Toast.LENGTH_SHORT).show()


                        }
                    })
        }
        // WITH EVIDENCE CUTI BESAR
    }

    // post eleave without evidence
    private fun submitOneEleaveWithoutEvidence(nik: String?, buscd: String?, jenisCuti: String?,
                                            jenisCutiBesar: String?, deskripsi: String?, tunjangan: String, date: ArrayList<String>){

        val beginDate = tgl
        val endDate = "9999-12-31"
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val dateToPost = date

        // set cuti besar value
        var jnsCtBsr : String = ""
        Log.d(tags, "jenis cuti besarnya w/o evidence : $jenisCutiBesar")
        if (jenisCutiBesar == null){

            // MASUK KE BUKAN CUTI BESAR
                jnsCtBsr = ""
            // check value
            val amountOfIndexDate = dateToPost.size
            Log.d(tags, "check date list size w/o evidence bukan cuti besar : ${amountOfIndexDate} \n dateToPost value cuti besar : $dateToPost")
            Log.d(tags,
                    "check value-value w/o evidence bukan cuti besar : \n " +
                            "beginDate : $beginDate \n " +
                            "endDate : $endDate \n " +
                            "buscd : $buscd \n " +
                            "pernr : $nik \n " +
                            "leavecode/jeniscuti : $jenisCuti \n" +
                            "leaveDescription : $deskripsi \n " +
                            "flagAllowance : $tunjangan \n ")

            // POST
            val postConfUrl = "${baseUrl}hcis/api/leave"
            Log.d(tags, "check url post eleave w/o evidence bukan cuti besar : $postConfUrl")

            AndroidNetworking.upload(postConfUrl)
                    .addHeaders("Authorization", "Bearer $token")
                    .addMultipartParameter("begin_date", beginDate)
                    .addMultipartParameter("end_date", endDate)
                    .addMultipartParameter("business_code", buscd)
                    .addMultipartParameter("personnel_number", nik)
                    .addMultipartParameter("leave_code", jenisCuti)
                    .addMultipartParameter("leave_description", deskripsi)
                    .addMultipartParameter("flag_allowance", tunjangan)
                    .addMultipartParameter("cuti_besar_type", jnsCtBsr)
                    .addMultipartParameter("arr_leave_date", dateToPost.toString())
                    .addMultipartParameter("mobile_post","true")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d(tags, "response submit eleave w/o evidence bukan cuti besar : $response")
                            val message = response?.getString("message")

                            if (response?.getInt("status") == 200) {
                                Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                finish()
//                                intentToEleaveList()
                            } else {
                                Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()

                            }
                        }

                        override fun onError(anError: ANError?) {

                            var errorMessage = anError?.errorBody.toString()
                            Log.d(tags, "check value error message create eleave w/o evidence bukan cuti besar : $errorMessage ")

                            val objOnerror = JSONObject(errorMessage)
                            val error = objOnerror.getString("message")
                            Toast.makeText(this@CreateEleave, error, Toast.LENGTH_SHORT).show()

                        }
                    })
        // MASUK KE BUKAN CUTI BESAR

        } else {
            // MASUK KE JENIS CUTI BESAR
            jnsCtBsr = jenisCutiBesar

            Log.d(tags,
                    "check value-value w/o evidence cuti besar : \n " +
                            "beginDate : $beginDate \n " +
                            "endDate : $endDate \n " +
                            "buscd : $buscd \n " +
                            "pernr : $nik \n " +
                            "leavecode/jeniscuti : $jenisCuti \n" +
                            "jenis cuti besar : $jnsCtBsr \n " +
                            "leaveDescription : $deskripsi \n " +
                            "flagAllowance : $tunjangan \n " +
                            "leaveDate : $date")

            // POST
            val postConfUrl = "${baseUrl}hcis/api/leave"
            Log.d(tags, "check url post eleave w/o evidence cuti besar : $postConfUrl")

            AndroidNetworking.upload(postConfUrl)
                    .addHeaders("Authorization", "Bearer $token")
                    .addMultipartParameter("begin_date", beginDate)
                    .addMultipartParameter("end_date", endDate)
                    .addMultipartParameter("business_code", buscd)
                    .addMultipartParameter("personnel_number", nik)
                    .addMultipartParameter("leave_code", jenisCuti)
                    .addMultipartParameter("leave_description", deskripsi)
                    .addMultipartParameter("flag_allowance", tunjangan)
                    .addMultipartParameter("cuti_besar_type", jnsCtBsr)
                    .addMultipartParameter("mobile_post","true")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.d(tags, "response submit eleave w/o evidence cuti besar : $response")
                            val message = response?.getString("message")

                            if (response?.getInt("status") == 200) {
                                Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()
                                finish()
//                                intentToEleaveList()
                            } else {
                                Toast.makeText(this@CreateEleave, "$message", Toast.LENGTH_SHORT).show()

                            }

                        }

                        override fun onError(anError: ANError?) {

                            var errorMessage = anError?.errorBody.toString()
                            Log.d(tags, "check value error message create eleave w/o evidence cuti besar : $errorMessage ")

                            val objOnerror = JSONObject(errorMessage)
                            val error = objOnerror.getString("message")
                            Toast.makeText(this@CreateEleave, error, Toast.LENGTH_SHORT).show()
                        }
                    })
        }
        // MASUK KE JENIS CUTI BESAR
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}

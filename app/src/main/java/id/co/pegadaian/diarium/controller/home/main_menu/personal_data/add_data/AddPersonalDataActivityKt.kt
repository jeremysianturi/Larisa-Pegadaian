package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.*
import id.co.pegadaian.diarium.model.DataAddDataModelKt
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_add_personal_data_newlayout.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddPersonalDataActivityKt : AppCompatActivity(), View.OnClickListener {

    private val tags = AddPersonalDataActivityKt::class.java.simpleName

    private lateinit var viewModel : DataAddDataModelKt
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var dateIssue : OnDateSetListener
    private lateinit var expireDate : OnDateSetListener
    private lateinit var educationStartDate : OnDateSetListener
    private lateinit var educationEndDate : OnDateSetListener
    private lateinit var insuranceStartDate : OnDateSetListener
    private lateinit var insuranceEndDate : OnDateSetListener
    private var categoryClicked : String? = null

    private var codeChoosen : String? = ""
    private var codeChoosenFaculty : String? = ""
    private var codeChoosenMajor : String? = ""
    private var codeChoosenEducationLevel : String? = ""
    private var codeChoosenProvince : String? = ""
    private var codeChoosenCity : String? = ""
    private var codeChoosenDistrict : String? = ""
    private var codeChoosenInsuranceType : String? = ""

    private var intentNameExtraFam : String? = null
    private var intentTypeExtraFam : String? = null
    private var intentNumberExtraFam : String? = null

    private lateinit var tgl : String
    private var strListComSpinner : String? = null
    private var strListTypes : String? = null

    private lateinit var dataSpinnerClassDistrict : DataSpinnerAddDataDistrict
    private lateinit var dataSpinnerClassCity : DataSpinnerAddDataCity
    private lateinit var dataSpinnerClassProvince : DataSpinnerAddDataProvince
    private lateinit var dataSpinnerClassEducationLevel : DataSpinnerAddDataEducationLevel
    private lateinit var dataSpinnerClassMajor : DataSpinnerAddDataMajor
    private lateinit var dataSpinnerClassFaculty : DataSpinnerAddDataFaculty
    private lateinit var dataSpinnerClass : DataSpinnerAddData
    private lateinit var dataSpinnerClassInsuranceType : DataSpinnerAddDataInsuranceType
    private lateinit var dataSpinnerTypesClass : DataCategorySpinnerAddData

    private val listDistrictNumber = ArrayList<DataSpinnerAddDataDistrict>()
    private val listCityNumber = ArrayList<DataSpinnerAddDataCity>()
    private val listProvinceNumber = ArrayList<DataSpinnerAddDataProvince>()
    private val listEducationLevelNumber = ArrayList<DataSpinnerAddDataEducationLevel>()
    private val listMajorNumber = ArrayList<DataSpinnerAddDataMajor>()
    private val listFacultyNumber = ArrayList<DataSpinnerAddDataFaculty>()
    private val listComNumber = ArrayList<DataSpinnerAddData>()
    private val listInsuranceTypeNumber = ArrayList<DataSpinnerAddDataInsuranceType>()
    private val listTypesNumber = ArrayList<DataCategorySpinnerAddData>()

    private lateinit var listDataDistrict : ArrayList<String>
    private lateinit var listDataCity : ArrayList<String>
    private lateinit var listDataProvince : ArrayList<String>
    private lateinit var listDataMajor : ArrayList<String>
    private lateinit var listDataEducationLevel : ArrayList<String>
    private lateinit var listDataFaculty : ArrayList<String>
    private lateinit var listData : ArrayList<String>
    private lateinit var listDataInsuranceType : ArrayList<String>
    private lateinit var listDataCategory : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_personal_data_newlayout)

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listData = ArrayList<String>()
        listDataFaculty = ArrayList<String>()
        listDataMajor = ArrayList<String>()
        listDataEducationLevel = ArrayList<String>()
        listDataProvince = ArrayList<String>()
        listDataCity = ArrayList<String>()
        listDataDistrict = ArrayList<String>()
        listDataCategory = ArrayList<String>()
        listDataInsuranceType = ArrayList<String>()

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DataAddDataModelKt::class.java]
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        intentNameExtraFam = intent.getStringExtra("name")
        intentTypeExtraFam = intent.getStringExtra("status")
        intentNumberExtraFam = intent.getStringExtra("number")
        Log.d(tags,"Check value intentExtraFam : \n name : $intentNameExtraFam \n type : $intentTypeExtraFam \n number : $intentNumberExtraFam")

        getSpinnerTypesList()

        spinner_datatype_newlayout.setOnClickListener(this)
        spinner_data_newlayout.setOnClickListener(this)
        btn_submit_addData_newlayout.setOnClickListener(this)

        // education
        spinner_faculty.setOnClickListener(this)
        spinner_major.setOnClickListener(this)
        spinner_education_level.setOnClickListener(this)
        btn_submit_education_newlayout.setOnClickListener(this)

        // address
        btn_submit_address_newlayout.setOnClickListener(this)
        spinner_addresstype_newlayout.setOnClickListener(this)
        spinner_province_newlayout.setOnClickListener(this)
        spinner_city_newlayout.setOnClickListener(this)
        spinner_district_newlayout.setOnClickListener(this)

        // insurance
        spinner_insurance_type.setOnClickListener(this)
        btn_submit_insurance_newlayout.setOnClickListener(this)
    }


    private fun getSpinnerTypesList(){

        viewModel.setDataTypesList()

        viewModel.getDataTypesList().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listTypesNumber.clear()
                listDataCategory.clear()

                for (i in 0 until model.size) {
                    val category = model[i].name

                    Log.d(tags, "check value getSpinnerTypesList: \n model size : ${model.size} \n category : $category ")

                    dataSpinnerTypesClass = DataCategorySpinnerAddData(category)
                    listTypesNumber.add(dataSpinnerTypesClass)
                    listDataCategory.add(category.toString())

                    Log.d(tags, "listTypesNumber : ${listTypesNumber} \n listDataCategory : $listDataCategory ")
                }
            }
        })
    }

    private fun getListSpinner(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = categoryClicked

        viewModel.setSpinnerDataList(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerDataList().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listComNumber.clear()
                listData.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClass = DataSpinnerAddData(name, code)
                    listComNumber.add(dataSpinnerClass)
                    listData.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListEducationLevel(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "EDULV"

        viewModel.setSpinnerDataEducationLevel(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerDataEducationLevel().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listEducationLevelNumber.clear()
                listDataEducationLevel.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClassEducationLevel = DataSpinnerAddDataEducationLevel(name, code)
                    listEducationLevelNumber.add(dataSpinnerClassEducationLevel)
                    listDataEducationLevel.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListFaculty(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "EDUFC"

        viewModel.setSpinnerDataFaculty(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerDataFaculty().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listFacultyNumber.clear()
                listDataFaculty.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClassFaculty = DataSpinnerAddDataFaculty(name, code)
                    listFacultyNumber.add(dataSpinnerClassFaculty)
                    listDataFaculty.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListMajor(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "EDUBR"

        viewModel.setSpinnerDataMajor(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerDataMajor().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listMajorNumber.clear()
                listDataMajor.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClassMajor = DataSpinnerAddDataMajor(name, code)
                    listMajorNumber.add(dataSpinnerClassMajor)
                    listDataMajor.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }


    private fun getListAddressType(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = categoryClicked

        viewModel.setSpinnerAddressType(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerAddressType().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listComNumber.clear()
                listData.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClass = DataSpinnerAddData(name, code)
                    listComNumber.add(dataSpinnerClass)
                    listData.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListProvince(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "PRVNC"

        viewModel.setSpinnerProvince(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerProvince().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listProvinceNumber.clear()
                listDataProvince.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClassProvince = DataSpinnerAddDataProvince(name, code)
                    listProvinceNumber.add(dataSpinnerClassProvince)
                    listDataProvince.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }


    private fun getListCity(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val parentType = "PRVNC"
        val objType = "CITY"

        viewModel.setSpinnerCity(baseUrl, token, parentType, codeChoosenProvince, objType, date, bussCode)

        viewModel.getSpinnerCity().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listCityNumber.clear()
                listDataCity.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    Log.d(tags,"check di get city list : $name : $code")

                    dataSpinnerClassCity = DataSpinnerAddDataCity(name, code)
                    listCityNumber.add(dataSpinnerClassCity)
                    listDataCity.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }


    private fun getListDistrict(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val parentType = "CITY"
        val objType = "DSTRC"

        viewModel.setSpinnerDistrict(baseUrl, token, parentType, codeChoosenCity, objType, date, bussCode)

        viewModel.getSpinnerDistrict().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listDistrictNumber.clear()
                listDataDistrict.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    Log.d(tags,"check di get district list : $name : $code")

                    dataSpinnerClassDistrict = DataSpinnerAddDataDistrict(name, code)
                    listDistrictNumber.add(dataSpinnerClassDistrict)
                    listDataDistrict.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListInsuranceType(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = categoryClicked

        viewModel.setSpinnerInsuranceType(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerInsuranceType().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listInsuranceTypeNumber.clear()
                listDataInsuranceType.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerClassInsuranceType = DataSpinnerAddDataInsuranceType(name, code)
                    listInsuranceTypeNumber.add(dataSpinnerClassInsuranceType)
                    listDataInsuranceType.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }


    private fun educationCatClicked(){

        if (!intentNameExtraFam.equals("")){

            makeToast("Family member cannot add education info")
            tv_title_data.visibility = View.GONE
            tv_title_et1.visibility = View.GONE
            et1_newlayout.visibility = View.GONE
            et2_newlayout.visibility = View.GONE
            tv_title_et2.visibility = View.GONE
            spinner_data_newlayout.visibility = View.GONE
            linear_for_identification.visibility = View.GONE
            btn_submit_addData_newlayout.visibility = View.GONE
            ll_for_address.visibility = View.GONE
            ll_for_education.visibility = View.GONE
            ll_for_insurance.visibility = View.GONE

        } else {

            tv_title_data.visibility = View.GONE
            tv_title_et1.visibility = View.GONE
            et1_newlayout.visibility = View.GONE
            et2_newlayout.visibility = View.GONE
            tv_title_et2.visibility = View.GONE
            spinner_data_newlayout.visibility = View.GONE
            linear_for_identification.visibility = View.GONE
            btn_submit_addData_newlayout.visibility = View.GONE
            ll_for_address.visibility = View.GONE
            ll_for_insurance.visibility = View.GONE
            ll_for_education.visibility = View.VISIBLE

            // date picker for education
            popUpEducationStartDatePicker()
            popUpEducationEndDatePicker()

            // get list spinner education
            getListFaculty()
            getListMajor()
            getListEducationLevel()

        }
    }

    private fun insuranceCatClicked(){

        if (!intentNameExtraFam.equals("")){

            ll_for_insurance.visibility = View.VISIBLE
            ll_for_address.visibility = View.GONE
            ll_for_education.visibility = View.GONE
            linear_for_identification.visibility = View.GONE
            btn_submit_addData_newlayout.visibility = View.GONE
            getListInsuranceType()
            popUpInsuranceStartDatePicker()
            popUpInsuranceEndDatePicker()

        }  else {

            ll_for_insurance.visibility = View.VISIBLE
            ll_for_address.visibility = View.GONE
            ll_for_education.visibility = View.GONE
            linear_for_identification.visibility = View.GONE
            btn_submit_addData_newlayout.visibility = View.GONE
            getListInsuranceType()
            popUpInsuranceStartDatePicker()
            popUpInsuranceEndDatePicker()

        }
    }

    private fun addressCatClicked(){
        tv_title_data.visibility = View.GONE
        tv_title_et1.visibility = View.GONE
        et1_newlayout.visibility = View.GONE
        et2_newlayout.visibility = View.GONE
        tv_title_et2.visibility = View.GONE
        spinner_data_newlayout.visibility = View.GONE
        linear_for_identification.visibility = View.GONE
        btn_submit_addData_newlayout.visibility = View.GONE
        ll_for_education.visibility = View.GONE
        ll_for_insurance.visibility = View.GONE
        ll_for_address.visibility = View.VISIBLE

        getListAddressType()
        getListProvince()
    }

    private fun communicationCatClicked(){

        tv_title_data.text = "Tipe Alat Komunikasi"
        tv_title_data.visibility = View.VISIBLE
        tv_title_et1.text = "Alat Komunikasi Order"
        tv_title_et1.visibility = View.VISIBLE
        et1_newlayout.inputType = InputType.TYPE_CLASS_NUMBER
        et1_newlayout.visibility = View.VISIBLE
        et2_newlayout.visibility = View.VISIBLE
        tv_title_et2.text = "Nomor Alat Komunikasi"
        tv_title_et2.visibility = View.VISIBLE
        spinner_data_newlayout.visibility = View.VISIBLE
        spinner_data_newlayout.text = ""
        linear_for_identification.visibility = View.GONE
        btn_submit_addData_newlayout.visibility = View.VISIBLE
        ll_for_address.visibility = View.GONE
        ll_for_education.visibility = View.GONE
        ll_for_insurance.visibility = View.GONE

//        tvTitleData.text = "Tipe Alat Komunikasi"
//        tvTitleData.visibility = View.VISIBLE
//        tvet1.text = "Alat Komunikasi Order"
//        tvet1.visibility = View.VISIBLE
//        et1.inputType = InputType.TYPE_CLASS_NUMBER
//        et1.visibility = View.VISIBLE
//        et2.visibility = View.VISIBLE
//        tvet2.text = "Nomor Alat Komunikasi"
//        tvet2.visibility = View.VISIBLE
//        dataSpinner.visibility = View.VISIBLE
//        linearIdentification.visibility = View.GONE
//        btnSubmit.visibility = View.VISIBLE
        getListSpinner()
    }

    private fun identificationCatClicked(){

        tv_title_data.text = "Tipe Identitas"
        tv_title_data.visibility = View.VISIBLE
        tv_title_et1.text = "Place Issue"
        tv_title_et1.visibility = View.VISIBLE
        et1_newlayout.inputType = InputType.TYPE_CLASS_TEXT
        et1_newlayout.visibility = View.VISIBLE
        et2_newlayout.visibility = View.VISIBLE
        tv_title_et2.text = "Identification Number"
        tv_title_et2.visibility = View.VISIBLE
        linear_for_identification.visibility = View.VISIBLE
        spinner_data_newlayout.visibility = View.VISIBLE
        btn_submit_addData_newlayout.visibility = View.VISIBLE
        spinner_data_newlayout.text = ""
        ll_for_address.visibility = View.GONE
        ll_for_education.visibility = View.GONE
        ll_for_insurance.visibility = View.GONE

//        tvTitleData.text = "Tipe Identitas"
//        tvTitleData.visibility = View.VISIBLE
//        tvet1.text = "Place Issue"
//        tvet1.visibility = View.VISIBLE
//        et2.visibility = View.VISIBLE
//        et1.inputType = InputType.TYPE_CLASS_TEXT
//        et1.visibility = View.VISIBLE
//        tvet2.text = "Identification Number"
//        tvet2.visibility = View.VISIBLE
//        linearIdentification.visibility = View.VISIBLE
//        dataSpinner.visibility = View.VISIBLE
//        btnSubmit.visibility = View.VISIBLE

        popUpDateIssuePicker()
        popUpExpireDatePicker()
        getListSpinner()
    }

//  DATE ISSUE PICKER

    private fun popUpDateIssuePicker(){

        dateIssue = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelDateIssue()
        }
        tv_dateissue_identification.setOnClickListener(this)
    }

    private fun updateLabelDateIssue(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_dateissue_identification.text = sdf.format(myCalendar.time)
    }

//  DATE ISSUE PICKER


//  EXPIRE DATE PICKER

    private fun popUpExpireDatePicker(){
        expireDate = OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelExpireDate()
        }

        tv_expiredate_identification.setOnClickListener(this)
    }

    private fun updateLabelExpireDate(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_expiredate_identification.text = sdf.format(myCalendar.time)
    }

//  EXPIRE DATE PICKER

//  EDUCATION START DATE PICKER

    private fun popUpEducationStartDatePicker(){
        educationStartDate = OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelEducationStartDate()
        }

        tv_education_startdate.setOnClickListener(this)
    }

    private fun updateLabelEducationStartDate(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_education_startdate.text = sdf.format(myCalendar.time)
    }

//  EDUCATION START DATE PICKER

//  EDUCATION END DATE PICKER

    private fun popUpEducationEndDatePicker(){
        educationEndDate = OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelEducationEndDate()
        }

        tv_education_enddate.setOnClickListener(this)
    }

    private fun updateLabelEducationEndDate(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_education_enddate.text = sdf.format(myCalendar.time)
    }

//  EDUCATION END DATE PICKER

//  INSURANCE START DATE PICKER
    private fun popUpInsuranceStartDatePicker(){
        insuranceStartDate = OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelInsuranceStartDate()
        }

    tv_insurance_startdate.setOnClickListener(this)
    }

    private fun updateLabelInsuranceStartDate(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_insurance_startdate.text = sdf.format(myCalendar.time)
    }
//  INSURANCE START DATE PICKER

//  INSURANCE END DATE PICKER
    private fun popUpInsuranceEndDatePicker(){
        insuranceEndDate = OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelInsuranceEndDate()
        }

        tv_insurance_enddate.setOnClickListener(this)
    }

    private fun updateLabelInsuranceEndDate(){
        val myFormat : String = "yyyy-MM-dd"
        val sdf : SimpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        tv_insurance_enddate.text = sdf.format(myCalendar.time)
    }
//  INSURANCE END DATE PICKER



    private fun getAllValueAddress(actFrom: String?){

        if (ll_for_address.visibility == View.VISIBLE){

            // masuk ke submit address

            if (spinner_addresstype_newlayout.text.equals("")){
                makeToast("Address type is empty")
            } else if (spinner_province_newlayout.text.equals("")){
                makeToast("Please select province")
            } else if (spinner_city_newlayout.text.equals("")){
                makeToast("Please select city")
            } else if (spinner_district_newlayout.text.equals("")){
                makeToast("Please select district")
            } else if (spinner_addressnum_newlayout.text.toString() == ""){
                makeToast("Please input address order number (Urutan alamat)")
            } else if (spinner_street_newlayout.text.toString() == ""){
                makeToast("Please input street name")
            } else if (spinner_postalcode_newlayout.text.toString() == ""){
                makeToast("Please input postal code")
            } else {
                Log.d(tags,"Masuk ke submit : h${spinner_addressnum_newlayout.text}h")
                submitPostData("address",actFrom)
            }

        } else if (ll_for_education.visibility == View.VISIBLE){

            // masuk ke submit education
            if (et_intitution.text.toString().equals("")){
                makeToast("Please fill institution field!")
            } else if (spinner_faculty.text.equals("")){
                makeToast("Please select faculty field!")
            } else if (spinner_major.text.equals("")){
                makeToast("Please select major field")
            } else if (et_submajor.text.toString().equals("")){
                makeToast("Please fill subMajor field!")
            } else if (spinner_education_level.text.equals("")){
                makeToast("Please select education level field!")
            } else if (tv_education_startdate.text.equals("")){
                makeToast("Please fill start date field!")
            } else if (tv_education_enddate.text.equals("")){
                makeToast("Please fill end date field!")
            } else if (et_academic_score.text.toString().equals("")){
                makeToast("Please fill academic score field!")
            } else {
                submitPostData("education",actFrom)
            }

        } else {

            // masuk ke submit insurance
            if (spinner_insurance_type.text.equals("")){
                makeToast("Please select insurance type!")
            } else if (et_insurance_company.text.toString().equals("")) {
                makeToast("Please fill insurance company!")
            } else if (et_insurance_number.text.toString().equals("")){
                makeToast("Please fill insurance number!")
            } else if (tv_insurance_startdate.text.toString().equals("")){
                makeToast("Please select insurance date issued!")
            } else if (tv_insurance_enddate.text.toString().equals("")) {
                makeToast("Please select insurance expired date!")
            } else if (et_insurance_companypaid_ammount.text.toString().equals("")) {
                makeToast("Please fill company paid ammount!")
            } else if (et_insurance_percentage.text.toString().equals("")) {
                makeToast("Please fill company precentage!")
            } else if (et_insurance_employeepaid_ammount.text.toString().equals("")) {
                makeToast("Please fill employee paid ammount!")
            } else if (et_insurance_percentageemployee.text.toString().equals("")) {
                makeToast("Please fill employee precentage!")
            } else {
                submitPostData("insurance",actFrom)
            }



        }

    }

    // POST PERSONAL DATA
    private fun submitPostData(category: String?,actFrom : String?){

        // general
        val urlPostData = session.serverURLHCIS
        val token = session.token

        // for identification & communication
        val beginDate = tgl
        val endDate = "9999-12-31"
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        val type = codeChoosen
        val typeOrder = et1_newlayout.text
        val typeNumber = et2_newlayout.text
        val changeUser = session.userNIK
        val dateIssue = tv_dateissue_identification.text
        val expireDate = tv_expiredate_identification.text
        val familyType = intentTypeExtraFam
        val familyNumber = intentNumberExtraFam

        // for address
        val addressType = codeChoosen
        val addressOrderNumber = spinner_addressnum_newlayout.text
        val streetName = spinner_street_newlayout.text
        val postalCode = spinner_postalcode_newlayout.text
        val city = codeChoosenCity
        val province = codeChoosenProvince
        val districtCode = codeChoosenDistrict

        // for education
        val intitution = et_intitution.text.toString()
        val educationLevelCode = codeChoosenEducationLevel
        val facultyCode = codeChoosenFaculty
        val majorCode = codeChoosenMajor
        val subMajor = et_submajor.text.toString()
        val educationStartDate = tv_education_startdate.text.toString()
        val educationEndDate = tv_education_enddate.text.toString()
        val academicScore = et_academic_score.text.toString()

        // for insurance
        val insuranceType = codeChoosenInsuranceType
        val insuranceCompany = et_insurance_company.text.toString()
        val insuranceNumber = et_insurance_number.text.toString()
        val insuranceStartDate = tv_insurance_startdate.text.toString()
        val insuranceEndDate = tv_insurance_enddate.text.toString()
        val companyPaid = et_insurance_companypaid_ammount.text.toString()
        val companyPaidPercentage = et_insurance_percentage.text.toString()
        val employeePaid = et_insurance_employeepaid_ammount.text.toString()
        val employeePaidPercentage = et_insurance_percentageemployee.text.toString()


        var varResponse : String? = null
        var varError : String? = null

        if (actFrom.equals("personal")){                        // PERSONAL

            if (category.equals("communication")){

                val postComUrl ="${urlPostData}hcis/api/$category"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("communication_type", type)
                body.put("communication_order", typeOrder)
                body.put("communication_number", typeNumber)
                body.put("change_user", changeUser)

                AndroidNetworking.post(postComUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response.toString()
                                Log.d(tags, "var response's personal value com : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {

                                    if (response?.get("message")!!.equals("integer out of range${'\n'}")) {
                                        Toast.makeText(this@AddPersonalDataActivityKt, "Masukan alat komunikasi order lebih rendah!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@AddPersonalDataActivityKt, response.getString("message"), Toast.LENGTH_SHORT).show()
                                    }

                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError.toString()
                                Log.d(tags, "var error's personal value com : $varError")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postComUrl,varResponse,varError)
                Log.d(tags,"ini napaa ya : $varResponse")

            } else if (category.equals("identification")){

                val postIdenUrl = "${urlPostData}hcis/api/$category"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("identification_type", type)
                body.put("place_issue", typeOrder) // dinaikin
                body.put("identification_number", typeNumber)
                body.put("date_issue", dateIssue)
                body.put("expire_date", expireDate)
                body.put("change_user", changeUser)

                AndroidNetworking.post(postIdenUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response.toString()
                                Log.d(tags, "var response's personal value iden : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError.toString()
                                Log.d(tags, "var error's personal value iden : $varError")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postIdenUrl,varResponse,varError)
            } else if (category.equals("address")){

                val postAddressUrl = "${urlPostData}hcis/api/employeeaddress"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("address_type", addressType)
                body.put("address_number", addressOrderNumber)
                body.put("street", streetName)
                body.put("postal_code", postalCode)
                body.put("city", city)
                body.put("province", province)
                body.put("country", "Indonesia")
                body.put("district", districtCode)
                body.put("change_user", pernr)

                Log.d(tags,"check body di post address personal data : $body")

                AndroidNetworking.post(postAddressUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response?.getString("message").toString()
                                Log.d(tags, "var response's personal value address : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError?.errorBody.toString()
                                Log.d(tags, "var error's personal value address : ${varError}")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postAddressUrl,varResponse,varError)

            } else if (category.equals("education")){

                val postAddressUrl = "${urlPostData}hcis/api/education"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("level", educationLevelCode)
                body.put("department", majorCode)
                body.put("sub_department", subMajor)
                body.put("faculty", facultyCode)
                body.put("institution", intitution)
                body.put("certificate", "")
                body.put("funding", "")
                body.put("recognition", "")
                body.put("recognition_date", educationEndDate)
                body.put("education_start", educationStartDate)
                body.put("education_end", educationEndDate)
                body.put("nilai", academicScore)

                AndroidNetworking.post(postAddressUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response?.getString("message").toString()
                                Log.d(tags, "var response's personal value education : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError?.errorBody.toString()
                                Log.d(tags, "var error's personal value education : ${varError}")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postAddressUrl,varResponse,varError)

            } else if (category.equals("insurance")){

                val postInsuranceUrl = "${urlPostData}hcis/api/insurance"

                val body = JSONObject()
                body.put("begin_date", insuranceStartDate)
                body.put("end_date", insuranceEndDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("insurance_type", insuranceType)
                body.put("insurance_number", insuranceNumber)
                body.put("insurance_company", insuranceCompany)
                body.put("amount_paid_employer", companyPaid)
                body.put("percent_paid_employer", companyPaidPercentage)
                body.put("amount_paid_employee", employeePaid)
                body.put("percent_paid_employee", employeePaidPercentage)
                body.put("change_user", pernr)

                AndroidNetworking.post(postInsuranceUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response?.getString("message").toString()
                                Log.d(tags, "var response's personal value insurance : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError?.errorBody.toString()
                                Log.d(tags, "var error's personal value insurance : ${varError}")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postInsuranceUrl,varResponse,varError)
            }

        } else if (actFrom.equals("family")){                   // FAMILY

            if (category.equals("communication")){

                val postComUrl ="${urlPostData}hcis/api/family$category"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("communication_type", type)
                body.put("serial_number", typeOrder)
                body.put("communication_number", typeNumber)
                body.put("family_type",familyType)
                body.put("family_number",familyNumber)
                body.put("change_user", changeUser)

                AndroidNetworking.post(postComUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response.toString()
                                Log.d(tags, "var response's family value com : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {

                                    if (response?.get("message")!!.equals("integer out of range${'\n'}")) {
                                        Toast.makeText(this@AddPersonalDataActivityKt, "Masukan alat komunikasi order lebih rendah!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this@AddPersonalDataActivityKt, response.getString("message"), Toast.LENGTH_SHORT).show()
                                    }

                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError.toString()
                                Log.d(tags, "var error's family value com : $varError")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postComUrl,varResponse,varError)
                Log.d(tags,"ini napaa ya : $varResponse")

            } else if (category.equals("identification")){

                val postIdenUrl = "${urlPostData}hcis/api/family$category"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("identification_type", type)
                body.put("place_issue", typeOrder) // dinaikin
                body.put("identification_number", typeNumber)
                body.put("date_issue", dateIssue)
                body.put("expire_date", expireDate)
                body.put("family_type",familyType)
                body.put("family_number",familyNumber)
                body.put("change_user", changeUser)
                Log.d(tags,"check value body family data identification : $body")

                AndroidNetworking.post(postIdenUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response.toString()
                                Log.d(tags, "var response's family value iden : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                val errorObject = JSONObject(anError?.errorBody.toString())
                                val messageError = errorObject.getString("message")
                                Log.d(tags, "var error's family value iden : $messageError")
                                Toast.makeText(this@AddPersonalDataActivityKt, messageError, Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postIdenUrl,varResponse,varError)
            } else if (category.equals("address")){

                val postAddressUrl = "${urlPostData}hcis/api/familyaddress"

                val body = JSONObject()
                body.put("begin_date", beginDate)
                body.put("end_date", endDate)
                body.put("business_code", buscd)
                body.put("personnel_number", pernr)
                body.put("family_type",familyType)
                body.put("family_number",familyNumber)
                body.put("address_type", addressType)
                body.put("address_number", addressOrderNumber)
                body.put("street", streetName)
                body.put("postal_code", postalCode)
                body.put("city", city)
                body.put("province", province)
                body.put("country", "Indonesia")
                body.put("district", districtCode)
                body.put("change_user", pernr)

                Log.d(tags,"check body address di family member personal data : $body")

                AndroidNetworking.post(postAddressUrl)
                        .addHeaders("Accept","application/json")
                        .addHeaders("Content-Type","application/json")
                        .addHeaders("Authorization","Bearer $token")
                        .addJSONObjectBody(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                varResponse = response.toString()
                                Log.d(tags, "var response's personal value address : $varResponse")

                                if (response?.getInt("status") == 200) {
                                    Toast.makeText(this@AddPersonalDataActivityKt, "Success!", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@AddPersonalDataActivityKt, response?.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onError(anError: ANError?) {
                                varError = anError?.errorBody.toString()
                                Log.d(tags, "var error's personal value address : $varError")
                                Toast.makeText(this@AddPersonalDataActivityKt, "Error!", Toast.LENGTH_SHORT).show()
                            }
                        })
                responseOrError(category,body,postAddressUrl,varResponse,varError)

            }

        }
    }

    private fun responseOrError(category : String? ,body : JSONObject , postUrl : String?, response : String?, onError : String?){
        Log.d(tags,"test isi category : $category")
        if (category.equals("communication")){
            Log.d(tags, "Submit post data communication info: \n " +
                    "Post body : $body \n" +
                    "Url post : $postUrl \n" +
                    "response post : $response \n" +
                    "error post : $onError \n")

        } else if (category.equals("identification")) {

            Log.d(tags, "Submit post data identification info: \n " +
                    "Post body : $body \n" +
                    "Url post : $postUrl \n" +
                    "response post : $response \n" +
                    "error post : $onError \n")

        } else if (category.equals("address")) {

            Log.d(tags, "Submit post data address info: \n " +
                    "Post body : $body \n" +
                    "Url post : $postUrl \n" +
                    "response post : $response \n" +
                    "error post : $onError \n")

        }
    }

    private fun makeToast(message : String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun changeProvince(){

        ll_city.visibility = View.VISIBLE
        spinner_city_newlayout.text = ""

        ll_district.visibility = View.GONE
        spinner_district_newlayout.text = ""

        ll_edittext_dan_button.visibility = View.GONE
        spinner_addressnum_newlayout.text.clear()
        spinner_street_newlayout.text.clear()
        spinner_postalcode_newlayout.text.clear()

    }

    private fun changeCity(){

        ll_district.visibility = View.VISIBLE
        spinner_district_newlayout.text = ""

        ll_edittext_dan_button.visibility = View.GONE
        spinner_addressnum_newlayout.text.clear()
        spinner_street_newlayout.text.clear()
        spinner_postalcode_newlayout.text.clear()
    }


    override fun onClick(v: View?) {
        when(v?.id){

            R.id.spinner_datatype_newlayout -> {

                var category: String? = null
                var code: String? = null
                val spinnerDialogDataTypes = SpinnerDialog(
                        this, listDataCategory, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogDataTypes.bindOnSpinerListener { s, i ->
                    category = listTypesNumber[i].name

                    Log.d(tags, "check value category onClick \n category : $category \n code : $code")

                    spinner_datatype_newlayout.text = category
                    strListTypes = code

                    if (category == "Communication") {
                        Log.d(tags, "masuk ke communication")
                        categoryClicked = "COMTY"
                        communicationCatClicked()
                    } else if (category == "Identification") {
                        Log.d(tags, "masuk ke identification")
                        categoryClicked = "IDTYP"
                        identificationCatClicked()
                    } else if (category == "Address"){
                        Log.d(tags,"masuk ke address")
                        categoryClicked = "ADDTY"
                        addressCatClicked()
                    } else if (category == "Education") {
                        Log.d(tags,"masuk ke education")
                        educationCatClicked()
                    } else if (category == "Insurance") {
                        Log.d(tags,"masuk ke insurance")
                        categoryClicked = "INSTY"
                        insuranceCatClicked()
                    }
                    Log.d(tags, "check value category clicked : $categoryClicked")
                }
                spinnerDialogDataTypes.showSpinerDialog()
            }

            R.id.spinner_data_newlayout -> {
                val spinnerDialog = SpinnerDialog(
                        this, listData, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listComNumber[i].dataName
                    val code = listComNumber[i].dataCode

                    spinner_data_newlayout.text = name
                    codeChoosen = code

                    // check if ktp, end date dissapear (karna ktp seumur hidup)
                    if (name.equals("KTP")){
                        tv_expiredate_identification.text = "9999-12-31"
                        linear_right_identification.visibility = View.INVISIBLE
                    } else {
                        tv_expiredate_identification.text = ""
                        linear_right_identification.visibility = View.VISIBLE
                    }

                }
                spinnerDialog.showSpinerDialog()

            }

            // FACULTY
            R.id.spinner_faculty -> {
                val spinnerDialog = SpinnerDialog(
                        this, listDataFaculty, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listFacultyNumber[i].dataName
                    val code = listFacultyNumber[i].dataCode

                    spinner_faculty.text = name
                    codeChoosenFaculty = code

                }
                spinnerDialog.showSpinerDialog()
            }

            // MAJOR
            R.id.spinner_major -> {
                val spinnerDialog = SpinnerDialog(
                        this, listDataMajor, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listMajorNumber[i].dataName
                    val code = listMajorNumber[i].dataCode

                    spinner_major.text = name
                    codeChoosenMajor = code

                }
                spinnerDialog.showSpinerDialog()
            }

            // EDUCATION LEVEL
            R.id.spinner_education_level -> {
                val spinnerDialog = SpinnerDialog(
                        this, listDataEducationLevel, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listEducationLevelNumber[i].dataName
                    val code = listEducationLevelNumber[i].dataCode

                    spinner_education_level.text = name
                    codeChoosenEducationLevel = code

                }
                spinnerDialog.showSpinerDialog()
            }

            // ADDRESS TYPE (RUMAH DINAS DLL)
            R.id.spinner_addresstype_newlayout -> {
                val spinnerDialog = SpinnerDialog(
                        this, listData, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listComNumber[i].dataName
                    val code = listComNumber[i].dataCode

                    spinner_addresstype_newlayout.text = name
                    codeChoosen = code

                }
                spinnerDialog.showSpinerDialog()
            }

            // PROVINCE
            R.id.spinner_province_newlayout -> {
                val spinnerDialog = SpinnerDialog(
                        this, listDataProvince, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listProvinceNumber[i].dataName
                    val code = listProvinceNumber[i].dataCode

                    spinner_province_newlayout.text = name
                    codeChoosenProvince = code
                    Log.d(tags,"tes code chosen province : $codeChoosenProvince")

                    // set layout
                    changeProvince()

                    // getting city relation
                    getListCity()
                }
                spinnerDialog.showSpinerDialog()


            }

            // CITY
            R.id.spinner_city_newlayout -> {

                Log.d(tags,"check list data city di on click : $listDataCity")

                val spinnerDialog = SpinnerDialog(
                        this, listDataCity, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listCityNumber[i].dataName
                    val code = listCityNumber[i].dataCode

                    spinner_city_newlayout.text = name
                    codeChoosenCity = code

                    // set layout
                    changeCity()

                    // getting district relation
                    getListDistrict()
                }
                spinnerDialog.showSpinerDialog()
            }

            // DISTRICT
            R.id.spinner_district_newlayout -> {



                val spinnerDialog = SpinnerDialog(
                        this, listDataDistrict, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listDistrictNumber[i].dataName
                    val code = listDistrictNumber[i].dataCode

                    spinner_district_newlayout.text = name
                    codeChoosenDistrict = code

                    ll_edittext_dan_button.visibility = View.VISIBLE

                }
                spinnerDialog.showSpinerDialog()
            }


            R.id.btn_submit_addData_newlayout -> {

                if (!intentNameExtraFam.equals("")){

                    // pamily aktivet
                    if (categoryClicked.equals("COMTY")){
                        submitPostData("communication","family")
                    } else if (categoryClicked.equals("IDTYP")) {
                        submitPostData("identification","family")
                    }

                } else {

                    // personal aktivet
                    if (categoryClicked.equals("COMTY")) {
                        submitPostData("communication","personal")
                    } else if (categoryClicked.equals("IDTYP")) {
                        submitPostData("identification","personal")
                    }

                }
            }

            // submit address
            R.id.btn_submit_address_newlayout -> {
                Log.d(tags,"check intent value di button submit address : \n intent : $intentNameExtraFam")
                if (!intentNameExtraFam.equals("")){

                    Log.d(tags,"masuk ke pamili aktivet address")
                    // pamily aktivet
                    getAllValueAddress("family")

                } else {

                    Log.d(tags,"masuk ke personal aktivet address")

                    // personal aktivet
                    getAllValueAddress("personal")
                }
            }

            // submit education
            R.id.btn_submit_education_newlayout -> {
                Log.d(tags,"check intent value di button submit education : \n intent : $intentNameExtraFam")
                if (!intentNameExtraFam.equals("")){

                    Log.d(tags,"masuk ke pamili aktivet education")
                    // pamily aktivet
                    getAllValueAddress("family")

                } else {

                    Log.d(tags,"masuk ke personal aktivet education")

                    // personal aktivet
                    getAllValueAddress("personal")
                }
            }

            // submit insurance
            R.id.btn_submit_insurance_newlayout -> {
                Log.d(tags,"check intent value di button submit insurance : \n intent : $intentNameExtraFam")
                if (!intentNameExtraFam.equals("")){

                    Log.d(tags,"masuk ke pamili aktivet insurance")
                    // pamily aktivet
                    getAllValueAddress("family")

                } else {

                    Log.d(tags,"masuk ke personal aktivet insurance")

                    // personal aktivet
                    getAllValueAddress("personal")
                }
            }


            R.id.tv_expiredate_identification -> {
                DatePickerDialog(this, expireDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.tv_dateissue_identification -> {
                DatePickerDialog(this, dateIssue, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.tv_education_startdate -> {
                DatePickerDialog(this, educationStartDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.tv_education_enddate -> {
                DatePickerDialog(this, educationEndDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.spinner_insurance_type -> {
                val spinnerDialog = SpinnerDialog(
                        this, listDataInsuranceType, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listInsuranceTypeNumber[i].dataName
                    val code = listInsuranceTypeNumber[i].dataCode

                    spinner_insurance_type.text = name
                    codeChoosenInsuranceType = code
                    Log.d(tags,"tes code chosen insurance : $codeChoosenInsuranceType")

                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.tv_insurance_startdate -> {
                DatePickerDialog(this, insuranceStartDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.tv_insurance_enddate -> {
                DatePickerDialog(this, insuranceEndDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }


}
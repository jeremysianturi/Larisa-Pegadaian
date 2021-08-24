package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.familydata

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.*
import id.co.pegadaian.diarium.model.AddFamilyMemberModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_add_family_member.*
import kotlinx.android.synthetic.main.activity_add_personal_data_newlayout.*
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddFamilyMember : AppCompatActivity(), View.OnClickListener {

    private val tags = AddFamilyMember::class.java.simpleName
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private lateinit var viewModel : AddFamilyMemberModel

    private lateinit var dataSpinnerFamilyType : DataSpinnerAddFamilyMemberFamilyType
    private lateinit var dataSpinnerFamilyGender : DataSpinnerAddFamilyMemberGender
    private lateinit var dataSpinnerFamilyReligion : DataSpinnerAddFamilyMemberReligion
    private lateinit var dataSpinnerFamilyStatus : DataSpinnerAddFamilyMemberFamilyStatus
    private lateinit var dataSpinnerMaritalStatus : DataSpinnerAddFamilyMemberMaritalStatus

    private val listFamilyType = ArrayList<DataSpinnerAddFamilyMemberFamilyType>()
    private val listGender = ArrayList<DataSpinnerAddFamilyMemberGender>()
    private val listReligion = ArrayList<DataSpinnerAddFamilyMemberReligion>()
    private val listFamilyStatus = ArrayList<DataSpinnerAddFamilyMemberFamilyStatus>()
    private val listMaritalStatus = ArrayList<DataSpinnerAddFamilyMemberMaritalStatus>()

    private lateinit var listData : ArrayList<String>
    private lateinit var listDataGender : ArrayList<String>
    private lateinit var listDataReligion : ArrayList<String>
    private lateinit var listDataFamilyStatus : ArrayList<String>
    private lateinit var listDataMaritalStatus : ArrayList<String>


    private var codeChoosenFamilyType : String? = ""
    private var codeChoosenGender : String? = ""
    private var codeChoosenReligion : String? = ""
    private var codeChoosenFamilyStatus : String? = ""
    private var codeChoosenMaritalStatus : String? = ""


    // for date picker
    private lateinit var birthDate : OnDateSetListener
    private lateinit var maritalDate : OnDateSetListener
    private lateinit var familyStatusDate : OnDateSetListener
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var tgl : String
    private lateinit var duplicateBirthDate : String
    private lateinit var duplicateMaritalDate : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_family_member)

        setTitle("Add family member")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AddFamilyMemberModel::class.java]

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listData = ArrayList<String>()
        listDataGender = ArrayList<String>()
        listDataReligion = ArrayList<String>()
        listDataFamilyStatus = ArrayList<String>()
        listDataMaritalStatus = ArrayList<String>()
        duplicateBirthDate = ""
        duplicateMaritalDate = ""

        // for spinner date
        popUpBirthDate()
        popUpMaritalDate()
        popUpFamilyStatusDate()

        // get spinner list
        getListSpinner()
        getListSpinnerGender()
        getListSpinnerReligion()
        getListSpinnerFamilyStatus()
        getListSpinnerMaritalStatus()


        spinner_family_type.setOnClickListener(this)
        spinner_family_gender.setOnClickListener(this)
        spinner_family_religion.setOnClickListener(this)
        spinner_familystatus.setOnClickListener(this)
        spinner_marital_status.setOnClickListener(this)
        btn_submit_add_familymember.setOnClickListener(this)

    }

    private fun getListSpinner(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "FAMTY"

        viewModel.setSpinnerFamilyType(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerFamilyType().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listFamilyType.clear()
                listData.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerFamilyType = DataSpinnerAddFamilyMemberFamilyType(name, code)
                    listFamilyType.add(dataSpinnerFamilyType)
                    listData.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListSpinnerGender(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "GENDR"

        viewModel.setSpinnerFamilyGender(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerFamilyGender().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listGender.clear()
                listDataGender.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerFamilyGender = DataSpinnerAddFamilyMemberGender(name, code)
                    listGender.add(dataSpinnerFamilyGender)
                    listDataGender.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListSpinnerReligion(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "RELIG"

        viewModel.setSpinnerFamilyReligion(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerFamilyReligion().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listReligion.clear()
                listDataReligion.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerFamilyReligion = DataSpinnerAddFamilyMemberReligion(name, code)
                    listReligion.add(dataSpinnerFamilyReligion)
                    listDataReligion.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }


    private fun getListSpinnerFamilyStatus(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "FAMST"

        viewModel.setSpinnerFamilyStatus(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerFamilyStatus().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listFamilyStatus.clear()
                listDataFamilyStatus.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerFamilyStatus = DataSpinnerAddFamilyMemberFamilyStatus(name, code)
                    listFamilyStatus.add(dataSpinnerFamilyStatus)
                    listDataFamilyStatus.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    private fun getListSpinnerMaritalStatus(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val bussCode = session.userBusinessCode
        val objType = "MRTST"

        viewModel.setSpinnerMaritalStatus(baseUrl, token, date, bussCode, objType)

        viewModel.getSpinnerMaritalStatus().observe(this, Observer { model ->
            Log.d(tags, "check value model list spinner : $model")

            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
            } else {

                listMaritalStatus.clear()
                listDataMaritalStatus.clear()

                for (i in 0 until model.size) {
                    val name = model[i].dataName
                    val code = model[i].dataCode

                    dataSpinnerMaritalStatus = DataSpinnerAddFamilyMemberMaritalStatus(name, code)
                    listMaritalStatus.add(dataSpinnerMaritalStatus)
                    listDataMaritalStatus.add(name.toString())
                }
                progressDialogHelper.dismissProgressDialog(this)

            }
        })
    }

    //  date birth
    private fun popUpBirthDate(){

        birthDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelBirthDate()
        }

        iv_datepicker_birthdate.setOnClickListener(this)
    }

    private fun updateLabelBirthDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_pc_birthdate.text = sdf.format(myCalendar.time)

        // untuk date family status
        duplicateBirthDate = sdf.format(myCalendar.time)
        if (ll_date_family_status_date.visibility == View.VISIBLE && spinner_familystatus.text.equals("HIDUP")){
            tv_pc_date_familystatus.text = duplicateBirthDate
        }

        // untuk date marital kalo belom pernah menikah
        duplicateMaritalDate = sdf.format(myCalendar.time)
    }

    // family status date

    private fun popUpFamilyStatusDate(){

        familyStatusDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFamilyStatusDate()
        }

        iv_datepicker_date_familystatus.setOnClickListener(this)
    }

    private fun updateLabelFamilyStatusDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_pc_date_familystatus.text = sdf.format(myCalendar.time)

    }


    //  marital date
    private fun popUpMaritalDate(){

        maritalDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelMaritalDate()
        }

        iv_datepicker_marital.setOnClickListener(this)
    }

    private fun updateLabelMaritalDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_pc_date_marital.text = sdf.format(myCalendar.time)

    }


    private fun checkingEmptyFieldnGetAllValue(){

        // assign to variable
        val familyType = spinner_family_type.text.toString()
        val familyOrderNumber = et_familynumber.text.toString()
        val familyName = et_familyname.text.toString()
        val familyNickname = et_familynickname.text.toString()
        val familyBirthplace = et_family_birthplace.text.toString()
        val familyBirthdate = tv_pc_birthdate.text.toString()
        val familyGender = tv_title_gender.text.toString()
        val familyReligion = spinner_family_religion.text.toString()
        val familyStatus = spinner_familystatus.text.toString()
        val familyStatusDate = tv_pc_date_familystatus.text.toString()
        val familyJobtitle = et_family_jobtitle.text.toString()
        val familyCompanyName = et_family_jobcompany.text.toString()
        val familyMaritalStatus = spinner_marital_status.text.toString()
        val familyMaritalDate = tv_pc_date_marital.text.toString()


        // checkin empty field
        if (familyType.equals("")){
            makeToast("Please input family type field ")
        } else if (familyOrderNumber.equals("")){
            makeToast("Please input order number field!")
        } else if (familyName.equals("")){
            makeToast("Please input name field")
        } else if (familyNickname.equals("")){
            makeToast("Please input nickname field")
        } else if (familyBirthplace.equals("")){
            makeToast("Please input birthplace field")
        } else if (familyBirthdate.equals("")){
            makeToast("Please input birthdate field")
        } else if (familyGender.equals("")){
            makeToast("Please input gender field!")
        } else if (familyReligion.equals("")){
            makeToast("Please input religion field!")
        } else if (familyStatus.equals("")){
            makeToast("Please input family status field!")
        } else if (familyStatusDate.equals("")){
            makeToast("Please fill date of death / birthdate")
        }
//        else if (familyJobtitle.equals("")){
//            makeToast("Please input jobtitle field!")
//        } else if (familyCompanyName.equals("")){
//            makeToast("Please input company name!")
//        }
        else if (familyMaritalStatus.equals("")){
            makeToast("Please input marital status field!")
        }
//        else if (familyMaritalDate.equals("")){
//            makeToast("Please input marital date field!")
//        }



        // buat check date
//        if (checkDate(tgl,date)){
//            submitPresenceConfirmation(nik, name, buscd, date, jenisInCode, description)
//        } else {
//            Toast.makeText(this, "Start date cannot be greater than end date", Toast.LENGTH_SHORT).show()
//        }


        // submit adding family member
        submitAddingFamMember(familyType, familyOrderNumber, familyName, familyNickname, familyBirthplace, familyBirthdate, familyGender, familyReligion, familyStatus, familyStatusDate, familyJobtitle,
                familyCompanyName, familyMaritalStatus, familyMaritalDate)


    }

    private fun submitAddingFamMember(familyType: String?, familyOrderNumber: String?, name: String?, nickname: String?, birthplace: String?, birthdate: String?,
                                      gender: String?, religion: String?, familyStatus: String?, familyStatusDate: String?, jobTitle: String?, company: String?, maritalStatus: String?, maritalDate: String?){

        val urlPostData = session.serverURLHCIS
        val token = session.token
        val beginDate = tgl
        val endDate = "9999-12-31"
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        var varResponse : String? = null
        var varError : String? = null

        val postAddMemberUrl ="${urlPostData}hcis/api/datafamily"

        val body = JSONObject()
        body.put("begin_date", beginDate)
        body.put("end_date", endDate)
        body.put("business_code", buscd)
        body.put("personnel_number", pernr)
        body.put("family_type", codeChoosenFamilyType)
        body.put("change_date", tgl)
        body.put("change_user", pernr)
        body.put("family_number", familyOrderNumber)
        body.put("family_name", name)
        body.put("nickname", nickname)
        body.put("birth_place", birthplace)
        body.put("birth_date", birthdate)
        body.put("gender", codeChoosenGender)
        body.put("religion", codeChoosenReligion)
        body.put("tax_flag", "y")
        body.put("med_flag", "y")
        body.put("family_status", codeChoosenFamilyStatus)
        body.put("family_status_date", familyStatusDate)
        body.put("report_date", tgl)
        body.put("job_title_family", jobTitle)
        body.put("family_work", company)
        body.put("marital_date", maritalDate)
        body.put("marital_status", codeChoosenMaritalStatus)


        AndroidNetworking.post(postAddMemberUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        varResponse = response.toString()
                        Log.d(tags, "var response's adding family member : $varResponse")

                        if (response?.getInt("status") == 200) {
                            makeToast("Success")
                            finish()
                        } else {
                            makeToast(response?.getString("message"))
                        }

                    }

                    override fun onError(anError: ANError?) {
                        varError = anError?.errorBody.toString()

                        val obj = JSONObject(varError!!)

                        Log.d(tags, "var error's adding family member : $obj")
                        makeToast(obj.getString("message"))
                    }
                })

    }


    // check date
    private fun checkDate(startDate: String?, endDate: String?): Boolean {
        var status = true
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        try {
            val sDate = sdf.parse(startDate)
            val eDate = sdf.parse(endDate)
            if (sDate.after(eDate)) {
                status = false
                println("check end date after start date")
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return status
    }


    private fun belomMenikah(){
        ll_date_marital.visibility = View.GONE
        tv_pc_date_marital.text = duplicateMaritalDate
    }

    private fun sudahMenikah(){
        ll_date_marital.visibility = View.VISIBLE
        tv_pc_date_marital.text = ""
    }

    private fun hidup(){
        tv_pc_date_familystatus.hint = "Please fill birthdate field above"
        tv_pc_date_familystatus.text = duplicateBirthDate
        iv_datepicker_date_familystatus.visibility = View.GONE
        tv_title_date_familystatus.text = "Birthdate"
        ll_date_family_status_date.visibility = View.VISIBLE
    }

    private fun meninggal(){
        tv_pc_date_familystatus.hint = ""
        tv_pc_date_familystatus.text = ""
        iv_datepicker_date_familystatus.visibility = View.VISIBLE
        ll_date_family_status_date.visibility = View.VISIBLE
        tv_title_date_familystatus.text = "Select date of death"

    }

    private fun makeToast(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.spinner_family_type -> {

                val spinnerDialog = SpinnerDialog(
                        this, listData, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listFamilyType[i].dataName
                    val code = listFamilyType[i].dataCode

                    spinner_family_type.text = name
                    codeChoosenFamilyType = code

                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.iv_datepicker_birthdate -> {
                DatePickerDialog(this, birthDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.spinner_family_gender -> {

                val spinnerDialog = SpinnerDialog(
                        this, listDataGender, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listGender[i].dataName
                    val code = listGender[i].dataCode

                    spinner_family_gender.text = name
                    codeChoosenGender = code

                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.spinner_family_religion -> {

                val spinnerDialog = SpinnerDialog(
                        this, listDataReligion, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listReligion[i].dataName
                    val code = listReligion[i].dataCode

                    spinner_family_religion.text = name
                    codeChoosenReligion = code

                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.spinner_familystatus -> {

                val spinnerDialog = SpinnerDialog(
                        this, listDataFamilyStatus, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listFamilyStatus[i].dataName
                    val code = listFamilyStatus[i].dataCode


                    spinner_familystatus.text = name
                    codeChoosenFamilyStatus = code

                    // cek if hidup / meninggal
                    if (listFamilyStatus[i].dataName.equals("HIDUP")) {
                        hidup()
                    } else {
                        meninggal()
                    }


                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.spinner_marital_status -> {

                val spinnerDialog = SpinnerDialog(
                        this, listDataMaritalStatus, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialog.bindOnSpinerListener { s, i ->
                    val name = listMaritalStatus[i].dataName
                    val code = listMaritalStatus[i].dataCode

                    spinner_marital_status.text = name
                    codeChoosenMaritalStatus = code

                    // cek udah nikah apa belom
                    if (spinner_marital_status.text.equals("LAJANG")) {
                        belomMenikah()
                    } else {
                        sudahMenikah()
                    }

                }
                spinnerDialog.showSpinerDialog()

            }

            R.id.iv_datepicker_marital -> {
                DatePickerDialog(this, maritalDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.btn_submit_add_familymember -> {
                checkingEmptyFieldnGetAllValue()
            }

            R.id.iv_datepicker_date_familystatus -> {
                DatePickerDialog(this, familyStatusDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}
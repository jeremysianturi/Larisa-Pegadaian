package id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.SuratTugasAdapter
import id.co.pegadaian.diarium.entity.DataSpinnerStatusEleaveList
import id.co.pegadaian.diarium.entity.DataStatusSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.model.SuratTugasModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_surat_tugas.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SuratTugas : AppCompatActivity() ,View.OnClickListener{

    private val tags = SuratTugas::class.java.simpleName

    // initialize
    private lateinit var viewModel : SuratTugasModel
    private lateinit var adapter : SuratTugasAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date spinner & status filter
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var startDate : OnDateSetListener
    private lateinit var endDate : OnDateSetListener
    private lateinit var tgl : String
    private lateinit var dataStatusST : DataStatusSuratTugas
    private val listStatusSTNumber = ArrayList<DataStatusSuratTugas>()
    private lateinit var listStatus : ArrayList<String>
    private var strListStatus : String? = null
    private var statusChosenBoolean : String? = ""
    private var startDateChosen : String? = ""
    private var endDateChosen : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surat_tugas)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Surat Tugas")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SuratTugasModel::class.java]

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listStatus = ArrayList<String>()

        // get all surat tugas
        getAssignmentLetter()
        showList()

        // for spinner date
        popUpFilterStartDate()
        popUpFilterEndDate()

        // get status for spinner status
        getStatusSuratTugas()

        // on click
        spinner_filter_status.setOnClickListener(this)
        btn_filter_surattugas.setOnClickListener(this)
        btn_reset_surattugas.setOnClickListener(this)

    }

    private fun getAssignmentLetter(){

        progressDialogHelper.showProgressDialog(this, "Getting data")

        val baseurl = session.serverURLSPPD
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK

        viewModel.setDataSuratTugas(baseurl, token, buscd, pernr)


        viewModel.getDataSuratTugas().observe(this, Observer { model ->
            Log.d(tags, "check model value surat tugas : $model")
            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
            } else {
                if (model[0].intResponse == 200) {
                    Log.d(tags, "test getAssignmentLetter")
                    Log.d(tags,"masuk ke elsesese nya if")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
                    Log.d(tags,"masuk ke elsesese")
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModel.getDataSuratTugas()
    }



    // get surat tugas yang sudah di filter
    private fun getFilteredSuratTugas(startDate: String?, endDate: String?, status: String?){

        val baseUrl = session.serverURLSPPD
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        val dateNow = tgl
        var startDateEdited = ""
        var endDateEdited = ""
        var statusEdited = ""

        // print value date dan status
        Log.d(tags, "check value filter component : \n start date : $startDate \n end date : $endDate \n status : $status")

        if (!startDate.equals("") && !endDate.equals("") ){
            startDateEdited = "&surat_tugas_date=$startDate"
            endDateEdited = "&surat_tugas_date_end=$endDate"
        } else if (!startDate.equals("") || !endDate.equals("")){
            Toast.makeText(this, "Please input both date", Toast.LENGTH_SHORT).show()
        } else {

        }

        if (!status.equals("") ){
            statusEdited = "&status_approval1=$status"
        } else {
            statusEdited = ""
        }

        Log.d(tags, "check value before hit api \n startdate : $startDateEdited \n enddate : $endDateEdited \n status : $statusEdited")

        progressDialogHelper.showProgressDialog(this, "Getting data")

        viewModel.setDataSuratTugasFiltered(baseUrl, token, buscd, pernr, startDateEdited, endDateEdited, statusEdited,dateNow)

        viewModel.getDataSuratTugasFiltered().observe(this, Observer { model ->

            if (model.isEmpty()) {
                Log.d(tags,"test masuk ke is empty surat tugas filtered")
                showingNoData()
                progressDialogHelper.dismissProgressDialog(this)
            } else {
                if (model[0].intResponse == 200) {
                    Log.d(tags, "test getAssignmentLetterFIltered")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
                    Log.d(tags,"check masuk ke else surat tugas filtered")
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })
        viewModel.getDataSuratTugasFiltered()

    }



    private fun approveSuratTugas(objIdentifier: String?){

        progressDialogHelper.showProgressDialog(this, "Approving data..")

        val baseUrl = session.serverURLSPPD
        val buscd = session.userBusinessCode
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags, "chosen objidentifier : $objIden")

        viewModel.approveData(baseUrl, token, buscd, objIden)
        getAssignmentLetter()
        showList()
        progressDialogHelper.dismissProgressDialog(this)
    }


    private fun rejectSuratTugas(objIdentifier: String?){

        progressDialogHelper.showProgressDialog(this, "Rejecting data..")

        val baseUrl = session.serverURLSPPD
        val buscd = session.userBusinessCode
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags, "chosen objidentifier : $objIden")

        viewModel.rejectData(baseUrl, token, buscd, objIden)
        getAssignmentLetter()
        showList()
        progressDialogHelper.dismissProgressDialog(this)
    }

    private fun showList(){

        adapter = SuratTugasAdapter()
        rv_surat_tugas.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_surat_tugas.adapter = adapter

        adapter.setOnItemClickCallback(object : SuratTugasAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataSuratTugas) {
                Log.d(tags, "data value at onClick list : $data")

                val objIdentifierSTNumber = data.objIdentifierSTNumber.toString()
                val objIdentifierForApproval = data.objIdentifierForApproval.toString()
                val suratTugasNumber = data.suratTugasNumber
                val stDate = data.beginDate
                val status = data.statusApprovalOne

                popupMenuActivity(objIdentifierSTNumber, objIdentifierForApproval, suratTugasNumber, stDate, status)
            }
        })
    }

    private fun popupMenuActivity(objIdentifierSTNumber : String?, objectIdentifierForApproval : String?,
                                  suratTugasNumber: String?, stDate: String?, status : String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_surattugas_online)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnDetail = dialog.findViewById<View>(R.id.btnDetail) as Button
        val btnApprove = dialog.findViewById<View>(R.id.btnApprove) as Button
        val btnReject = dialog.findViewById<View>(R.id.btnReject) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = suratTugasNumber
        dialog.show()
        dialog.setCancelable(true)

        // button approve
        if (status.equals("02") || status.equals("03")) {
            btnApprove.visibility = View.GONE
        } else {
            btnApprove.visibility = View.VISIBLE
            btnApprove.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to approve this assignment?")
                builder.setPositiveButton("Yes") { dialog, which -> approveSuratTugas(objectIdentifierForApproval) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }
        }


        // button reject
        if (status.equals("02") || status.equals("03")){
            btnReject.visibility = View.GONE
        } else {
            btnReject.visibility = View.VISIBLE
            btnReject.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to reject this assignment?")
                builder.setPositiveButton("Yes") { dialog, which -> rejectSuratTugas(objectIdentifierForApproval) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }
        }

        //button detail
        btnDetail.setOnClickListener {
            val i = Intent(this, SuratTugasDetail::class.java)

            i.putExtra("suratTugasNumber", suratTugasNumber)
            i.putExtra("suratTugasDate", stDate)
            Log.d(tags, "personal identity name family intent: $suratTugasNumber dan $")

            startActivity(i)
            dialog.dismiss()
        }
    }

//    private fun getStatusSuratTugas(){
//
//        viewModel.setStatusSuratTugas()
//
//        viewModel.getStatusSuratTugas().observe(this, Observer { model ->
//            if (model.isNotEmpty()) {
//                listStatusSTNumber.clear()
//                listStatus.clear()
//
//                for (i in 0 until model.size) {
//                    val status = model[i].status
//
//                    Log.d(tags, "check value getSpinner status surat tugas: \n model size : ${model.size} \n status : $status ")
//
//                    dataStatusST = DataStatusSuratTugas(status)
//                    listStatusSTNumber.add(dataStatusST)
//                    listStatus.add(status)
//
//                    Log.d(tags, "list status number : ${listStatusSTNumber} \n list status : $listStatus ")
//                }
//            }
//        })
//    }

    private fun getStatusSuratTugas(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val tgl = tgl
        val type = "APVST"


        viewModel.setStatusSuratTugas(baseUrl,token,tgl,buscd,type)

        viewModel.getStatusSuratTugas().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listStatusSTNumber.clear()
                listStatus.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val statusName = model[i].nameStatus
                    val statusCode = model[i].codeStatus

                    Log.d(tags, "check value getSpinner status eleave: \n model size : ${model.size} \n name : $statusName")

                    dataStatusST = DataStatusSuratTugas(intResponse, statusName, statusCode)
                    listStatusSTNumber.add(dataStatusST)
                    listStatus.add(statusName)

                }
            }
        })

        viewModel.getStatusSuratTugas()
    }



    //  filter date
    private fun popUpFilterStartDate(){

        startDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFilterStartDate()
        }

        spinner_filter_startdate.setOnClickListener(this)
    }

    private fun updateLabelFilterStartDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        spinner_filter_startdate.text = sdf.format(myCalendar.time)

        Log.d(tags, "check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
        startDateChosen = sdf.format(myCalendar.time)
        Log.d(tags, "check value datechosen at updateLabelFilterDate : $startDateChosen")

    }

    private fun popUpFilterEndDate(){

        endDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFilterEndDate()
        }

        spinner_filter_enddate.setOnClickListener(this)
    }

    private fun updateLabelFilterEndDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        spinner_filter_enddate.text = sdf.format(myCalendar.time)

        Log.d(tags, "check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
        endDateChosen = sdf.format(myCalendar.time)
        Log.d(tags, "check value datechosen at updateLabelFilterDate : $endDateChosen")

    }

    private fun checkDate(startDate: String, endDate: String): Boolean {
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
    //  filter date

    private fun resetAllFilter() {
        spinner_filter_startdate.text = ""
        spinner_filter_enddate.text = ""
        spinner_filter_status.text = ""
        startDateChosen = ""
        endDateChosen = ""
        statusChosenBoolean = ""

        getAssignmentLetter()
        showList()

    }

    private fun showingNoData (){
        rv_surat_tugas.visibility = View.VISIBLE
        no_data_surattugas.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        resetAllFilter()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    override fun onClick(p0: View?) {
        when(p0?.id){

            R.id.spinner_filter_startdate -> {
                popUpFilterStartDate()
                DatePickerDialog(this, startDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
            R.id.spinner_filter_enddate -> {
                popUpFilterEndDate()
                DatePickerDialog(this, endDate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.spinner_filter_status -> {

                var status: String? = null
                var code: String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listStatus, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    status = listStatusSTNumber[i].nameStatus

                    Log.d(tags, "check value category onClick \n category : $status")

                    spinner_filter_status.text = status
                    strListStatus = code

                    if (status.equals("Waiting Approval")){
                        Log.d(tags, "masuk ke waiting approval")
                        statusChosenBoolean = "01"
                    } else if (status.equals("Approved")) {
                        Log.d(tags, "masuk ke approved")
                        statusChosenBoolean = "02"
                    } else if (status.equals("Rejected")) {
                        Log.d(tags, "masuk ke Rejected")
                        statusChosenBoolean = "03"
                    }
                    spinner_filter_status.text = status
                    Log.d(tags, "check value category clicked : $statusChosenBoolean")
                }
                spinnerDialogStatus.showSpinerDialog()

            }

            R.id.btn_filter_surattugas -> {
                Log.d(tags, "check value date and status at on click button search \n startdatechosen : $startDateChosen \n" +
                        " startdatechosen : $endDateChosen" +
                        " \n statusChosenBoolean : $statusChosenBoolean")

                if (checkDate(startDateChosen.toString(), endDateChosen.toString())) {
                    getFilteredSuratTugas(startDateChosen, endDateChosen, statusChosenBoolean)
                    showList()
                } else {
                    Toast.makeText(this, "End date must be greater than start date!", Toast.LENGTH_SHORT).show()
                }


            }

            R.id.btn_reset_surattugas -> {
                resetAllFilter()
            }
        }
    }
}
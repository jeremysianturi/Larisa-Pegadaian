package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.EleaveListAdapter
import id.co.pegadaian.diarium.adapter.EleaveListChildAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.sppd.SppdDetail
import id.co.pegadaian.diarium.entity.DataEleaveList
import id.co.pegadaian.diarium.entity.DataSpinnerEleave
import id.co.pegadaian.diarium.entity.DataSpinnerStatusEleaveList
import id.co.pegadaian.diarium.entity.DataStatusSuratTugas
import id.co.pegadaian.diarium.model.EleaveListModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_create_eleave.*
import kotlinx.android.synthetic.main.activity_eleave_list.*
import kotlinx.android.synthetic.main.activity_surat_tugas.*
import kotlinx.android.synthetic.main.user_data_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class EleaveList : AppCompatActivity(), View.OnClickListener {

    private val tags = EleaveList::class.java.simpleName

    // initialize
    private lateinit var viewModel : EleaveListModel
    private lateinit var adapter : EleaveListAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date spinner
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var filterStartDate : DatePickerDialog.OnDateSetListener
    private lateinit var filterEndDate : DatePickerDialog.OnDateSetListener
    private lateinit var tgl : String
    private var startDateChosen : String? = ""
    private var endDateChosen : String? = ""

    // for filter status
    private lateinit var dataStatusApprovalEleave : DataSpinnerStatusEleaveList
    private val listStatusEleaveList = ArrayList<DataSpinnerStatusEleaveList>()
    private lateinit var listStatus : ArrayList<String>
    private var strListStatus : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleave_list)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Permohonan Cuti")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EleaveListModel::class.java]

        // date
        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listStatus = ArrayList<String>()

        // get list eleave
        getEleave()
        showList()

        // get spinner status approval
        getStatusApprovalEleaveList()

        // onclick
        spinner_filter_status_eleave.setOnClickListener(this)
        btn_filter_eleave.setOnClickListener(this)
        btn_reset_eleave.setOnClickListener(this)
        btn_gotoApproval_eleave.setOnClickListener(this)

    }

    private fun getEleave(){

        progressDialogHelper.showProgressDialog(this, "Getting data")

        val baseurl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK

        viewModel.setData(baseurl, token, buscd,pernr)

        viewModel.getData().observe(this, Observer { model ->
            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
            } else {
                Log.d(tags, "test getAssignmentLetter")
                if (model[0].intResponse == 200) {
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                    showingData()
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModel.getData()
    }

    private fun getFilteredEleave(status : String?){
        progressDialogHelper.showProgressDialog(this, "Getting data")

        val baseurl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        val status = status

        viewModel.setDataFiltered(baseurl, token, buscd,pernr,status)

        viewModel.getDataFiltered().observe(this, Observer { model ->
            Log.d(tags, "check model value : $model")
            if (model.isEmpty()) {
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
            } else {
                Log.d(tags, "test getAssignmentLetter")
                if (model[0].intResponse == 200) {
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                    showingData()
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModel.getDataFiltered()
    }

    private fun getStatusApprovalEleaveList(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val tgl = tgl
        val type = "APSTS"


        viewModel.setStatusApprovalEleaveList(baseUrl,token,buscd,tgl,type)

        viewModel.getDataStatusApprovalEleaveList().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listStatusEleaveList.clear()
                listStatus.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val statusName = model[i].name
                    val statusCode = model[i].code

                    Log.d(tags, "check value getSpinner status eleave: \n model size : ${model.size} \n name : $statusName")

                    dataStatusApprovalEleave = DataSpinnerStatusEleaveList(intResponse, statusCode, statusName)
                    listStatusEleaveList.add(dataStatusApprovalEleave)
                    listStatus.add(statusName)

                    Log.d(tags, "list status number : ${listStatusEleaveList} \n list status : $listStatus \n list code : $statusCode")
                }
            }
        })

        viewModel.getDataStatusApprovalEleaveList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        if (session.getUserNIK().equals(session.getTempPers())) {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_at_eleave, menu)
        //        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.make_eleave -> {
                val intent = Intent(this, CreateEleave::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showList(){

        adapter = EleaveListAdapter()
        rv_eleave.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_eleave.adapter = adapter

        adapter.setOnItemClickCallback(object : EleaveListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataEleaveList) {
                Log.d(tags, "data value at onClick list eleave onclick item : $data")
                val oid = data.objIdentifier.toString()

                popupMenuActivity(oid)
            }
        })
    }

    private fun popupMenuActivity(objectIdentifier : String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_eleave_detail)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnDetail = dialog.findViewById<View>(R.id.btnDetail_eleavedetail) as Button
        val btnDelete = dialog.findViewById<View>(R.id.btnReject_eleavedetail) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle_eleavedetail) as TextView
        tvTitle.text = objectIdentifier
        dialog.show()
        dialog.setCancelable(true)

        // button approve TAPI GA KEPAKE
        btnDelete.visibility = View.GONE
        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to delete this form?")
            builder.setPositiveButton("Yes") { dialog, which ->  }
            builder.setNegativeButton("No") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }

        //button detail
        btnDetail.setOnClickListener {
            val i = Intent(this, EleaveDetail::class.java)

            i.putExtra("object identifier to detail", objectIdentifier)

            Log.d(tags, "personal identity object identifier at sppd intent to sppd detail : $objectIdentifier")

            startActivity(i)
            dialog.dismiss()
        }
    }

    private fun resetAllFilter() {
        spinner_filter_status_eleave.text = ""
        strListStatus = ""
        getEleave()
        showList()
        showingData()
    }

    private fun showingData(){
        no_data_eleave_list.visibility = View.GONE
        rv_eleave.visibility = View.VISIBLE
    }

    private fun showingNoData (){
        no_data_eleave_list.visibility = View.VISIBLE
        rv_eleave.visibility = View.INVISIBLE
    }


    //  filter date (engga kepake)
//    private fun popUpFilterStartDate(){
//
//        filterStartDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, monthOfYear)
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updateLabelFilterStartDate()
//        }
//
//        spinner_filter_startdate_eleave.setOnClickListener(this)
//    }
//
//    private fun updateLabelFilterStartDate(){
//        val myFormat = "yyyy-MM-dd"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        spinner_filter_startdate_eleave.text = sdf.format(myCalendar.time)
//
//        Log.d(tags, "check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
//        startDateChosen = sdf.format(myCalendar.time)
//        Log.d(tags, "check value datechosen at updateLabelFilterDate : $startDateChosen")
//
//    }
//
//    private fun popUpFilterEndDate(){
//
//        filterEndDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, monthOfYear)
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updateLabelFilterEndDate()
//        }
//
//        spinner_filter_enddate_eleave.setOnClickListener(this)
//    }
//
//    private fun updateLabelFilterEndDate(){
//        val myFormat = "yyyy-MM-dd"
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        spinner_filter_enddate_eleave.text = sdf.format(myCalendar.time)
//
//        Log.d(tags, "check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
//        endDateChosen = sdf.format(myCalendar.time)
//        Log.d(tags, "check value datechosen at updateLabelFilterDate : $endDateChosen")
//    }
    //  filter date (engga kepake)

    override fun onResume() {
        super.onResume()
        resetAllFilter()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

        override fun onClick(v: View?) {
        when(v?.id){
            R.id.spinner_filter_status_eleave -> {
                var name: String? = null
                var code: String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listStatus, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    code = listStatusEleaveList[i].code
                    name = listStatusEleaveList[i].name


                    spinner_filter_status_eleave.text = name
                    strListStatus = code

                    spinner_filter_status_eleave.text = name
                }
                spinnerDialogStatus.showSpinerDialog()
            }

            R.id.btn_filter_eleave -> {
                Log.d(tags,"check code filter : $strListStatus")

                if (strListStatus == null || strListStatus.equals("")){
                    getEleave()
                    showList()
                } else {
                    getFilteredEleave(strListStatus)
                    showList()
                }
            }

            R.id.btn_reset_eleave -> {
                resetAllFilter()
            }

            R.id.btn_gotoApproval_eleave -> {
                val intent = Intent(this, ApprovalListEleave::class.java)
                startActivity(intent)
            }
        }
    }
}
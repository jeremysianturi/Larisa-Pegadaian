package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import id.co.pegadaian.diarium.R
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.adapter.ApprovalListEleaveAdapter
import id.co.pegadaian.diarium.adapter.EleaveListAdapter
import id.co.pegadaian.diarium.entity.DataApprovalEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveList
import id.co.pegadaian.diarium.entity.DataSpinnerStatusEleaveList
import id.co.pegadaian.diarium.model.ApprovalListEleaveModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_approval_list_eleave.*
import kotlinx.android.synthetic.main.activity_eleave_list.*
import java.text.SimpleDateFormat
import java.util.*

class ApprovalListEleave : AppCompatActivity(), View.OnClickListener {

    private val tags = ApprovalListEleave::class.java.simpleName

    // initialize
    private lateinit var viewModel : ApprovalListEleaveModel
    private lateinit var adapter : ApprovalListEleaveAdapter
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
    private val listStatusEleaveApprovalList = ArrayList<DataSpinnerStatusEleaveList>()
    private lateinit var listStatusApproval : ArrayList<String>
    private var strListStatusApproval : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_list_eleave)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Permohonan Cuti")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ApprovalListEleaveModel::class.java]

        // date
        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listStatusApproval = ArrayList<String>()

        // get list eleave approval
        getEleaveApproval()
        showList()

        // get spinner status approval
        getStatusApprovalEleaveList()

        // onclick
        spinner_filter_status_eleave_approval.setOnClickListener(this)
        btn_filter_eleave_approval.setOnClickListener(this)
        btn_reset_eleave_approval.setOnClickListener(this)
    }

    private fun getEleaveApproval(){

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
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModel.getData()
    }

    private fun getFilteredEleaveApproval(status : String?){
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
                listStatusEleaveApprovalList.clear()
                listStatusApproval.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val statusName = model[i].name
                    val statusCode = model[i].code

                    Log.d(tags, "check value getSpinner status eleave: \n model size : ${model.size} \n name : $statusName")

                    dataStatusApprovalEleave = DataSpinnerStatusEleaveList(intResponse, statusCode, statusName)
                    listStatusEleaveApprovalList.add(dataStatusApprovalEleave)
                    listStatusApproval.add(statusName)

                    Log.d(tags, "list status number : ${listStatusEleaveApprovalList} \n list status : $listStatusApproval \n list code : $statusCode")
                }
            }
        })

        viewModel.getDataStatusApprovalEleaveList()
    }

    private fun approveEleave(objIdentifier : String?){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags,"chosen objidentifier : $objIden")

        viewModel.approveData(baseUrl,token,objIden)
        getEleaveApproval()
        showList()
        progressDialogHelper.dismissProgressDialog(this)
    }


    private fun rejectEleave(objIdentifier : String?){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags,"chosen objidentifier : $objIden")

        viewModel.rejectData(baseUrl,token,objIden)
        getEleaveApproval()
        showList()
        progressDialogHelper.dismissProgressDialog(this)
    }

    private fun showList(){

        adapter = ApprovalListEleaveAdapter()
        rv_eleave_approval.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_eleave_approval.adapter = adapter

        adapter.setOnItemClickCallback(object : ApprovalListEleaveAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataApprovalEleaveList) {
                Log.d(tags, "data value at onClick list eleave onclick item : $data")
                val oid = data.objIdentifier.toString()
                val statusApproval = data.status
                val approvableStatus = data.approvable

                Log.d(tags,"check value data chosen : \n oid : $oid \n status : $statusApproval")

                popupMenuActivity(oid,statusApproval,approvableStatus)
            }
        })
    }

    private fun popupMenuActivity(objectIdentifier : String?, statusApproval : String?, approvableStatus : String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_approval_eleave_detail)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnApprove = dialog.findViewById<View>(R.id.btnApprove_eleavedetail_approval) as Button
        val btnReject = dialog.findViewById<View>(R.id.btnReject_eleavedetail_approval) as Button
        val btnDetail = dialog.findViewById<View>(R.id.btnDetail_eleavedetail_approval) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle_eleavedetail_approval) as TextView
        tvTitle.text = objectIdentifier
        dialog.show()
        dialog.setCancelable(true)

        // set approvable or not
        if (approvableStatus.equals("false")){

            // set visibility button approve dan button reject
            btnApprove.visibility = View.GONE
            btnReject.visibility = View.GONE

            // button detail
            btnDetail.setOnClickListener{

                Log.d(tags,"check object identifier to eleave approval detail : $objectIdentifier")
                val intent = Intent(this, ApprovalEleaveDetail::class.java)
                intent.putExtra("object identifier to detail",objectIdentifier)
                intent.putExtra("status approval to detail",statusApproval)
                startActivity(intent)

                dialog.dismiss()
            }

        } else {

            // button reject
            btnReject.visibility = View.VISIBLE
            btnReject.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to reject this form?")
                builder.setPositiveButton("Yes") { dialog, which -> rejectEleave(objectIdentifier) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }

            //button approve
            btnApprove.visibility = View.VISIBLE
            btnApprove.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to approve this form?")
                builder.setPositiveButton("Yes") { dialog, which -> approveEleave(objectIdentifier) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }

            // button detail
            btnDetail.setOnClickListener{

                Log.d(tags,"check object identifier to eleave approval detail : $objectIdentifier & status approval : $statusApproval")
                val intent = Intent(this, ApprovalEleaveDetail::class.java)
                intent.putExtra("object identifier to detail",objectIdentifier)
                intent.putExtra("status approval to detail",statusApproval)
                startActivity(intent)

                dialog.dismiss()
            }

        }
    }

    private fun resetAllFilter() {
        spinner_filter_status_eleave_approval.text = ""
        strListStatusApproval = ""
        getEleaveApproval()
        showList()
    }

    private fun showingNoData (){
        no_data_eleave_list_approval.visibility = View.VISIBLE
        rv_eleave_approval.visibility = View.INVISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.spinner_filter_status_eleave_approval -> {

                var name: String? = null
                var code: String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listStatusApproval, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    code = listStatusEleaveApprovalList[i].code
                    name = listStatusEleaveApprovalList[i].name


                    spinner_filter_status_eleave_approval.text = name
                    strListStatusApproval = code

                    spinner_filter_status_eleave_approval.text = name
                }
                spinnerDialogStatus.showSpinerDialog()

            }

            R.id.btn_filter_eleave_approval -> {

                if (strListStatusApproval == null || strListStatusApproval.equals("")){
                    getEleaveApproval()
                    showList()
                } else {
                    getFilteredEleaveApproval(strListStatusApproval)
                    showList()
                }
            }

            R.id.btn_reset_eleave_approval -> {
                resetAllFilter()
            }
        }
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
}
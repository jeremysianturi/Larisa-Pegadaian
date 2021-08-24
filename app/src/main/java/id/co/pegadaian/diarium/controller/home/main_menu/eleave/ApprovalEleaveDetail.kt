package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import id.co.pegadaian.diarium.R
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.adapter.*
import id.co.pegadaian.diarium.entity.DataApprovalEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveDetailApprovalTanggal
import id.co.pegadaian.diarium.model.ApprovalEleaveDetailModel
import id.co.pegadaian.diarium.model.ApprovalEleaveDetailTanggalModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_approval_eleave_detail.*
import kotlinx.android.synthetic.main.activity_approval_list_eleave.*
import kotlinx.android.synthetic.main.activity_eleave_detail.*
import kotlinx.android.synthetic.main.activity_eleave_detail.rv_eleave_detail
import kotlinx.android.synthetic.main.activity_eleave_detail.rv_tgl_eleave_detail

class ApprovalEleaveDetail : AppCompatActivity(), View.OnClickListener {

    private val tags = ApprovalEleaveDetail::class.java.simpleName

    // initialize
    private lateinit var viewModel : ApprovalEleaveDetailModel
    private lateinit var viewModelTanggal : ApprovalEleaveDetailTanggalModel
    private lateinit var adapter : ApprovalEleaveDetailAdapter
    private lateinit var adapterTanggal : ApprovalEleaveDetailTanggalAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private var objIdentifierIntent : String? = null
    private var statusApprovalIntent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approval_eleave_detail)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Detail Persetujuan Cuti")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ApprovalEleaveDetailModel::class.java]
        viewModelTanggal = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ApprovalEleaveDetailTanggalModel::class.java]

        objIdentifierIntent = intent.getStringExtra("object identifier to detail")
        statusApprovalIntent = intent.getStringExtra("status approval to detail")
        Log.d(tags,"check value from intent : \n oid intent : $objIdentifierIntent \n status approval intent : $statusApprovalIntent")

        // determine visibility button approve & reject
        visibiltyBtnApproveReject(statusApprovalIntent)

        // get eleave detail approval
        getEleaveDetailApproval()
        showList()

        // get eleave detail tanggal approval
        getEleaveDetailTanggalApproval()
        showListTanggal()

        btn_approve_approvaleleavedetail.setOnClickListener(this)
        btn_reject_approvaleleavedetail.setOnClickListener(this)

    }

    private fun getEleaveDetailApproval(){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        val objIdentifier = objIdentifierIntent

        viewModel.setData(baseurl,token,buscd,objIdentifier,pernr)


        viewModel.getData().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetter")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModel.getData()
    }

    private fun getEleaveDetailTanggalApproval(){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val objIdentifier = objIdentifierIntent
        val pernr = session.userNIK

        viewModelTanggal.setData(baseurl,token,buscd,objIdentifier,pernr)


        viewModelTanggal.getData().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                progressDialogHelper.dismissProgressDialog(this)
                showingNoDataTanggal()
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetter")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapterTanggal.setData(model)
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModelTanggal.getData()
    }

    private fun approveEleave(objIdentifier : String?){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags,"chosen objidentifier : $objIden")

        viewModel.approveData(baseUrl,token,objIden)

        // get eleave detail approval
        getEleaveDetailApproval()
        showList()

        // get eleave detail tanggal approval
        getEleaveDetailTanggalApproval()
        showListTanggal()

        progressDialogHelper.dismissProgressDialog(this)
    }

    private fun visibiltyBtnApproveReject( statusApproval : String? ){
        if (statusApproval.equals("2") || statusApproval.equals("3")){
            Log.d(tags,"masuk ke visibility gone untuk button approve dan button reject : $statusApproval")
            btn_approve_approvaleleavedetail.visibility = View.GONE
            btn_reject_approvaleleavedetail.visibility = View.GONE
        }
    }


    private fun rejectEleave(objIdentifier : String?){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val objIden = objIdentifier
        Log.d(tags,"chosen objidentifier : $objIden")

        viewModel.rejectData(baseUrl,token,objIden)

        // get eleave detail approval
        getEleaveDetailApproval()
        showList()

        // get eleave detail tanggal approval
        getEleaveDetailTanggalApproval()
        showListTanggal()

        progressDialogHelper.dismissProgressDialog(this)
    }


    private fun showList(){
        adapter = ApprovalEleaveDetailAdapter()
        rv_eleave_detail_approval.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_eleave_detail_approval.adapter = adapter
    }

    private fun showListTanggal(){
        adapterTanggal = ApprovalEleaveDetailTanggalAdapter()
        rv_tgl_eleave_detail_approval.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_tgl_eleave_detail_approval.adapter = adapterTanggal
    }

    private fun showingNoData (){
        no_data_eleave_list_detail_approval.visibility = View.VISIBLE
        rv_eleave_detail_approval.visibility = View.INVISIBLE
    }

    private fun showingNoDataTanggal (){
        no_data_eleave_list_detail_approval_tanggal.visibility = View.VISIBLE
        rv_tgl_eleave_detail_approval.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        getEleaveDetailApproval()
        showList()

        getEleaveDetailTanggalApproval()
        showListTanggal()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_approve_approvaleleavedetail -> {
                approveEleave(objIdentifierIntent)
            }

            R.id.btn_reject_approvaleleavedetail -> {
                rejectEleave(objIdentifierIntent)
            }
        }
    }
}
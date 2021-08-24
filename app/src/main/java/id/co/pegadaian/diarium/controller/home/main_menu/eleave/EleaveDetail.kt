package id.co.pegadaian.diarium.controller.home.main_menu.eleave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.EleaveDetailAdapter
import id.co.pegadaian.diarium.adapter.EleaveDetailTanggalAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasDetailAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasDetailParticipantAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugasDetail
import id.co.pegadaian.diarium.model.EleaveDetailModel
import id.co.pegadaian.diarium.model.EleaveDetailTanggalModel
import id.co.pegadaian.diarium.model.SuratTugasDetailModel
import id.co.pegadaian.diarium.model.SuratTugasDetailParticipantModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_eleave_detail.*
import kotlinx.android.synthetic.main.activity_surat_tugas_detail.*
import kotlinx.android.synthetic.main.activity_surat_tugas_detail.rv_surat_tugas_detail

class EleaveDetail : AppCompatActivity() {

    private val tags = EleaveDetail::class.java.simpleName

    // initialize
    private lateinit var viewModel : EleaveDetailModel
    private lateinit var viewModelTanggal : EleaveDetailTanggalModel
    private lateinit var adapter : EleaveDetailAdapter
    private lateinit var adapterTanggal : EleaveDetailTanggalAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private var objIdentifierIntent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleave_detail)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Eleave Detail")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EleaveDetailModel::class.java]
        viewModelTanggal = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EleaveDetailTanggalModel::class.java]

        objIdentifierIntent = intent.getStringExtra("object identifier to detail")

        // get eleave detail
        getEleaveDetail()
        showList()

        // get eleave detail tanggal
        getEleaveDetailTanggal()
        showListTanggal()

    }

    private fun getEleaveDetail(){

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

    private fun getEleaveDetailTanggal(){

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

    private fun showList(){
        adapter = EleaveDetailAdapter()
        rv_eleave_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_eleave_detail.adapter = adapter
    }

    private fun showListTanggal(){
        adapterTanggal = EleaveDetailTanggalAdapter()
        rv_tgl_eleave_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_tgl_eleave_detail.adapter = adapterTanggal
    }

    override fun onResume() {
        super.onResume()
        getEleaveDetail()
        showList()

        getEleaveDetailTanggal()
        showListTanggal()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }
}
package id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.SuratTugasAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasDetailAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasDetailParticipantAdapter
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import id.co.pegadaian.diarium.model.SuratTugasDetailModel
import id.co.pegadaian.diarium.model.SuratTugasDetailParticipantModel
import id.co.pegadaian.diarium.model.SuratTugasModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_surat_tugas.*
import kotlinx.android.synthetic.main.activity_surat_tugas_detail.*

class SuratTugasDetail : AppCompatActivity() {

    private val tags = SuratTugasDetail::class.java.simpleName

    // initialize
    private lateinit var viewModel : SuratTugasDetailModel
    private lateinit var viewModelParticipant : SuratTugasDetailParticipantModel
    private lateinit var adapter : SuratTugasDetailAdapter
    private lateinit var adapterParticipant : SuratTugasDetailParticipantAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private var suratTugasNumberIntent : String? = null
    private var suratTugasDateIntent : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surat_tugas_detail)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Surat Tugas Detail")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SuratTugasDetailModel::class.java]
        viewModelParticipant = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SuratTugasDetailParticipantModel::class.java]

        // get intent
        suratTugasNumberIntent = intent.getStringExtra("suratTugasNumber")
        suratTugasDateIntent = intent.getStringExtra("suratTugasDate")

        // surat tugas detail
        getSuratTugasDetail()
        showList()

        // surat tugas detail participant
        getParticipant()
        showListParticipant()

    }

    private fun getSuratTugasDetail(){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLSPPD
        val token = session.token
        val buscd = session.userBusinessCode
        val stNumber = suratTugasNumberIntent
        val pernr = session.userNIK
        val stDate = suratTugasDateIntent

        viewModel.setData(baseurl,token,buscd,stNumber,pernr)


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


    private fun getParticipant(){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLSPPD
        val token = session.token
        val buscd = session.userBusinessCode
        val stNumber = suratTugasNumberIntent
        val stDate = suratTugasDateIntent

        viewModelParticipant.setDataParticipant(baseurl,token,buscd,stNumber,stDate)

        viewModelParticipant.getDataParticipant().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                progressDialogHelper.dismissProgressDialog(this)
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetter")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapterParticipant.setData(model)
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })

        viewModelParticipant.getDataParticipant()

    }

    private fun showList(){

        adapter = SuratTugasDetailAdapter()
        rv_surat_tugas_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_surat_tugas_detail.adapter = adapter

    }


    private fun showListParticipant(){

        adapterParticipant = SuratTugasDetailParticipantAdapter()
        rv_menugaskan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_menugaskan.adapter = adapterParticipant

    }


    override fun onResume() {
        super.onResume()
        getSuratTugasDetail()
        showList()

        getParticipant()
        showListParticipant()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}
package id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.sppd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.SppdAdapter
import id.co.pegadaian.diarium.adapter.SppdDetailAdapter
import androidx.lifecycle.Observer
import id.co.pegadaian.diarium.adapter.SuratTugasDetailAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasDetailParticipantAdapter
import id.co.pegadaian.diarium.entity.DataSppd
import id.co.pegadaian.diarium.model.SppdDetailModel
import id.co.pegadaian.diarium.model.SuratTugasDetailModel
import id.co.pegadaian.diarium.model.SuratTugasDetailParticipantModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_sppd.*
import kotlinx.android.synthetic.main.activity_sppd_detail.*

class SppdDetail : AppCompatActivity(), View.OnClickListener {

    private val tags = SppdDetail::class.java.simpleName

    // initialize
    private lateinit var viewModel : SppdDetailModel
    private lateinit var viewModelParticipant : SuratTugasDetailParticipantModel
    private lateinit var adapter : SppdDetailAdapter
    private lateinit var adapterParticipant : SuratTugasDetailParticipantAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private var sppdNumberIntent : String? = null
    private var suratTugasDateIntent : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sppd_detail)


        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("SPPD Detail")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SppdDetailModel::class.java]

        sppdNumberIntent = intent.getStringExtra("Sppd number")

        // show loading
        showLoading(true)

        // get list
        getSppdDetail()
        showList()

        // onclick

    }

    private fun getSppdDetail(){

        val baseurl = session.serverURLSPPD
        val token = session.token
        val buscd = session.userBusinessCode
        val sppdNumber = sppdNumberIntent

        viewModel.setData(baseurl,token,buscd,sppdNumber)

        viewModel.getData().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
//                progressDialogHelper.dismissProgressDialog(this)
                showLoading(false)
            } else {
                Log.d(tags,"test getAssignmentLetter")
                if (model[0].intResponse == 200){
//                    progressDialogHelper.dismissProgressDialog(this)
                    showLoading(false)
                    adapter.setData(model)
                } else {
//                    progressDialogHelper.dismissProgressDialog(this)
                    showLoading(false)
                }
            }
        })

        viewModel.getData()

    }

    private fun showList(){

        adapter = SppdDetailAdapter()
        rv_sppd_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_sppd_detail.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_sppddetail.visibility = View.VISIBLE
        } else {
            progressBar_sppddetail.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        getSppdDetail()
        showList()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

}
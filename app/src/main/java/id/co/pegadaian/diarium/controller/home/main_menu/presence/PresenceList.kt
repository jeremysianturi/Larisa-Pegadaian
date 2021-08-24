package id.co.pegadaian.diarium.controller.home.main_menu.presence

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PresenceListAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugasDetail
import id.co.pegadaian.diarium.entity.DataPresenceList
import id.co.pegadaian.diarium.entity.DataStatusPresenceList
import id.co.pegadaian.diarium.entity.DataStatusSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.model.PresenceListModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_detail_courses.*
import kotlinx.android.synthetic.main.activity_eleave_list.*
import kotlinx.android.synthetic.main.activity_presence_list.*
import kotlinx.android.synthetic.main.activity_surat_tugas.*
import java.text.SimpleDateFormat
import java.util.*

class PresenceList : AppCompatActivity(), View.OnClickListener {


    private val tags = PresenceList::class.java.simpleName

    // initialize
    private lateinit var viewModel : PresenceListModel
    private lateinit var adapter : PresenceListAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    private lateinit var tgl : String
    private lateinit var dataStatusPresenceConf : DataStatusPresenceList
    private val listStatusPresConfNumber = ArrayList<DataStatusPresenceList>()
    private lateinit var listStatus : ArrayList<String>
    private var strListStatus : String? = null
    private var statusChosenBoolean : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence_list)

        // check value token
//        Log.d(tags,"check value token yayayaya ${session.token}")

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("My Presence Confirmation")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PresenceListModel::class.java]

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listStatus = ArrayList<String>()

        // get all presence list
        getPresenceList()
        showList()

        // get status for spinner status
        getStatusPresence()

        // onclick
        spinner_filter_status_presence.setOnClickListener(this)
        btn_goto_approval.setOnClickListener(this)
        btn_new_presenceconf.setOnClickListener(this)
        btn_filter_presenceconf.setOnClickListener(this)
        btn_reset_presenceconf.setOnClickListener(this)
        spinner_filter_status_presence.setOnClickListener(this)
        spinner_filter_date_presence.setOnClickListener(this)

    }

    private fun getPresenceList(){

        val baseUrl = session.serverURL
        val token = session.token
        Log.d(tags,"check tokentokentokennnnnn $token")
        val pernr = session.userNIK

        viewModel.setDataPresenceList(baseUrl,token,pernr)

        viewModel.getDataPresenceList().observe(this, Observer { model ->
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

        viewModel.getDataPresenceList()

    }

    private fun getFilteredPresenceList(status : String?){

        val baseUrl = session.serverURL
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        var statusEdited = ""

        // print value date dan status
        Log.d(tags,"check value filter component : \n status : $status")

//        if (!date.equals("") ){
//            Log.d(tags,"masuk kesokin broooqqq qqqqqqq")
//            dateEdited = "&begin_date=$date"
//        } else {
//            Log.d(tags,"masuk kesokin broooqqq")
//            dateEdited = ""
//        }

        if (!status.equals("") ){
            statusEdited = "&approval_status=$status"
        } else {
            statusEdited = ""
        }

        Log.d(tags,"check value before hit api \n status : $statusEdited")

        viewModel.setDataPresenceListFiltered(baseUrl,token,buscd,pernr,statusEdited)

        viewModel.getDataPresenceListFiltered().observe(this, Observer { model ->
            if (model.isEmpty()){
                showingNoData()
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetterFIltered")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
            }
        })
        viewModel.getDataPresenceListFiltered()

    }

    private fun getStatusPresence(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val beginDate = tgl
        val objectType = "APVST"


        viewModel.setStatusPresence(baseUrl,token,buscd,beginDate,objectType)

        viewModel.getStatusPresence().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listStatusPresConfNumber.clear()
                listStatus.clear()

                for (i in 0 until model.size) {
                    val intResponse = model[i].intResponse
                    val status = model[i].code
                    val name = model[i].name

                    Log.d(tags, "check value getSpinner status surat tugas: \n model size : ${model.size} \n status : $status \n name : $name")

                    dataStatusPresenceConf = DataStatusPresenceList(intResponse,status,name)
                    listStatusPresConfNumber.add(dataStatusPresenceConf)
                    listStatus.add(name)

                    Log.d(tags, "list status number : ${listStatusPresConfNumber} \n list status : $listStatus ")
                }
            }
        })
    }

    private fun showList(){

        adapter = PresenceListAdapter()
        rv_presenceconf.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_presenceconf.adapter = adapter

        adapter.setOnItemClickCallback(object : PresenceListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataPresenceList) {
                Log.d(tags, "data value at onClick list : $data")

                val objIdentifier = data.objIdentifier.toString()
                val presenceName = data.statusName

                popupMenuActivity(objIdentifier,presenceName)
            }
        })
    }

    private fun popupMenuActivity(objIdentifier: String?, suratTugasNumber: String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_sppd_online)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnDetail = dialog.findViewById<View>(R.id.btnDetail) as Button
        val btnApprove = dialog.findViewById<View>(R.id.btnReject) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = suratTugasNumber
        dialog.show()
        dialog.setCancelable(true)

        // button reject
        btnApprove.visibility = View.GONE
        btnApprove.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to approve this assignment?")
//            builder.setPositiveButton("Yes") { dialog, which -> approveSuratTugas(objIdentifier) }
            builder.setNegativeButton("No") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }

        //button detail
        btnDetail.setOnClickListener {
            val i = Intent(this, PresenceListDetail::class.java)

            i.putExtra("objectIdentifier", objIdentifier)
            Log.d(tags,"personal identity name family intent: $objIdentifier")

            startActivity(i)
            dialog.dismiss()
        }
    }

    private fun resetAllFilter() {
        spinner_filter_date_presence.text = ""
        spinner_filter_status_presence.text = ""
        statusChosenBoolean = ""

        getPresenceList()
        showList()

    }

    private fun showingNoData (){
        no_data_presenceconf.visibility = View.VISIBLE
        rv_presenceconf.visibility = View.INVISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id){

            R.id.btn_goto_approval -> {
                val i = Intent(this,PresenceListApprove2::class.java)
                startActivity(i)
            }

            R.id.btn_new_presenceconf -> {
                val i = Intent(this,PresenceConfirmation::class.java)
                startActivity(i)
            }

            R.id.btn_filter_presenceconf -> {
                if (strListStatus == null || strListStatus.equals("")){
                    getPresenceList()
                    showList()
                } else {
                    getFilteredPresenceList(strListStatus)
                    showList()
                }
            }

            R.id.btn_reset_presenceconf -> {
                resetAllFilter()
            }

            R.id.spinner_filter_status_presence -> {
                Log.d(tags,"masuk sini gak yawwwwwwwwwww")
                var name: String? = null
                var code : String? = null
                val spinnerDialogStatus = SpinnerDialog(
                        this, listStatus, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogStatus.bindOnSpinerListener { s, i ->
                    name = listStatusPresConfNumber[i].name
                    code = listStatusPresConfNumber[i].code

                    Log.d(tags, "check value category onClick \n category : $name")

                    spinner_filter_status_presence.text = name
                    strListStatus = code

                    Log.d(tags, "check value category clicked : $strListStatus")
                }
                spinnerDialogStatus.showSpinerDialog()
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
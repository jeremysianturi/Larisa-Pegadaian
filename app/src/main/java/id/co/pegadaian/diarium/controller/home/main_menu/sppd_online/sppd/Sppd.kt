package id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.sppd

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.SppdAdapter
import id.co.pegadaian.diarium.adapter.SuratTugasAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugas
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugasDetail
import id.co.pegadaian.diarium.entity.DataSppd
import id.co.pegadaian.diarium.entity.DataStatusSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.model.SppdModel
import id.co.pegadaian.diarium.model.SuratTugasModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_presence_list_approve.*
import kotlinx.android.synthetic.main.activity_sppd.*
import kotlinx.android.synthetic.main.activity_surat_tugas.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Sppd : AppCompatActivity(), View.OnClickListener {

    private val tags = Sppd::class.java.simpleName

    // initialize
    private lateinit var viewModel : SppdModel
    private lateinit var adapter : SppdAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date spinner
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var filterStartDate : DatePickerDialog.OnDateSetListener
    private lateinit var filterEndDate : DatePickerDialog.OnDateSetListener
    private lateinit var tgl : String
//    private lateinit var dataStatusST : DataStatusSuratTugas
//    private val listStatusSTNumber = ArrayList<DataStatusSuratTugas>()
//    private lateinit var listStatus : ArrayList<String>
    private var strListStatus : String? = null
    private var startDateChosen : String? = ""
    private var endDateChosen : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sppd)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("SPPD")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[SppdModel::class.java]

        // date
        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        // get list sppd
        getSppd()
        showList()

        // for spinner date
        popUpFilterStartDate()
        popUpFilterEndDate()

        // show loading
        showLoading(true)

        // onclick
        spinner_filter_startdate_sppd.setOnClickListener(this)
        spinner_filter_enddate_sppd.setOnClickListener(this)
        btn_filter_sppd.setOnClickListener(this)
        btn_reset_sppd.setOnClickListener(this)
    }

    private fun getSppd(){

//        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLSPPD
        val token = session.token
        val nik = session.userNIK
        val buscd = session.userBusinessCode

        viewModel.setData(baseurl,token,nik,buscd)

        viewModel.getData().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
                showLoading(false)
            } else {
                Log.d(tags,"test getAssignmentLetter")
                if (model[0].intResponse == 200){
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                    showingData()
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
                showLoading(false)
            }
        })

        viewModel.getData()

    }

    private fun getFilteredSppd(startDate : String?, endDate : String?){

        progressDialogHelper.showProgressDialog(this,"Getting data")

        val baseurl = session.serverURLSPPD
        val token = session.token
        val nik = session.userNIK
        val buscd = session.userBusinessCode

        viewModel.setDataFiltered(baseurl,token,nik,buscd,startDate,endDate)

        viewModel.getDataFiltered().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                Log.d(tags,"masa iya gamasuk kesini bossssssss")
                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
                showLoading(false)
            } else {
                Log.d(tags,"test getAssignmentLetter")
                if (model[0].intResponse == 200){
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                    showingData()
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }
                showLoading(false)
            }
        })

        viewModel.getData()

    }

    private fun showList(){

        adapter = SppdAdapter()
        rv_sppd.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_sppd.adapter = adapter

        adapter.setOnItemClickCallback(object : SppdAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataSppd) {
                Log.d(tags, "data value at onClick list : $data")
                val sppdNumber = data.sppdNumber

                popupMenuActivity(sppdNumber)
            }
        })
    }

    private fun popupMenuActivity(sppdNumber : String?) {
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
        tvTitle.text = sppdNumber
        dialog.show()
        dialog.setCancelable(true)

        // button approve TAPI GA KEPAKE
        btnApprove.visibility = View.GONE
        btnApprove.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to approve this assignment?")
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
            val i = Intent(this, SppdDetail::class.java)

            i.putExtra("Sppd number", sppdNumber)

            Log.d(tags,"personal identity sppd number at sppd intent to sppd detail : $sppdNumber")

            startActivity(i)
            dialog.dismiss()
        }
    }

    //  filter date
    private fun popUpFilterStartDate(){

        filterStartDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFilterStartDate()
        }

        spinner_filter_startdate_sppd.setOnClickListener(this)
    }

    private fun updateLabelFilterStartDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        spinner_filter_startdate_sppd.text = sdf.format(myCalendar.time)

        Log.d(tags,"check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
        startDateChosen = sdf.format(myCalendar.time)
        Log.d(tags,"check value datechosen at updateLabelFilterDate : $startDateChosen")

    }

    private fun popUpFilterEndDate(){

        filterEndDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFilterEndDate()
        }

        spinner_filter_enddate_sppd.setOnClickListener(this)
    }

    private fun updateLabelFilterEndDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        spinner_filter_enddate_sppd.text = sdf.format(myCalendar.time)

        Log.d(tags,"check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
        endDateChosen = sdf.format(myCalendar.time)
        Log.d(tags,"check value datechosen at updateLabelFilterDate : $endDateChosen")
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
        spinner_filter_startdate_sppd.text = ""
        spinner_filter_enddate_sppd.text = ""
        startDateChosen = ""
        endDateChosen = ""

        getSppd()
        showList()

    }

    private fun showingNoData (){
        rv_sppd.visibility = View.INVISIBLE
        no_data_sppd.visibility = View.VISIBLE
    }

    private fun showingData () {
        rv_sppd.visibility = View.VISIBLE
        no_data_sppd.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_filter_sppd -> {
                if (startDateChosen == null || startDateChosen.equals("") && endDateChosen == null || endDateChosen.equals("")){
                    getSppd()
                    showList()
                } else {
                    if (checkDate(startDateChosen.toString(), endDateChosen.toString())) {
                        getFilteredSppd(startDateChosen,endDateChosen)
                        showList()
                    } else {
                        Toast.makeText(this, "End date must be greater than start date!", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            R.id.btn_reset_sppd -> {
                resetAllFilter()
            }

            R.id.spinner_filter_startdate_sppd -> {
                popUpFilterStartDate()
                DatePickerDialog(this,filterStartDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.spinner_filter_enddate_sppd -> {
                popUpFilterEndDate()
                DatePickerDialog(this,filterEndDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }
}
package id.co.pegadaian.diarium.controller.home.main_menu.presence

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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PresenceListApproveAdapter
import id.co.pegadaian.diarium.entity.DataPresenceList
import id.co.pegadaian.diarium.entity.DataStatusPresenceList
import id.co.pegadaian.diarium.model.PresenceListApproveModel
import id.co.pegadaian.diarium.util.UserSessionManager
import kotlinx.android.synthetic.main.activity_presence_list_approve.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import android.app.DatePickerDialog.OnDateSetListener


class PresenceListApprove2 : AppCompatActivity(), View.OnClickListener {


    private val tags = PresenceListApprove2::class.java.simpleName

    // initialize
    private lateinit var viewModel : PresenceListApproveModel
    private lateinit var adapter : PresenceListApproveAdapter
    private lateinit var session : UserSessionManager
//    private lateinit var progressDialogHelper : ProgressDialogHelper

    // for date spinner
    private var myCalendar : Calendar = Calendar.getInstance()
    private lateinit var filterDate : DatePickerDialog.OnDateSetListener
    private lateinit var tgl : String
    private lateinit var dataStatusPresenceConf : DataStatusPresenceList
    private val listStatusPresConfNumber = ArrayList<DataStatusPresenceList>()
    private lateinit var listStatus : ArrayList<String>
    private var strListStatus : String? = null
    private var statusChosenBoolean : String? = ""
    private var dateChosen : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence_list_approve2)

        Log.d(tags,"yap")


//        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Approval Presence Confirmation")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PresenceListApproveModel::class.java]

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        listStatus = ArrayList<String>()

        // show loading
        showLoading(true)

        // get list presence approval
        getPresenceList()
        showList()

        // get status spinner for filter
        getStatusPresence()

        // onclick
        spinner_filter_date_approve.setOnClickListener(this)
        spinner_filter_status_approve.setOnClickListener(this)
        btn_filter_presenceconf_approve.setOnClickListener(this)
        btn_reset_presenceconf_approve.setOnClickListener(this)

    }

    private fun getPresenceList(){

        val baseUrl = session.serverURL
        val token = session.token
        val pernr = session.userNIK

        Log.d(tags,"check tokentokentokennnnnn $token")


        viewModel.setDataApprovePresenceList(baseUrl,token,pernr)

        viewModel.getDataApprovePresenceList().observe(this, Observer { model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
//                progressDialogHelper.dismissProgressDialog(this)
                showingNoData()
                showLoading(false)
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetter")
//                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
//                    progressDialogHelper.dismissProgressDialog(this)
                }
                showLoading(false)
            }
        })

        viewModel.getDataApprovePresenceList()

    }


    private fun getFilteredPresenceList(date : String? , status : String?){

        val baseUrl = session.serverURL
        val token = session.token
        val pernr = session.userNIK
        var dateEdited = ""
        var statusEdited = ""

        // print value date dan status
        Log.d(tags,"check value filter component : \n date : $date \n status : $status")

//        if (!date.equals("") ){
//            Log.d(tags,"masuk kesokin broooqqq qqqqqqq")
//            dateEdited = "&begin_date=$date"
//        } else {
//            Log.d(tags,"masuk kesokin broooqqq")
//            dateEdited = ""
//        }

        statusEdited = "&approval_status=$status"


        Log.d(tags,"check value before hit api \n date : $dateEdited \n status : $statusEdited")

        viewModel.setDataApprovePresenceListFiltered(baseUrl,token,pernr,dateEdited,statusEdited)

        viewModel.getDataApprovePresenceListFiltered().observe(this, Observer { model ->
            if (model.isEmpty()){
                showingNoData()
                showLoading(false)
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"test getAssignmentLetterFIltered")
//                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
//                    progressDialogHelper.dismissProgressDialog(this)
                }
                showLoading(false)
            }
        })
        viewModel.getDataApprovePresenceListFiltered()

    }

    private fun getStatusPresence(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val beginDate = tgl
        val objectType = "APVST"


        viewModel.setStatusApprovePresence(baseUrl,token,buscd,beginDate,objectType)

        viewModel.getStatusApprovePresence().observe(this, Observer { model ->
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

//    private fun approvePresenceConfirmation (objectIdentifier : String?) {
//
//        val baseUrl = session.serverURL
//        val token = session.token
//
//        val boday = JSONObject()
//        boday.put("oid","${objectIdentifier}")
//        boday.put("approval_status", "02")
//
//        Log.d(tags,"check body approve presence confirmation : $boday")
//
//        val requestUrl = "${baseUrl}users/presensi/absent"
//        Log.d(tags,"api approve presence confirmation : $requestUrl")
//
//        AndroidNetworking.put(requestUrl)
//                .addHeaders("Authorization", token)
//                .addHeaders("Accept","application/json")
//                .addHeaders("Content-Type","application/json")
//                .addJSONObjectBody(boday)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(object : JSONObjectRequestListener {
//
//                    override fun onResponse(response: JSONObject?) {
//
//                        Log.d(tags, "response approve presence confirmation : $response")
//                        val intResponse = response?.getInt("status")
//
//                        if (intResponse == 200) {
//
//                            Toast.makeText(this@PresenceListApprove, "Approved!", Toast.LENGTH_SHORT).show()
//                            resetAllFilter() // reset list setelah approve
//
//                        } else {
//                            Toast.makeText(this@PresenceListApprove, response?.getString("message"), Toast.LENGTH_SHORT).show()
//                        }
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        Log.d(tags, "an error at approve presence confirmation : ${anError?.errorBody}")
//                        Log.d(tags, "an error at approve presence confirmation : ${anError?.errorDetail}")
//                        //Log.d(tags,"check omom : $requ")
//                        Toast.makeText(this@PresenceListApprove, "ERROR!", Toast.LENGTH_SHORT).show()
//                    }
//                })
//    }

    private fun approvePresenceConfirmation(objIdentifier: String?){

        Log.d(tags,"bener kok masuk ke approve : $objIdentifier ")

        val baseUrl = session.serverURL
        val token = session.token

        val body = JSONObject()
        body.put("oid",objIdentifier)
        body.put("approval_status","02")
        Log.d(tags,"check body approve presence confirmation 2 : $body")

//        val urlApprovePresenceConf = "${baseUrl}users/presensi/absent"    // API LAMA
        val urlApprovePresenceConf = "${baseUrl}users/presensi/approve"     // API BARU
        Log.d(tags,"check url approve presence confirmation di list approval 2 : $urlApprovePresenceConf")

        AndroidNetworking.put(urlApprovePresenceConf)
                .addHeaders("Authorization",token)
                .addHeaders("Content-Type","application/json")
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val responseMessage = response?.getString("message")
                        Log.d(tags, "response approve presence confirmation di list approval : $responseMessage")

                        if (response?.getString("status") == "true") {
                            showLoading(true)
                            resetAllFilter()
                            Toast.makeText(this@PresenceListApprove2, responseMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@PresenceListApprove2, responseMessage, Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onError(anError: ANError?) {
                        val onErrornya = anError?.errorBody.toString()
                        Log.d(tags, "masuk ke on error approve presence confirmation di list approval : $onErrornya")
                        Toast.makeText(this@PresenceListApprove2, onErrornya, Toast.LENGTH_SHORT).show()
                    }
                })

    }


    private fun rejectPresenceConfirmation (objectIdentifier : String?) {

        val baseUrl = session.serverURL
        val token = session.token

        var body = JSONObject()


        try {
            body.put("oid", objectIdentifier)
            body.put("approval_status", "03")
        } catch (e: Exception){
            Log.d(tags, "cannot fill body -> error : $e")
        }

        Log.d(tags,"check body reject presence confirmation : $body")

        val urlRejectPresenceConf = "${baseUrl}users/presensi/approve"     // API BARU
//        val urlRejectPresenceConf = "${baseUrl}users/presensi/absent"       // API LAMA
        Log.d(tags,"api reject presence confirmation : $urlRejectPresenceConf")

        AndroidNetworking.put(urlRejectPresenceConf)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization",token)
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags, "response reject presence confirmation : $response")
                        val intResponse = response?.getString("status")

                        if (intResponse == "true"){
                            Toast.makeText(this@PresenceListApprove2, "Rejected!", Toast.LENGTH_SHORT).show()
                            showLoading(true)
                            resetAllFilter() // reset list setelah reject
                        } else {
                            Toast.makeText(this@PresenceListApprove2, response?.getString("message"), Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tags, "an error at reject presence confirmation : ${anError?.errorBody}")
                        Log.d(tags, "an error at reject presence confirmation : ${anError?.errorDetail}")
                        //Log.d(tags,"check omom : $requ")
                        Toast.makeText(this@PresenceListApprove2, "ERROR!", Toast.LENGTH_SHORT).show()
                    }
                })

    }


    private fun showList(){

        adapter = PresenceListApproveAdapter()
        rv_presence_approve.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_presence_approve.adapter = adapter

        adapter.setOnItemClickCallback(object : PresenceListApproveAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataPresenceList) {
                Log.d(tags, "data value at onClick list : $data")

                val objIdentifier = data.objIdentifier.toString()
                val dateAbsent = data.date
                val statusApproval = data.statusName

                Log.d(tags,"check value oid dan date absent di presence list approve : " +
                        "\n oid : ${objIdentifier} \n date absent : $dateAbsent \n " + "status : $statusApproval" )
                popupMenuActivity(objIdentifier,dateAbsent,statusApproval)

            }
        })
    }

    private fun popupMenuActivity(objIdentifier: String?, dateAbsent : String?, statusApproval : String?) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_presence_approval)
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
        tvTitle.text = dateAbsent
        dialog.show()
        dialog.setCancelable(true)

        if (statusApproval.equals("Approved")||statusApproval.equals("Rejected")){

            // set visibility button approve dan button reject
            btnApprove.visibility = View.GONE
            btnReject.visibility = View.GONE

            //button detail
            btnDetail.setOnClickListener {
                val i = Intent(this, PresenceListApproveDetail::class.java)

                i.putExtra("objIdentifier Presence Confirmation", objIdentifier)
                Log.d(tags,"go to detail to presence confirmation detail activity objiden : ${objIdentifier}")

                startActivity(i)
                dialog.dismiss()
            }

        } else {

            // button approve
            btnApprove.visibility = View.VISIBLE
            btnApprove.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to approve this assignment?")
                builder.setPositiveButton("Yes") { dialog, which -> approvePresenceConfirmation(objIdentifier) }
                builder.setNegativeButton("No") { dialog, which -> dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }

            // button reject
            btnReject.visibility = View.VISIBLE
            btnReject.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to reject this assignment?")
                builder.setPositiveButton("Yes") { dialog, which -> rejectPresenceConfirmation(objIdentifier) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }

            //button detail
            btnDetail.setOnClickListener {
                val i = Intent(this, PresenceListApproveDetail::class.java)

                i.putExtra("objIdentifier Presence Confirmation", objIdentifier)
                Log.d(tags,"go to detail to presence confirmation detail activity objiden : ${objIdentifier}")

                startActivity(i)
                dialog.dismiss()
            }

        }
    }


    //  filter date
    private fun popUpFilterDate(){

        filterDate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelFilterDate()
        }

        spinner_filter_date_approve.setOnClickListener(this)
    }

    private fun updateLabelFilterDate(){
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        spinner_filter_date_approve.text = sdf.format(myCalendar.time)

        Log.d(tags,"check value date at updateLabelFilterDate : ${sdf.format(myCalendar.time)}")
        dateChosen = sdf.format(myCalendar.time)
        Log.d(tags,"check value datechosen at updateLabelFilterDate : $dateChosen")

    }
    //  filter date


    private fun resetAllFilter() {
        spinner_filter_date_approve.text = ""
        spinner_filter_status_approve.text = ""
        dateChosen = ""
        statusChosenBoolean = ""

        getPresenceList()
        showList()

    }

    private fun showingNoData (){
        rv_presence_approve.visibility = View.VISIBLE
        no_data_presenceconf_approve.visibility = View.INVISIBLE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.spinner_filter_date_approve -> {
                popUpFilterDate()
                DatePickerDialog(this,filterDate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            R.id.spinner_filter_status_approve -> {
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

                    spinner_filter_status_approve.text = name
                    strListStatus = code

                    Log.d(tags, "check value category clicked : $strListStatus")
                }
                spinnerDialogStatus.showSpinerDialog()
            }

            R.id.btn_filter_presenceconf_approve -> {

                if (dateChosen == null || dateChosen.equals("") && strListStatus == null || strListStatus.equals("")){
                    getPresenceList()
                    showList()
                } else {
                    getFilteredPresenceList(dateChosen,strListStatus)
                    showList()
                }
            }


            R.id.btn_reset_presenceconf_approve -> {
                resetAllFilter()
            }


        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_presencelistapprove.visibility = View.VISIBLE
        } else {
            progressBar_presencelistapprove.visibility = View.GONE
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
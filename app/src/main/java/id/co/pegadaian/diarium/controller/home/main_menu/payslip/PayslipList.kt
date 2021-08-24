package id.co.pegadaian.diarium.controller.home.main_menu.payslip

import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PayslipPotonganAdapter
import id.co.pegadaian.diarium.adapter.PayslipRincianGajiAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.today_activity.ShiftCard
import id.co.pegadaian.diarium.entity.DataCategorySpinnerAddData
import id.co.pegadaian.diarium.entity.DataPayslipRincianGaji
import id.co.pegadaian.diarium.model.PayslipRincianGajiModel
import id.co.pegadaian.diarium.util.MonthYearPickerDialog
import id.co.pegadaian.diarium.util.UserSessionManager
import kotlinx.android.synthetic.main.activity_payslip_list.*
import kotlinx.android.synthetic.main.activity_sppd.*
import kotlinx.android.synthetic.main.activity_surat_tugas_detail.*
import kotlinx.android.synthetic.main.lay_item_payslip_rinciangaji.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PayslipList : AppCompatActivity(), View.OnClickListener {

    private val tags = PayslipList::class.java.simpleName

    private lateinit var viewModel : PayslipRincianGajiModel
    private lateinit var adapter : PayslipRincianGajiAdapter
    private lateinit var adapterPotongan : PayslipPotonganAdapter
    private lateinit var session : UserSessionManager

    private val listTypesNumber = ArrayList<DataCategorySpinnerAddData>()
    private lateinit var dataSpinnerTypesClass : DataCategorySpinnerAddData

    private lateinit var tgl : String
    private lateinit var listDataCategory : ArrayList<String>

    private var strListTypes : String? = null
    private var typeClicked : String? = null
    private var monthChosenCode : String? = ""
    private var monthChosenName : String? = ""
    private var yearChosen : String? = ""
    private var totalPenghasilanForTakeHomePay : Double? = 0.0
    private var totalPotonganForTakeHomePay : Double? = 0.0
    private var oidForWebview : String = ""

    // cek untuk month date picker
    var monthYearStr: String? = null
    var sdf = SimpleDateFormat("MMM yyyy")
    var input = SimpleDateFormat("yyyy-MM-dd")
    // cek untuk month date picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payslip_list)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Pay Slip")

        val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        tgl = sdf.format(Date())

        session = UserSessionManager(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PayslipRincianGajiModel::class.java]

        listDataCategory = ArrayList<String>()

        getSpinnerTypesList()
        showListPenghasilan()
        showListPotongan()

        spinner_payslip_type.setOnClickListener(this)
        btn_gotodetail_payslip.setOnClickListener(this)
        btn_search_payslip.setOnClickListener(this)
        spinner_payslip_period.setOnClickListener(this)
    }

    // regular dan iregular
    private fun getSpinnerTypesList(){

        viewModel.setDataTypesList()

        viewModel.getDataTypesList().observe(this, Observer { model ->
            if (model.isNotEmpty()) {
                listTypesNumber.clear()
                listDataCategory.clear()

                for (i in 0 until model.size) {
                    val category = model[i].name

                    Log.d(tags, "check value getSpinnerTypesList: \n model size : ${model.size} \n category : $category ")

                    dataSpinnerTypesClass = DataCategorySpinnerAddData(category)
                    listTypesNumber.add(dataSpinnerTypesClass)
                    listDataCategory.add(category.toString())

                    Log.d(tags, "listTypesNumber : ${listTypesNumber} \n listDataCategory : $listDataCategory ")
                }
            }
        })
    }

    // get penghasilan
    private fun getPenghasilan(){

        showLoading(true)

        val baseurl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val pernr = session.userNIK
        val payslipType = typeClicked

        viewModel.setDataPenghasilan(baseurl, token, date, pernr, payslipType)

        viewModel.getDataPenghasilan().observe(this, Observer { model ->
            Log.d(tags, "check model value : $model")
            if (model.isEmpty()) {
                showLoading(false)
            } else {
                if (model[0].intResponse == 200) {
                    Log.d(tags, "test getAssignmentLetter")
                    adapter.setData(model)
                } else {
                }
                showLoading(false)
            }
        })

        viewModel.getDataPenghasilan()
    }
    // get penghasilan


    // get potongan
    private fun getPotongan(){

        showLoading(true)

        val baseurl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val pernr = session.userNIK
        val payslipType = typeClicked


        viewModel.setDataPotongan(baseurl, token, date, pernr, payslipType)

        viewModel.getDataPotongan().observe(this, Observer { model ->
            Log.d(tags, "check model value : $model")
            if (model.isEmpty()) {
                showLoading(false)
            } else {
                if (model[0].intResponse == 200) {
                    Log.d(tags, "test getAssignmentLetter")
                    adapterPotongan.setData(model)
                } else {
                }
                showLoading(false)
            }
        })

        viewModel.getDataPenghasilan()
    }
    // get potongan

    // show list penghasilan
    private fun showListPenghasilan(){
        adapter = PayslipRincianGajiAdapter()
        rv_penghasilan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_penghasilan.adapter = adapter

    }
    // show list penghasilan

    // show list potongan
    private fun showListPotongan(){
        adapterPotongan = PayslipPotonganAdapter()
        rv_potongan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_potongan.adapter = adapterPotongan
    }
    // show list potongan


    // get total penghasilan
    private fun getTotalPenghasilan(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val pernr = session.userNIK
        val payslipType = typeClicked

        val requestUrl = "${baseUrl}hcis/api/payrolldetail?begin_date_lte=$date&end_date_gte=$date&personnel_number=$pernr&payroll_date=$payslipType&flag_revenue=y&tax_mode=1&include=total_value"
        Log.d(tags,"url get total penghasilan : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags,"response get total penghasilan : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonObjectMeta = response.getJSONObject("meta")
                            val totalPenghasilan = jsonObjectMeta.getString("total_value")

                            // to settext method
                            setTextTotalPenghasilan(totalPenghasilan)
                            Log.d(tags,"check value total penghasilan : $totalPenghasilan")

                            // for take home pay
                            totalPenghasilanForTakeHomePay = totalPenghasilan.toDouble()

                            // to take home pay summary
                            if (totalPenghasilanForTakeHomePay!=0.0 && totalPotonganForTakeHomePay!=0.0){
                                takeHomePaySummary()
                            }


                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {


                    }
                })
    }
    // get total penghasilan

    // get total potongan
    private fun getTotalPotongan(){

        val baseUrl = session.serverURLHCIS
        val token = session.token
        val date = tgl
        val pernr = session.userNIK
        val payslipType = typeClicked

        val requestUrl = "${baseUrl}hcis/api/payrolldetail?begin_date_lte=$date&end_date_gte=$date&personnel_number=$pernr&payroll_date=$payslipType&flag_revenue=n&tax_mode=1&include=total_value"
        Log.d(tags,"url get total potongan : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags,"response get total potongan : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonObjectMeta = response.getJSONObject("meta")
                            val totalPotongan = jsonObjectMeta.getString("total_value")

                            // to settext method
                            setTextTotalPotongan(totalPotongan)
                            Log.d(tags,"check value total potongan : $totalPotongan")

                            // for take home pay
                            totalPotonganForTakeHomePay = totalPotongan.toDouble()


                            // to take home pay summary
                            if (totalPenghasilanForTakeHomePay!=0.0 && totalPotonganForTakeHomePay!=0.0){
                                takeHomePaySummary()
                            }


                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {


                    }
                })
    }
    // get total potongan

    // get Oid for webview
    private fun getOid(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val buscd = session.userBusinessCode
        val pernr = session.userNIK
        val typeClickedDate = typeClicked

        val requestUrl = "${baseUrl}hcis/api/payrollheader?business_code=$buscd&personnel_number=$pernr&payroll_date=$typeClickedDate"
        Log.d(tags,"url get oid detail payslip : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags,"response get list di get Oid : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayOid : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayOid.length()) {
                                val objOid : JSONObject = jsonArrayOid.getJSONObject(a)

                                val objectIdentifier = objOid.getInt("object_identifier")
                                oidForWebview = objectIdentifier.toString()
                                Log.d(tags,"check value oid : $oidForWebview")
                            }

                        } else {

                            Log.d(tags,"check value response masuk ke else di getOid() : ${response?.getString("message")}")

                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tags,"masuk ke on error di getOid() : ${anError?.errorBody}")
                    }
                })
    }
    // get Oid for webview

    // DATE PICKER JUST MONTH AND YEAR
    fun formatMonthYear(str: String?): String? {
        var date: Date? = null
        try {
            date = input.parse(str)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return sdf.format(date)
    }
    // DATE PICKER JUST MONTH AND YEAR

    //SHOWLOADING
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_payslip.visibility = View.VISIBLE
        } else {
            progressBar_payslip.visibility = View.GONE
        }
    }
    //SHOWLOADING

    //SETVISIBILITY RECYCLERVIEW
    private fun setVisibilityAllTable(){
        ll_rincian_gaji.visibility = View.VISIBLE
        ll_penghasilan.visibility = View.VISIBLE
        ll_potongan.visibility = View.VISIBLE
        btn_gotodetail_payslip.visibility = View.VISIBLE
    }
    //SETVISIBILITY RECYCLERVIEW

    // RESET VIEW AND SET TO GONE
    private fun resetViewAndSetGone(){
        ll_rincian_gaji.visibility = View.GONE
        ll_penghasilan.visibility = View.GONE
        ll_potongan.visibility = View.GONE
        btn_gotodetail_payslip.visibility = View.GONE

        // clear take home pay text
        tv_value_totaltakehomepay.text = ""
    }
    // RESET VIEW AND SET TO GONE

    // setText total penghasilan
    private fun setTextTotalPenghasilan( total : String? ){

        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val formatedRupiahPenghasilan = formatRupiah.format(total?.toDouble())

        tv_value_totalpenghasilan.text = formatedRupiahPenghasilan
        value_total_penghasilan.text = formatedRupiahPenghasilan
    }
    // setText total penghasilan

    // setText total potongan
    private fun setTextTotalPotongan( total : String? ){

        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        val formatedRupiahPenghasilan = formatRupiah.format(total?.toDouble())

        tv_value_totalpotongan.text = formatedRupiahPenghasilan
        value_total_potongan.text = formatedRupiahPenghasilan
    }
    // setText total potongan

    // summary take home pay
    private fun takeHomePaySummary(){
        if (totalPenghasilanForTakeHomePay != null && totalPotonganForTakeHomePay != null){
            val takeHomePay = totalPenghasilanForTakeHomePay!!.minus(totalPotonganForTakeHomePay!!)
            Log.d(tags,"check take home pay summary : $takeHomePay")

            // format to rupiah & set text take home pay
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            val formatedRupiahTakeHomePay = formatRupiah.format(takeHomePay.toDouble())
            tv_value_totaltakehomepay.text = formatedRupiahTakeHomePay.toString()
        }
    }
    // summary take home pay

    // convert month to number
    private fun convertMonthToNumber(month: String?){
        if (month!!.contains("Jan")){
            monthChosenCode = "01"
            monthChosenName = "January"
        } else if (month.contains("Feb")){
            monthChosenCode = "02"
            monthChosenName = "February"
        } else if (month.contains("Mar")){
            monthChosenCode = "03"
            monthChosenName = "March"
        } else if (month.contains("Apr")){
            monthChosenCode = "04"
            monthChosenName = "April"
        } else if (month.contains("May")){
            monthChosenCode = "05"
            monthChosenName = "May"
        } else if (month.contains("Jun")){
            monthChosenCode = "06"
            monthChosenName = "June"
        } else if (month.contains("Jul")){
            monthChosenCode = "07"
            monthChosenName = "July"
        } else if (month.contains("Aug")){
            monthChosenCode = "08"
            monthChosenName = "August"
        } else if (month.contains("Sep")){
            monthChosenCode = "09"
            monthChosenName = "September"
        } else if (month.contains("Oct")){
            monthChosenCode = "10"
            monthChosenName = "October"
        } else if (month.contains("Nov")){
            monthChosenCode = "11"
            monthChosenName = "November"
        } else if (month.contains("Des")){
            monthChosenCode = "12"
            monthChosenName = "December"
        }
        Log.d(tags, "check month : $month \n monthChosen : $monthChosenCode")
    }
    // convert month to number

    // intent to webview detail paylist
    private fun goToDetail(){

        val pernr = session.userNIK
        val monthName = monthChosenName
        val objIdentifier = oidForWebview
        val dateTypeChosen = typeClicked
        val yearChosen = yearChosen

        val webViewUrl = ("https://hcis.pegadaian.co.id/slip-gaji?nik=$pernr&p=$monthName-$yearChosen&oid=$objIdentifier&paydt=$dateTypeChosen")
        val intent1 = Intent(this, DetailPaylistWebview::class.java)
        intent1.putExtra("url", webViewUrl)
        startActivity(intent1)
        println("print webview url payslip : $webViewUrl")

        finish()

    }
    // intent to webview detail paylist


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.spinner_payslip_period -> {
//                DatePickerDialog(this, dateIssue, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()

                // cek untuk month date picker
                val pickerDialog = MonthYearPickerDialog()
                pickerDialog.setListener(OnDateSetListener { datePicker, year, month, i2 ->
                    monthYearStr = year.toString() + "-" + (month + 1) + "-" + i2
                    val formatedMonthYear = formatMonthYear(monthYearStr).toString()
                    spinner_payslip_period.text = formatedMonthYear
                    convertMonthToNumber(formatedMonthYear)
                    yearChosen = formatedMonthYear.substring(formatedMonthYear.length - 4)

                    // clear type setiap ganti bulan
                    spinner_payslip_type.text = ""

                })
                pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
                // cek untuk month date picker

                // reset view
                resetViewAndSetGone()

            }

            R.id.spinner_payslip_type -> {

                var category: String? = null
                var code: String? = null
                val spinnerDialogDataTypes = SpinnerDialog(
                        this, listDataCategory, "Select Item :", R.style.DialogAnimations_SmileWindow
                )

                spinnerDialogDataTypes.bindOnSpinerListener { s, i ->
                    category = listTypesNumber[i].name

                    Log.d(tags, "check value category onClick \n category : $category \n code : $code")

                    spinner_payslip_type.text = category
                    strListTypes = code

                    if (category == "Reguler") {
                        Log.d(tags, "reguler terpilih")
                        typeClicked = "2021-${monthChosenCode}-25"
                    } else if (category == "Irreguler") {
                        Log.d(tags, "irreguler terpilih")
                        typeClicked = "2021-${monthChosenCode}-26"
                    }
                    Log.d(tags, "check value type clicked : ${typeClicked}")
                }
                spinnerDialogDataTypes.showSpinerDialog()

                // reset view
                resetViewAndSetGone()

            }

            R.id.btn_search_payslip -> {

                val valuePeriod = spinner_payslip_period.text.toString()
                val valueType = spinner_payslip_type.text.toString()

                if (!valuePeriod.equals("") && !valueType.equals("")) {

                    Log.d(tags, "masuk ke search paylist ---> \n valuePeriod : $valuePeriod \n valueType : $valueType")

                    // setVisibility all table
                    setVisibilityAllTable()

                    // penghasilan
                    getPenghasilan()
                    showListPenghasilan()

                    // potongan
                    getPotongan()
                    showListPotongan()

                    // get oid
                    getOid()

                    // get total penghasilan dan total potongan
                    getTotalPenghasilan()
                    getTotalPotongan()

                } else {
                    Toast.makeText(this, "Please fill period and type paylist", Toast.LENGTH_SHORT).show()
                }


            }

            R.id.btn_gotodetail_payslip -> {
                goToDetail()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }
}
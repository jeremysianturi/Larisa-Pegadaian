package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataCategorySpinnerAddData
import id.co.pegadaian.diarium.entity.DataPayslipPotongan
import id.co.pegadaian.diarium.entity.DataPayslipRincianGaji
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import org.json.JSONArray
import org.json.JSONObject

class PayslipRincianGajiModel : ViewModel() {
    val tag = PayslipRincianGajiModel::class.java.simpleName
    private val listDataPenghasilan = MutableLiveData<ArrayList<DataPayslipRincianGaji>>()
    private val listDataPotongan = MutableLiveData<ArrayList<DataPayslipPotongan>>()
    private val listDataCategory = MutableLiveData<ArrayList<DataCategorySpinnerAddData>>()

    fun setDataPenghasilan(baseUrl : String?, token : String?, date : String?, pernr : String?, paySlipType : String?) {
        val listPenghasilan = ArrayList<DataPayslipRincianGaji>()

        val requestUrl = "${baseUrl}hcis/api/payrolldetail?begin_date_lte=$date&end_date_gte=$date&personnel_number=$pernr&payroll_date=$paySlipType&flag_revenue=y&tax_mode=1&per_page=999"
        Log.d(tag,"url get data penghasilan : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list penghasilan detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayPenghasilan : JSONArray = response.getJSONArray("data")
                            for (a in 0 until jsonArrayPenghasilan.length()) {
                                val objPenghasilan : JSONObject = jsonArrayPenghasilan.getJSONObject(a)

                                val wageType = objPenghasilan.getString("wage_type")
                                val value = objPenghasilan.getString("value")
                                Log.d(tag,"check value \n wage penghasilan : $wageType \n value penghasilan : $value")

                                if (paySlipType!!.contains("25") && !wageType.equals("S623")){
                                    val data = DataPayslipRincianGaji(intResponse, wageType, value)
                                    listPenghasilan.add(data)
                                }

                            }

                            listDataPenghasilan.postValue(listPenghasilan)

                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {

                        errorLog(anError,"Penghasilan at payslip")

                    }
                })
    }

    fun getDataPenghasilan() : LiveData<ArrayList<DataPayslipRincianGaji>> {
        return listDataPenghasilan
    }

    fun setDataPotongan(baseUrl : String?, token : String?, date : String?, pernr : String?, paySlipType : String?) {
        val listPotongan = ArrayList<DataPayslipPotongan>()

        val requestUrl = "${baseUrl}hcis/api/payrolldetail?begin_date_lte=$date&end_date_gte=$date&personnel_number=$pernr&payroll_date=$paySlipType&flag_revenue=n&tax_mode=1&per_page=999"
        Log.d(tag,"url get data potongan : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list potongan detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayPenghasilan : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayPenghasilan.length()) {
                                val objPenghasilan : JSONObject = jsonArrayPenghasilan.getJSONObject(a)

                                val wageType = objPenghasilan.getString("wage_type")
                                val value = objPenghasilan.getString("value")

                                Log.d(tag,"check value \n wage potongan : $wageType \n value potongan : $value")

                                if (paySlipType!!.contains("25") && !wageType.equals("S423")){
                                    val data = DataPayslipPotongan(intResponse, wageType, value)
                                    listPotongan.add(data)
                                }

                            }

                            listDataPotongan.postValue(listPotongan)

                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {

                        errorLog(anError,"Potongan at payslip")

                    }
                })
    }

    fun getDataPotongan() : LiveData<ArrayList<DataPayslipPotongan>> {
        return listDataPotongan
    }

    fun setDataTypesList (){
        val listDataTypes = ArrayList<DataCategorySpinnerAddData>()

        //      first array
        var dataTypesListSpinner = DataCategorySpinnerAddData("Reguler")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
        //      first array

//      second array
        dataTypesListSpinner = DataCategorySpinnerAddData("Irreguler")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
//      second array

    }

    fun getDataTypesList() : LiveData<ArrayList<DataCategorySpinnerAddData>>{
        return listDataCategory
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }

}
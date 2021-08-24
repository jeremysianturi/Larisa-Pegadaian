package id.co.pegadaian.diarium.model

import DataSpinnerEleaveCutiBesar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataSpinnerEleave
import id.co.pegadaian.diarium.entity.DataSpinnerPresenceConfirmation
import org.json.JSONArray
import org.json.JSONObject

class CreateEleaveModel: ViewModel() {
    val tag = CreateEleaveModel::class.java.simpleName
    private val listDataSpinner = MutableLiveData<ArrayList<DataSpinnerEleave>>()
    private val listDataSpinnerCutiBesar = MutableLiveData<ArrayList<DataSpinnerEleaveCutiBesar>>()

    fun setDataSpinner (baseurl : String?, token : String?){

        val listEleaveType = ArrayList<DataSpinnerEleave>()
        val requestUrl = "${baseurl}hcis/api/leave-parameter?per_page=1000"
        Log.d(tag,"get data spinner new eleave: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list status eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayStatusEleave : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayStatusEleave.length()) {
                                val objSpinner: JSONObject = jsonArrayStatusEleave.getJSONObject(a)

                                val typeNameEleave = objSpinner.getString("leave_name")
                                val typeCodeEleave = objSpinner.getString("leave_code")

                                val data = DataSpinnerEleave(intResponse,typeNameEleave,typeCodeEleave)
                                listEleaveType.add(data)
                            }
                            listDataSpinner.postValue(listEleaveType)

                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"error get spinner at : $tag")
                    }
                })

    }

    fun getDataSpinner() : LiveData<ArrayList<DataSpinnerEleave>> {
        return listDataSpinner
    }

    fun setDataSpinnerCutiBesar(baseurl : String?, token : String?, date : String?, buscd : String?, objType : String?){

        val listEleaveTypeCutiBesar = ArrayList<DataSpinnerEleaveCutiBesar>()
        val requestUrl = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=$buscd&object_type=$objType"
        Log.d(tag,"get data spinner new eleave cuti besar : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list status eleave cuti besar : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {
                            val jsonArrayStatusEleaveCutiBesar : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayStatusEleaveCutiBesar.length()) {
                                val objSpinner: JSONObject = jsonArrayStatusEleaveCutiBesar.getJSONObject(a)

                                val typeName = objSpinner.getString("object_name")
                                val typeCode = objSpinner.getString("object_code")


                                val data = DataSpinnerEleaveCutiBesar(intResponse,typeName,typeCode)
                                listEleaveTypeCutiBesar.add(data)
                            }
                            listDataSpinnerCutiBesar.postValue(listEleaveTypeCutiBesar)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"error get spinner cuti besar at : $tag")
                    }
                })

    }

    fun getDataSpinnerCutiBesar() : LiveData<ArrayList<DataSpinnerEleaveCutiBesar>> {
        return listDataSpinnerCutiBesar
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
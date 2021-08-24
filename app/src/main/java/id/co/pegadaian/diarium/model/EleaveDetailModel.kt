package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.EleaveDetail
import id.co.pegadaian.diarium.entity.DataEleaveDetail
import id.co.pegadaian.diarium.entity.DataEleaveList
import org.json.JSONArray
import org.json.JSONObject

class EleaveDetailModel : ViewModel() {
    val tag = EleaveDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataEleaveDetail>>()

    fun setData(baseUrl: String?, token: String?, buscd: String?, objIdentifier: String?, pernr: String?) {
        val listEleaveDetail = ArrayList<DataEleaveDetail>()

        val requestUrl = "${baseUrl}hcis/api/leave?business_code=$buscd&personnel_number=$pernr&" +
                "include=leave_detail&include=leave_code&include=personnel_number&include=leave_approval&object_identifier=$objIdentifier"
        Log.d(tag, "get data eleave detail: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list eleave detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleave: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleave.length()) {
                                val objEleave: JSONObject = jsonArrayEleave.getJSONObject(a)

                                // creator
                                val objCreator = objEleave.getJSONObject("personnel_number")
                                val creator = objCreator.getString("complete_name")

                                // jenis cuti
                                val objJenisCuti = objEleave.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // allowance
                                val allowance = objEleave.getBoolean("flag_allowance").toString()
                                Log.d("EleaveDetailModel", "check value allowance : $allowance")

                                // lama cuti
                                val jsonArrayLeaveDetail = objEleave.getJSONArray("leave_detail")
                                val lamaCuti = jsonArrayLeaveDetail.length()

                                val data = DataEleaveDetail(intResponse,creator,jenisCuti,lamaCuti,allowance)
                                listEleaveDetail.add(data)

                            }
                            listData.postValue(listEleaveDetail)
                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"$tag an error get eleave detail")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataEleaveDetail>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
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
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveDetailTanggal
import org.json.JSONArray
import org.json.JSONObject

class EleaveDetailTanggalModel : ViewModel() {
    val tag = EleaveDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataEleaveDetailTanggal>>()

    fun setData(baseUrl: String?, token: String?, buscd: String?, objIdentifier: String?, pernr: String?) {
        val listEleaveDetailTanggal = ArrayList<DataEleaveDetailTanggal>()

        val requestUrl = "${baseUrl}hcis/api/leave?business_code=$buscd&personnel_number=$pernr&" +
                "include=leave_detail&include=leave_code&include=personnel_number&include=leave_approval&object_identifier=$objIdentifier"
        Log.d(tag, "get data eleave detail: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list eleave detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleave: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleave.length()) {
                                val objEleave: JSONObject = jsonArrayEleave.getJSONObject(a)

                                // approval status
                                val approvalStatus = objEleave.getString("approval_status")

                                val jsonArrayLeaveDetail = objEleave.getJSONArray("leave_detail")
                                var leaveDate : String? = null

                                // date detail
                                for (i in 0 until jsonArrayLeaveDetail.length()) {

                                    // tanggal cuti
                                    val objDateDetail : JSONObject = jsonArrayLeaveDetail.getJSONObject(i)
                                    leaveDate = objDateDetail.getString("leave_date")
                                    Log.d(tag,"check looping leave date E-leave : $leaveDate")

                                    // no
                                    val no = (i+1).toString()

                                    val data = DataEleaveDetailTanggal(intResponse,no,leaveDate,approvalStatus)
                                    listEleaveDetailTanggal.add(data)
                                }
                            }
                            listData.postValue(listEleaveDetailTanggal)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"$tag an error on tanggal eleave detail")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataEleaveDetailTanggal>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
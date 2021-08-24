package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.adapter.ApprovalEleaveDetailAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.ApprovalEleaveDetail
import id.co.pegadaian.diarium.entity.DataApprovalEleaveDetail
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveDetailApprovalTanggal
import id.co.pegadaian.diarium.entity.DataEleaveDetailTanggal
import org.json.JSONArray
import org.json.JSONObject

class ApprovalEleaveDetailTanggalModel : ViewModel() {
    val tag = ApprovalEleaveDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataEleaveDetailApprovalTanggal>>()

    fun setData(baseUrl: String?, token: String?, buscd: String?, objIdentifier: String?, pernr: String?) {
        val listEleaveDetailTanggalApproval = ArrayList<DataEleaveDetailApprovalTanggal>()

        val requestUrl = "${baseUrl}hcis/api/leave-approval?include=leave&include=leave_detail&include=personnel_number&include=leave_approval&approver=$pernr" +
                         "&include=leave_code&object_identifier=$objIdentifier"
        Log.d(tag, "get data eleave detail tanggal : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list eleave tanggal : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleaveApproval: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleaveApproval.length()) {
                                val objEleave: JSONObject = jsonArrayEleaveApproval.getJSONObject(a)

                                // object leave
                                val objLeave = objEleave.getJSONObject("leave")

                                val arrayLeaveDetail = objLeave.getJSONArray("leave_detail")

                                // status
                                val status = objLeave.getString("approval_status")

                                for (i in 0 until arrayLeaveDetail.length()) {
                                    val objDateDetail: JSONObject = arrayLeaveDetail.getJSONObject(i)

                                    // no
                                    val no = (i + 1).toString()

                                    // tanggal cuti
                                    val tanggal = objDateDetail.getString("leave_date")

                                    val data = DataEleaveDetailApprovalTanggal(intResponse, no, tanggal, status)
                                    listEleaveDetailTanggalApproval.add(data)

                                }
                            }

                            listData.postValue(listEleaveDetailTanggalApproval)
                        }


                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataEleaveDetailApprovalTanggal>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
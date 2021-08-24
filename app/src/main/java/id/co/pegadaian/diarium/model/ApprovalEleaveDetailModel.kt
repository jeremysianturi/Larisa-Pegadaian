package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.ApprovalEleaveDetail
import id.co.pegadaian.diarium.entity.DataApprovalEleaveDetail
import id.co.pegadaian.diarium.entity.DataApprovalEleaveList
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveDetail
import org.json.JSONArray
import org.json.JSONObject

class ApprovalEleaveDetailModel : ViewModel() {

    val tag = ApprovalEleaveDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataApprovalEleaveDetail>>()

    fun setData(baseUrl: String?, token: String?, buscd: String?, objIdentifier: String?, pernr: String?) {
        val listEleaveApprovalDetail = ArrayList<DataApprovalEleaveDetail>()

        val requestUrl = "${baseUrl}hcis/api/leave-approval?include=leave&include=leave_detail&include=personnel_number&include=leave_approval&approver=$pernr" +
                         "&include=leave_code&object_identifier=$objIdentifier"
        Log.d(tag, "get data eleave detail: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleaveApproval: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleaveApproval.length()) {
                                val objEleave: JSONObject = jsonArrayEleaveApproval.getJSONObject(a)

                                // obj u/ jenis cuti & creator
                                val objLeaveDetail = objEleave.getJSONObject("leave")

                                // jenis cuti name
                                val objJenisCuti = objLeaveDetail.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // name
                                val objCreator = objLeaveDetail.getJSONObject("personnel_number")
                                val name = objCreator.getString("complete_name")


                                // lama cuti
                                val jsonArrayLeaveDetail = objLeaveDetail.getJSONArray("leave_detail")
                                val lamaCuti = jsonArrayLeaveDetail.length()

                                // alowance
                                val allowance = objLeaveDetail.getString("flag_allowance")

                                val data = DataApprovalEleaveDetail(intResponse,name,jenisCuti,lamaCuti,allowance)
                                listEleaveApprovalDetail.add(data)
                            }
                            listData.postValue(listEleaveApprovalDetail )
                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataApprovalEleaveDetail>> {
        return listData
    }

    fun approveData(baseUrl : String?,token: String?, objIdentifier : String?){

        val approveUrl = "${baseUrl}hcis/api/leave-approval"
        Log.d(tag,"url approve eleave di detail : $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("approval_status", "2")

        Log.d(tag,"check body approve eleave di detail : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response approve eleave di detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                        } else {

                        }



                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"cek anError approve eleave di detail : $anError")
                    }
                })
    }

    fun rejectData(baseUrl : String?,token: String?, objIdentifier : String?){

        val approveUrl = "${baseUrl}hcis/api/leave-approval"
        Log.d(tag,"url reject eleave di detail: $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("approval_status", "3")

        Log.d(tag,"check body reject eleave di detail : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response reject eleave di detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                        } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"cek anError reject eleave di detail : $anError")
                    }
                })
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataApprovalEleaveList
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveList
import id.co.pegadaian.diarium.entity.DataSpinnerStatusEleaveList
import org.json.JSONArray
import org.json.JSONObject

class ApprovalListEleaveModel : ViewModel() {

    val tag = ApprovalListEleaveModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataApprovalEleaveList>>()
//    private val listDataChild = MutableLiveData<ArrayList<DataChildEleaveList>>()         // untuk nested recycler view
    private val listDataFiltered = MutableLiveData<ArrayList<DataApprovalEleaveList>>()
    private val listDataStatusApproval = MutableLiveData<ArrayList<DataSpinnerStatusEleaveList>>()

    fun setData(baseUrl : String?, token : String?, buscd : String?, pernr : String?) {
        val listApprovalEleave = ArrayList<DataApprovalEleaveList>()
        val listEleaveChild = ArrayList<DataChildEleaveList>()
        val requestUrl = "${baseUrl}hcis/api/leave-approval?include=leave&include=leave_detail&include=personnel_number&include=leave_approval&approver=$pernr" +
                "&include=leave_code&per_page=1000"
        Log.d(tag, "get data eleave approval : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleaveApproval: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleaveApproval.length()) {
                                val objEleave: JSONObject = jsonArrayEleaveApproval.getJSONObject(a)

                                // object identifier
                                val objIdentifier = objEleave.getInt("object_identifier")

                                // approvable
                                val approvable = objEleave.getBoolean("approvable").toString()

                                // status approval
                                val statusApproval = objEleave.getString("approval_status")

                                // obj u/ jenis cuti & creator
                                val objLeaveDetail = objEleave.getJSONObject("leave")

                                // jenis cuti name
                                val objJenisCuti = objLeaveDetail.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // creator
                                val objCreator = objLeaveDetail.getJSONObject("personnel_number")
                                val creator = objCreator.getString("complete_name")

                                // created date
                                val createdDate = objEleave.getString("begin_date")

                                // lama cuti
                                val jsonArrayLeaveDetail = objLeaveDetail.getJSONArray("leave_detail")
                                val lamaCuti = jsonArrayLeaveDetail.length()

                                // children (detail tanggal cutinya)
                                var leaveDate : String? = null
                                for (i in 0 until jsonArrayLeaveDetail.length()) {

                                    Log.d(tag,"TADAAAAAAAAAAAAAAAA array ke : $i &&&& panjangnya : ${jsonArrayLeaveDetail.length()}")

                                    val objDateDetail : JSONObject = jsonArrayLeaveDetail.getJSONObject(i)
                                    leaveDate = objDateDetail.getString("leave_date")

                                    Log.d(tag,"check looping leave date E-leave : $leaveDate")

                                    val dataChild = DataChildEleaveList("tanggal cuti",leaveDate)
                                    listEleaveChild.add(dataChild)
                                }

                                val data = DataApprovalEleaveList(intResponse,objIdentifier,approvable,statusApproval,jenisCuti,creator,createdDate,lamaCuti,listEleaveChild)
                                listApprovalEleave.add(data)
                            }
                            listData.postValue(listApprovalEleave )
                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataApprovalEleaveList>> {
        return listData
    }

    fun setDataFiltered(baseUrl : String?, token : String?, buscd : String?, pernr : String?,status : String?) {

        val listEleaveApprovalFiltered = ArrayList<DataApprovalEleaveList>()
        val listEleaveChildApprovalFiltered = ArrayList<DataChildEleaveList>()
        val requestUrl = "${baseUrl}hcis/api/leave-approval?include=leave&include=leave_detail&include=personnel_number&include=leave_approval&approval_status=1&approver=$pernr" +
                "&include=leave_code&approval_status=$status&per_page=1000"
        Log.d(tag, "get data eleave approval filtered : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", "Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayEleaveApproval: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleaveApproval.length()) {
                                val objEleave: JSONObject = jsonArrayEleaveApproval.getJSONObject(a)

                                // object identifier
                                val objIdentifier = objEleave.getInt("object_identifier")

                                // approvable
                                val approvable = objEleave.getBoolean("approvable").toString()

                                // status approval
                                val statusApproval = objEleave.getString("approval_status")

                                // obj u/ jenis cuti & creator
                                val objLeaveDetail = objEleave.getJSONObject("leave")

                                // jenis cuti name
                                val objJenisCuti = objLeaveDetail.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // creator
                                val objCreator = objLeaveDetail.getJSONObject("personnel_number")
                                val creator = objCreator.getString("complete_name")

                                // created date
                                val createdDate = objEleave.getString("begin_date")

                                // lama cuti
                                val jsonArrayLeaveDetail = objLeaveDetail.getJSONArray("leave_detail")
                                val lamaCuti = jsonArrayLeaveDetail.length()

                                // children (detail tanggal cutinya)
                                var leaveDate : String? = null
                                for (i in 0 until jsonArrayLeaveDetail.length()) {

                                    Log.d(tag,"TADAAAAAAAAAAAAAAAA array ke : $i &&&& panjangnya : ${jsonArrayLeaveDetail.length()}")

                                    val objDateDetail : JSONObject = jsonArrayLeaveDetail.getJSONObject(i)
                                    leaveDate = objDateDetail.getString("leave_date")

                                    Log.d(tag,"check looping leave date E-leave : $leaveDate")

                                    val dataChild = DataChildEleaveList("tanggal cuti",leaveDate)
                                    listEleaveChildApprovalFiltered.add(dataChild)
                                }

                                val data = DataApprovalEleaveList(intResponse,objIdentifier,approvable,statusApproval,jenisCuti,creator,createdDate,lamaCuti,listEleaveChildApprovalFiltered)
                                listEleaveApprovalFiltered.add(data)
                            }
                            listData.postValue(listEleaveApprovalFiltered)
                        }

                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getDataFiltered() : LiveData<ArrayList<DataApprovalEleaveList>> {
        return listDataFiltered
    }

    fun setStatusApprovalEleaveList(baseUrl: String?, token: String?, buscd: String?, tgl:String?, objType : String?){
        val listSpinnerStatusApprovalEleave = ArrayList<DataSpinnerStatusEleaveList>()
        val requestUrl = "${baseUrl}ldap/api/object?begin_date_lte=$tgl&end_date_gt=$tgl&business_code=$buscd&object_type=$objType"
        Log.d(tag,"get data spinner status approval eleave : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list spinner status approval eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {
                            val jsonArrayEleave: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleave.length()) {
                                val objEleave: JSONObject = jsonArrayEleave.getJSONObject(a)

                                // code
                                val code = objEleave.getString("object_code")

                                // name
                                val name = objEleave.getString("object_name")

                                Log.d(tag,"check status approval eleave : $code and $name")
                                val data = DataSpinnerStatusEleaveList(intResponse,code,name)
                                listSpinnerStatusApprovalEleave.add(data)
                            }
                            listDataStatusApproval.postValue(listSpinnerStatusApprovalEleave)
                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getDataStatusApprovalEleaveList() : LiveData<ArrayList<DataSpinnerStatusEleaveList>> {
        return listDataStatusApproval
    }


    fun approveData(baseUrl : String?,token: String?, objIdentifier : String?){

        val approveUrl = "${baseUrl}hcis/api/leave-approval"
        Log.d(tag,"url approve eleave: $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("approval_status", "2")

        Log.d(tag,"check body approve eleave : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response approve eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                        } else {

                        }



                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"cek anError approve eleave : $anError")
                    }
                })
    }

    fun rejectData(baseUrl : String?,token: String?, objIdentifier : String?){

        val approveUrl = "${baseUrl}hcis/api/leave-approval"
        Log.d(tag,"url reject eleave: $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("approval_status", "3")

        Log.d(tag,"check body reject eleave : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response reject eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                        } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"cek anError reject eleave : $anError")
                    }
                })
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
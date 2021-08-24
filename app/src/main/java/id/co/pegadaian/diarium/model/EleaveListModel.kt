package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveList
import id.co.pegadaian.diarium.entity.DataSpinnerStatusEleaveList
import id.co.pegadaian.diarium.entity.DataSuratTugas
import org.json.JSONArray
import org.json.JSONObject

class EleaveListModel : ViewModel() {

    val tag = EleaveListModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataEleaveList>>()
    private val listDataChild = MutableLiveData<ArrayList<DataChildEleaveList>>()
    private val listDataFiltered = MutableLiveData<ArrayList<DataEleaveList>>()
    private val listDataStatusApproval = MutableLiveData<ArrayList<DataSpinnerStatusEleaveList>>()

    fun setData(baseUrl : String?, token : String?, buscd : String?, pernr : String?){
        val listEleave = ArrayList<DataEleaveList>()
        val listEleaveChild = ArrayList<DataChildEleaveList>()
        val requestUrl = "${baseUrl}hcis/api/leave?business_code=$buscd&personnel_number=$pernr" +
                "&include=leave_detail&include=leave_code&include=personnel_number&include=leave_approval&per_page=1000"
        Log.d(tag,"get data eleave : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list eleave : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayEleave : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleave.length()) {
                                val objEleave: JSONObject = jsonArrayEleave.getJSONObject(a)

                                // object identifier
                                val objIdentifier = objEleave.getInt("object_identifier")

                                // approval completion
                                val approvalCompletion = objEleave.getString("approval_completion")

                                // approval status
                                val approvalStatus = objEleave.getString("approval_status")

                                // jenis cuti
                                val objJenisCuti = objEleave.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // creator
                                val objCreator = objEleave.getJSONObject("personnel_number")
                                val creator = objCreator.getString("complete_name")

                                // created date
                                val createdDate = objEleave.getString("begin_date")

                                // lama cuti
                                val leaveDetailNull = objEleave.getString("leave_detail")                   // for leave_detail null
                                var leaveDate : String? = ""
                                var lamaCuti = 0


                                if (leaveDetailNull.equals("null")){
                                    lamaCuti = 0
                                } else {
                                    val jsonArrayLeaveDetail = objEleave.getJSONArray("leave_detail")       // for leave_detail not null
                                    lamaCuti = jsonArrayLeaveDetail.length()                                       // for leave_detail not null

                                    // date detail
                                    for (i in 0 until jsonArrayLeaveDetail.length()) {

                                        Log.d(tag,"TADAAAAAAAAAAAAAAAA array ke : $i &&&& panjangnya : ${jsonArrayLeaveDetail.length()}")

                                        val objDateDetail : JSONObject = jsonArrayLeaveDetail.getJSONObject(i)
                                        leaveDate = objDateDetail.getString("leave_date")

                                        Log.d(tag,"check looping leave date E-leave : $leaveDate")

                                        val dataChild = DataChildEleaveList("tanggal cuti",leaveDate)
                                        listEleaveChild.add(dataChild)
                                        printChildDulu(listEleaveChild)

                                    }

                                }
//                                listDataChild.postValue(listEleaveChild)

                                val data = DataEleaveList(intResponse,objIdentifier,approvalCompletion,approvalStatus,jenisCuti,creator,createdDate,lamaCuti,listEleaveChild)
                                listEleave.add(data)

                                printParentDulu(listEleave)
                            }
                            listData.postValue(listEleave)

                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataEleaveList>> {
        return listData
    }


    fun setDataFiltered(baseUrl : String?, token : String?, buscd : String?, pernr : String?,status : String?){

        val listEleaveFiltered = ArrayList<DataEleaveList>()
        val listEleaveChildFiltered = ArrayList<DataChildEleaveList>()
        val requestUrl = "${baseUrl}hcis/api/leave?business_code=$buscd&personnel_number=$pernr" +
               "&include=leave_detail&include=leave_code&include=personnel_number&include=leave_approval&approval_status=$status&per_page=1000"
        Log.d(tag,"get data eleave filtered : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list eleave filtered : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArrayEleave : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayEleave.length()) {
                                val objEleave: JSONObject = jsonArrayEleave.getJSONObject(a)

                                // object identifier
                                val objIdentifier = objEleave.getInt("object_identifier")

                                // approval completion
                                val approvalCompletion = objEleave.getString("approval_completion")

                                // approval status
                                val approvalStatus = objEleave.getString("approval_status")

                                // jenis cuti
                                val objJenisCuti = objEleave.getJSONObject("leave_code")
                                val jenisCuti = objJenisCuti.getString("leave_name")

                                // creator
                                val objCreator = objEleave.getJSONObject("personnel_number")
                                val creator = objCreator.getString("complete_name")

                                // created date
                                val createdDate = objEleave.getString("begin_date")

                                // lama cuti
                                val jsonArrayLeaveDetail = objEleave.getJSONArray("leave_detail")
                                val lamaCuti = jsonArrayLeaveDetail.length()

                                var leaveDate : String? = null

                                // date detail
                                for (i in 0 until jsonArrayLeaveDetail.length()) {

                                    Log.d(tag,"TADAAAAAAAAAAAAAAAA array ke : $i &&&& panjangnya : ${jsonArrayLeaveDetail.length()}")

                                    val objDateDetail : JSONObject = jsonArrayLeaveDetail.getJSONObject(i)
                                    leaveDate = objDateDetail.getString("leave_date")

                                    Log.d(tag,"check looping leave date E-leave : $leaveDate")

                                    val dataChild = DataChildEleaveList("tanggal cuti",leaveDate)
                                    listEleaveChildFiltered.add(dataChild)
                                    printChildDulu(listEleaveChildFiltered)

                                }

//                                listDataChild.postValue(listEleaveChild)
                                Log.d(tag,"kesini dulu kan?")

                                val data = DataEleaveList(intResponse,objIdentifier,approvalCompletion,approvalStatus,jenisCuti,creator,createdDate,lamaCuti,listEleaveChildFiltered)
                                listEleaveFiltered.add(data)

                                printParentDulu(listEleaveFiltered)
                            }
                            listDataFiltered.postValue(listEleaveFiltered)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"an error filtered eleave list : $anError")
                    }
                })
    }

    fun getDataFiltered() : LiveData<ArrayList<DataEleaveList>> {
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

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
    fun printChildDulu(listChild : List<DataChildEleaveList>){
        Log.d(tag,"list child : $listChild")
    }
    fun printParentDulu(listParent : List<DataEleaveList>){
        Log.d(tag,"list parent : $listParent")
    }
}
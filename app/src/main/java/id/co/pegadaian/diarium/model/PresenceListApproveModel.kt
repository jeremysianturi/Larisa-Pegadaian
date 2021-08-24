package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataPresenceList
import id.co.pegadaian.diarium.entity.DataStatusPresenceList
import org.json.JSONArray
import org.json.JSONObject

class PresenceListApproveModel : ViewModel() {

    val tag = PresenceListApproveModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataPresenceList>>()
    private val listDataFiltered = MutableLiveData<ArrayList<DataPresenceList>>()
    private val listStatus = MutableLiveData<ArrayList<DataStatusPresenceList>>()

    fun setDataApprovePresenceList(baseUrl : String?, token : String?, pernr : String?) {

        val listPresence = ArrayList<DataPresenceList>()
        val requestUrl = "${baseUrl}users/presensi/absent?personal_number_approval=$pernr&include=absent_type&include=approval_status" +
                "&include=personal_number&per_page=1000"
        Log.d(tag, "get presence approval listing : $requestUrl")


        AndroidNetworking.get(requestUrl)
//                .addHeaders("Accept", "application/json")
//                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag, "response get list presence approval : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayPresence : JSONArray = response.getJSONArray("data")
                            for (a in 0 until jsonArrayPresence.length()) {
                                val objSurat: JSONObject = jsonArrayPresence.getJSONObject(a)

                                val objIdentifier = objSurat.getInt("object_identifier")
                                val dateAbsent = objSurat.getString("date_absent")

                                // pernr pemohon
                                val objPernr = objSurat.getJSONObject("personal_number")
                                val pernrPemohon = objPernr.getString("full_name")

                                // status
                                val objAbsentApproval : JSONObject = objSurat.getJSONObject("approval_status")
                                val statusName = objAbsentApproval.getString("object_name")

                                // absent type
                                val objAbsentType = objSurat.getJSONObject("absent_type")
                                val absentType = objAbsentType.getString("object_name")


                                val data = DataPresenceList(intResponse,objIdentifier,pernrPemohon,dateAbsent,statusName,absentType)
                                listPresence.add(data)

                            }

                            listData.postValue(listPresence)
                        }

                        }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK NGERROOOORRRR approval yeha")

                    }
                })

    }

    fun getDataApprovePresenceList() : LiveData<ArrayList<DataPresenceList>> {
        return listData
    }

    fun setDataApprovePresenceListFiltered(baseurl : String?, token : String?, pernr : String?, date : String?, status : String?){

        val listPresenceFiltered = ArrayList<DataPresenceList>()
        val requestUrl = "${baseurl}users/presensi/absent?personal_number_approval=$pernr&include=absent_type&include=approval_status" +
                "&include=personal_number$date$status"
        Log.d(tag,"get presence approval list filtered: $requestUrl")

        AndroidNetworking.get(requestUrl)
//                .addHeaders("Accept", "application/json")
//                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization",token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag, "response get list presence approval filtered : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayPresence : JSONArray = response.getJSONArray("data")
                            for (a in 0 until jsonArrayPresence.length()) {
                                val objSurat: JSONObject = jsonArrayPresence.getJSONObject(a)

                                val objIdentifier = objSurat.getInt("object_identifier")
                                val dateAbsent = objSurat.getString("date_absent")

                                // pernr pemohon
                                val objPernr = objSurat.getJSONObject("personal_number")
                                val pernrPemohon = objPernr.getString("full_name")

                                // status
                                val objAbsentApproval : JSONObject = objSurat.getJSONObject("approval_status")
                                val statusName = objAbsentApproval.getString("object_name")

                                // absent type
                                val objAbsentType = objSurat.getJSONObject("absent_type")
                                val absentType = objAbsentType.getString("object_name")


                                val data = DataPresenceList(intResponse,objIdentifier,pernrPemohon,dateAbsent,statusName,absentType)
                                listPresenceFiltered.add(data)

                            }

                            listDataFiltered.postValue(listPresenceFiltered)
                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK NGERROOOORRRR approval yeha filtereddd" )

                    }

                })

    }

    fun getDataApprovePresenceListFiltered() : LiveData<ArrayList<DataPresenceList>> {
        return listDataFiltered
    }

    fun setStatusApprovePresence(baseurl : String?, token : String?, buscd : String? ,beginDate : String?, objectType : String?) {

        val listAbsentType = ArrayList<DataStatusPresenceList>()
        val requestUrl = "${baseurl}ldap/api/object?begin_date_lte=$beginDate&end_date_gt=$beginDate&business_code=$buscd&object_type=$objectType"
        Log.d(tag,"get presence list approval atasan list: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list status presence confirmartion : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayStatusPresence : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayStatusPresence.length()) {
                                val objSurat: JSONObject = jsonArrayStatusPresence.getJSONObject(a)

                                val objectCode = objSurat.getString("object_code")
                                val objectName = objSurat.getString("object_name")

                                val data = DataStatusPresenceList(intResponse,objectCode,objectName)
                                listAbsentType.add(data)
                            }
                            listStatus.postValue(listAbsentType)
                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK presence conffff stat")
                    }
                })
    }

    fun getStatusApprovePresence() : LiveData<ArrayList<DataStatusPresenceList>>{
        return listStatus
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }


}
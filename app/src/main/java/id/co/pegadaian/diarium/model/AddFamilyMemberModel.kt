package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.*
import org.json.JSONArray
import org.json.JSONObject

class AddFamilyMemberModel : ViewModel() {

    val tag = AddFamilyMemberModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSpinnerAddFamilyMemberFamilyType>>()
    private val listDataGender = MutableLiveData<ArrayList<DataSpinnerAddFamilyMemberGender>>()
    private val listDataReligion = MutableLiveData<ArrayList<DataSpinnerAddFamilyMemberReligion>>()
    private val listDataFamilyStatus = MutableLiveData<ArrayList<DataSpinnerAddFamilyMemberFamilyStatus>>()
    private val listDataMaritalStatus = MutableLiveData<ArrayList<DataSpinnerAddFamilyMemberMaritalStatus>>()


    fun setSpinnerFamilyType(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddFamilyMemberFamilyType>()
        val getFamilyType = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list family type spinner: $getFamilyType")

        AndroidNetworking.get(getFamilyType)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner family type list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value family type objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddFamilyMemberFamilyType(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listData.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner family type list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner family type list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity family type")
                    }
                })
    }

    fun getSpinnerFamilyType() : LiveData<ArrayList<DataSpinnerAddFamilyMemberFamilyType>> {
        return listData
    }

    fun setSpinnerFamilyGender(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddFamilyMemberGender>()
        val getFamilyGender = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list gender spinner: $getFamilyGender")

        AndroidNetworking.get(getFamilyGender)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner gender list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value gender objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddFamilyMemberGender(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataGender.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner gender list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner gender list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity gender")
                    }
                })
    }

    fun getSpinnerFamilyGender() : LiveData<ArrayList<DataSpinnerAddFamilyMemberGender>> {
        return listDataGender
    }


    fun setSpinnerFamilyReligion(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddFamilyMemberReligion>()
        val getFamilyReligion = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list religion spinner: $getFamilyReligion")

        AndroidNetworking.get(getFamilyReligion)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner religion list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value religion objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddFamilyMemberReligion(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataReligion.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner religion list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner religion list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity religion")
                    }
                })
    }

    fun getSpinnerFamilyReligion() : LiveData<ArrayList<DataSpinnerAddFamilyMemberReligion>> {
        return listDataReligion
    }


    fun setSpinnerFamilyStatus(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddFamilyMemberFamilyStatus>()
        val getFamilyStatus = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list family status spinner: $getFamilyStatus")

        AndroidNetworking.get(getFamilyStatus)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner family status list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value family status objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddFamilyMemberFamilyStatus(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataFamilyStatus.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner family status list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner family status list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity family status")
                    }
                })
    }

    fun getSpinnerFamilyStatus() : LiveData<ArrayList<DataSpinnerAddFamilyMemberFamilyStatus>> {
        return listDataFamilyStatus
    }


    fun setSpinnerMaritalStatus(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddFamilyMemberMaritalStatus>()
        val getFamilyMaritalStatus = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list marital status spinner: $getFamilyMaritalStatus")

        AndroidNetworking.get(getFamilyMaritalStatus)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner marital status list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value marital status objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddFamilyMemberMaritalStatus(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataMaritalStatus.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner marital status list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner marital status list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity marital status")
                    }
                })
    }

    fun getSpinnerMaritalStatus() : LiveData<ArrayList<DataSpinnerAddFamilyMemberMaritalStatus>> {
        return listDataMaritalStatus
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }


}
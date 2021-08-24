package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data.AddPersonalDataActivityKt
import id.co.pegadaian.diarium.entity.*
import org.json.JSONArray
import org.json.JSONObject

class DataAddDataModelKt : ViewModel() {
    val tag = AddPersonalDataActivityKt::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSpinnerAddData>>()
    private val listDataFaculty = MutableLiveData<ArrayList<DataSpinnerAddDataFaculty>>()
    private val listDataMajor = MutableLiveData<ArrayList<DataSpinnerAddDataMajor>>()
    private val listDataEducationLevel = MutableLiveData<ArrayList<DataSpinnerAddDataEducationLevel>>()
    private val listDataProvince = MutableLiveData<ArrayList<DataSpinnerAddDataProvince>>()
    private val listDataCity = MutableLiveData<ArrayList<DataSpinnerAddDataCity>>()
    private val listDataDistrict = MutableLiveData<ArrayList<DataSpinnerAddDataDistrict>>()
    private val listDataInsurance = MutableLiveData<ArrayList<DataSpinnerAddDataInsuranceType>>()
    private val listDataCategory = MutableLiveData<ArrayList<DataCategorySpinnerAddData>>()

    val dataTypes = arrayOf(
    "Select data type...",
    "Communication",
    "Identification"
    )

    fun setSpinnerDataList(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddData>()
        val getComListSpinner = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=$buscd&object_type=$objType&per_page=1000"
        Log.d(tag, "url get list communication spinner: $getComListSpinner")

        AndroidNetworking.get(getComListSpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddData(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listData.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list activity")
                    }
                })
    }

    fun getSpinnerDataList() : LiveData<ArrayList<DataSpinnerAddData>>{
        return listData
    }


    fun setSpinnerDataFaculty(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataFaculty>()
        val getListFaculty = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=$buscd&object_type=$objType&per_page=1000"
        Log.d(tag, "url get list faculty spinner: $getListFaculty")

        AndroidNetworking.get(getListFaculty)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner faculty list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value objName & objCode faculty : \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataFaculty(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataFaculty.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner faculty list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner faculty list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list faculty activity")
                    }
                })
    }

    fun getSpinnerDataFaculty() : LiveData<ArrayList<DataSpinnerAddDataFaculty>>{
        return listDataFaculty
    }

    fun setSpinnerDataMajor(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataMajor>()
        val getListMajor = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=$buscd&object_type=$objType&per_page=1000"
        Log.d(tag, "url get list major spinner: $getListMajor")

        AndroidNetworking.get(getListMajor)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner major list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value objName & objCode major : \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataMajor(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataMajor.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner major list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner major list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list major activity")
                    }
                })
    }

    fun getSpinnerDataMajor() : LiveData<ArrayList<DataSpinnerAddDataMajor>>{
        return listDataMajor
    }

    fun setSpinnerDataEducationLevel(baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataEducationLevel>()
        val getListEducationLevel = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gt=$date&business_code=*&business_code=$buscd&object_type=$objType&per_page=1000"
        Log.d(tag, "url get list education level spinner: $getListEducationLevel")

        AndroidNetworking.get(getListEducationLevel)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner education level list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value objName & objCode education level : \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataEducationLevel(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataEducationLevel.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner education level list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner education level list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list education level activity")
                    }
                })
    }

    fun getSpinnerDataEducationLevel() : LiveData<ArrayList<DataSpinnerAddDataEducationLevel>>{
        return listDataEducationLevel
    }



    fun setSpinnerAddressType (baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddData>()
        val getAddressTypeSpinner = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gte=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list spinner address type : $getAddressTypeSpinner")

        AndroidNetworking.get(getAddressTypeSpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner address type list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value address type objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddData(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listData.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner address type list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner address type list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list address type activity")
                    }
                })
    }

    fun getSpinnerAddressType() : LiveData<ArrayList<DataSpinnerAddData>>{
        return listData
    }

    fun setSpinnerProvince (baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataProvince>()
        val getProvinceSpinner = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gte=$date&business_code=*&business_code=$buscd&object_type=$objType&per_page=1000"
        Log.d(tag, "url get list spinner province : $getProvinceSpinner")

        AndroidNetworking.get(getProvinceSpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner province list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value province objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataProvince(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataProvince.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner province list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner province list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list province activity")
                    }
                })
    }

    fun getSpinnerProvince() : LiveData<ArrayList<DataSpinnerAddDataProvince>>{
        return listDataProvince
    }


    fun setSpinnerCity (baseurl: String?, token: String?, parentType : String?, provinceCodeChosen : String?,
                        objType : String?, date : String?, buscd: String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataCity>()
        val getCitySpinner = "${baseurl}ldap/api/relation?parent_type=$parentType&include=child&parent=$provinceCodeChosen" +
                "&relation_type=A&relation_code=001&child_type=$objType&begin_date_lte=$date&end_date_gte=$date" +
                "&business_code=*&per_page=1000"
        Log.d(tag, "url get list spinner city : $getCitySpinner")

        AndroidNetworking.get(getCitySpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner city list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val objChild : JSONObject = objName.getJSONObject("child")
                                    val name = objChild.getString("object_name")
                                    val code = objChild.getString("object_code")

                                    Log.d(tag,"check value city objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataCity(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataCity.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner city list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner city list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list city activity")
                    }
                })
    }

    fun getSpinnerCity() : LiveData<ArrayList<DataSpinnerAddDataCity>>{
        return listDataCity
    }


    fun setSpinnerDistrict (baseurl: String?, token: String?, parentType : String?, cityCodeChosen : String?,
                        objType : String?, date : String?, buscd: String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataDistrict>()
        val getDistrictSpinner = "${baseurl}ldap/api/relation?parent_type=$parentType&include=child&parent=$cityCodeChosen" +
                "&relation_type=A&relation_code=001&child_type=$objType&begin_date_lte=$date&end_date_gte=$date" +
                "&business_code=*&per_page=1000"
        Log.d(tag, "url get list spinner district : $getDistrictSpinner")

        AndroidNetworking.get(getDistrictSpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner district list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val objChild : JSONObject = objName.getJSONObject("child")
                                    val name = objChild.getString("object_name")
                                    val code = objChild.getString("object_code")

                                    Log.d(tag,"check value district objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataDistrict(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataDistrict.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner district list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner district list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list district activity")
                    }
                })
    }

    fun getSpinnerDistrict() : LiveData<ArrayList<DataSpinnerAddDataDistrict>>{
        return listDataDistrict
    }

    fun setSpinnerInsuranceType (baseurl: String?, token: String?, date: String?, buscd: String?,objType : String?){

        val listPersonalData = ArrayList<DataSpinnerAddDataInsuranceType>()
        val getInsuranceTypeSpinner = "${baseurl}ldap/api/object?begin_date_lte=$date&end_date_gte=$date&business_code=*&business_code=$buscd&object_type=$objType"
        Log.d(tag, "url get list spinner insurance type : $getInsuranceTypeSpinner")

        AndroidNetworking.get(getInsuranceTypeSpinner)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer $token")
                //.addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val intResponse = response?.getInt("status")
                        Log.d(tag,"response get spinner insurance type list: $response")

                        try {
                            if (response?.getInt("status") == 200){
                                val jsonArray : JSONArray = response.getJSONArray("data")
                                for (a in 0 until jsonArray.length()){
                                    val objName : JSONObject = jsonArray.getJSONObject(a)
                                    val name = objName.getString("object_name")
                                    val code = objName.getString("object_code")

                                    Log.d(tag,"check value insurance type objName & objCode: \n name : $name \n code : $code")

                                    val dataListSpinner = DataSpinnerAddDataInsuranceType(name,code)
                                    listPersonalData.add(dataListSpinner)
                                }
                                listDataInsurance.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get spinner insurance type list: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get spinner insurance type list: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"get spinner list insurance type activity")
                    }
                })
    }

    fun getSpinnerInsuranceType() : LiveData<ArrayList<DataSpinnerAddDataInsuranceType>>{
        return listDataInsurance
    }


    fun setDataTypesList (){
        val listDataTypes = ArrayList<DataCategorySpinnerAddData>()

////      first array
//        var dataTypesListSpinner = DataSpinnerAddData("Select data type...","0")
//        listDataTypes.add(dataTypesListSpinner)
//        listData.postValue(listDataTypes)
////      first array

        //      first array
        var dataTypesListSpinner = DataCategorySpinnerAddData("Address")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
        //      first array

//      second array
        dataTypesListSpinner = DataCategorySpinnerAddData("Communication")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
//      second array


//      third array
        dataTypesListSpinner = DataCategorySpinnerAddData("Identification")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
//      third array

//      forth array
        dataTypesListSpinner = DataCategorySpinnerAddData("Education")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
//      forth array

//      fifth array
        dataTypesListSpinner = DataCategorySpinnerAddData("Insurance")
        listDataTypes.add(dataTypesListSpinner)
        listDataCategory.postValue(listDataTypes)
//      fifth array

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

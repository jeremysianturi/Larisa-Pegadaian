package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.personaldata.PersonalDataList
import id.co.pegadaian.diarium.entity.DataPersonal
import org.json.JSONArray
import org.json.JSONObject

class PersonalDataListModel : ViewModel() {
    val tag = PersonalDataList::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataPersonal>>()

    fun setData(baseUrl : String, token : String, buscd : String){
        val listPersonalData = ArrayList<DataPersonal>()
        val requestUrl = baseUrl + "users/mydetailidentity/buscd/" + buscd
        Log.d(tag,"get data personal list: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"Response get my identity : $response")
                        val intResponse = response?.getInt("status")

                        try {
                            if (response?.getInt("status") == 200) {
                                var jsonArrayCommunication: JSONArray = response.getJSONObject("data").getJSONArray("communication")
                                print("json array length: " + jsonArrayCommunication.length())


                                // COMMUNICATION
                                for (a in 0 until jsonArrayCommunication.length()) {
                                    val objcommunication: JSONObject = jsonArrayCommunication.getJSONObject(a)
                                    val value = objcommunication.getString("communication_number")
                                    // JSONObject objname = objcommunication.getJSONObject("communication_name");
                                    val name = objcommunication.getString("communication_name")
                                    val objIdentifier = objcommunication.getString("object_identifier")
                                    val objectCode = objcommunication.getString("communication_type")
                                    val type = "communication"
                                    val flag = objcommunication.getBoolean("status_editable").toString()

                                    val data = DataPersonal(intResponse,name,value,objIdentifier,type,objectCode,flag)
                                    listPersonalData.add(data)
                                    Log.d(tag,"Data Personal Communication : $name , $value , $objIdentifier , $objectCode , $type , $flag")
                                }
                                // COMMUNICATION

                                // IDENTIFICATION
                                val jsonArrayidentification = response.getJSONObject("data").getJSONArray("identification")
                                for (b in 0 until jsonArrayidentification.length()) {
                                    val objidentification = jsonArrayidentification.getJSONObject(b)
                                    val value = objidentification.getString("identification_number")
                                    val name = objidentification.getString("identification_name")
                                    val objIndentifier = objidentification.getString("object_identifier")
                                    val objectCode = objidentification.getString("identification_type")
                                    val type = "identification"
                                    val flag = objidentification.getBoolean("status_editable").toString()

                                    val data = DataPersonal(intResponse,name,value,objIndentifier,type,objectCode,flag)
                                    listPersonalData.add(data)
                                    Log.d(tag,"Data Personal Identification : $name , $value , $objIndentifier , $objectCode , $type , $flag")
                                }
                                // IDENTIFICATION

                                // EDUCATION
                                val jsonArrayEducation = response.getJSONObject("data").getJSONArray("education")
                                for (b in 0 until jsonArrayEducation.length()) {
                                    val objEducation = jsonArrayEducation.getJSONObject(b)

                                    val objectIdentifier = objEducation.getInt("object_identifier").toString()
                                    val educationStartDate = objEducation.getString("education_start")
                                    val educationEndDate = objEducation.getString("education_end")
                                    val academicScore = objEducation.getDouble("nilai").toString()
                                    val educationLevelInCode = objEducation.getString("level")
                                    val educationLevelName = objEducation.getString("education_level_name")
                                    val institution = objEducation.getString("institution")
                                    val facultyInCode = objEducation.getString("faculty")
                                    val facultyName = objEducation.getString("faculty_name")
                                    val majorInCode = objEducation.getString("department")
                                    val majorName = objEducation.getString("department_name")
                                    val subMajor = objEducation.getString("sub_department")

                                    val type = "education"
                                    val objectCode = "tidak punya object code"
                                    val name = "Education ($educationStartDate - $educationEndDate)"
                                    val value = "$educationLevelName $subMajor, $majorName, $facultyName, $institution with grade : $academicScore"

                                    val data = DataPersonal(intResponse,name,value,objectIdentifier,type,objectCode,"true")
                                    listPersonalData.add(data)
                                    Log.d(tag, "Data Personal Education : \n name : $name \n value : $value \n type : $type \n " +
                                            "object code : $objectCode , true")


                                }
                                // EDUCATION

                                // ADDRESS
                                val jsonArrayAddress = response.getJSONObject("data").getJSONArray("address")
                                for (j in 0 until jsonArrayAddress.length()){
                                    val objAddress = jsonArrayAddress.getJSONObject(j)

                                    val objIdentifier = objAddress.getInt("object_identifier").toString()
                                    val addressTypeCode = objAddress.getString("address_type")
                                    val addressTypeName = objAddress.getString("address_type_name")
                                    val addressOrderNumber = objAddress.getString("address_number")
                                    val street = objAddress.getString("street")
                                    val postalCode = objAddress.getString("postal_code")
                                    val districtName = objAddress.getString("district_name")
                                    val districtCode = objAddress.getString("district")
                                    val cityName = objAddress.getString("city_name")
                                    val cityCode = objAddress.getString("city")
                                    val provinceName = objAddress.getString("province_name")
                                    val provinceCode = objAddress.getString("province")
                                    val countryName = objAddress.getString("country")

                                    val name = "Address"
                                    val type = "address"
                                    val value = "$street $districtName $cityName $provinceName $countryName $postalCode"
                                    Log.d(tag,"check value nya di sini : $value")

                                    val data = DataPersonal(intResponse,addressTypeName,value,objIdentifier,type,addressTypeCode,"true")
                                    listPersonalData.add(data)
                                    Log.d(tag,"Data Personal Address : $name , $value , $objIdentifier , $addressTypeCode , $type , true")
                                }
                                // ADDRESS

                                // NPWP
                                val jsonArrayNPWP = response.getJSONObject("data").getJSONArray("npwp")

                                for (b in 0 until jsonArrayNPWP.length()) {
                                    val objNpwp = jsonArrayNPWP.getJSONObject(b)
                                    val jsonarrayNpwp_name = "NPWP"
                                    val jsonarrayNpwp_number = objNpwp.getString("npwp_number")
                                    val objIndentifier = objNpwp.getString("object_identifier")
                                    //                                    String objectCode = objNpwp.getString("communication_type");
                                    val objectCode = "00000"
                                    val type = "npwp"

                                    val data = DataPersonal(intResponse,jsonarrayNpwp_name,jsonarrayNpwp_number,objIndentifier,type,objectCode,"")
                                    listPersonalData.add(data)
                                    Log.d(tag,"Data Personal NPWP : $jsonarrayNpwp_name , $jsonarrayNpwp_number , $objIndentifier , $objectCode , $type")

                                }
                                // NPWP

                                // BPJS KESEHATAN
//                                val bpjs_kesehatan_length = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").length()
//                                if (bpjs_kesehatan_length > 0) {
//                                    val obj = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").getJSONObject(0)
//                                    val value_bpjs_kesehatan = obj.getString("insurance_number")
//                                    val objIndentifier = obj.getString("object_identifier")
//                                    //                                    String objectCode = obj.getString("communication_type");
//                                    val name = "BPJS Kesehatan"
//                                    val objectCode = "00000"
//                                    val type = "bpjs_kesehatan"
//
//                                    val data = DataPersonal(intResponse,name,value_bpjs_kesehatan,objIndentifier,type,objectCode,"")
//                                    listPersonalData.add(data)
//
//                                    Log.d(tag,"Data Personal BPJS Kesehatan : $name , $value_bpjs_kesehatan , $objIndentifier , $objectCode , $type")
//                                }
                                // BPJS KESEHATAN

                                // BPJS KETENAGAKERJAAN
                                val bpjs_ketenagakerjaan_length = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").length()
                                if (bpjs_ketenagakerjaan_length > 0) {
                                    val obj = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").getJSONObject(0)
                                    val value_bpjs_ketenagakerjaan = obj.getString("bpjs_number")
                                    val objIndentifier = obj.getString("object_identifier")
                                    //                                    String objectCode = obj.getString("communication_type");
                                    val objectCode = "00000"
                                    val type = "bpjs_ketenagakerjaan"
                                    val name = "BPJS Ketenagakerjaan"

                                    val data = DataPersonal(intResponse,name,value_bpjs_ketenagakerjaan,objIndentifier,type,objectCode,"")
                                    listPersonalData.add(data)

                                    Log.d(tag,"Data Personal BPJS Ketenagakerjaan : $name , $value_bpjs_ketenagakerjaan , $objIndentifier , $objectCode , $type")
                                }
                                // BPJS KETENAGAKERJAAN

                                // INSURANCE
                                val insurance_length = response.getJSONObject("data").getJSONArray("insurance").length()
                                if (insurance_length > 0){
                                    val insuranceArray = response.getJSONObject("data").getJSONArray("insurance")

                                    for (a in 0 until insurance_length) {

                                        val objInsurance = insuranceArray.getJSONObject(a)
                                        val objIndentifierInsurance = objInsurance.getString("object_identifier")
                                        val insuranceType = objInsurance.getString("insurance_type")
                                        val insuranceNumber = objInsurance.getString("insurance_number")
                                        val insuranceCompany = objInsurance.getString("insurance_company")
                                        val insuranceName = objInsurance.getString("insurance_name")
                                        val objectCode = "00000"

                                        val data = DataPersonal(intResponse,insuranceCompany,insuranceNumber,objIndentifierInsurance,insuranceType,objectCode,"")
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal insurance : $insuranceCompany , $insuranceNumber , $objIndentifierInsurance , $objectCode , $insuranceType")

                                    }

                                    }
                                // INSURANCE

                                // BANK
                                val arrayBankLength = response.getJSONObject("data").getJSONArray("bank_employee").length()
                                if (arrayBankLength > 0) {
                                    val jsonarraybank_employee = response.getJSONObject("data").getJSONArray("bank_employee")
                                    for (a in 0 until jsonarraybank_employee.length()) {
                                        val objBank = jsonarraybank_employee.getJSONObject(a)
                                        val value = objBank.getString("account_number")
                                        val name = objBank.getString("bank_type")
                                        val name_edit = "No. Rekening $name"
                                        val objIndentifier = objBank.getString("object_identifier")
                                        //                                        String objectCode = objBank.getString("communication_type");
                                        val objectCode = "00000"
                                        val type = "bank_employee"

                                        val data = DataPersonal(intResponse,name,value,objIndentifier,type,objectCode,"")
                                        listPersonalData.add(data)

                                        Log.d(tag,"Data Personal BANK : $name , $value , $objIndentifier , $objectCode , $type")
                                    }
                                }
                                // BANK

                                listData.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di getMyIdentity: ${response?.getString("message")}")
                            }
                        } catch (e: Exception) {
                            Log.d(tag,"catch di getMyIdentity: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError, " Personal Data")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataPersonal>>{
        Log.d(tag,"check value listData di getData Model: $listData")
        return listData
    }

    fun deleteData(baseUrl : String?, token: String?, dataType : String?, personalIdentifier: String?) {

        println("ketikamasukdelete : $personalIdentifier  $dataType")
        var dataTypeVar = dataType

        if (dataTypeVar.equals("address") || dataTypeVar.equals("education")) {
            when(dataTypeVar){
                "address" -> dataTypeVar = "employeeaddress"
                "education" -> dataTypeVar = "education"
            }

            val deleteUrlAddress = "${baseUrl}hcis/api/$dataTypeVar?object_identifier=$personalIdentifier"
            Log.d(tag, "url delete personal data address : $deleteUrlAddress")
            println("Test type value : $dataTypeVar")

            AndroidNetworking.delete(deleteUrlAddress)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", "Bearer $token")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            // do anything with response
                            Log.d(tag, "response delete data personal address : $response")
                            try {
                                if (response.getInt("status") == 200) {

                                } else {

                                }
                            } catch (e: java.lang.Exception) {
                                println(e)
                            }
                        }

                        override fun onError(error: ANError) {
                            errorLog(error, "Delete Personal Data address")
                        }
                    })


        } else {
            when (dataTypeVar) {
                "communication" -> dataTypeVar = "communication"
                "identification" -> dataTypeVar = "identification"
                "bpjs_ketenagakerjaan" -> dataTypeVar = "jamsostek"
                "bpjs_kesehatan" -> dataTypeVar = "insurance"
                "bank_employee" -> dataTypeVar = "bankemployee"
                "npwp" -> dataTypeVar = "tax"
            }
            val deleteUrl = baseUrl + dataTypeVar + "?object_identifier=" + personalIdentifier
            Log.d(tag,"url delete personal data: $deleteUrl")
            println("Test type value : $dataTypeVar")

            AndroidNetworking.delete(deleteUrl)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Content-Type", "application/json")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            // do anything with response
                            Log.d(tag,"response delete data personal : $response")
                            try {
                                if (response.getInt("status") == 200) {

                                } else {

                                }
                            } catch (e: java.lang.Exception) {
                                println(e)
                            }
                        }

                        override fun onError(error: ANError) {
                            errorLog(error, "Delete Personal Data")
                        }
                    })
        }
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
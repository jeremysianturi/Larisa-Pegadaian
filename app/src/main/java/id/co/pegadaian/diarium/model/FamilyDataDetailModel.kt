package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.familydata.FamilyDataDetail
import id.co.pegadaian.diarium.entity.DataFamilyPersonal
import org.json.JSONArray
import org.json.JSONObject

class FamilyDataDetailModel : ViewModel() {
    val tag = FamilyDataDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataFamilyPersonal>>()

    fun setDataCommunication(baseUrl : String, token : String, employeeNik : String, familyType : String, familyNumber : String){

        val listPersonalData = ArrayList<DataFamilyPersonal>()
        val requestUrl = "${baseUrl}hcis/api/datafamily?personnel_number=${employeeNik}&family_type=${familyType}&family_number=${familyNumber}" +
                "&include=identification_ktp&include=identification_kk&include=primary_residence&include=official_residence" +
                "&include=communication_email&include=communication_phone_number&include=communication_facebook&include=communication_instagram" +
                "&include=communication_twitter&include=communication_linkedin&include=secondary_residence&include=identification_passport" +
                "&include=identification_simc&include=identification_simb&include=identification_sima&include=identification_insurance"
        Log.d(tag,"get data personal family list : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"Response get family personal identity : $response workworkworkworkwork")
                        val intResponse = response?.getInt("status")
                        try {
                            if (response?.getInt("status") == 200){

                                val jsonArrayData: JSONArray = response.getJSONArray("data")

                                for (a in 0 until jsonArrayData.length()) {
                                    val objData: JSONObject = jsonArrayData.getJSONObject(a)

//                                    val objIdentifier = objData.getInt("object_identifier")
//                                    val evidence = objData.getString("evidence")

                                    // KTP
                                    val objKtp : JSONObject = objData.getJSONObject("identification_ktp")
                                    if (objKtp.getString("identification_type").equals("")){

                                        // masuk ke ga ada value ktp
                                        Log.d(tag,"check masuk ke ga ada value di object ktp")

                                    } else {

                                        val objIdentifierKtp = 0       // karna data gabisa diubah , makanya object identifiernya ga ada
                                        val identificationTypeKtp = objKtp.getString("identification_type")
                                        val identificationNumberKtp = objKtp.getString("identification_number")
                                        val dateIssueKtp = objKtp.getString("date_issue")
                                        val expireDateKtp = objKtp.getString("expire_date")
                                        val placeIssueKtp = objKtp.getString("place_issue")
//                                    val evidenceKtp = objKtp.getString("evidence")
                                        val typeCategoryKtp = "identification"

                                        val objNameKtp = defineDataName(typeCategoryKtp,identificationTypeKtp)
                                        Log.d(tag,"check value objName ktp: $objNameKtp")

                                        val dataKtp = DataFamilyPersonal(intResponse,objNameKtp,objIdentifierKtp,identificationTypeKtp,identificationNumberKtp,typeCategoryKtp,1)
                                        listPersonalData.add(dataKtp)
                                        Log.d(tag,"Data Personal Identification : $objIdentifierKtp , $identificationTypeKtp , $identificationNumberKtp , $typeCategoryKtp")

                                    }
                                    // KTP



                                    // KARTU KELUARGA
                                    val objKk : JSONObject = objData.getJSONObject("identification_kk")
                                    if (objKk.getString("identification_type").equals("")){

                                        // masuk ke ga ada value kartu keluarga
                                        Log.d(tag,"check masuk ke ga ada value di object kartu keluarga")

                                    } else {

                                        val objIdentifierKk = 0       // karna data gabisa diubah , makanya object identifiernya ga ada
                                        val identificationTypeKk = objKk.getString("identification_type")
                                        val identificationNumberKk = objKk.getString("identification_number")
                                        val dateIssueKk = objKk.getString("date_issue")
                                        val expireDateKk = objKk.getString("expire_date")
                                        val placeIssueKk = objKk.getString("place_issue")
//                                    val evidenceKk = objKk.getString("evidence")
                                        val typeCategoryKk = "identification"

                                        val objNameKk = defineDataName(typeCategoryKk,identificationTypeKk)
                                        Log.d(tag,"check value objName kk : $objNameKk")

                                        val dataKk = DataFamilyPersonal(intResponse,objNameKk,objIdentifierKk,identificationTypeKk,identificationNumberKk,typeCategoryKk,1)
                                        listPersonalData.add(dataKk)
                                        Log.d(tag,"Data Personal Identification : $objIdentifierKk , $identificationTypeKk , $identificationNumberKk , $typeCategoryKk")

                                    }
                                    // KARTU KELUARGA

                                    // SIM A
                                    val objSimA : JSONObject = objData.getJSONObject("identification_sima")
                                    if (objSimA.getString("identification_type").equals("")){

                                        // masuk ke ga ada value Sim A
                                        Log.d(tag,"check masuk ke ga ada value di object Sim A")

                                    } else {

                                        val objIdentifierSimA = objSimA.getInt("object_identifier")
                                        val identificationTypeSimA = objSimA.getString("identification_type")
                                        val identificationNumberSimA = objSimA.getString("identification_number")
                                        val typeCategorySimA = "identification"

                                        val objNameSimA = defineDataName(typeCategorySimA,identificationTypeSimA)
                                        Log.d(tag,"check value objName Sim A : $objNameSimA")

                                        val dataSimA = DataFamilyPersonal(intResponse,objNameSimA,objIdentifierSimA,identificationTypeSimA,identificationNumberSimA,typeCategorySimA,1)
                                        listPersonalData.add(dataSimA)
                                        Log.d(tag,"Data Personal Identification : $objIdentifierSimA , $identificationTypeSimA , $identificationNumberSimA , $typeCategorySimA")
                                    }
                                    // SIM A

                                    // SIM B
                                    val objSimB : JSONObject = objData.getJSONObject("identification_simb")
                                    if (objSimB.getString("identification_type").equals("")){

                                        // masuk ke ga ada value Sim B
                                        Log.d(tag,"check masuk ke ga ada value di object Sim B")

                                    } else {

                                        val objIdentifierSimB = objSimB.getInt("object_identifier")
                                        val identificationTypeSimB = objSimB.getString("identification_type")
                                        val identificationNumberSimB = objSimB.getString("identification_number")
                                        val typeCategorySimB = "identification"

                                        val objNameSimB = defineDataName(typeCategorySimB,identificationTypeSimB)
                                        Log.d(tag,"check value objName Sim B : $objNameSimB")

                                        val dataSimB = DataFamilyPersonal(intResponse,objNameSimB,objIdentifierSimB,identificationTypeSimB,identificationNumberSimB,typeCategorySimB,1)
                                        listPersonalData.add(dataSimB)
                                        Log.d(tag,"Data Personal Identification : $objIdentifierSimB , $identificationTypeSimB , $identificationNumberSimB , $typeCategorySimB")
                                    }
                                    // SIM B

                                    // SIM C
                                    val objSimC : JSONObject = objData.getJSONObject("identification_simc")
                                    if (objSimC.getString("identification_type").equals("")){

                                        // masuk ke ga ada value Sim C
                                        Log.d(tag,"check masuk ke ga ada value di object Sim C")

                                    } else {

                                        val objIdentifierSimC = objSimC.getInt("object_identifier")
                                        val identificationTypeSimC = objSimC.getString("identification_type")
                                        val identificationNumberSimC = objSimC.getString("identification_number")
                                        val typeCategorySimC = "identification"

                                        val objNameSimC = defineDataName(typeCategorySimC,identificationTypeSimC)
                                        Log.d(tag,"check value objName Sim C : $objNameSimC")

                                        val dataSimC = DataFamilyPersonal(intResponse,objNameSimC,objIdentifierSimC,identificationTypeSimC,identificationNumberSimC,typeCategorySimC,1)
                                        listPersonalData.add(dataSimC)
                                        Log.d(tag,"Data Personal Identification : $objIdentifierSimC , $identificationTypeSimC , $identificationNumberSimC , $typeCategorySimC")
                                    }
                                    // SIM C


                                    // PASSPORT
                                    val objPassport : JSONObject = objData.getJSONObject("identification_passport")
                                    if (objPassport.getString("identification_type").equals("")){

                                        // masuk ke ga ada value passport
                                        Log.d(tag,"check masuk ke ga ada value di object passport")

                                    } else {

                                        val objectIdentifierPassport = objPassport.getInt("object_identifier")

                                        val objIndentificationName = objPassport.getJSONObject("identification_name")

                                        val typeCategoryPassport = "identification"
                                        val identificationTypePassport = objPassport.getString("identification_type")
                                        val identificationNumberPassport = objPassport.getString("identification_number")

                                        val objNamePassport = defineDataName(typeCategoryPassport,identificationTypePassport)
                                        Log.d(tag,"check value objName passport : $objNamePassport")

                                        val dataPassport = DataFamilyPersonal(intResponse,objNamePassport,objectIdentifierPassport,identificationTypePassport,identificationNumberPassport,typeCategoryPassport,1)
                                        listPersonalData.add(dataPassport)
                                        Log.d(tag,"Data Personal Identification : $objectIdentifierPassport , $identificationTypePassport , $identificationNumberPassport , $typeCategoryPassport")

                                    }
                                    // PASSPORT


                                    // INSURANCE
                                    val objInsurance : JSONObject = objData.getJSONObject("identification_insurance")

                                    if (objInsurance.getString("identification_name").equals("")){

                                        // masuk ke ga ada value insurance
                                        Log.d(tag,"check masuk ke ga ada value di object insurance")

                                    } else {


                                        val objIdentifierInsurance = objInsurance.getInt("object_identifier")
//                                        val evidence = objInsurance.getString("evidence")
                                        val identificationTypeInsurance = objInsurance.getString("identification_type")
                                        val identificationNumberInsurance = objInsurance.getString("identification_number")

                                        val objIdentificationNameInsurance = objInsurance.getJSONObject("identification_name")
                                        val identificationNameInsurance = objIdentificationNameInsurance.getString("object_name")
                                        val typeCategoryInsurance = "identification"


//                                        val objNameInsurance = defineDataName(typeCategoryInsurance,identificationTypeInsurance)
//                                        Log.d(tag,"check value objName insurance : $objNameInsurance")

                                        val dataInsurance = DataFamilyPersonal(intResponse,identificationNameInsurance,objIdentifierInsurance,identificationTypeInsurance,identificationNumberInsurance,typeCategoryInsurance,1)
                                        listPersonalData.add(dataInsurance)
                                        Log.d(tag,"Data Personal Identification Insurance : $objIdentifierInsurance , $identificationTypeInsurance , $identificationNumberInsurance , $typeCategoryInsurance")

                                    }
                                    // INSURANCE


                                    // PRIMARY RESIDENCE
                                    val jsonArrayDataPrimRes = objData.getJSONArray("primary_residence")
                                    for (a in 0 until jsonArrayDataPrimRes.length()){
                                        val objDataPrimRes : JSONObject = jsonArrayDataPrimRes.getJSONObject(a)

                                        val objectIndetifier = objDataPrimRes.getInt("object_identifier")
                                        val streetName = objDataPrimRes.getString("street")
                                        val postalCode = objDataPrimRes.getString("postal_code")
                                        val countryName = objDataPrimRes.getString("country")
                                        val serialNumber = objDataPrimRes.getInt("address_number")

                                        // address name object
                                        val objAddressName = objDataPrimRes.getJSONObject("address_name")
                                        val objTypeCodeAddress = objAddressName.getString("otype_code")
                                        val jenisRumahInCode = objAddressName.getString("object_code")
                                        val jenisRumah = objAddressName.getString("object_name")

                                        // district name object
                                        val objDistrict = objDataPrimRes.getJSONObject("district_name")
                                        val objTypeCodeDistrict = objDistrict.getString("otype_code")
                                        val districtInCode = objDistrict.getString("object_code")
                                        val districtName = objDistrict.getString("object_name")

                                        // city name object
                                        val objCity = objDataPrimRes.getJSONObject("city_name")
                                        val objTypeCodeCity = objCity.getString("otype_code")
                                        val cityInCode = objCity.getString("object_code")
                                        val cityName = objCity.getString("object_name")

                                        // province name object
                                        val objProvince = objDataPrimRes.getJSONObject("province_name")
                                        val objTypeCodeProvince = objProvince.getString("otype_code")
                                        val provinceInCode = objProvince.getString("object_code")
                                        val provinceName = objProvince.getString("object_name")

                                        val completeAddress = "$streetName, $districtName, $cityName, $provinceName, $countryName, $postalCode."
                                        val type = "address"


                                        Log.d(tag,"check value di family data detail model : $jenisRumah, $objectIndetifier, $jenisRumahInCode, $completeAddress, $type" +
                                                ", $serialNumber")

                                        val data = DataFamilyPersonal(intResponse,jenisRumah,objectIndetifier,jenisRumahInCode,completeAddress,type,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal primary residence : $jenisRumah , $objectIndetifier , $jenisRumahInCode , $completeAddress, $type , $serialNumber")
                                    }

                                    // OFFICIAL RESIDENCE
                                    val jsonArrayDataOffRes = objData.getJSONArray("official_residence")
                                    for (q in 0 until jsonArrayDataOffRes.length()) {
                                        val objDataOffRes : JSONObject = jsonArrayDataOffRes.getJSONObject(q)

                                        val objectIndetifier = objDataOffRes.getInt("object_identifier")
                                        val streetName = objDataOffRes.getString("street")
                                        val postalCode = objDataOffRes.getString("postal_code")
                                        val countryName = objDataOffRes.getString("country")
                                        val serialNumber = objDataOffRes.getInt("address_number")

                                        // address name object
                                        val objAddressName = objDataOffRes.getJSONObject("address_name")
                                        val objTypeCodeAddress = objAddressName.getString("otype_code")
                                        val jenisRumahInCode = objAddressName.getString("object_code")
                                        val jenisRumah = objAddressName.getString("object_name")

                                        // district name object
                                        val objDistrict = objDataOffRes.getJSONObject("district_name")
                                        val objTypeCodeDistrict = objDistrict.getString("otype_code")
                                        val districtInCode = objDistrict.getString("object_code")
                                        val districtName = objDistrict.getString("object_name")

                                        // city name object
                                        val objCity = objDataOffRes.getJSONObject("city_name")
                                        val objTypeCodeCity = objCity.getString("otype_code")
                                        val cityInCode = objCity.getString("object_code")
                                        val cityName = objCity.getString("object_name")

                                        // province name object
                                        val objProvince = objDataOffRes.getJSONObject("province_name")
                                        val objTypeCodeProvince = objProvince.getString("otype_code")
                                        val provinceInCode = objProvince.getString("object_code")
                                        val provinceName = objProvince.getString("object_name")

                                        val completeAddress = "$streetName, $districtName, $cityName, $provinceName, $countryName, $postalCode."
                                        val type = "address"

                                        Log.d(tag,"check value di family data detail model : $jenisRumah, $objectIndetifier, $jenisRumahInCode, $completeAddress, $type" +
                                                ", $serialNumber")

                                        val data = DataFamilyPersonal(intResponse,jenisRumah,objectIndetifier,jenisRumahInCode,completeAddress,type,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal official residence : $jenisRumah , $objectIndetifier , $jenisRumahInCode , $completeAddress, $type , $serialNumber")

                                    }
                                    // OFFICIAL RESIDENCE

                                    // SECONDARY RESIDENCE
                                    val jsonArrayDataScnRes = objData.getJSONArray("secondary_residence")
                                    for (p in 0 until jsonArrayDataScnRes.length()) {
                                        val objDataScnRes : JSONObject = jsonArrayDataScnRes.getJSONObject(p)

                                        val objectIndetifier = objDataScnRes.getInt("object_identifier")
                                        val streetName = objDataScnRes.getString("street")
                                        val postalCode = objDataScnRes.getString("postal_code")
                                        val countryName = objDataScnRes.getString("country")
                                        val serialNumber = objDataScnRes.getInt("address_number")

                                        // address name object
                                        val objAddressName = objDataScnRes.getJSONObject("address_name")
                                        val objTypeCodeAddress = objAddressName.getString("otype_code")
                                        val jenisRumahInCode = objAddressName.getString("object_code")
                                        val jenisRumah = objAddressName.getString("object_name")

                                        // district name object
                                        val objDistrict = objDataScnRes.getJSONObject("district_name")
                                        val objTypeCodeDistrict = objDistrict.getString("otype_code")
                                        val districtInCode = objDistrict.getString("object_code")
                                        val districtName = objDistrict.getString("object_name")

                                        // city name object
                                        val objCity = objDataScnRes.getJSONObject("city_name")
                                        val objTypeCodeCity = objCity.getString("otype_code")
                                        val cityInCode = objCity.getString("object_code")
                                        val cityName = objCity.getString("object_name")

                                        // province name object
                                        val objProvince = objDataScnRes.getJSONObject("province_name")
                                        val objTypeCodeProvince = objProvince.getString("otype_code")
                                        val provinceInCode = objProvince.getString("object_code")
                                        val provinceName = objProvince.getString("object_name")

                                        val completeAddress = "$streetName, $districtName, $cityName, $provinceName, $countryName, $postalCode."
                                        val type = "address"


                                        Log.d(tag,"check value di family data detail model : $jenisRumah, $objectIndetifier, $jenisRumahInCode, $completeAddress, $type" +
                                                ", $serialNumber")

                                        val data = DataFamilyPersonal(intResponse,jenisRumah,objectIndetifier,jenisRumahInCode,completeAddress,type,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal secondary residence : $jenisRumah , $objectIndetifier , $jenisRumahInCode , $completeAddress, $type , $serialNumber")

                                    }
                                    // SECONDARY RESIDENCE

                                    // PHONE NUMBER
                                    val jsonArrayDataComPhoneNum = objData.getJSONArray("communication_phone_number")
                                    for (a in 0 until jsonArrayDataComPhoneNum.length()){
                                        val objDataComPhoneNum : JSONObject = jsonArrayDataComPhoneNum.getJSONObject(a)

                                        val objIdentifier = objDataComPhoneNum.getInt("object_identifier")

                                        // gadipake yang dalem object communication_name
                                        val objComName = objDataComPhoneNum.getJSONObject("communication_name")
                                        val objIdentifierDalem = objComName.getInt("object_identifier")
                                        // gadipake yang dalem object communication_name

                                        val communicationType = objDataComPhoneNum.getString("communication_type")
                                        val serialNumber = objDataComPhoneNum.getInt("serial_number")
                                        val communicationNumber = objDataComPhoneNum.getString("communication_number")
                                        val typeCategory= "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName phone number: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")

                                    }
                                    // PHONE NUMBER

                                    val jsonArrayDataComEmail = objData.getJSONArray("communication_email")
                                    for (a in 0 until jsonArrayDataComEmail.length()){
                                        val objDataComEmail : JSONObject = jsonArrayDataComEmail.getJSONObject(a)

                                        val objIdentifier = objDataComEmail.getInt("object_identifier")
                                        val communicationType = objDataComEmail.getString("communication_type")
                                        val serialNumber = objDataComEmail.getInt("serial_number")
                                        val communicationNumber = objDataComEmail.getString("communication_number")
                                        val typeCategory = "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName Email: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")
                                    }

                                    val jsonArrayDataFacebook = objData.getJSONArray("communication_facebook")
                                    for (a in 0 until jsonArrayDataFacebook.length()){
                                        val objDataFacebook : JSONObject = jsonArrayDataFacebook.getJSONObject(a)

                                        val objIdentifier = objDataFacebook.getInt("object_identifier")
                                        val communicationType = objDataFacebook.getString("communication_type")
                                        val serialNumber = objDataFacebook.getInt("serial_number")
                                        val communicationNumber = objDataFacebook.getString("communication_number")
                                        val typeCategory = "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName Facebook: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")
                                    }

                                    val jsonArrayDataInstagram = objData.getJSONArray("communication_instagram")
                                    for (a in 0 until jsonArrayDataInstagram.length()){
                                        val objDataInstagram : JSONObject = jsonArrayDataInstagram.getJSONObject(a)

                                        val objIdentifier = objDataInstagram.getInt("object_identifier")
                                        val communicationType = objDataInstagram.getString("communication_type")
                                        val serialNumber = objDataInstagram.getInt("serial_number")
                                        val communicationNumber = objDataInstagram.getString("communication_number")
                                        val typeCategory = "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName Instagram: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")
                                    }

                                    val jsonArrayDataTwitter = objData.getJSONArray("communication_twitter")
                                    for (a in 0 until jsonArrayDataTwitter.length()){
                                        val objDataTwitter : JSONObject = jsonArrayDataTwitter.getJSONObject(a)

                                        val objIdentifier = objDataTwitter.getInt("object_identifier")
                                        val communicationType = objDataTwitter.getString("communication_type")
                                        val serialNumber = objDataTwitter.getInt("serial_number")
                                        val communicationNumber = objDataTwitter.getString("communication_number")
                                        val typeCategory = "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName Twitter: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")
                                    }

                                    val jsonArrayDataLinkedin = objData.getJSONArray("communication_linkedin")
                                    for (a in 0 until jsonArrayDataLinkedin.length()){
                                        val objDataLinkedin : JSONObject = jsonArrayDataLinkedin.getJSONObject(a)

                                        val objIdentifier = objDataLinkedin.getInt("object_identifier")
                                        val communicationType = objDataLinkedin.getString("communication_type")
                                        val serialNumber = objDataLinkedin.getInt("serial_number")
                                        val communicationNumber = objDataLinkedin.getString("communication_number")
                                        val typeCategory = "communication"

                                        val objName = defineDataName(typeCategory,communicationType)
                                        Log.d(tag,"check value objName Linkedin: $objName")

                                        val data = DataFamilyPersonal(intResponse,objName,objIdentifier,communicationType,communicationNumber,typeCategory,serialNumber)
                                        listPersonalData.add(data)
                                        Log.d(tag,"Data Personal Communication : $objIdentifier , $communicationType , $communicationNumber , $typeCategory")
                                    }

                                    val beginDate = objData.getString("begin_date")
                                    val endDate = objData.getString("end_date")
                                    val name = objData.getString("family_name")
                                    val nickname = objData.getString("nickname")
                                    val birthPlace = objData.getString("birth_place")
                                    val birthDate = objData.getString("birth_date")
                                    val gender = objData.getString("gender")
                                    val religion = objData.getString("religion")
                                    val taxFlag = objData.getString("tax_flag")
                                    val medFlag = objData.getString("med_flag")
                                    val familyStatusDate = objData.getString("family_status_date")
                                    val reportDate = objData.getString("report_date")
                                    val jobTitleFamily = objData.getString("job_title_family")
                                    val familyWork = objData.getString("family_work")
                                    val maritalDate = objData.getString("marital_date")
                                    val maritalStatus = objData.getString("marital_status")
                                    val identicalCompany = objData.getString("identical_company")


                                }
                                listData.postValue(listPersonalData)
                            } else {
                                Log.d(tag,"else di get Family Personal Identity: ${response?.getString("message")}")
                            }
                        } catch (e : Exception){
                            Log.d(tag,"else di get Family Personal Identity: ${e}")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"Family Data Detail")
                    }
                })

    }

    fun getDataCommunication() : LiveData<ArrayList<DataFamilyPersonal>>{
        return listData
    }

    fun deleteData(baseUrl : String?, dataType : String?, personalIdentifier: String?,token: String?){

//        var dataTypeVar = dataType
//        when (dataTypeVar) {
//            "communication" -> dataTypeVar = "communication"
//            "identification" -> dataTypeVar = "identification"
//            "bpjs_ketenagakerjaan" -> dataTypeVar = "jamsostek"
//            "bpjs_kesehatan" -> dataTypeVar = "insurance"
//            "bank_employee" -> dataTypeVar = "bankemployee"
//            "npwp" -> dataTypeVar = "tax"
//        }

        val deleteUrl = "${baseUrl}hcis/api/family$dataType?object_identifier=$personalIdentifier"
        Log.d(tag,"url delete personal data: $deleteUrl")

        AndroidNetworking.delete(deleteUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
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
                        errorLog(error, "Delete Family Personal Data")
                    }
                })
    }

    fun defineDataName(type : String?,number : String?) : String? {
        var name : String? = ""
        if (type.equals("identification")){
            when(number){
                "1" -> name = "KTP"
                "2" -> name = "KK"
                "3" -> name = "SIM A"
                "4" -> name = "Former Personnel Number"
                "5" -> name = "NIK Panjang"
                "6" -> name = "PANDU"
                "7" -> name = "Identity Card"
                "02" -> name = "Passport"
                "8" -> name = "SIM B"
                "9" -> name = "SIM C"
                "10" -> name = "Insurance"
            }
        } else if (type.equals("communication")){
            when(number){
                "1" -> name = "Telephone"
                "2" -> name = "Email"
                "3" -> name = "Facebook"
                "4" -> name = "Instagram"
                "5" -> name = "Twitter"
                "6" -> name = "LinkedIn"
                "8" -> name = "Phone Number"
            }
        }
        return name
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
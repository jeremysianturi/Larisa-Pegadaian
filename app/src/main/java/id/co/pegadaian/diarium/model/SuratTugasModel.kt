package id.co.pegadaian.diarium.model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugas
import id.co.pegadaian.diarium.entity.*
import org.json.JSONArray
import org.json.JSONObject

class SuratTugasModel : ViewModel(){
    val tag = SuratTugasModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSuratTugas>>()
    private val listDataFiltered = MutableLiveData<ArrayList<DataSuratTugas>>()
    private val listStatus = MutableLiveData<ArrayList<DataStatusSuratTugas>>()


    fun setDataSuratTugas(baseUrl : String?, token : String?, buscd : String?, pernr : String?){
        val listSuratTugas = ArrayList<DataSuratTugas>()
//        val requestUrl = "$baseUrl/surattugasapproval?business_code=$buscd&include=personal_number_maker&include=personal_number_signer"
        val requestUrl = "$baseUrl/surattugasparticipant?business_code=$buscd&personal_number=$pernr&include=surat_tugas_number" +
                "&include=personal_number_assigner&include=personal_number&include=approval_status"
        Log.d(tag,"get surat tugas list: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list surat tugas : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArraySurat : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArraySurat.length()) {
                                val objSurat: JSONObject = jsonArraySurat.getJSONObject(a)

                                // date penugasan
                                val datePenugasan = objSurat.getString("surat_tugas_date")
                                val dateKembali = objSurat.getString("surat_tugas_date_end")

//                                // status
//                                val statusOne = objSurat.getString("status_approval1")
//                                val statusTwo = objSurat.getString("status_approval2")

                                // pernr assigner
                                val objPernrAssigner = objSurat.getJSONObject("personal_number_assigner")
                                val pernrAssigner = objPernrAssigner.getString("personal_number")
                                val fullnameAssigner = objPernrAssigner.getString("full_name")

                                // pernr
                                val objPernr = objSurat.getJSONObject("personal_number")
                                val personalNumber = objPernr.getString("personal_number")
                                val fullnamePernr = objPernr.getString("full_name")

                                // surat tugas number array
                                val jsonArraySTNumber = objSurat.getJSONArray("surat_tugas_number")
                                for (i in 0 until jsonArraySTNumber.length()) {
                                    val objSTNumber : JSONObject = jsonArraySTNumber.getJSONObject(i)

                                    val objIdentifierSTNumber = objSTNumber.getInt("object_identifier").toString()
                                    val suratTugasNumber = objSTNumber.getString("surat_tugas_number")


                                    // status
                                    val statusOne : String?
                                    val statusTwo : String?
                                    val objIdentifierForApproval : String?
                                    Log.d(tag,"check value surat_tugas_approval dari api yang dummy : ${objSTNumber.getString("surat_tugas_approval")}")
                                    if (objSTNumber.getString("surat_tugas_approval") == "null"){
                                        statusOne = ""
                                        statusTwo = ""
                                        objIdentifierForApproval = ""
                                    } else {
                                        val objSTApproval = objSTNumber.getJSONObject("surat_tugas_approval")
                                        statusOne = objSTApproval.getString("status_approval1")
                                        statusTwo = objSTApproval.getString("status_approval2")

                                        // get object identifier for approval
                                        objIdentifierForApproval = objSTApproval.getInt("object_identifier").toString()
                                    }

                                    val data = DataSuratTugas(intResponse,objIdentifierSTNumber,suratTugasNumber,datePenugasan,dateKembali,pernrAssigner,fullnameAssigner
                                            ,personalNumber,fullnamePernr,statusOne,statusTwo,objIdentifierForApproval)
                                    Log.d(tag,"check status one : $statusOne \n check status two : $statusTwo")
                                    listSuratTugas.add(data)
                                }

                            }
                            listData.postValue(listSuratTugas)

                            } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK TUGAS")

                        listData.postValue(listSuratTugas)
                    }
                })
    }

    fun getDataSuratTugas() : LiveData<ArrayList<DataSuratTugas>> {
        return listData
    }


    fun setDataSuratTugasFiltered(baseurl : String?, token : String?, buscd : String?, pernr : String?, startDate : String?
                                  , endDate : String?, status : String?, dateNow : String?){

        val listSuratTugasFiltered = ArrayList<DataSuratTugas>()
        val requestUrl = "$baseurl/surattugasparticipant?business_code=$buscd&personal_number=$pernr" +
                "&begin_date_lte=$dateNow&end_date_gte=$dateNow&include=surat_tugas_number&include=surat_tugas_type$status$startDate$endDate&" +
                "include=personal_number_assigner&include=personal_number&include=approval_status"
//        val requestUrl = "$baseurl/surattugasapproval?business_code=$buscd&include=personal_number_maker&include=personal_number_signer$date$status"
        Log.d(tag,"get surat tugas filtered: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list surat tugas filtered : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArraySurat : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArraySurat.length()) {
                                val objSurat: JSONObject = jsonArraySurat.getJSONObject(a)

                                // date penugasan
                                val datePenugasan = objSurat.getString("surat_tugas_date")
                                val dateKembali = objSurat.getString("surat_tugas_date_end")


                                // pernr assigner
                                val objPernrAssigner = objSurat.getJSONObject("personal_number_assigner")
                                val pernrAssigner = objPernrAssigner.getString("personal_number")
                                val fullnameAssigner = objPernrAssigner.getString("full_name")

                                // pernr
                                val objPernr = objSurat.getJSONObject("personal_number")
                                val personalNumber = objPernr.getString("personal_number")
                                val fullnamePernr = objPernr.getString("full_name")

                                // surat tugas number array
                                val jsonArraySTNumber = objSurat.getJSONArray("surat_tugas_number")
                                for (i in 0 until jsonArraySTNumber.length()) {
                                    val objSTNumber : JSONObject = jsonArraySTNumber.getJSONObject(i)

                                    val objIdentifier = objSTNumber.getInt("object_identifier").toString()
                                    val suratTugasNumber = objSTNumber.getString("surat_tugas_number")

                                    // status
                                    val statusOne : String?
                                    val statusTwo : String?
                                    val objIdentifierForApproval : String?
                                    Log.d(tag,"check value surat_tugas_approval dari api yang dummy : ${objSTNumber.getString("surat_tugas_approval")}")
                                    if (objSTNumber.getString("surat_tugas_approval") == "null"){
                                        statusOne = ""
                                        statusTwo = ""
                                        objIdentifierForApproval = ""
                                    } else {
                                        val objSTApproval = objSTNumber.getJSONObject("surat_tugas_approval")
                                        statusOne = objSTApproval.getString("status_approval1")
                                        statusTwo = objSTApproval.getString("status_approval2")

                                        // get object identifier for approval
                                        objIdentifierForApproval = objSTApproval.getInt("object_identifier").toString()

                                    }

                                    val data = DataSuratTugas(intResponse,objIdentifier,suratTugasNumber,datePenugasan,dateKembali,pernrAssigner,fullnameAssigner
                                            ,personalNumber,fullnamePernr,statusOne,statusTwo,objIdentifierForApproval)
                                    Log.d(tag,"check status one : $statusOne \n check status two : $statusTwo")
                                    listSuratTugasFiltered.add(data)
                                }

//                                val data = DataSuratTugas(intResponse,objIdentifier,suratTugasNumber,beginDate,pernrMaker,fullnameMaker,pernrSigner,fullnameSigner,statusApproval)
//                                listSuratTugasFiltered.add(data)

                            }
                            listDataFiltered.postValue(listSuratTugasFiltered)

                        } else {
                            Log.d(tag,"check response di else setDataSuratTugasFiltered : ${response?.getString("message")}")
                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK TUGAS FILTERED")
                    }
                })

    }

    fun getDataSuratTugasFiltered() : LiveData<ArrayList<DataSuratTugas>> {
        return listDataFiltered
    }



    fun setStatusSuratTugas (baseUrl: String?, token: String?, tgl:String?, buscd: String?, objType : String?){

//        val listStatusST = ArrayList<DataStatusSuratTugas>()
////      first array
//        var dataStatusTrueorFalse = DataStatusSuratTugas("Approved")
//        listStatusST.add(dataStatusTrueorFalse)
//        listStatus.postValue(listStatusST)
//
////      second array
//        dataStatusTrueorFalse = DataStatusSuratTugas("Not Approved")
//        listStatusST.add(dataStatusTrueorFalse)
//        listStatus.postValue(listStatusST)


        val listSpinnerStatusST = ArrayList<DataStatusSuratTugas>()
        val requestUrl = "${baseUrl}ldap/api/object?begin_date_lte=$tgl&end_date_gt=$tgl&business_code=$buscd&object_type=$objType"
        Log.d(tag,"get data spinner status surat tugas list : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list spinner status surat tugas list : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {
                            val jsonArrayStatusSuratTugas: JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayStatusSuratTugas.length()) {
                                val objStatusSuratTugas: JSONObject = jsonArrayStatusSuratTugas.getJSONObject(a)

                                // name
                                val name = objStatusSuratTugas.getString("object_name")

                                // code
                                val code = objStatusSuratTugas.getString("object_code")

                                Log.d(tag,"check status surat tugas list : $code and $name")
                                val data = DataStatusSuratTugas(intResponse,name,code)
                                listSpinnerStatusST.add(data)
                            }
                            listStatus.postValue(listSpinnerStatusST)
                        }
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }

    fun getStatusSuratTugas() : LiveData<ArrayList<DataStatusSuratTugas>>{
        return listStatus
    }


    fun approveData(baseUrl : String?,token: String?, buscd : String?, objIdentifier : String?){

        val approveUrl = "$baseUrl/surattugasapproval"
        Log.d(tag,"url approve surat tugas: $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("status_approval1", "02")
        body.put("status_approval2", "02")

        Log.d(tag,"check body approve surat tugas : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag, "response approve surat tugas : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                        } else {

                        }


                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag, "cek anError : $anError")
                    }
                })
    }


    fun rejectData(baseUrl : String?,token: String?, buscd : String?, objIdentifier : String?){

        val approveUrl = "$baseUrl/surattugasapproval"
        Log.d(tag,"url reject surat tugas: $approveUrl")

        val body = JSONObject()
        body.put("object_identifier", objIdentifier.toString())
        body.put("status_approval1", "03")
        body.put("status_approval2", "03")

        Log.d(tag,"check body reject surat tugas : $body")

        AndroidNetworking.put(approveUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response reject surat tugas : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                        } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"cek anError : $anError")
                    }
                })
    }


    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}



// getlist surat tugas yang lama

//    val objIdentifier = objSurat.getInt("object_identifier").toString()
//    val datePenugasan = objSurat.getString("penugasan_date_start")
//
//    // pernr assigner
//    val objAssigner = objSurat.getJSONObject("personal_number_assigner")
//    val pernrAssigner = objAssigner.getString("personal_number")
//    val fullnameAssigner = objAssigner.getString("full_name")
//
//    // pernr
//    val objPernr = objSurat.getJSONObject("personal_number")
//    val pernr = objPernr.getString("personal_number")
//    val fullnamePernr = objPernr.getString("full_name")
//
//    // approval status
//    var status = ""
//    val jsonArrayApprovalStatus = objSurat.getJSONArray("approval_status")
//    for (u in 0 until jsonArrayApprovalStatus.length()){
//        val objApproval = jsonArrayApprovalStatus.getJSONObject(u)
//
//        status = objApproval.getString("status")
//
//    }
//
//    // surat tugas number
//    val jsonArraySTNumber = objSurat.getJSONArray("surat_tugas_number")
//    for (i in 0 until jsonArraySTNumber.length()) {
//        val objSTNumber: JSONObject = jsonArraySTNumber.getJSONObject(i)
//        val suratTugasNumber = objSTNumber.getString("surat_tugas_number")
//
//        val data = DataSuratTugas(intResponse,objIdentifier,suratTugasNumber,datePenugasan,pernrAssigner,fullnameAssigner
//                ,pernr,fullnamePernr,status)
//        Log.d(tag,"check status : $status")
//        listSuratTugas.add(data)
//    }


// get list surat tugas filtered yang lama

//val objIdentifier = objSurat.getInt("object_identifier").toString()
//val suratTugasNumber = objSurat.getString("surat_tugas_number")
//val beginDate = objSurat.getString("begin_date")
//
//
//// maker
//val obj_pernr_maker = objSurat.getJSONObject("personal_number_maker")
//val pernrMaker = obj_pernr_maker.getString("personal_number")
//val fullnameMaker = obj_pernr_maker.getString("full_name")
//
////signer
//val obj_pernr_signer = objSurat.getJSONObject("personal_number_signer")
//val pernrSigner = obj_pernr_maker.getString("personal_number")
//val fullnameSigner = obj_pernr_maker.getString("full_name")
//
//// status approval
//val statusApproval = objSurat.getBoolean("status").toString()

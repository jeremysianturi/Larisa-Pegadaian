package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugas
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugasDetail
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import org.json.JSONArray
import org.json.JSONObject

class SuratTugasDetailModel : ViewModel() {
    val tag = SuratTugasDetail::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSuratTugasDetail>>()
    private val listDataParticipant = MutableLiveData<ArrayList<DataSuratTugasDetail>>()


    fun setData(baseUrl : String?, token : String?, buscd : String?,suratTugasNum : String?,pernr : String?){
        val listSuratTugas = ArrayList<DataSuratTugasDetail>()


        val requestUrl = "$baseUrl/surattugasparticipant?business_code=$buscd&personal_number=$pernr&include=surat_tugas_number" +
                "&include=personal_number_assigner&include=personal_number&include=approval_status&&surat_tugas_number=$suratTugasNum"
        Log.d(tag,"get data surat tugas detail: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list surat tugas detail di sppd : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArraySuratDetail : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArraySuratDetail.length()) {
                                val objSurat: JSONObject = jsonArraySuratDetail.getJSONObject(a)

                                // surat tugas start date - end date
                                val tglPenugasan = objSurat.getString("surat_tugas_date")
                                val tglKepulangan = objSurat.getString("surat_tugas_date_end")
                                val kotaAsal = objSurat.getString("kota_asal")
                                val kotaTujuan = objSurat.getString("kota_tujuan")


                                // assigner
                                val objAssigner = objSurat.getJSONObject("personal_number_assigner")
                                val nameAssigner = objAssigner.getString("full_name")


                                // surat tugas number array
                                val jsonArrayStNumber = objSurat.getJSONArray("surat_tugas_number")
                                for (i in 0 until jsonArrayStNumber.length()) {
                                    val objStNumber: JSONObject = jsonArrayStNumber.getJSONObject(i)

                                    val objIdentifier = objStNumber.getInt("object_identifier").toString()
                                    val suratTugasNumber = objStNumber.getString("surat_tugas_number")
                                    val office = objStNumber.getString("office")
                                    val positionId = objStNumber.getString("position_id")
                                    val rincianTugas = objStNumber.getString("surat_tugas_detail")

                                    // surat tugas type

                                    val objSuratTugasType = objStNumber.getJSONObject("surat_tugas_type")
                                    val suratTugasType = objSuratTugasType.getString("object_name")

                                    val data = DataSuratTugasDetail(intResponse, objIdentifier, nameAssigner, suratTugasNumber, suratTugasType,
                                            office, positionId, tglPenugasan, tglKepulangan, kotaAsal, kotaTujuan, rincianTugas)
                                    listSuratTugas.add(data)
                                }


                            }

                            listData.postValue(listSuratTugas)

                        } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK TUGAS")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataSuratTugasDetail>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}

// set data yang lama

//val tglPenugasan = objSurat.getString("penugasan_date_start")
//val tglKepulangan = objSurat.getString("penugasan_date_end")
//val kotaAsal = objSurat.getString("origin_country")
//val kotaTujuan = objSurat.getString("destination_country")
//
//// name assigner
//val objNameAssigner = objSurat.getJSONObject("personal_number_assigner")
//val nameAssigner = objNameAssigner.getString("full_name")
//
//// ST number, tipe, office, positionID, rincian tugas
//val jsonArraySTNumber = objSurat.getJSONArray("surat_tugas_number")
//for (a in 0 until jsonArraySTNumber.length()) {
//    val objSTNumber: JSONObject = jsonArraySTNumber.getJSONObject(a)
//
//    val suratTugasNumber = objSTNumber.getString("surat_tugas_number")
//    val suratTugasType = objSTNumber.getString("surat_tugas_type")
//    val office = objSTNumber.getString("office")
//    val positionId = objSTNumber.getString("position_id")
//    val rincianTugas = objSTNumber.getString("surat_tugas_detail")
//
//    // variable yang gakepake
//    val objIdentifier = objSTNumber.getInt("object_identifier").toString()
//    val attachFile = objSTNumber.getString("attachment_file")
//
//    val data = DataSuratTugasDetail(intResponse, objIdentifier, attachFile, nameAssigner, suratTugasNumber, suratTugasType, office, positionId, tglPenugasan,
//            tglKepulangan, kotaAsal, kotaTujuan, rincianTugas)
//    listSuratTugas.add(data)
//}






// set data yang lebih lama

//    fun setData(baseUrl : String?, token : String?, buscd : String?,suratTugasNum : String?,pernr : String?){
//        val listSuratTugas = ArrayList<DataSuratTugasDetail>()
//
//
////        val requestUrl = "$baseUrl/surattugas?business_code=$buscd&surat_tugas_number=$suratTugasNum&include=detail_participant&" +
////                "include=surat_tugas_type&begin_date=$suratTugasDate"
//        Log.d(tag,"get data surat tugas detail di sppd: $requestUrl")
//
//        AndroidNetworking.get(requestUrl)
//                .addHeaders("Accept", "application/json")
//                .addHeaders("Content-Type", "application/json")
//                .addHeaders("Authorization","Bearer $token")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(object : JSONObjectRequestListener {
//                    override fun onResponse(response: JSONObject?) {
//
//                        Log.d(tag,"response get list surat tugas detail di sppd : $response")
//                        val intResponse = response?.getInt("status")
//
//                        if (intResponse == 200){
//
//                            val jsonArraySurat : JSONArray = response.getJSONArray("data")
//
//                            for (a in 0 until jsonArraySurat.length()) {
//                                val objSurat: JSONObject = jsonArraySurat.getJSONObject(a)
//
//                                val objIdentifier = objSurat.getInt("object_identifier").toString()
//                                val attachFile = objSurat.getString("attachment_file")
//                                val pernr = objSurat.getString("personal_number")
//                                val suratTugasNumber = objSurat.getString("surat_tugas_number")
//                                val office = objSurat.getString("office")
//                                val idPosisi = objSurat.getString("position_id")
//                                val rincianTugas = objSurat.getString("surat_tugas_detail")
//
//                                // object surat tugas type detail
//                                val objSTType : JSONObject = objSurat.getJSONObject("surat_tugas_type")
//                                val typeCode = objSTType.getString("object_code")
//                                val typeName = objSTType.getString("object_name")
//
//                                // object detail participant
//                                val jsonArrayParticipant : JSONArray = objSurat.getJSONArray("detail_participant")
//                                if (jsonArrayParticipant.length() == 0){
//                                    Log.d(tag,"jsonArrayParticipant length == 0")
//                                } else {
//                                        val objParticipant: JSONObject = jsonArrayParticipant.getJSONObject(0)
//                                        val tglPenugasan = objParticipant.getString("penugasan_date_start")
//                                        val tglKepulangan = objParticipant.getString("penugasan_date_end")
//                                        val kotaAsal = objParticipant.getString("origin_country")
//                                        val kotaTujuan = objParticipant.getString("destination_country")
//
//                                        val data = DataSuratTugasDetail(intResponse,objIdentifier,attachFile,pernr,suratTugasNumber
//                                                ,typeCode,typeName, office,idPosisi,tglPenugasan,tglKepulangan,kotaAsal
//                                                ,kotaTujuan,rincianTugas)
//                                        listSuratTugas.add(data)
//                                }
//                            }
//                            listData.postValue(listSuratTugas)
//
//                        } else {
//
//                        }
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        errorLog(anError,"WKWKWKWKKWK TUGAS")
//                    }
//                })
//    }
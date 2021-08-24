package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.sppd.Sppd
import id.co.pegadaian.diarium.entity.DataDetailSppd
import id.co.pegadaian.diarium.entity.DataSppd
import org.json.JSONArray
import org.json.JSONObject

class SppdDetailModel : ViewModel()  {

    val tag = SppdDetailModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataDetailSppd>>()

    fun setData(baseUrl : String?, token : String?, buscd : String?, sppdNumber : String?){
        val listSppd = ArrayList<DataDetailSppd>() // &include=surat_tugas_number&include=detail_biaya&include=surat_tugas_type&include=personal_number
//        val requestUrl = "$baseUrl/sppd?business_code=$buscd&sppd_number=$sppdNumber"
        val requestUrl = "$baseUrl/sppd/rekap?business_code=$buscd" +
                "&include=surat_tugas_type&include=sppd_type&include=surat_tugas_number&include=detail_biaya&include=personal_number"
        Log.d(tag,"get data sppd detail: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list sppd : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArraySurat : JSONArray = response.getJSONArray("data")

//                            for (a in 0 until jsonArraySurat.length()) {
//                                val objSppd: JSONObject = jsonArraySurat.getJSONObject(a)
//
//                                val tglBerangkat = objSppd.getString("surat_tugas_date")
//                                val tglKembali = objSppd.getString("surat_tugas_date_end")
//                                val kotaAsal = objSppd.getString("origin_country")
//                                val kotaTujuan = objSppd.getString("destination_country")
//                                val transportation = objSppd.getString("transportation")
//                                val rincianTugas = objSppd.getString("description")
//
//
//                                // tipe perjalanan sppd
//                                val objTipePerjalanan = objSppd.getJSONObject("sppd_type")
//                                val tipeSppd = objTipePerjalanan.getString("object_name")
//
//                                // name assigner
//                                val objAssigner = objSppd.getJSONObject("surat_tugas_number")
//
//                                val objNameAssigner = objAssigner.getJSONObject("personal_number")
//                                val nameAssigner = objNameAssigner.getString("full_name")
//
//                                // position id
//                                val position = objAssigner.getString("position_id")
//
//                                // surat tugas number
//                                val suratTugasNumber = objAssigner.getString("surat_tugas_number")
//
//                                val data = DataDetailSppd(intResponse,tipeSppd,nameAssigner,position,suratTugasNumber,sppdNumber,tglBerangkat, tglKembali,kotaAsal
//                                        ,kotaTujuan, transportation,rincianTugas)
//                                listSppd.add(data)

                            for (a in 0 until jsonArraySurat.length()) {

                                val objFilteredSppd: JSONObject = jsonArraySurat.getJSONObject(a)

                                val sppdNumber = objFilteredSppd.getString("sppd_number")
                                val kotaAsal = objFilteredSppd.getString("origin_country")
                                val kotaTujuan = objFilteredSppd.getString("destination_country")
                                val tglBerangkat = objFilteredSppd.getString("surat_tugas_date")
                                val tglKembali = objFilteredSppd.getString("surat_tugas_date_end")
                                val transportation = objFilteredSppd.getString("transportation")
                                val rincianTugas = objFilteredSppd.getString("description")

                                // position & surat tugas number & nameAssigner
                                val jsonArraySTNumber : JSONArray = objFilteredSppd.getJSONArray("surat_tugas_number")

                                var position : String? = null
                                var stNumber : String? = null
                                var nameAssigner : String? = null

                                for (i in 0 until jsonArraySTNumber.length()) {

                                    val objSTNumber: JSONObject = jsonArraySTNumber.getJSONObject(i)

                                    position = objSTNumber.getString("position_id")

                                    stNumber = objSTNumber.getString("surat_tugas_number")

                                    // assigner
                                    if (objSTNumber.getString("surat_tugas_approval").equals("null")){
                                        nameAssigner = "Data not valid"
                                        Log.d(tag,"masuk ke assigner null at detail sppd : --> ${objSTNumber.getString("surat_tugas_approval").equals(null)}")
                                    } else {
                                        val objStApproval : JSONObject = objSTNumber.getJSONObject("surat_tugas_approval")
                                        nameAssigner = objStApproval.getString("personal_number_maker")
                                        Log.d(tag,"masuk ke tidak null at detail sppd : --> ${objSTNumber.getJSONObject("surat_tugas_approval").equals(null)}" )
                                    }

                                }


                                // tipe sppd
                                val objTipePerjalanan = objFilteredSppd.getJSONObject("sppd_type")
                                val tipeSppd = objTipePerjalanan.getString("object_name")

                                // tipe perjalanan
                                val objSppdType = objFilteredSppd.getJSONObject("sppd_type")
                                val tipePerjalanan = objSppdType.getString("object_name")

//                                val data = DataDetailSppd(intResponse,sppdNumber,tipePerjalanan,kotaAsal,kotaTujuan,tglPenugasan,tglKembali)
                                val data = DataDetailSppd(intResponse,tipeSppd,nameAssigner,position,stNumber,sppdNumber,tglBerangkat, tglKembali,kotaAsal
                                        ,kotaTujuan, transportation,rincianTugas)
                                listSppd.add(data)

                            }
                            listData.postValue(listSppd)

                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"masuk ke error nih : $anError")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataDetailSppd>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
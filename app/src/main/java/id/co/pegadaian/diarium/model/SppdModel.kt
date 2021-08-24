package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataSppd
import org.json.JSONArray
import org.json.JSONObject

class SppdModel : ViewModel() {

    val tag = SppdModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSppd>>()

    fun setData(baseUrl : String?, token : String?,nik : String?, buscd : String?){
        val listSppd = ArrayList<DataSppd>()
//        val requestUrl = "$baseUrl/sppd?business_code=$buscd&personal_number=$nik&per_page=100" // &include=surat_tugas_number&include=detail_biaya&include=surat_tugas_type
        val requestUrl = "$baseUrl/sppd/rekap?business_code=$buscd&personal_number=$nik" +
                "&include=surat_tugas_type&include=sppd_type&include=surat_tugas_number&include=detail_biaya&include=personal_number"
        Log.d(tag,"get data sppd : $requestUrl")

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

                            for (a in 0 until jsonArraySurat.length()) {

//                                val objSppd: JSONObject = jsonArraySurat.getJSONObject(a)
//
//                                val sppdNumber = objSppd.getString("sppd_number")
//                                val kotaAsal = objSppd.getString("origin_country")
//                                val kotaTujuan = objSppd.getString("destination_country")
//                                val tglPenugasan = objSppd.getString("surat_tugas_date")
//                                val tglKembali = objSppd.getString("surat_tugas_date_end")
//
//                                // tipe perjalanan
//                                val objSppdType = objSppd.getJSONObject("sppd_type")
//                                val tipePerjalanan = objSppdType.getString("object_name")
//
//
//                                val data = DataSppd(intResponse,sppdNumber,tipePerjalanan,kotaAsal,kotaTujuan,tglPenugasan,tglKembali)
//                                listSppd.add(data)
//
//                            }
//                            listData.postValue(listSppd)
                                val objFilteredSppd: JSONObject = jsonArraySurat.getJSONObject(a)

                                val sppdNumber = objFilteredSppd.getString("sppd_number")
                                val kotaAsal = objFilteredSppd.getString("origin_country")
                                val kotaTujuan = objFilteredSppd.getString("destination_country")
                                val tglPenugasan = objFilteredSppd.getString("surat_tugas_date")
                                val tglKembali = objFilteredSppd.getString("surat_tugas_date_end")

                                // tipe perjalanan
                                val objSppdType = objFilteredSppd.getJSONObject("sppd_type")
                                val tipePerjalanan = objSppdType.getString("object_name")

                                val data = DataSppd(intResponse,sppdNumber,tipePerjalanan,kotaAsal,kotaTujuan,tglPenugasan,tglKembali)
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

    fun getData() : LiveData<ArrayList<DataSppd>> {
        return listData
    }


    fun setDataFiltered(baseUrl : String?, token : String?, nik : String?, buscd : String?, startDate : String?, endDate : String?){

        val listSppd = ArrayList<DataSppd>()
        val requestUrl = "$baseUrl/sppd/rekap?start_date_penugasan=$startDate&end_date_penugasan=$endDate&business_code=$buscd&personal_number=$nik" +
                "&include=surat_tugas_type&include=sppd_type&include=surat_tugas_number&include=detail_biaya&include=personal_number"
        Log.d(tag,"get data filtered sppd: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list sppd filtered : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArraySppdFiltered : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArraySppdFiltered.length()) {
                                val objFilteredSppd: JSONObject = jsonArraySppdFiltered.getJSONObject(a)

                                val sppdNumber = objFilteredSppd.getString("sppd_number")
                                val kotaAsal = objFilteredSppd.getString("origin_country")
                                val kotaTujuan = objFilteredSppd.getString("destination_country")
                                val tglPenugasan = objFilteredSppd.getString("surat_tugas_date")
                                val tglKembali = objFilteredSppd.getString("surat_tugas_date_end")

                                // tipe perjalanan
                                val objSppdType = objFilteredSppd.getJSONObject("sppd_type")
                                val tipePerjalanan = objSppdType.getString("object_name")

                                val data = DataSppd(intResponse,sppdNumber,tipePerjalanan,kotaAsal,kotaTujuan,tglPenugasan,tglKembali)
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

    fun getDataFiltered() : LiveData<ArrayList<DataSppd>> {
        return listData
    }



    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}

// set data yang lama

//val objIdentifier = objSurat.getInt("object_identifier")
//val evidence = objSurat.getString("evidence")
//val sppdType = objSurat.getString("sppd_type")
//val sppdNumber = objSurat.getString("sppd_number")
//val suratTugasType = objSurat.getString("surat_tugas_type")
//val suratTugasNumber = objSurat.getString("surat_tugas_number")
//val suratTugasDate = objSurat.getString("surat_tugas_date")
//val transportation = objSurat.getString("transportation")
//val distance = objSurat.getInt("distance").toString()
//val description = objSurat.getString("description")
//val tipePerjalanan = objSurat.getString("tipe_perjalanan")
//
//// detail biaya
//val jsonArrayBiaya = objSurat.getJSONArray("detail_biaya")
//
//for (a in 0 until jsonArrayBiaya.length()){
//    val objBiaya = jsonArrayBiaya.getJSONObject(a)
//
//    val objIdentifierBiaya = objBiaya.getInt("object_identifier").toString()
//    val sppdNumberBiaya = objBiaya.getString("sppd_number")
//    val akomodasi = objBiaya.getString("acomodation_type")
//    val nominal = objBiaya.getInt("nominal").toString()
//    val quantity = objBiaya.getInt("quantity").toString()
//    val precentage = objBiaya.getInt("percentage").toString()
//    val total = objBiaya.getDouble("total").toString()
//
//    val data = DataSppd(intResponse,objIdentifier,evidence,suratTugasType,suratTugasNumber,suratTugasDate,"",transportation,distance,description,tipePerjalanan,
//            objIdentifierBiaya,sppdNumberBiaya,akomodasi,nominal,quantity,precentage,total)
//    Log.d(tag,"check value surat tugas : \n objidentifier : $objIdentifier \n total : $total")
//    listSppd.add(data)
//}



// set data filtered sppd yang lama

//                                val objIdentifier = objFilteredSppd.getInt("object_identifier")
//
//                                // yang ga kepake
//                                val evidence = objFilteredSppd.getString("evidence")
//                                val transportation = objFilteredSppd.getString("transportation")
//                                val distance = objFilteredSppd.getInt("distance").toString()
//                                val description = objFilteredSppd.getString("description")
//                                val tipePerjalanan = objFilteredSppd.getString("tipe_perjalanan")
//
//
//                                // yang kepake
//                                val startDateST = objFilteredSppd.getString("surat_tugas_date")
//                                val endDateST = objFilteredSppd.getString("surat_tugas_date_end")
//
//
//                                // surat tugas type
//                                val objSTType = objFilteredSppd.getJSONObject("surat_tugas_type")
//                                val suratTugasType = objSTType.getString("object_name") // ga kepake
//
//
//                                // biaya // ga kepake
//                                var objIdentifierBiaya = ""
//                                var sppdNumberBiaya = ""
//                                var akomodasi = ""
//                                var nominal = ""
//                                var quantity = ""
//                                var precentage = ""
//                                var total = ""
//
//                                val jsonArrayBiaya = objFilteredSppd.getJSONArray("detail_biaya")
//                                for (i in 0 until jsonArrayBiaya.length()) {
//                                    val objBiaya: JSONObject = jsonArrayBiaya.getJSONObject(a)
//
//                                    objIdentifierBiaya = objBiaya.getInt("object_identifier").toString()
//                                    sppdNumberBiaya = objBiaya.getString("sppd_number")
//
//                                    val objAkomodasi = objBiaya.getJSONObject("acomodation_type")
//                                    akomodasi = objAkomodasi.getString("object_code")
//
//                                    nominal = objBiaya.getInt("nominal").toString()
//                                    quantity = objBiaya.getInt("quantity").toString()
//                                    precentage = objBiaya.getInt("percentage").toString()
//                                    total = objBiaya.getInt("total").toString()
//                                }
//
//                                // surat tugas number
//                                val jsonArraySTNumber = objFilteredSppd.getJSONArray("surat_tugas_number")
//                                for (i in 0 until jsonArraySTNumber.length()) {
//                                    val objSTNumber: JSONObject = jsonArraySTNumber.getJSONObject(i)
//
//                                    val suratTugasNumber = objSTNumber.getString("surat_tugas_number") // gakepake
//
//
//                                    val data = DataSppd(intResponse,)
//                                    Log.d(tag, "check value surat tugas : \n objidentifier : $objIdentifier \n total : $total")
//                                    listSppd.add(data)
//                                }



// set data filtered sppd yang lebih lama

//fun setDataFiltered(baseUrl : String?, token : String?, buscd : String?){
//    val listSppd = ArrayList<DataSppd>()
//    val requestUrl = "$baseUrl/sppd?business_code=$buscd&include=surat_tugas_number&include=detail_biaya&include=surat_tugas_type&include=sppd_type"
//    Log.d(tag,"get data sppd: $requestUrl")
//
//    AndroidNetworking.get(requestUrl)
//            .addHeaders("Accept", "application/json")
//            .addHeaders("Content-Type", "application/json")
//            .addHeaders("Authorization","Bearer $token")
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject?) {
//                    Log.d(tag,"response get list sppd : $response")
//                    val intResponse = response?.getInt("status")
//
//                    if (intResponse == 200){
//                        val jsonArraySurat : JSONArray = response.getJSONArray("data")
//
//                        for (a in 0 until jsonArraySurat.length()) {
//                            val objSurat: JSONObject = jsonArraySurat.getJSONObject(a)
//
//                            val objIdentifier = objSurat.getInt("object_identifier")
//                            val evidence = objSurat.getString("evidence")
//                            val sppdType = objSurat.getString("sppd_type")
//                            val sppdNumber = objSurat.getString("sppd_number")
//                            val suratTugasType = objSurat.getString("surat_tugas_type")
//                            val suratTugasNumber = objSurat.getString("surat_tugas_number")
//                            val suratTugasDate = objSurat.getString("surat_tugas_date")
//                            val transportation = objSurat.getString("transportation")
//                            val distance = objSurat.getInt("distance")
//                            val description = objSurat.getString("description")
//                            val tipePerjalanan = objSurat.getString("tipe_perjalanan")
//
//                            // detail biaya
//                            val jsonArrayBiaya = objSurat.getJSONArray("detail_biaya")
//
//                            for (a in 0 until jsonArrayBiaya.length()){
//                                val objBiaya = jsonArrayBiaya.getJSONObject(a)
//
//                                val objIdentifierBiaya = objBiaya.getInt("object_identifier")
//                                val sppdNumberBiaya = objBiaya.getString("sppd_number")
//                                val akomodasi = objBiaya.getString("acomodation_type")
//                                val nominal = objBiaya.getInt("nominal")
//                                val quantity = objBiaya.getInt("quantity")
//                                val precentage = objBiaya.getInt("percentage")
//                                val total = objBiaya.getDouble("total")
//
//                                val data = DataSppd(intResponse,objIdentifier,evidence,suratTugasType,suratTugasNumber,suratTugasDate,transportation,distance,description,tipePerjalanan,
//                                        objIdentifierBiaya,sppdNumberBiaya,akomodasi,nominal,quantity,precentage,total)
//                                Log.d(tag,"check value surat tugas : \n objidentifier : $objIdentifier \n total : $total")
//                                listSppd.add(data)
//                            }
//
//                        }
//                        listData.postValue(listSppd)
//
//                    } else {
//
//                    }
//                }
//
//                override fun onError(anError: ANError?) {
//                    Log.d(tag,"masuk ke error nih : $anError")
//                }
//            })
//
//
//}
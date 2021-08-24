package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugasDetail
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import id.co.pegadaian.diarium.entity.DataSuratTugasDetailParticipant
import org.json.JSONArray
import org.json.JSONObject

class SuratTugasDetailParticipantModel  : ViewModel()  {

    val tag = SuratTugasDetailParticipantModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataSuratTugasDetailParticipant>>()
    private val listDataParticipant = MutableLiveData<ArrayList<DataSuratTugasDetailParticipant>>()

    fun setDataParticipant(baseUrl : String?, token : String?, buscd : String?,suratTugasNum : String?, suratTugasDate : String?){

        val listSuratTugasParticipant = ArrayList<DataSuratTugasDetailParticipant>()
        val requestUrl = "$baseUrl/surattugas?business_code=$buscd&surat_tugas_number=$suratTugasNum&include=detail_participant&" +
                "include=surat_tugas_type"
        Log.d(tag,"get detail surat tugas participant: $requestUrl")


        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list surat tugas participant : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArraySurat : JSONArray = response.getJSONArray("data")

                                for (a in 0 until jsonArraySurat.length()) {
                                    val objSurat: JSONObject = jsonArraySurat.getJSONObject(a)

                                    // object detail participant
                                    val jsonArrayParticipant: JSONArray = objSurat.getJSONArray("detail_participant")
                                    Log.d(tag,"check jsonaray participant length : ${jsonArrayParticipant.length()}")
                                    for (i in 0 until jsonArrayParticipant.length()) {

                                        val objParticipant: JSONObject = jsonArrayParticipant.getJSONObject(i)
                                        val nomor = i+1

                                        // name and pernr
                                        val objName = objParticipant.getJSONObject("presonal_number")
                                        val name = objName.getString("full_name")
                                        val pernr = objName.getString("personal_number")

                                        val data = DataSuratTugasDetailParticipant(intResponse, nomor, name, pernr)
                                        listSuratTugasParticipant.add(data)
                                    }
                                }

                            listData.postValue(listSuratTugasParticipant)

                        } else {

                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK TUGAS detail participant")
                    }
                })
    }

    fun getDataParticipant() : LiveData<ArrayList<DataSuratTugasDetailParticipant>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
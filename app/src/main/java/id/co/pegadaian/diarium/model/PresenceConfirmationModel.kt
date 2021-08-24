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
import id.co.pegadaian.diarium.entity.DataSpinnerPresenceConfirmation
import id.co.pegadaian.diarium.entity.DataStatusPresenceList
import id.co.pegadaian.diarium.entity.DataStatusSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugas
import org.json.JSONArray
import org.json.JSONObject

class PresenceConfirmationModel : ViewModel() {

    val tag = PresenceConfirmationModel::class.java.simpleName
    private val listDataSpinner = MutableLiveData<ArrayList<DataSpinnerPresenceConfirmation>>()

    fun setDataSpinner (baseurl : String?, token : String?, buscd : String? ,beginDate : String?, objectType : String?){

        val listAbsentType = ArrayList<DataSpinnerPresenceConfirmation>()
        val requestUrl = "${baseurl}ldap/api/object?begin_date_lte=$beginDate&end_date_gt=$beginDate&business_code=$buscd&object_type=$objectType"
        Log.d(tag,"get data spinner new confirmation: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tag,"response get list status presence confirmartion : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayStatusPresence : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayStatusPresence.length()) {
                                val objSurat: JSONObject = jsonArrayStatusPresence.getJSONObject(a)

                                val objectCode = objSurat.getString("object_code")
                                val objectName = objSurat.getString("object_name")

                                val data = DataSpinnerPresenceConfirmation(intResponse,objectCode,objectName)
                                listAbsentType.add(data)
                            }
                            listDataSpinner.postValue(listAbsentType)
                        }

                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK presence conffff stat")
                    }
                })

    }

    fun getDataSpinner() : LiveData<ArrayList<DataSpinnerPresenceConfirmation>> {
        return listDataSpinner
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
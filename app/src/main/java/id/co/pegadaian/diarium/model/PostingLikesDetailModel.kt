package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataPostingLikesDetail
import org.json.JSONArray
import org.json.JSONObject

class PostingLikesDetailModel : ViewModel() {

    val tag = PostingLikesDetailModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataPostingLikesDetail>>()

    fun setData(baseUrl : String?, token : String?, buscd : String?, postingId : String?){
        val listLikesDetail = ArrayList<DataPostingLikesDetail>()
        val requestUrl = "${baseUrl}users/likedetail?business_code=$buscd&posting_id=$postingId"
        Log.d(tag,"url get data likes detail : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "$token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list likes detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArrayData : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayData.length()) {

                                val objLikesDetail : JSONObject = jsonArrayData.getJSONObject(a)

                                val likerName = objLikesDetail.getString("full_name")
                                val likerAvatar = objLikesDetail.getString("profile")


                                val data = DataPostingLikesDetail(intResponse,likerName,likerAvatar)
                                listLikesDetail.add(data)

                            }
                            listData.postValue(listLikesDetail)

                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"onerror di posting likes detail : $anError")
                        errorLog(anError,"Posting likes detail")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataPostingLikesDetail>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }

}
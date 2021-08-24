package id.co.pegadaian.diarium.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.entity.DataPostingCommentDetail
import id.co.pegadaian.diarium.entity.DataPostingLikesDetail
import org.json.JSONArray
import org.json.JSONObject

class PostingCommentDetailModel : ViewModel() {

    val tag = PostingCommentDetailModel::class.java.simpleName
    private val listData = MutableLiveData<ArrayList<DataPostingCommentDetail>>()

    fun setData(baseUrl : String?, token : String?, buscd : String?, postingId : String?){
        val listCommentDetail = ArrayList<DataPostingCommentDetail>()
        val requestUrl = "${baseUrl}users/commentdetail?business_code=$buscd&posting_id=$postingId"
        Log.d(tag,"url get data comment detail : $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "$token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.d(tag,"response get list comment detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){
                            val jsonArrayData : JSONArray = response.getJSONArray("data")

                            for (a in 0 until jsonArrayData.length()) {

                                val objCommentDetail : JSONObject = jsonArrayData.getJSONObject(a)

                                val name = objCommentDetail.getString("full_name")
                                val avatar = objCommentDetail.getString("profile")
                                val comment = objCommentDetail.getString("text_comment")
                                val date = objCommentDetail.getString("date")
                                val time = objCommentDetail.getString("time")


                                val data = DataPostingCommentDetail(intResponse, avatar, name, comment, date, time)
                                listCommentDetail.add(data)

                            }
                            listData.postValue(listCommentDetail)

                        } else {

                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tag,"onerror di posting comment detail : $anError")
                        errorLog(anError,"Posting comment detail")
                    }
                })
    }

    fun getData() : LiveData<ArrayList<DataPostingCommentDetail>> {
        return listData
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tag, "error $activity : $anError")
        Log.e(tag, "error $activity : ${anError?.response}")
        Log.e(tag, "error $activity : ${anError?.errorBody}")
    }
}
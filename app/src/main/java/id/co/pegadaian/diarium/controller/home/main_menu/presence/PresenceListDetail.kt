package id.co.pegadaian.diarium.controller.home.main_menu.presence

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.squareup.picasso.Picasso
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_presence_list_detail.*
import org.json.JSONArray
import org.json.JSONObject

class PresenceListDetail : AppCompatActivity() {

    private val tags = PresenceListApproveDetail::class.java.simpleName

    // initialize
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper


    private var objIdenFromIntent : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence_list_detail)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Presence Confirmation Detail")

        // get extra
        objIdenFromIntent = intent.getStringExtra("objectIdentifier")

        // get detail
        getDetailPresenceConf(objIdenFromIntent)


    }

    private fun getDetailPresenceConf(objIdentifier: String?){

        val baseUrl = session.serverURL
        val token = session.token
        val pernr = session.userNIK
        val objIden = objIdentifier

        val requestUrl = "${baseUrl}users/presensi/absent?personal_number=$pernr" +
                "&include=absent_type&include=approval_status&object_identifier=$objIden"
        Log.d(tags, "get presence list detail nya: $requestUrl")

        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags, "response get list presence confirmartion detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            val jsonArrayPresence: JSONArray = response.getJSONArray("data")
                            for (a in 0 until jsonArrayPresence.length()) {
                                val objSurat: JSONObject = jsonArrayPresence.getJSONObject(a)

                                val objIdentifier = objSurat.getInt("object_identifier")
                                val pernr = objSurat.getString("personal_number")
                                val description = objSurat.getString("description")
                                val evidence = objSurat.getString("evidence")

                                // date absent
                                val date = objSurat.getString("date_absent")

                                // status absent
                                val objAbsentType: JSONObject = objSurat.getJSONObject("absent_type")
                                val statusAbsentName = objAbsentType.getString("object_name")
                                val statusAbsentType = objAbsentType.getString("object_code")

                                // approval status
                                val objAbsentApproval: JSONObject = objSurat.getJSONObject("approval_status")
                                val statusName = objAbsentApproval.getString("object_name")
                                val statusCode = objAbsentApproval.getString("object_code")

                                // set value to textview
                                nik_detail_presence_nik.text = pernr
                                status_detail_presence_nik.text = statusName
                                tanggal_detail_presence_nik.text = date
                                jeniskonf_detail_presence_nik.text = statusAbsentName
                                deskripsi_detail_presence_nik.text = description
//                                file_detail_presence_nik.text = evidence


                                // untuk image evidence
                                try {
                                    Picasso.get().load(evidence).error(R.drawable.placeholder_gallery).into(file_detail_presence_nik)
                                } catch (e: Exception) {
                                    Picasso.get().load(R.drawable.placeholder_gallery).into(file_detail_presence_nik)
                                }

                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError, "WKWKWKWKKWK NGERROOOORRRR")
                    }
                })
    }

    private fun errorLog(anError: ANError?, activity: String){
        Log.e(tags, "error $activity : $anError")
        Log.e(tags, "error $activity : ${anError?.response}")
        Log.e(tags, "error $activity : ${anError?.errorBody}")
    }
}
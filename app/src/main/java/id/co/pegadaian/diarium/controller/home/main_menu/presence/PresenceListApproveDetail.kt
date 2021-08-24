package id.co.pegadaian.diarium.controller.home.main_menu.presence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.squareup.picasso.Picasso
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPresenceList
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_presence_list_approve_detail.*
import kotlinx.android.synthetic.main.activity_presence_list_detail.*
import org.json.JSONArray
import org.json.JSONObject

class PresenceListApproveDetail : AppCompatActivity(), View.OnClickListener {

    private val tags = PresenceListApproveDetail::class.java.simpleName
    private var objIdenFromIntent : String? = null


    // initialize
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence_list_approve_detail)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Presence Confirmation Approval")

        // get extra
        objIdenFromIntent = intent.getStringExtra("objIdentifier Presence Confirmation")

        // get detail
        getDetailPresenceApproveConf(objIdenFromIntent)

        // onClick
        btn_reject_presenceconf_detail.setOnClickListener(this)
        btn_approve_presenceconf_detail.setOnClickListener(this)
    }

    private fun getDetailPresenceApproveConf(objIdentifier : String?){

        val baseUrl = session.serverURL
        val token = session.token
        val pernr = session.userNIK
        val objIden = objIdentifier

        val requestUrl = "${baseUrl}users/presensi/absent?personal_number_approval=$pernr&include=absent_type&include=approval_status&object_identifier=$objIden"
        Log.d(tags,"get presence approval list nya: $requestUrl")


        AndroidNetworking.get(requestUrl)
                .addHeaders("Authorization",token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags,"response get list presence confirmartion approval detail : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200){

                            val jsonArrayPresence : JSONArray = response.getJSONArray("data")
                            for (a in 0 until jsonArrayPresence.length()) {
                                val objSurat: JSONObject = jsonArrayPresence.getJSONObject(a)

                                val objIdentifier = objSurat.getInt("object_identifier")
                                val pernr = objSurat.getString("personal_number")
                                val description = objSurat.getString("description")
                                val evidence = objSurat.getString("evidence")

                                // date absent
                                val date = objSurat.getString("date_absent")

                                // status absent
                                val objAbsentType : JSONObject = objSurat.getJSONObject("absent_type")
                                val statusAbsentName = objAbsentType.getString("object_name")
                                val statusAbsentType = objAbsentType.getString("object_code")

                                // approval status
                                val objAbsentApproval : JSONObject = objSurat.getJSONObject("approval_status")
                                val statusName = objAbsentApproval.getString("object_name")
                                val statusCode = objAbsentApproval.getString("object_code")


                                // set value to textview
                                nik_detail_presence_approval.text = pernr
                                status_detail_presence_approval.text = statusName
                                tanggal_detail_presence_approval.text = date
                                jeniskonf_detail_presence_approval.text = statusAbsentName
                                deskripsi_detail_presence_approval.text = description
//                                file_detail_presence_approval.text = evidence

                                // untuk image evidence
                                try {
                                    Picasso.get().load(evidence).error(R.drawable.placeholder_gallery).into(file_detail_presence_approval_nik)
                                } catch (e: Exception) {
                                    Picasso.get().load(R.drawable.placeholder_gallery).into(file_detail_presence_approval_nik)
                                }

                            }
                        }
                    }

                    override fun onError(anError: ANError?) {
                        errorLog(anError,"WKWKWKWKKWK NGERROOOORRRR approval ")
                    }
                })
    }


    private fun approvePresenceConfirmation (objectIdentifier : String?) {

        val baseUrl = session.serverURL
        val token = session.token

        val body = JSONObject()
        body.put("oid", objectIdentifier.toString())
        body.put("approval_status", "02")

        Log.d(tags,"check body approve detail presence confirmation : $body")

        val requestUrl = "${baseUrl}users/presensi/absent"
        Log.d(tags,"api approve detail presence confirmation : $requestUrl")

        AndroidNetworking.put(requestUrl)
                .addHeaders("Authorization",token)
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags, "response approve detail presence confirmation : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            Toast.makeText(this@PresenceListApproveDetail, "Approved!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@PresenceListApproveDetail, response?.getString("message"), Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tags, "an error at approve detail presence confirmation : $anError")
                        Toast.makeText(this@PresenceListApproveDetail, "ERROR!", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun rejectPresenceConfirmation (objectIdentifier : String?) {

        val baseUrl = session.serverURL
        val token = session.token

        val body = JSONObject()
        body.put("oid", objectIdentifier.toString())
        body.put("approval_status", "03")

        Log.d(tags,"check body reject detail presence confirmation : $body")

        val requestUrl = "${baseUrl}users/presensi/absent"
        Log.d(tags,"api reject detail presence confirmation : $requestUrl")

        AndroidNetworking.put(requestUrl)
                .addHeaders("Authorization",token)
                .addJSONObjectBody(body)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {

                    override fun onResponse(response: JSONObject?) {

                        Log.d(tags, "response reject detail presence confirmation : $response")
                        val intResponse = response?.getInt("status")

                        if (intResponse == 200) {

                            Toast.makeText(this@PresenceListApproveDetail, "Rejected!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@PresenceListApproveDetail, response?.getString("message"), Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.d(tags, "an error at reject detail presence confirmation : $anError")
                        Toast.makeText(this@PresenceListApproveDetail, "ERROR!", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.btn_reject_presenceconf_detail -> {
                Log.d(tags,"check object identifier sebelum di reject : $objIdenFromIntent")
                rejectPresenceConfirmation(objIdenFromIntent)
            }

            R.id.btn_approve_presenceconf_detail -> {
                Log.d(tags,"check object identifier sebelum di approve : $objIdenFromIntent")
                approvePresenceConfirmation(objIdenFromIntent)
            }
        }
    }

    private fun errorLog(anError: ANError?, activity : String){
        Log.e(tags, "error $activity : $anError")
        Log.e(tags, "error $activity : ${anError?.response}")
        Log.e(tags, "error $activity : ${anError?.errorBody}")
    }

}
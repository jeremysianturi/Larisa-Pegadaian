package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.familydata

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.FamilyDataAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data.AddPersonalDataActivityKt
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity
import id.co.pegadaian.diarium.model.FamilyDataModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import org.json.JSONArray
import org.json.JSONObject

class FamilyDataOnly : AppCompatActivity() {

    private lateinit var progressDialogHelper : ProgressDialogHelper
    lateinit var session : UserSessionManager
    private lateinit var listModel : List<FamilyDataModel>
    private lateinit var model : FamilyDataModel
    private lateinit var adapter : FamilyDataAdapter
    lateinit var lvDataFamilyOnly : ListView
    var tag = "FamilyData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_data_only)

        setTitle("Family Data")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

        lvDataFamilyOnly = findViewById(R.id.lv_data_family_detail)
        getFamily()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_familymember, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_family_member -> {
                val intent = Intent(this, AddFamilyMember::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFamily(){
        var urlGetFamily : String = session.serverURLHCIS + "hcis/api/datafamily?personnel_number=" + session.userNIK
        Log.d(tag,"url get my family: $urlGetFamily" )
                AndroidNetworking.get(urlGetFamily)
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Content-Type", "application/json")
                        .addHeaders("Authorization","Bearer ${session.token}")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener{
                            override fun onResponse(response: JSONObject?) {
                                Log.d(tag,"Response get my family: " + response)
                                listModel = ArrayList<FamilyDataModel>()
                                try {
                                    if (response?.getInt("status") == 200){
                                        var jsonArrayFamily: JSONArray = response.getJSONArray("data")
                                        Log.d(tag,"json array length: " + jsonArrayFamily.length())

                                        for (a in 0 until jsonArrayFamily.length()) {
                                            val objFamily: JSONObject = jsonArrayFamily.getJSONObject(a)
                                            val objectIdentifier = objFamily.getInt("object_identifier").toString()
                                            val status = objFamily.getString("family_type")
                                            val name = objFamily.getString("family_name")
                                            val famNumber = objFamily.getString("family_number")

                                            model = FamilyDataModel(objectIdentifier,name,status,famNumber)
                                            (listModel as ArrayList<FamilyDataModel>).add(model)

                                            Log.d(tag,"nama : $name " + "status : $status " +"family number : $famNumber")
                                        }

                                    } else{
                                        Log.d(tag,"else di getMyFamily : " + response?.getString("message"))
                                    }
                                } catch (e : Exception){
                                    Log.d(tag,"Catch di getMyFamily : " + response?.getString("message"))
                                }
                                adapter = FamilyDataAdapter(this@FamilyDataOnly, listModel)
                                lvDataFamilyOnly.adapter = adapter


                                lvDataFamilyOnly.setOnItemClickListener { parent, view, position, id ->
                                    val oid = listModel[position].objectIdentifier
                                    val familyName = listModel[position].name
                                    val familyStatus = listModel[position].value
                                    val familyNumber = listModel[position].number

                                    popupMenuActivity(oid,familyName,familyStatus,familyNumber)

                                }
                            }

                            override fun onError(anError: ANError?) {
                                Log.d(tag,"Error di getMyIdentity: " + anError)
                            }
                        })

    }

    private fun deleteData(objectIdentifier : String?){

        val baseUrl = session.serverURLHCIS
        val token = session.token

        val deleteUrl = "${baseUrl}hcis/api/datafamily?object_identifier=$objectIdentifier"
        Log.d(tag,"url delete family member : $deleteUrl")

        AndroidNetworking.delete(deleteUrl)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization","Bearer $token")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        Log.d(tag, "response delete family member : $response")
                        try {
                            if (response.getInt("status") == 200) {

                                Toast.makeText(this@FamilyDataOnly, "Success!", Toast.LENGTH_SHORT).show()
                                getFamily()

                            } else {
                                Toast.makeText(this@FamilyDataOnly, response.getString("message"), Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: java.lang.Exception) {
                            Log.d(tag, "masuk ke catch delete family member : ${e.message}")
                            Toast.makeText(this@FamilyDataOnly, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(error: ANError) {
                        val anError = error.errorBody
                        val obj = JSONObject(anError!!)
                        Log.d(tag,"anError delete family member : ${obj.getString("message")}")
                        Toast.makeText(this@FamilyDataOnly, obj.getString("message"), Toast.LENGTH_SHORT).show()

                    }
                })
    }

    private fun popupMenuActivity( oid : String?, familyName : String?, familyStatus : String?, familyNumber : String? ) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_editdelete_personaldata)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.setTitle("Input Code Here")
        val btnChange = dialog.findViewById<View>(R.id.btnChange) as Button
        val btnDelete = dialog.findViewById<View>(R.id.btnDelete) as Button
        val tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
        tvTitle.text = "$familyName"
        dialog.show()
        dialog.setCancelable(true)

        // DELETE FAMILY MEMBER
        btnDelete.visibility = View.VISIBLE
        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to delete this family member ?")
            builder.setPositiveButton("Yes") { dialog, which -> deleteData(oid)}
            builder.setNegativeButton("No") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }
        // DELETE FAMILY MEMBER

        // Go to Detail Family Member
        btnChange.text = "Go to detail"
        btnChange.setOnClickListener {
            val intent = Intent(this@FamilyDataOnly, FamilyDataDetail::class.java)
            intent.putExtra("nama",familyName)
            intent.putExtra("status",familyStatus)
            intent.putExtra("number",familyNumber)
            startActivity(intent)
            dialog.dismiss()
        }
        // Go to Detail Family Member

    }

    override fun onResume() {
        super.onResume()
        getFamily()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }
}
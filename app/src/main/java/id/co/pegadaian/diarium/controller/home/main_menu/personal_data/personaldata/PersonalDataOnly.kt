package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.personaldata

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.DataPersonalAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data.AddPersonalDataActivity
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity
import id.co.pegadaian.diarium.model.PersonalDataModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class PersonalDataOnly : AppCompatActivity() {

    private lateinit var progressDialogHelper : ProgressDialogHelper
    lateinit var session : UserSessionManager
    private lateinit var listModel : List<PersonalDataModel>
    private lateinit var model : PersonalDataModel
    private lateinit var adapter : DataPersonalAdapter
    lateinit var lvDataPersonalOnly : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data_only)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)

//        lvDataPersonalOnly = findViewById(R.id.lv_data_personal_detail)
        getMyIdentity()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_post -> {
                val intent = Intent(this, AddPersonalDataActivity::class.java)
                //                    intent.putExtra("name",session.getUserFullName());
//                    intent.putExtra("email","diarium@telkom.co.id");
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getMyIdentity(){
        var tgl : java.text.SimpleDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
        var tRes : String = tgl.format(Date())
        progressDialogHelper.showProgressDialog(this, "Getting data...")

        var urlGetMyIdentity : String = session.serverURL + "users/mydetailidentity/buscd/" + session.userBusinessCode
        print("Check url getMyIdentity: " + urlGetMyIdentity)

        AndroidNetworking.get(urlGetMyIdentity)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        print("Response get my identity: " + response)
                        listModel = ArrayList<PersonalDataModel>()

                        try {
                            if (response?.getInt("status") == 200) {
                                var jsonArrayCommunication: JSONArray = response.getJSONObject("data").getJSONArray("communication")
                                print("json array length: " + jsonArrayCommunication.length())

                                for (a in 0 until jsonArrayCommunication.length()) {
                                    val objcommunication: JSONObject = jsonArrayCommunication.getJSONObject(a)
                                    val value = objcommunication.getString("communication_number")
                                    // JSONObject objname = objcommunication.getJSONObject("communication_name");
                                    val name = objcommunication.getString("communication_name")
                                    val objIdentifier = objcommunication.getString("object_identifier")
                                    val objectCode = objcommunication.getString("communication_type")
                                    val type = "communication"
                                    model = PersonalDataModel(name, value, objIdentifier, type, objectCode)
                                    (listModel as ArrayList<PersonalDataModel>).add(model)
                                    println("name first$a$name")
                                }

                                println("error disini : No Value for communication_type")
                                val jsonArrayidentification = response.getJSONObject("data").getJSONArray("identification")
                                for (b in 0 until jsonArrayidentification.length()) {
                                    val objidentification = jsonArrayidentification.getJSONObject(b)
                                    val value = objidentification.getString("identification_number")
                                    val name = objidentification.getString("identification_name")
                                    val objIndentifier = objidentification.getString("object_identifier")
                                    val objectCode = objidentification.getString("identification_type")
                                    val type = "identification"
                                    model = PersonalDataModel(name, value, objIndentifier, type, objectCode)
                                    (listModel as ArrayList<PersonalDataModel>).add(model)
                                    // for (int o=0;o<namearray.length();o++) {
//                                        JSONObject objname = namearray.getJSONObject(o);
//                                        String name = objname.getString("name");
//
//                                    }
                                    println("name second : $b$name")
                                }

                                val jsonArrayNPWP = response.getJSONObject("data").getJSONArray("npwp")
                                for (b in 0 until jsonArrayidentification.length()) {
                                    val objNpwp = jsonArrayNPWP.getJSONObject(b)
                                    val jsonarrayNpwp_name = "NPWP"
                                    val jsonarrayNpwp_number = objNpwp.getString("npwp_number")
                                    val objIndentifier = objNpwp.getString("object_identifier")
                                    //                                    String objectCode = objNpwp.getString("communication_type");
                                    val objectCode = "00000"
                                    val type = "npwp"
                                    model = PersonalDataModel(jsonarrayNpwp_name, jsonarrayNpwp_number, objIndentifier, type, objectCode)
                                    (listModel as ArrayList<PersonalDataModel>).add(model)
                                    println("name third : $jsonarrayNpwp_number")
//                                String jsonarrayNpwp_name = "NPWP";
//                                String jsonarrayNpwp_number = response.getJSONObject("data").getString("npwp");
                                }

                                val bpjs_kesehatan_length = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").length()
                                if (bpjs_kesehatan_length > 0) {
                                    val obj = response.getJSONObject("data").getJSONArray("bpjs_kesehatan").getJSONObject(0)
                                    val value_bpjs_kesehatan = obj.getString("insurance_number")
                                    val objIndentifier = obj.getString("object_identifier")
                                    //                                    String objectCode = obj.getString("communication_type");
                                    val objectCode = "00000"
                                    val type = "bpjs_kesehatan"
                                    model = PersonalDataModel("BPJS Kesehatan", value_bpjs_kesehatan, objIndentifier, type, objectCode)
                                    (listModel as ArrayList<PersonalDataModel>).add(model)
                                }

                                val bpjs_ketenagakerjaan_length = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").length()
                                if (bpjs_ketenagakerjaan_length > 0) {
                                    val obj = response.getJSONObject("data").getJSONArray("bpjs_ketenagakerjaan").getJSONObject(0)
                                    val value_bpjs_ketenagakerjaan = obj.getString("bpjs_number")
                                    val objIndentifier = obj.getString("object_identifier")
                                    //                                    String objectCode = obj.getString("communication_type");
                                    val objectCode = "00000"
                                    val type = "bpjs_ketenagakerjaan"
                                    model = PersonalDataModel("BPJS Ketenagakerjaan", value_bpjs_ketenagakerjaan, objIndentifier, type, objectCode)
                                    (listModel as ArrayList<PersonalDataModel>).add(model)
                                }

                                val arrayBankLength = response.getJSONObject("data").getJSONArray("bank_employee").length()
                                if (arrayBankLength > 0) {
                                    val jsonarraybank_employee = response.getJSONObject("data").getJSONArray("bank_employee")
                                    for (a in 0 until jsonarraybank_employee.length()) {
                                        val objBank = jsonarraybank_employee.getJSONObject(a)
                                        val value = objBank.getString("account_number")
                                        val name = objBank.getString("bank_type")
                                        val name_edit = "No. Rekening $name"
                                        val objIndentifier = objBank.getString("object_identifier")
                                        //                                        String objectCode = objBank.getString("communication_type");
                                        val objectCode = "00000"
                                        val type = "bank_employee"
                                        model = PersonalDataModel(name_edit, value, objIndentifier, type, objectCode)
                                        (listModel as ArrayList<PersonalDataModel>).add(model)
                                        println("name sixth : $name$value")
                                    }
                                }

                                print("Model Personal: " + listModel)
                                progressDialogHelper.dismissProgressDialog(this@PersonalDataOnly)

                            } else {
                                progressDialogHelper.dismissProgressDialog(this@PersonalDataOnly)
                                print("else di getMyIdentity: " + response?.getString("message"))
                            }
                        } catch (e: Exception) {
                            progressDialogHelper.dismissProgressDialog(this@PersonalDataOnly)
                            print("catch di getMyIdentity: " + e)
                        }
                        adapter = DataPersonalAdapter(this@PersonalDataOnly, listModel)
                        lvDataPersonalOnly.adapter = adapter

                        lvDataPersonalOnly.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                            val identityName = listModel[position].name
                            val identityValue = listModel[position].value
                            val objectIdentifier = listModel[position].objectIdentifier
                            val type = listModel[position].type
                            val objectCodeClicked = listModel[position].objectCode
                            popupMenuActivity(identityName, identityValue, objectIdentifier, type, objectCodeClicked)
                        })

                    }

                    override fun onError(anError: ANError?) {
                        progressDialogHelper.dismissProgressDialog(this@PersonalDataOnly)
                        print("onError di getMyIdentity: " + anError)
                    }
                })
    }

    private fun popupMenuActivity(identityName: String, identityValue: String, identityObjIdentifier: String,
                                  identityType: String, codeObject: String) {
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
        tvTitle.text = "Update your $identityName data"
        dialog.show()
        dialog.setCancelable(true)

//        if (activity_type.equals("03")) {
//            btnDelete.setVisibility(View.GONE);
//        }
//        else {
        btnDelete.visibility = View.VISIBLE
        btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to delete this personal data ?")
            builder.setPositiveButton("Yes") { dialog, which -> submitDelete(identityObjIdentifier, identityType) }
            builder.setNegativeButton("No") { dialog, which -> // Do nothing
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
            dialog.dismiss()
        }
        //        }
        btnChange.setOnClickListener {
            val i = Intent(this, editNewPersonalDataActivity::class.java)
            i.putExtra("personal_identity_name", identityName)
            i.putExtra("personal_identity_value", identityValue)
            i.putExtra("personal_identity_objidentifier", identityObjIdentifier)
            i.putExtra("personal_identity_type", identityType)
            i.putExtra("object_code", codeObject)
            startActivity(i)
            dialog.dismiss()
        }
    }

    private fun submitDelete(personalIdentifier: String, identityType: String) {
        println("ketikamasukdelete : $personalIdentifier  $identityType")
        var type = ""
        when (identityType) {
            "communication" -> type = "communication"
            "identification" -> type = "identification"
            "bpjs_ketenagakerjaan" -> type = "jamsostek"
            "bpjs_kesehatan" -> type = "insurance"
            "bank_employee" -> type = "bankemployee"
            "npwp" -> type = "tax"
        }
        println("Test type value : $type")
        AndroidNetworking.delete(session.serverURL + type + "?object_identifier=" + personalIdentifier)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json") //                .addHeaders("Authorization",session.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        println(response.toString() + "HASILDELETENYA")
                        try {
                            if (response.getInt("status") == 200) {
                                Toast.makeText(this@PersonalDataOnly, "Success delete personal data", Toast.LENGTH_SHORT).show()
                                adapter.notifyDataSetChanged()
                                //                                getMyIdentity();
//                                getTodayActivityList(session.getTodayActivity());
                            } else {
                                Toast.makeText(this@PersonalDataOnly, "error", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: java.lang.Exception) {
                            println(e)
                        }
                    }

                    override fun onError(error: ANError) {
                        println(error)
                    }
                })
    }


}
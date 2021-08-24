package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.familydata

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.FamilyDataDetailAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data.AddPersonalDataActivityKt
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity
import id.co.pegadaian.diarium.entity.DataFamilyPersonal
import id.co.pegadaian.diarium.model.FamilyDataDetailModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper

class FamilyDataDetail : AppCompatActivity() {

    private val tags = FamilyDataDetail::class.java.simpleName

    private lateinit var viewModel : FamilyDataDetailModel
    private lateinit var adapter : FamilyDataDetailAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper

    private lateinit var rvPersonalDataList : RecyclerView

    lateinit var familyName : String
    lateinit var familyStatus : String
    lateinit var familyNumber : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data_only)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FamilyDataDetailModel::class.java]

        rvPersonalDataList = findViewById(R.id.rv_data_personal_detail)

        var intent : Intent = intent
        familyName = intent.getStringExtra("nama")!!
        familyStatus = intent.getStringExtra("status")!!
        familyNumber  = intent.getStringExtra("number")!!

        Log.d(tags,"check intent value \n name : $familyName \n type : $familyStatus \n number : $familyNumber")

        setTitle("$familyName's data")

        getPersonalDataFamily()
        showList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_post -> {
                val intent = Intent(this, AddPersonalDataActivityKt::class.java)
                intent.putExtra("name",familyName);
                intent.putExtra("status",familyStatus);
                intent.putExtra("number",familyNumber);
                Log.d(tags,"check intent value add family personal data : \n name : $familyName \n status : $familyStatus \n number : $familyNumber")
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPersonalDataFamily(){
        val baseUrl = session.serverURLHCIS
        val token = session.token
        val nik = session.userNIK
        val familyType = familyStatus
        val familyNumber = familyNumber

        viewModel.setDataCommunication(baseUrl,token,nik,familyType,familyNumber)

        viewModel.getDataCommunication().observe(this, Observer { model ->
            Log.d(tags,"Check model value : $model")
            if (model.isEmpty()){
                progressDialogHelper.dismissProgressDialog(this)
            } else {
                if (model[0].intResponse == 200){
                    Log.d(tags,"Masuk ke if sini")
                    Log.d(tags,"PersonalDataListPrint ${model[0].intResponse == 200}")
                    progressDialogHelper.dismissProgressDialog(this)
                    adapter.setData(model)
                } else {
                    progressDialogHelper.dismissProgressDialog(this)
                }

            }
        })

        viewModel.getDataCommunication()
    }

    private fun deleteData(objIdentifier : String?, type : String?,token : String?){
        val baseUrl = session.serverURLHCIS
        val dataType = type
        val objIdentifier = objIdentifier
        val token = token

        viewModel.deleteData(baseUrl,dataType,objIdentifier,token)

        getPersonalDataFamily()
        showList()
    }

    private fun showList(){
        adapter = FamilyDataDetailAdapter()
        rvPersonalDataList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvPersonalDataList.adapter = adapter

        adapter.setOnItemClickCallback(object : FamilyDataDetailAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFamilyPersonal) {
                Log.d(tags,"data value at onClick list : $data")
                val name = data.name
                val value = data.number
                val objIdentifier = data.objIdentifier
                val objIdentifierToString = objIdentifier.toString()
                val objCode = data.objCode
                val type = data.type
                val serialNumber = data.serialNumber

                popupMenuActivity(name,value,objIdentifierToString,type,objCode,serialNumber)
            }
        })
    }

    private fun popupMenuActivity(identityName: String?, identityValue: String?, identityObjIdentifier: String?,
                                  identityType: String?, codeObject: String?,serialNumber : Int?) {
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


        if (identityType.equals("address")){

            // DELETE FAMILY PERSONAL DATA
            btnDelete.visibility = View.VISIBLE
            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to delete this personal data ?")
                builder.setPositiveButton("Yes") { dialog, which -> deleteData(identityObjIdentifier, identityType,session.token) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }
            // DELETE FAMILY PERSONAL DATA

            // EDIT FAMILY PERSONAL DATA SET VIEW GONE
            btnChange.visibility = View.GONE

        } else {

            // DELETE FAMILY PERSONAL DATA
            btnDelete.visibility = View.VISIBLE
            btnDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Confirm")
                builder.setMessage("Are you sure to delete this personal data ?")
                builder.setPositiveButton("Yes") { dialog, which -> deleteData(identityObjIdentifier, identityType,session.token) }
                builder.setNegativeButton("No") { dialog, which -> // Do nothing
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
                dialog.dismiss()
            }
            // DELETE FAMILY PERSONAL DATA


            // EDIT FAMILY PERSONAL DATA
            btnChange.setOnClickListener {
                val i = Intent(this, editNewPersonalDataActivity::class.java)

                i.putExtra("personal_identity_name", identityName)
                Log.d(tags,"personal identity name family intent: $identityName")

                i.putExtra("personal_identity_value", identityValue)
                Log.d(tags,"personal identity value family intent: $identityValue")

                i.putExtra("personal_identity_objidentifier", identityObjIdentifier)
                Log.d(tags,"personal identity objIdentifier family intent: $identityObjIdentifier")

                i.putExtra("personal_identity_type", identityType)
                Log.d(tags,"personal identity type family intent: $identityType")

                i.putExtra("object_code", codeObject)
                Log.d(tags,"object code family intent: $codeObject")

                i.putExtra("serial_number", serialNumber)
                Log.d(tags,"serial number family intent: $serialNumber")

                i.putExtra("from","family personal data")
                i.putExtra("family_number",familyNumber)
                i.putExtra("family_type",familyStatus)

                startActivity(i)
                dialog.dismiss()
            }
            // EDIT FAMILY PERSONAL DATA

        }

    }


//    private fun submitDelete(personalIdentifier: String?, identityType: String?) {
//        println("ketikamasukdelete : $personalIdentifier  $identityType")
//        var type = ""
//        when (identityType) {
//            "communication" -> type = "communication"
//            "identification" -> type = "identification"
//            "bpjs_ketenagakerjaan" -> type = "jamsostek"
//            "bpjs_kesehatan" -> type = "insurance"
//            "bank_employee" -> type = "bankemployee"
//            "npwp" -> type = "tax"
//        }
//        println("Test type value : $type")
//        AndroidNetworking.delete(session.serverURL + type + "?object_identifier=" + personalIdentifier)
//                .addHeaders("Accept", "application/json")
//                .addHeaders("Content-Type", "application/json") //                .addHeaders("Authorization",session.getToken())
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(object : JSONObjectRequestListener {
//                    override fun onResponse(response: JSONObject) {
//                        // do anything with response
//                        println(response.toString() + "HASILDELETENYA")
//                        try {
//                            if (response.getInt("status") == 200) {
//                                Toast.makeText(this@FamilyDataDetail, "Success delete personal data", Toast.LENGTH_SHORT).show()
//                                adapter.notifyDataSetChanged()
//                                //                                getMyIdentity();
////                                getTodayActivityList(session.getTodayActivity());
//                            } else {
//                                Toast.makeText(this@FamilyDataDetail, "error", Toast.LENGTH_SHORT).show()
//                            }
//                        } catch (e: java.lang.Exception) {
//                            println(e)
//                        }
//                    }
//
//                    override fun onError(error: ANError) {
//                        println(error)
//                    }
//                })
//    }

    override fun onResume() {
        super.onResume()
        getPersonalDataFamily()
        showList()
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }
}
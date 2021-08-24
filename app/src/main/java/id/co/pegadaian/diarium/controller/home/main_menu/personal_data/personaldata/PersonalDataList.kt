package id.co.pegadaian.diarium.controller.home.main_menu.personal_data.personaldata

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PersonalDataListAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.add_data.AddPersonalDataActivityKt
import id.co.pegadaian.diarium.controller.home.main_menu.personal_data.edit_data.editNewPersonalDataActivity
import id.co.pegadaian.diarium.entity.DataPersonal
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.model.PersonalDataListModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.popup_editdelete_personaldata.*

class PersonalDataList : AppCompatActivity() {

    private val tags = PersonalDataList::class.java.simpleName

    private lateinit var viewModel : PersonalDataListModel
    private lateinit var adapter : PersonalDataListAdapter
    private lateinit var session : UserSessionManager
    private lateinit var progressDialogHelper : ProgressDialogHelper
    private lateinit var rvPersonalDataList : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data_only)

        progressDialogHelper = ProgressDialogHelper()
        session = UserSessionManager(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PersonalDataListModel::class.java]

        rvPersonalDataList = findViewById(R.id.rv_data_personal_detail)
//        showProgressDialog(true)
//        getMyIdentity();

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getPersonalData()

//        Log.d(tags,"Masuk ke Oncreate nih buktinya!")
//
//        viewModel.getData().observe(this, Observer { data ->
//            if (data.isEmpty()){
//                Log.d(tags,"malah masuk kesini!!!")
//            } else {
//                Log.d(tags,"check data value di getData() : $data")
//                adapter.setData(data)
//            }
//        })

        showList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_post, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.add_post){
            val intent = Intent(this, AddPersonalDataActivityKt::class.java)
            intent.putExtra("name","")
            startActivity(intent)
            true
        }else super.onOptionsItemSelected(item)
    }

    private fun getPersonalData(){
        val baseUrl = session.serverURL
        val token = session.token
        val buscd = session.userBusinessCode

        viewModel.setData(baseUrl, token, buscd)

        viewModel.getData().observe(this, Observer { model ->
            Log.d(tags, "Check model value : $model")
            if (model.isEmpty()) {
//                showProgressDialog(false)
            } else {
                if (model[0].intResponse == 200) {
                    Log.d(tags, "Masuk ke if sini")
                    Log.d(tags, "PersonalDataListPrint ${model[0].intResponse == 200}")
//                    showProgressDialog(false)
                    adapter.setData(model)
                } else {
//                    showProgressDialog(false)
                }
            }
        })

        viewModel.getData()
    }

    private fun deleteData(objIdentifier: String?, type: String?){

        val baseUrl : String

        if (type.equals("address") || type.equals("education")){
            baseUrl = session.serverURLHCIS
        } else {
            baseUrl = session.serverURL
        }

        val token = session.token
        val dataType = type
        val objIdentifier = objIdentifier

        viewModel.deleteData(baseUrl, token, dataType, objIdentifier)

        getPersonalData()
        showList()
    }

    private fun showList(){

        adapter = PersonalDataListAdapter()
        rvPersonalDataList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPersonalDataList.adapter = adapter

        adapter.setOnItemClickCallback(object : PersonalDataListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataPersonal) {
                Log.d(tags, "data value at onClick list : $data")
                val name = data.name
                val value = data.value
                val objIdentifier = data.objIdentifier
                val objCode = data.objCode
                val type = data.type
                val flag = data.flag

                popupMenuActivity(name, value, objIdentifier, type, objCode,flag)
            }
        })
    }

        private fun popupMenuActivity(identityName: String?, identityValue: String?, identityObjIdentifier: String?,
                                      identityType: String?, codeObject: String?, flag : String?) {

            Log.d(tags, "Check value flag : $flag")
            if (flag.equals("false")){
                Toast.makeText(this, "This data cannot be change or delete", Toast.LENGTH_SHORT).show()
            } else if (flag.equals("true")){

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

                // ADDRESS ATAU EDUCATION BUTTON UPDATE DI HIDE
                if (identityType.equals("address") || identityType.equals("education")){

                    // EDIT PERSONAL DATA SET VIEW GONE
                    btnChange.visibility = View.GONE

                    // DELETE PERSONAL DATA
                    tvTitle.text = "Delete your personal data"
                    btnDelete.visibility = View.VISIBLE
                    btnDelete.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Confirm")
                        builder.setMessage("Are you sure to delete this personal data ?")
                        builder.setPositiveButton("Yes") { dialog, which -> deleteData(identityObjIdentifier, identityType)
                        }
                        builder.setNegativeButton("No") { dialog, which -> // Do nothing
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        dialog.dismiss()
                    }
                    // DELETE PERSONAL DATA

                } else {

                    // DELETE PERSONAL DATA
                    btnDelete.visibility = View.VISIBLE
                    btnDelete.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Confirm")
                        builder.setMessage("Are you sure to delete this personal data ?")
                        builder.setPositiveButton("Yes") { dialog, which -> deleteData(identityObjIdentifier, identityType)
                        }
                        builder.setNegativeButton("No") { dialog, which -> // Do nothing
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        dialog.dismiss()
                    }
                    // DELETE PERSONAL DATA

                    // EDIT PERSONAL DATA
                    btnChange.setOnClickListener {

                        val i = Intent(this, editNewPersonalDataActivity::class.java)

                        i.putExtra("personal_identity_name", identityName)
                        Log.d(tags, "personal identity name personal intent: $identityName")

                        i.putExtra("personal_identity_value", identityValue)
                        Log.d(tags, "personal identity value personal intent: $identityValue")

                        i.putExtra("personal_identity_objidentifier", identityObjIdentifier)
                        Log.d(tags, "personal identity objIdentifiew personal intent: $identityObjIdentifier")

                        i.putExtra("personal_identity_type", identityType)
                        Log.d(tags, "personal identity type personal intent: $identityType")

                        i.putExtra("object_code", codeObject)
                        Log.d(tags, "personal object code personal intent: $codeObject")

                        i.putExtra("from", "personal data")

                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        startActivity(i)
                        this.finish()
                        dialog.dismiss()
                    }
                    // EDIT PERSONAL DATA

                }
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
//        val deleteUrl = session.serverURL + type + "?object_identifier=" + personalIdentifier
//        Log.d(tags,"url delete personal data: $deleteUrl")
//        println("Test type value : $type")
//        AndroidNetworking.delete(deleteUrl)
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
//                                Toast.makeText(this@PersonalDataList, "Success delete personal data", Toast.LENGTH_SHORT).show()
//                                adapter.notifyDataSetChanged()
//                                getPersonalData()
//                                showList()
//                            } else {
//                                Toast.makeText(this@PersonalDataList, "error", Toast.LENGTH_SHORT).show()
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

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

    override fun onResume() {
        getPersonalData()
        showList()
        super.onResume()
    }


//    private fun showProgressDialog(state: Boolean) {
//        if (state) {
//            progressDialogHelper.showProgressDialog(this,"Loading...")
//        } else {
//            progressDialogHelper.dismissProgressDialog(this)
//        }
//    }

}

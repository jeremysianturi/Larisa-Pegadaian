package id.co.pegadaian.diarium.controller.home.main_menu.sppd_online

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.ActionBar
import com.androidnetworking.AndroidNetworking
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.MenuDinamisAdapter
import id.co.pegadaian.diarium.adapter.MenuDinamisEsppdAdapter
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.sppd.Sppd
import id.co.pegadaian.diarium.controller.home.main_menu.sppd_online.surat_tugas.SuratTugas
import id.co.pegadaian.diarium.model.MenuModel
import id.co.pegadaian.diarium.util.UserSessionManager
import id.co.pegadaian.diarium.util.element.ProgressDialogHelper
import kotlinx.android.synthetic.main.activity_sppd_online.*

class SppdOnline : AppCompatActivity(){

    lateinit var session : UserSessionManager
    lateinit var progressDialogHelper : ProgressDialogHelper
    var tag = "SPPD Online"

    lateinit var menuModel : MenuModel
    lateinit var listMenuModel : ArrayList<MenuModel>
    lateinit var menuAdapter : MenuDinamisEsppdAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sppd_online)

        // initialize
        session = UserSessionManager(this)
        AndroidNetworking.initialize(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("E-SPPD")
        progressDialogHelper = ProgressDialogHelper()

        getGridMenu()
    }

    private fun getGridMenu(){
        listMenuModel = ArrayList<MenuModel>()

        // list pertama
        menuModel = MenuModel("ST","Surat Tugas","")
        listMenuModel.add(menuModel)
        menuAdapter = MenuDinamisEsppdAdapter(this,listMenuModel)
        gridview.adapter = menuAdapter

        // list kedua
        menuModel = MenuModel("S-P-P-D","SPPD","")
        listMenuModel.add(menuModel)
        menuAdapter = MenuDinamisEsppdAdapter(this,listMenuModel)
        gridview.adapter = menuAdapter

        gridview.setOnItemClickListener { adapterView, view, i, l ->
            Log.d(tag,"Check value onItemClickListener : \n adapterView : $adapterView \n view : $view \n i : $i \n l : $l")

            if (i == 0){
                val i = Intent(this,SuratTugas::class.java)
                startActivity(i)
            } else if (i == 1){
                val i = Intent(this,Sppd::class.java)
                startActivity(i)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}
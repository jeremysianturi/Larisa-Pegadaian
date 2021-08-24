package id.co.pegadaian.diarium.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.model.MenuModel
import id.co.pegadaian.diarium.util.UserSessionManager
import java.lang.Exception

class MenuDinamisEsppdAdapter(
        private val context: Context,
        private val listModel : List<MenuModel>
        ) : BaseAdapter(){

//    private var layoutInflater : LayoutInflater? = null
    private lateinit var session : UserSessionManager
    private lateinit var model: MenuModel
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun getCount(): Int {
        return listModel.size
    }

    override fun getItem(p0: Int): Any {
        return listModel[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        session = UserSessionManager(context)

        model = listModel.get(p0)
        var convertView = p1
        if (convertView == null){
            var inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(R.layout.item_gridmenu_esppd,null)
        }

        textView = convertView!!.findViewById(R.id.txtJudul_sppd)
        imageView = convertView!!.findViewById(R.id.imView_sppd)

        textView.text = model.menuName

        if (textView.text == "Surat Tugas"){
            try {
                Picasso.get().load(R.drawable.surattugas).error(R.drawable.profile).into(imageView);
            } catch (e : Exception){
                Picasso.get().load(R.drawable.profile).into(imageView);
            }
        } else if (textView.text == "SPPD"){
            try {
                Picasso.get().load(R.drawable.sppd).error(R.drawable.profile).into(imageView);
            } catch (e : Exception){
                Picasso.get().load(R.drawable.profile).into(imageView);
            }
        }

        return convertView

    }

}
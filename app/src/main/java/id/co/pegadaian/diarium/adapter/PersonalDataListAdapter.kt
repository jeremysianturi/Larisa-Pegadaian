package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPersonal
import id.co.pegadaian.diarium.entity.DataSuratTugas

class PersonalDataListAdapter : RecyclerView.Adapter<PersonalDataListAdapter.UserViewHolder>() {

    private var onItemClickCallback : OnItemClickCallback? = null
    private val mData = ArrayList<DataPersonal>()
    private var mFilter = ArrayList<DataPersonal>()

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataPersonal>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalDataListAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_personal_data, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("adapterListRV", "getItemCount : ${mFilter.size}")
        return mFilter.size
    }
    

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataPersonal){
            with(itemView){
                val imageIcon = findViewById<View>(R.id.imageViewList)
                val title = findViewById<TextView>(R.id.tvTitle)
                val value = findViewById<TextView>(R.id.tvValue)

                title.text = data.name
                value.text = data.value

                var nameToString : String = title.text.toString()
                Log.d("PersonalDataListAdapter","check value nameToString : $nameToString")

                when(nameToString){
                    "Telephone" -> Glide.with(itemView.context).load(R.drawable.personal_phone).into(imageIcon as ImageView)
                    "Phone Number" -> Glide.with(itemView.context).load(R.drawable.personal_phone).into(imageIcon as ImageView)
                    "Email" -> Glide.with(itemView.context).load(R.drawable.personal_email).into(imageIcon as ImageView)
                    "Mail" -> Glide.with(itemView.context).load(R.drawable.personal_email).into(imageIcon as ImageView)
                    "Facebook" -> Glide.with(itemView.context).load(R.drawable.facebook_icon).into(imageIcon as ImageView)
                    "Instagram" -> Glide.with(itemView.context).load(R.drawable.instagram_icon).into(imageIcon as ImageView)
                    "Twitter" -> Glide.with(itemView.context).load(R.drawable.twitter_icon).into(imageIcon as ImageView)
                    "RUMAH DINAS" -> Glide.with(itemView.context).load(R.drawable.home_viewpager).into(imageIcon as ImageView)
                    "RUMAH SEWA" -> Glide.with(itemView.context).load(R.drawable.home_viewpager).into(imageIcon as ImageView)
                    "RUMAH UTAMA" -> Glide.with(itemView.context).load(R.drawable.home_viewpager).into(imageIcon as ImageView)
                    else ->
                        if (nameToString.contains("No. Rekening")){
                        Glide.with(itemView.context).load(R.drawable.personal_norek).into(imageIcon as ImageView)
                        } else {
                           Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }


//    private fun popupMenuActivity(identityName: String, identityValue: String, identityObjIdentifier: String,
//                                  identityType: String, codeObject: String) {
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.popup_editdelete_personaldata)
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.gravity = Gravity.CENTER
//        dialog.window!!.attributes = lp
//        dialog.setTitle("Input Code Here")
//        val btnChange = dialog.findViewById<View>(R.id.btnChange) as Button
//        val btnDelete = dialog.findViewById<View>(R.id.btnDelete) as Button
//        val tvTitle = dialog.findViewById<View>(R.id.tvTitle) as TextView
//        tvTitle.text = "Update your $identityName data"
//        dialog.show()
//        dialog.setCancelable(true)
//
////        if (activity_type.equals("03")) {
////            btnDelete.setVisibility(View.GONE);
////        }
////        else {
//        btnDelete.visibility = View.VISIBLE
//        btnDelete.setOnClickListener {
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Confirm")
//            builder.setMessage("Are you sure to delete this personal data ?")
//            builder.setPositiveButton("Yes") { dialog, which -> submitDelete(identityObjIdentifier, identityType) }
//            builder.setNegativeButton("No") { dialog, which -> // Do nothing
//                dialog.dismiss()
//            }
//            val alert = builder.create()
//            alert.show()
//            dialog.dismiss()
//        }
//        //        }
//        btnChange.setOnClickListener {
//            val i = Intent(this, editNewPersonalDataActivity::class.java)
//            i.putExtra("personal_identity_name", identityName)
//            i.putExtra("personal_identity_value", identityValue)
//            i.putExtra("personal_identity_objidentifier", identityObjIdentifier)
//            i.putExtra("personal_identity_type", identityType)
//            i.putExtra("object_code", codeObject)
//            startActivity(i)
//            dialog.dismiss()
//        }
//    }
//
//
//    private fun submitDelete(personalIdentifier: String, identityType: String) {
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
//                                Toast.makeText(this@PersonalDataList, "Success delete personal data", Toast.LENGTH_SHORT).show()
//                                adapter.notifyDataSetChanged()
//                                //                                getMyIdentity();
////                                getTodayActivityList(session.getTodayActivity());
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


    interface OnItemClickCallback {
        fun onItemClicked(data: DataPersonal)
    }


}

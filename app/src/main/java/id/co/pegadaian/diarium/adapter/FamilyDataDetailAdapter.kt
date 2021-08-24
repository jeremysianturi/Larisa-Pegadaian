package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataFamilyPersonal

class FamilyDataDetailAdapter : RecyclerView.Adapter<FamilyDataDetailAdapter.UserViewHolder>() {

    private var onItemClickCallback : FamilyDataDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataFamilyPersonal>()
    private var mFilter = ArrayList<DataFamilyPersonal>()

    fun setOnItemClickCallback(onItemClickCallback: FamilyDataDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataFamilyPersonal>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_personal_data, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("adapterListRVpersonal", "getItemCount : ${mFilter.size}")
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: DataFamilyPersonal){
            with(itemView){
                val imageIcon = findViewById<View>(R.id.imageViewList)
                val title = findViewById<TextView>(R.id.tvTitle)
                val value = findViewById<TextView>(R.id.tvValue)

                val objCode = data.objCode
                val category = data.type
                val resTitle = data.name

                Log.d("FamilyDataDetailAdapter", "check value categiruuuuu : $category")

                if (category == "identification"){
                    when(objCode){
                        "1" -> {
                            title.text = "KTP"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "2" -> {
                            title.text = "KK"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "3" -> {
                            title.text = "SIM A"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "4" -> {
                            title.text = "Former Personnel Number"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "5" -> {
                            title.text = "NIK Panjang"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "6" -> {
                            title.text = "PANDU"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "7" -> {
                            title.text = "Identity Card"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "02" -> {
                        title.text = "Passport"
                        Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "8" -> {
                            title.text = "SIM B"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "9" -> {
                            title.text = "SIM C"
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                        "10" -> {
                            title.text = data.name
                            Glide.with(itemView.context).load(R.drawable.personal_npwp).into(imageIcon as ImageView)
                        }
                    }
                } else if (category == "communication") {
                    when (objCode){
                        "1"  -> {
                            title.text = "Telephone"
                            Glide.with(itemView.context).load(R.drawable.personal_phone).into(imageIcon as ImageView)
                        }
                        "2"  -> {
                            title.text = "Email"
                            Glide.with(itemView.context).load(R.drawable.personal_email).into(imageIcon as ImageView)
                        }
                        "3"  -> {
                            title.text = "Facebook"
                            Glide.with(itemView.context).load(R.drawable.facebook_icon).into(imageIcon as ImageView)
                        }
                        "4"  -> {
                            title.text = "Instagram"
                            Glide.with(itemView.context).load(R.drawable.instagram_icon).into(imageIcon as ImageView)
                        }
                        "5"  -> {
                            title.text = "Twitter"
                            Glide.with(itemView.context).load(R.drawable.twitter_icon).into(imageIcon as ImageView)
                        }
                        "6"  -> {
                            title.text = "Linkedin"
                            Glide.with(itemView.context).load(R.drawable.linkedin_icon).into(imageIcon as ImageView)
                        }
                        "8"  -> {
                            title.text = "Phone Number"
                            Glide.with(itemView.context).load(R.drawable.personal_phone).into(imageIcon as ImageView)
                        }

                    }
                } else if (category == "address"){

                    Log.d("FamilyDataDetailAdapter","check value residence di adapter \n category : $category \n title : $resTitle")
                    title.text = resTitle
                    Glide.with(itemView.context).load(R.drawable.home_viewpager).into(imageIcon as ImageView)

                }

                value.text = data.number

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataFamilyPersonal)
    }

}

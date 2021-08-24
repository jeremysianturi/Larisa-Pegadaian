package id.co.pegadaian.diarium.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataSppd
import id.co.pegadaian.diarium.entity.DataSuratTugas

class SppdAdapter : RecyclerView.Adapter<SppdAdapter.UserViewHolder>() {
    private var onItemClickCallback : SppdAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataSppd>()
    private var mFilter = ArrayList<DataSppd>()

    fun setOnItemClickCallback(onItemClickCallback: SppdAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<DataSppd>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SppdAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_sppd, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: SppdAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("yayaya","sizenya surat tugas : ${mFilter.size}")
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(data: DataSppd){
            with(itemView){
                val title = findViewById<TextView>(R.id.tvTitle)
                val value = findViewById<TextView>(R.id.tvValue)
                val tanggal = findViewById<TextView>(R.id.tv_date_surattugas)
                val status = findViewById<TextView>(R.id.tv_status_surattugas)
                val imageSppd = findViewById<ImageView>(R.id.iv_sppd)

                title.text = data.sppdNumber
                value.text = "${data.kotaAsal} - ${data.kotaTujuan}"
                tanggal.text = "${data.tglPenugasan} - ${data.tglKembali}"
                status.text = data.tipePerjalanan

                if (data.tipePerjalanan.equals("Perjalanan Dalam Negri")){
                    imageSppd.setImageResource(R.drawable.sppd_negeri)
                } else if (data.tipePerjalanan.equals("Perjalanan Luar Negri")){
                    imageSppd.setImageResource(R.drawable.sppd_ln)
                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSppd)
    }
}
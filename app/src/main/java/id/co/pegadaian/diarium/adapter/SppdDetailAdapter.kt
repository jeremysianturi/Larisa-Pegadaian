package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataDetailSppd
import id.co.pegadaian.diarium.entity.DataSppd
import id.co.pegadaian.diarium.entity.DataSuratTugas
import kotlinx.android.synthetic.main.lay_item_detail_sppd.view.*

class SppdDetailAdapter : RecyclerView.Adapter<SppdDetailAdapter.UserViewHolder>() {


    private var onItemClickCallback : SppdDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataDetailSppd>()
    private var mFilter = ArrayList<DataDetailSppd>()

    fun setOnItemClickCallback(onItemClickCallback: SppdDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<DataDetailSppd>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_detail_sppd, parent, false)
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

        fun bind(data: DataDetailSppd){
            with(itemView){

                tv_tipe_sppd.text = data.tipeSppd
                tv_nama.text = data.name
                tv_posisi_sppd.text = data.posisi       // tadinya di comment
                tv_no_surattugas.text = data.noSuratTugas
                tv_no_sppd.text = data.noSppd
                tgl_penugasan.text = data.tglPenugasan
                tgl_kepulangan.text = data.tglKepulangan
                kota_asal.text = data.kotaAsal          // tadinya di comment
                kota_tujuan.text = data.kotaTujuan      // tadinya di comment
                tv_jenis_transformasi.text = data.jenisTransportasi
                rincian_tugas.text = data.rincianTugas


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataDetailSppd)
    }
}
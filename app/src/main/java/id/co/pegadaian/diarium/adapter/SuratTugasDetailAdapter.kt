package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataSuratTugas
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import kotlinx.android.synthetic.main.lay_item_surattugas_detail.view.*
import org.w3c.dom.Text

class SuratTugasDetailAdapter : RecyclerView.Adapter<SuratTugasDetailAdapter.UserViewHolder>() {
    private var onItemClickCallback : SuratTugasDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataSuratTugasDetail>()
    private var mFilter = ArrayList<DataSuratTugasDetail>()

    fun setOnItemClickCallback(onItemClickCallback: SuratTugasDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataSuratTugasDetail>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuratTugasDetailAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_surattugas_detail, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: SuratTugasDetailAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("tag", " check m filter . size : ${mFilter.size}")
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataSuratTugasDetail){
            with(itemView){

                nip_penugas.text = data.personalNumber
                no_surattugas.text = data.suratTugasNumber
                tipe.text = data.stTypeCode
                kantor.text = data.office
                id_posisi.text = data.idPosisi
                tgl_penugasan.text = data.tglPenugasan
                tgl_kepulangan.text = data.tglKepulangan
                kota_asal.text = data.kotaAsal
                kota_tujuan.text = data.kotaTujuan
                rincian_tugas.text = data.rincianTugas


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSuratTugasDetail)
    }

}
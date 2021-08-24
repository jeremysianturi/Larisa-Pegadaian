package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import id.co.pegadaian.diarium.entity.DataSuratTugasDetailParticipant
import kotlinx.android.synthetic.main.lay_item_surattugas_detail.view.*
import kotlinx.android.synthetic.main.lay_item_surattugas_detail_participant.view.*

class SuratTugasDetailParticipantAdapter : RecyclerView.Adapter<SuratTugasDetailParticipantAdapter.UserViewHolder>() {


    private var onItemClickCallback : SuratTugasDetailParticipantAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataSuratTugasDetailParticipant>()
    private var mFilter = ArrayList<DataSuratTugasDetailParticipant>()

    fun setOnItemClickCallback(onItemClickCallback: SuratTugasDetailParticipantAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataSuratTugasDetailParticipant>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_surattugas_detail_participant, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataSuratTugasDetailParticipant){
            with(itemView){

                tv_nolist_participant.text = data.noList.toString()
                tv_nama_penerima_participant.text = data.name
                tv_nip_penerima_participant.text = data.pernr


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSuratTugasDetailParticipant)
    }
}
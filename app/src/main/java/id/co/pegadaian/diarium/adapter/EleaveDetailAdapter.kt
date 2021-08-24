package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.controller.home.main_menu.eleave.EleaveDetail
import id.co.pegadaian.diarium.entity.DataEleaveDetail
import id.co.pegadaian.diarium.entity.DataSuratTugasDetail
import kotlinx.android.synthetic.main.lay_item_eleavedetail.view.*
import kotlinx.android.synthetic.main.lay_item_surattugas_detail.view.*

class EleaveDetailAdapter : RecyclerView.Adapter<EleaveDetailAdapter.UserViewHolder>() {

    private var onItemClickCallback : EleaveDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataEleaveDetail>()
    private var mFilter = ArrayList<DataEleaveDetail>()

    fun setOnItemClickCallback(onItemClickCallback: EleaveDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataEleaveDetail>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_eleavedetail, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataEleaveDetail){
            with(itemView){

                name_eleave_detail.text = data.name
                jeniscuti_eleave_detail.text = data.jenisCuti
                lamacuti_eleave_detail.text = data.lamaCuti.toString()
                allowance_eleave_detail.text = data.allowance
                Log.d("EleaveDetailAdapter","check allowance value : ${data.allowance}")


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataEleaveDetail)
    }
}
package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataEleaveDetailApprovalTanggal
import id.co.pegadaian.diarium.entity.DataEleaveDetailTanggal
import kotlinx.android.synthetic.main.lay_item_eleavedetail_tanggal.view.*

class ApprovalEleaveDetailTanggalAdapter : RecyclerView.Adapter<ApprovalEleaveDetailTanggalAdapter.UserViewHolder>(){


    private var onItemClickCallback : ApprovalEleaveDetailTanggalAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataEleaveDetailApprovalTanggal>()
    private var mFilter = ArrayList<DataEleaveDetailApprovalTanggal>()

    fun setOnItemClickCallback(onItemClickCallback: ApprovalEleaveDetailTanggalAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataEleaveDetailApprovalTanggal>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_eleavedetail_tanggal, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataEleaveDetailApprovalTanggal){
            with(itemView){

                tv_nolist_eleavedetail_tanggal.text = data.no
                tv_tgl_eleavedetail_tanggal.text = data.tanggal

                // setting image
                val status = data.status

                Log.d("tag","check status value in eleave list adapter : $status")
                if (status.equals("1")){
                    tv_status_eleavedetail_tanggal.text = "Waiting for approval"
                    iv_status_eleavedetail_tanggal.setImageResource(R.drawable.waiting_surattugas)
                } else if (status.equals("2")){
                    tv_status_eleavedetail_tanggal.text = "Approved"
                    iv_status_eleavedetail_tanggal.setImageResource(R.drawable.approved_surattugas)
                } else if (status.equals("3")){
                    tv_status_eleavedetail_tanggal.text = "Rejected"
                    iv_status_eleavedetail_tanggal.setImageResource(R.drawable.reject_surattugas)
                }


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataEleaveDetailApprovalTanggal)
    }
}
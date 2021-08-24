package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataApprovalEleaveDetail
import kotlinx.android.synthetic.main.lay_item_eleavedetail.view.*

class ApprovalEleaveDetailAdapter : RecyclerView.Adapter<ApprovalEleaveDetailAdapter.UserViewHolder>() {

    private var onItemClickCallback : ApprovalEleaveDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataApprovalEleaveDetail>()
    private var mFilter = ArrayList<DataApprovalEleaveDetail>()

    fun setOnItemClickCallback(onItemClickCallback: ApprovalEleaveDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataApprovalEleaveDetail>){
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

        fun bind(data: DataApprovalEleaveDetail){
            with(itemView){

                name_eleave_detail.text = data.name
                jeniscuti_eleave_detail.text = data.jenisCuti
                lamacuti_eleave_detail.text = data.lamaCuti.toString()
                allowance_eleave_detail.text = data.allowance


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataApprovalEleaveDetail)
    }
}
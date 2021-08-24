package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPresenceList
import id.co.pegadaian.diarium.entity.DataStatusPresenceList

class PresenceListAdapter : RecyclerView.Adapter<PresenceListAdapter.UserViewHolder>() {

    private var onItemClickCallback : PresenceListAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataPresenceList>()
    private var mFilter = ArrayList<DataPresenceList>()

    fun setOnItemClickCallback(onItemClickCallback: PresenceListAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataPresenceList>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_presence_list, parent, false)
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

        fun bind(data: DataPresenceList){
            with(itemView){
                val title = findViewById<TextView>(R.id.tvTitle)
                val value = findViewById<TextView>(R.id.tvValue)
                val status = findViewById<TextView>(R.id.tv_status_presence_conf)
                val date = findViewById<TextView>(R.id.tv_date_presence_conf)
                val image = findViewById<ImageView>(R.id.iv_status_presence_conf)

                title.text = data.name
                value.text = data.absentType
                status.text = data. statusName
                date.text = data.date

                val statusApproval = data.statusName


                Log.d("iyadah","check print di adapter : ${title.text} \n description : ${value.text}")

                if (statusApproval.equals("Approved")){
                    image.setImageResource(R.drawable.approved_surattugas)
                } else if (statusApproval.equals("Waiting Approval")){
                    image.setImageResource(R.drawable.waiting_surattugas)
                } else if (statusApproval.equals("Rejected")){
                    image.setImageResource(R.drawable.reject_surattugas)
                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPresenceList)
    }
}
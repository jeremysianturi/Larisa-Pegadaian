package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPersonal
import id.co.pegadaian.diarium.entity.DataSuratTugas

class SuratTugasAdapter : RecyclerView.Adapter<SuratTugasAdapter.UserViewHolder>() {
    private var onItemClickCallback : SuratTugasAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataSuratTugas>()
    private var mFilter = ArrayList<DataSuratTugas>()

    fun setOnItemClickCallback(onItemClickCallback: SuratTugasAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<DataSuratTugas>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_sppd, parent, false)
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

        fun bind(data: DataSuratTugas){
            with(itemView){
                val title = findViewById<TextView>(R.id.tvTitle)
                val value = findViewById<TextView>(R.id.tvValue)
                val status = findViewById<TextView>(R.id.tv_status_surattugas)
                val date = findViewById<TextView>(R.id.tv_date_surattugas)
                val image = findViewById<ImageView>(R.id.iv_sppd)

                title.text = data.suratTugasNumber
                value.text = data.nameMaker
                date.text = "${data.beginDate} - ${data.endDate}"

                var statusApprovalOne = data.statusApprovalOne
                var statusApprovalTwo = data.statusApprovalTwo

                Log.d("iyadah","check print di adapter : ${title.text} \n description : ${value.text}")

                if (statusApprovalOne.equals("03") || statusApprovalTwo.equals("03")){     // dibalik karna biar kalo ada yang ke reject antara 1 approval, gambar status jadi rejected
                status.text = "Rejected"
                image.setImageResource(R.drawable.reject_surattugas)
                } else if (statusApprovalOne.equals("01") || statusApprovalTwo.equals("01")){
                    status.text = "Waiting for Approval"
                    image.setImageResource(R.drawable.waiting_surattugas)
                } else if (statusApprovalOne.equals("02") && statusApprovalTwo.equals("02")){
                    status.text = "Approved"
                    image.setImageResource(R.drawable.approved_surattugas)
                }

                // for dummy, non valid data
                else {
                    Log.d("SuratTugasAdapter","dummy")
                    status.text = "Waiting for Approval"
                    image.setImageResource(R.drawable.waiting_surattugas)
                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataSuratTugas)
    }
}
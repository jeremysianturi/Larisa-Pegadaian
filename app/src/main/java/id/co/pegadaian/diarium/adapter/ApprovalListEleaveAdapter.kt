package id.co.pegadaian.diarium.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataApprovalEleaveList
import kotlinx.android.synthetic.main.eleave_normal_list.view.*

class ApprovalListEleaveAdapter : RecyclerView.Adapter<ApprovalListEleaveAdapter.UserViewHolder>() {

    private var onItemClickCallback : ApprovalListEleaveAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataApprovalEleaveList>()
    private var mFilter = ArrayList<DataApprovalEleaveList>()
    private val viewPool = RecyclerView.RecycledViewPool()


    fun setOnItemClickCallback(onItemClickCallback: ApprovalListEleaveAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataApprovalEleaveList>){
        Log.d("AdapterListRv", "Check items llist: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_eleave, parent, false)
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.eleave_normal_list, parent, false)   // list gapake lib
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        // yang biasanya
        @SuppressLint("SetTextI18n", "WrongConstant")
        fun bind(data: DataApprovalEleaveList){
            with(itemView){

                val jenisCuti = findViewById<TextView>(R.id.tvTitle_eleave)
                val creator = findViewById<TextView>(R.id.tvValue_eleave)
                val createdDate = findViewById<TextView>(R.id.tv_status_eleave)
                val lamaCuti = findViewById<TextView>(R.id.tv_date_eleave)
//                val recyclerView = findViewById<RecyclerView>(R.id.rv_child)

                jenisCuti.text = data.jenisCuti
                creator.text = "Creator : ${data.creator}"
                createdDate.text = "Created date : ${data.createdDate}"
                lamaCuti.text = "Lama cuti : ${data.lamaCuti.toString()} days"

                // set image status cuti
                val status = data.status
                Log.d("tag","check status value in eleave list adapter : $status")
                if (status.equals("1")){
                    iv_eleave.setImageResource(R.drawable.waiting_surattugas)
                } else if (status.equals("2")){
                    iv_eleave.setImageResource(R.drawable.approved_surattugas)
                } else if (status.equals("3")){
                    iv_eleave.setImageResource(R.drawable.reject_surattugas)
                }

//                val childLayoutManager = LinearLayoutManager(recyclerView.context, LinearLayout.HORIZONTAL, false)
//                recyclerView.apply {
//                    layoutManager = childLayoutManager
//                  adapter = EleaveListChildAdapter(data.children)
//                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }



                // yang dari tutorial
//        val recyclerView : RecyclerView = itemView.rv_child
//        val jenisCuti : TextView = itemView.tvTitle_eleave
//        val creator : TextView = itemView.tvValue_eleave
//        val createdDate : TextView = itemView.tv_status_eleave
//        val lamaCuti : TextView = itemView.tv_date_eleave
//        val imgStatus : ImageView = itemView.iv_eleave



            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataApprovalEleaveList)
    }
}
package id.co.pegadaian.diarium.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.vision.text.Line
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataEleaveList
import kotlinx.android.synthetic.main.lay_item_eleave.view.*
import org.w3c.dom.Text

class EleaveListAdapter : RecyclerView.Adapter<EleaveListAdapter.UserViewHolder>() {

    private var onItemClickCallback : EleaveListAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataEleaveList>()
    private var mFilter = ArrayList<DataEleaveList>()
    private val viewPool = RecyclerView.RecycledViewPool()


    fun setOnItemClickCallback(onItemClickCallback: EleaveListAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataEleaveList>){
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
        // yang biasanya
        holder.bind(mFilter[position])

        // yang dari tutorial
//        val parent = mFilter[position]
//        holder.jenisCuti.text = parent.jenisCuti
//        holder.creator.text = parent.creator
//        holder.createdDate.text = parent.createdDate
//        holder.lamaCuti.text = parent.lamaCuti.toString()
//
//        // setting image
//        val status = parent.status
//        Log.d("tag","check status value in eleave list adapter : $status")
//        if (status.equals("1")){
//            holder.imgStatus.setImageResource(R.drawable.waiting_surattugas)
//        } else if (status.equals("2")){
//            holder.imgStatus.setImageResource(R.drawable.approved_surattugas)
//        } else if (status.equals("3")){
//            holder.imgStatus.setImageResource(R.drawable.reject_surattugas)
//        }
//
//        val childLayoutManager = LinearLayoutManager(holder.recyclerView.context,LinearLayoutManager.HORIZONTAL,false)
////        childLayoutManager.initialPrefetchItemCount  = 1000
//
//
//        holder.recyclerView.apply {
//            layoutManager = childLayoutManager
////            layoutManager = LinearLayoutManager(holder.recyclerView.context, LinearLayoutManager.HORIZONTAL ,false)
//            adapter = EleaveListChildAdapter(parent.children)
//            setRecycledViewPool(viewPool)
//        }

    }

    override fun getItemCount(): Int {
        Log.d("Parent adapter eleave", "check size getItemCount : ${mFilter.size}")
        return mFilter.size
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


        // yang biasanya
        @SuppressLint("SetTextI18n", "WrongConstant")
        fun bind(data: DataEleaveList){
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

                // setting image
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
        fun onItemClicked(data: DataEleaveList)
    }

}
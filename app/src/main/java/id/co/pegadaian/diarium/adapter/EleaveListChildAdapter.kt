package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataChildEleaveList
import id.co.pegadaian.diarium.entity.DataSuratTugas
import kotlinx.android.synthetic.main.child_lay_item_eleave.view.*


//class EleaveListChildAdapter : RecyclerView.Adapter<EleaveListChildAdapter.UserViewHolder>(){
//
//    private var onItemClickCallback : EleaveListChildAdapter.OnItemClickCallback? = null
//    private val mData = ArrayList<DataChildEleaveList>()
//    private var mFilter = ArrayList<DataChildEleaveList>()
//
//    fun setOnItemClickCallback(onItemClickCallback: EleaveListChildAdapter.OnItemClickCallback){
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    fun setData(items: ArrayList<DataChildEleaveList>){
//        Log.d("AdapterListRv", "Check items child: $items")
//        mData.clear()
//        mFilter.clear()
//        mData.addAll(items)
//        mFilter.addAll(items)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val mView = LayoutInflater.from(parent.context).inflate(R.layout.child_lay_item_eleave, parent, false)
//        return UserViewHolder(mView)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        holder.bind(mFilter[position])
//    }
//
//    override fun getItemCount(): Int {
//        return mFilter.size
//    }
//
//
//    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//
//        fun bind(data: DataChildEleaveList){
//            with(itemView){
//
//                val title = findViewById<TextView>(R.id.child_textView)
//                val tglCuti = findViewById<TextView>(R.id.child_tglcuti)
//                val tvStatus = findViewById<TextView>(R.id.tv_child_status)
//
//                title.text = data.title
//                tglCuti.text = data.tanggalCuti
//
//
//                itemView.setOnClickListener{
//                    onItemClickCallback?.onItemClicked(data)
//
//                }
//            }
//        }
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(data: DataChildEleaveList)
//    }
//}

class EleaveListChildAdapter (private val children : List< DataChildEleaveList>)
    : RecyclerView.Adapter<EleaveListChildAdapter.UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EleaveListChildAdapter.UserViewHolder {

        val v =  LayoutInflater.from(parent.context)
                .inflate(R.layout.child_lay_item_eleave,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: EleaveListChildAdapter.UserViewHolder, position: Int) {
        val child = children[position]
        holder.textView.text = child.title
        holder.textview2.text = child.tanggalCuti
    }

    override fun getItemCount(): Int {
        return children.size
    }

    inner class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val textView : TextView = itemView.child_textView
        val textview2 : TextView = itemView.child_tglcuti

    }
}
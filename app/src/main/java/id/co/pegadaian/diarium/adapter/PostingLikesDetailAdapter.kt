package id.co.pegadaian.diarium.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPostingLikesDetail

class PostingLikesDetailAdapter : RecyclerView.Adapter<PostingLikesDetailAdapter.UserViewHolder>() {

    private var onItemClickCallback : PostingLikesDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataPostingLikesDetail>()
    private var mFilter = ArrayList<DataPostingLikesDetail>()

    fun setOnItemClickCallback(onItemClickCallback: PostingLikesDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<DataPostingLikesDetail>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.likes_detail_layout, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("yayaya", "sizenya likes detail : ${mFilter.size}")
        return mFilter.size
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(data: DataPostingLikesDetail){
            with(itemView){

                // liker full name
                val name = findViewById<TextView>(R.id.fullname_likes_detail)
                name.text = data.name


                // liker avatar
                val avatar = findViewById<CircleImageView>(R.id.avatar_likes_detail)
                try {
                    Picasso.get().load(data.avatar).error(R.drawable.profile).into(avatar)
                } catch (e: Exception) {
                    Picasso.get().load(R.drawable.profile).into(avatar)
                }

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPostingLikesDetail)
    }
}
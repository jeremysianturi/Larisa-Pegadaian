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
import id.co.pegadaian.diarium.entity.DataPostingCommentDetail
import id.co.pegadaian.diarium.entity.DataPostingLikesDetail
import org.w3c.dom.Text

class PostingCommentDetailAdapter : RecyclerView.Adapter<PostingCommentDetailAdapter.UserViewHolder>() {

    private var onItemClickCallback : PostingCommentDetailAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataPostingCommentDetail>()
    private var mFilter = ArrayList<DataPostingCommentDetail>()

    fun setOnItemClickCallback(onItemClickCallback: PostingCommentDetailAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    fun setData(items: ArrayList<DataPostingCommentDetail>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostingCommentDetailAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.comments_detail_layout, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: PostingCommentDetailAdapter.UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("yayaya", "sizenya comments detail : ${mFilter.size}")
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(data: DataPostingCommentDetail){
            with(itemView){

                // full name
                val name = findViewById<TextView>(R.id.fullname_comment_detail)
                name.text = data.name


                // avatar
                val avatar = findViewById<CircleImageView>(R.id.avatar_comment_detail)
                try {
                    Picasso.get().load(data.avatar).error(R.drawable.profile).into(avatar)
                } catch (e: Exception) {
                    Picasso.get().load(R.drawable.profile).into(avatar)
                }


                // comment
                val comment = findViewById<TextView>(R.id.tv_comment_detail)
                comment.text = data.comment


                // date and time
                val date = findViewById<TextView>(R.id.tv_date_comment_detail)
                date.text = "${data.date} at ${data.time}"


                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPostingCommentDetail)
    }
}
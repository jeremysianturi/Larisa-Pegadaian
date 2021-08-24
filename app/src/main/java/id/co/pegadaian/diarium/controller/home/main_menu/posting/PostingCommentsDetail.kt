package id.co.pegadaian.diarium.controller.home.main_menu.posting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.adapter.PostingCommentDetailAdapter
import id.co.pegadaian.diarium.adapter.PostingLikesDetailAdapter
import id.co.pegadaian.diarium.model.PostingCommentDetailModel
import id.co.pegadaian.diarium.model.PostingLikesDetailModel
import id.co.pegadaian.diarium.util.UserSessionManager
import kotlinx.android.synthetic.main.activity_posting_comments_detail.*
import kotlinx.android.synthetic.main.activity_posting_likes_detail.*
import kotlinx.android.synthetic.main.activity_posting_likes_detail.no_data_likes_detail
import kotlinx.android.synthetic.main.activity_posting_likes_detail.rv_posting_like_detail

class PostingCommentsDetail : AppCompatActivity() {

    private val tags = PostingCommentsDetail::class.java.simpleName

    // initialize
    private lateinit var viewModel : PostingCommentDetailModel
    private lateinit var adapter : PostingCommentDetailAdapter
    private lateinit var session : UserSessionManager

    private var postingIdFromIntent : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting_comments_detail)

        session = UserSessionManager(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle("Comment Detail")
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[PostingCommentDetailModel::class.java]

        // getting posting id from intent
        postingIdFromIntent = intent.getStringExtra("posting_id")


        // show loading
        showLoading(true)

        // get comment detail
        getCommentsDetail()
        showList()
    }


    private fun getCommentsDetail(){

        val baseurl = session.serverURL
        val token = session.token
        val buscd = session.userBusinessCode
        val postingId = postingIdFromIntent

        viewModel.setData(baseurl,token,buscd,postingId)

        viewModel.getData().observe(this, Observer {model ->
            Log.d(tags,"check model value : $model")
            if (model.isEmpty()){
                showingNoData()
                showLoading(false)
            } else {
                Log.d(tags,"test getAssignmentLetter")
                if (model[0].intResponse == 200){
                    adapter.setData(model)
                    showingData()
                } else {

                }
                showLoading(false)
            }
        })

        viewModel.getData()

    }

    private fun showList(){

        adapter = PostingCommentDetailAdapter()
        rv_posting_comment_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_posting_comment_detail.adapter = adapter

//        adapter.setOnItemClickCallback(object : PostingLikesDetailAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: DataPostingLikesDetail) {
//                Log.d(tags, "data value at onClick list di likes detail : $data")
//                val sppdNumber = data.sppdNumber
//
//                popupMenuActivity(sppdNumber)
//            }
//        })
    }

    private fun showingNoData (){
        rv_posting_comment_detail.visibility = View.INVISIBLE
        no_data_comment_detail.visibility = View.VISIBLE
    }

    private fun showingData () {
        rv_posting_comment_detail.visibility = View.VISIBLE
        no_data_comment_detail.visibility = View.GONE
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar_comment_detail.visibility = View.VISIBLE
        } else {
            progressBar_comment_detail.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
        finish()
        return true
    }

}
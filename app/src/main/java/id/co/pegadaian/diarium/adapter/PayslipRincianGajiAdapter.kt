package id.co.pegadaian.diarium.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pegadaian.diarium.R
import id.co.pegadaian.diarium.entity.DataPayslipRincianGaji
import kotlinx.android.synthetic.main.lay_item_payslip_rinciangaji.view.*
import kotlinx.android.synthetic.main.lay_item_surattugas_detail.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PayslipRincianGajiAdapter : RecyclerView.Adapter<PayslipRincianGajiAdapter.UserViewHolder>() {

    private var onItemClickCallback : PayslipRincianGajiAdapter.OnItemClickCallback? = null
    private val mData = ArrayList<DataPayslipRincianGaji>()
    private var mFilter = ArrayList<DataPayslipRincianGaji>()

    fun setOnItemClickCallback(onItemClickCallback: PayslipRincianGajiAdapter.OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<DataPayslipRincianGaji>){
        Log.d("AdapterListRv", "Check items: $items")
        mData.clear()
        mFilter.clear()
        mData.addAll(items)
        mFilter.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.lay_item_payslip_rinciangaji, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mFilter[position])
    }

    override fun getItemCount(): Int {
        Log.d("tag", " check m filter . size penghasilan : ${mFilter.size}")
        return mFilter.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(data: DataPayslipRincianGaji){
            with(itemView){

                title_payslip1.text = data.wageType


                // set text format rupiah
                val localeID = Locale("in", "ID")
                val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
                value_payslip1.text = formatRupiah.format(data.value?.toDouble())

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(data)

                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataPayslipRincianGaji)
    }
}
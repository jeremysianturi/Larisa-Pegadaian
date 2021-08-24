package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataChildEleaveList (
        val title : String?,
        val tanggalCuti : String?
        ) : Parcelable
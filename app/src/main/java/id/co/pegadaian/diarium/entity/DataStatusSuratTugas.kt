package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataStatusSuratTugas (
        val intResponse : Int,
        val nameStatus : String,
        val codeStatus : String
        ) : Parcelable
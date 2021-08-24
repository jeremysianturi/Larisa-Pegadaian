package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataStatusPresenceList (
        val intResponse : Int?,
        val code : String,
        val name : String
) : Parcelable
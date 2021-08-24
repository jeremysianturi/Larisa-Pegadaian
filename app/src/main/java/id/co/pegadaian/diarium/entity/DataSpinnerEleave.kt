package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSpinnerEleave (
        val intResponse : Int?,
        val typeName : String,
        val typeCode : String
        ) : Parcelable
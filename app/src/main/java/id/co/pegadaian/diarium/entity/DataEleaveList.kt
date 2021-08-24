package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataEleaveList (
        val intResponse : Int,
        val objIdentifier : Int?,
        val approvalCompletion : String?,
        val status : String?,
        val jenisCuti : String?,
        val creator : String?,
        val createdDate : String?,
        val lamaCuti : Int?,
        val children : List<DataChildEleaveList>
        ) : Parcelable
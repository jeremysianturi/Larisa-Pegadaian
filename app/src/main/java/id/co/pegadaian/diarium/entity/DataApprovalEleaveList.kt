package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataApprovalEleaveList (
        val intResponse : Int,
        val objIdentifier : Int?,
        val approvable : String?,
        val status : String?,
        val jenisCuti : String?,
        val creator : String?,
        val createdDate : String?,
        val lamaCuti : Int?,
        val children : List<DataChildEleaveList>
        ) : Parcelable
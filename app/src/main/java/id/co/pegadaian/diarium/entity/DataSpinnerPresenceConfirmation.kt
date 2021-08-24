package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSpinnerPresenceConfirmation (
        val intResponse : Int?,
        val code : String,
        val typeName : String
        ) : Parcelable
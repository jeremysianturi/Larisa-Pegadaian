package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSpinnerAddDataDistrict (
        val dataName : String?,
        val dataCode : String?
) : Parcelable
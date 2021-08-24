package id.co.pegadaian.diarium.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class DataSpinnerAddDataFaculty (
        val dataName : String?,
        val dataCode : String?
) : Parcelable
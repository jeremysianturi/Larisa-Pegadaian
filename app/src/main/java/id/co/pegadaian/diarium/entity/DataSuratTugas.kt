package id.co.pegadaian.diarium.entity

import android.os.Parcelable

data class DataSuratTugas (
        val intResponse : Int?, // pake
        val objIdentifierSTNumber : String?, // pake
        val suratTugasNumber : String?, // pake
        val beginDate : String?, // pake
        val endDate : String?, // pake
        val pernrMaker : String?,
        val nameMaker : String?, // pake
        val pernrSigner : String?,
        val nameSigner : String?,
        val statusApprovalOne : String?,
        val statusApprovalTwo : String?,
        val objIdentifierForApproval : String?
)
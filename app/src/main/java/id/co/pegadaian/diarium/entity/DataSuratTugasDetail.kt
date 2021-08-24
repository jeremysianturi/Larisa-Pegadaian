package id.co.pegadaian.diarium.entity

data class DataSuratTugasDetail (
        val intResponse : Int?,
        val objIdentifier : String?,
        val personalNumber : String?,
        val suratTugasNumber : String?,
        val stTypeCode : String?,
//        val stTypeName : String?,
        val office : String?,
        val idPosisi : String?,
        val tglPenugasan : String?,
        val tglKepulangan : String?,
        val kotaAsal : String?,
        val kotaTujuan : String?,
        val rincianTugas : String?
        )
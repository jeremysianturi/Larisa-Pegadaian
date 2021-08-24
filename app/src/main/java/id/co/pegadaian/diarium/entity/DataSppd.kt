package id.co.pegadaian.diarium.entity

data class DataSppd(
        val intResponse: Int?,
        val sppdNumber : String?,
        val tipePerjalanan : String?,
        val kotaAsal : String?,
        val kotaTujuan : String?,
        val tglPenugasan : String?,
        val tglKembali : String?,
)

// data sppd yang lama

//        val objIdentifier: Int?,
//        val evidence: String?,
//        val suratTugasType: String?,
//        val suratTugasNumber: String?,
//        val suratTugasStartDate: String?,
//        val suratTugasEndDate: String?,
//        val transportation: String?,
//        val distance: String,
//        val description: String?,
//        val tipePerjalanan: String?,
//        val objIdentifierBiaya: String,
//        val sppdNumberBiaya: String?,
//        val accomodationTypeBiaya: String?,
//        val nominalBiaya: String,
//        val quantityBiaya: String,
//        val precentageBiaya: String,
//        val totalBiaya: String
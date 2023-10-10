package com.enfotrix.cgs_principal.Models

import com.enfotrix.cgs_principal.ModelItem

data class ClassModel @JvmOverloads constructor(
    var AdmissionFee: String? = null,
    var ClassFee: String? = null,
    var ClassName: String = "", // Not nullable
    var HostelFee: String? = null,
    var Id: String = "", // Not nullable
    var LabFee: String? = null,
    var LibraryFee: String? = null,
    var MagazineFee: String? = null,
    var MedicalFee: String? = null,
    var MedicalHostelFee: String? = null,
    var StationaryFee: String? = null
)

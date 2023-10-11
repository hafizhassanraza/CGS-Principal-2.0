package com.enfotrix.cgs_principal.Models

import com.google.firebase.Timestamp

data class AttendenceModel @JvmOverloads constructor(
    var Date: String? = null,
    var Id: String = "", // Not nullable
    var SectionID: String = "", // Not nullable
    var Status: String? = null,
    var StudentID: String? = null,
    val createdAt: Timestamp = Timestamp.now() // Creation timestamp
)

package com.enfotrix.cgs_principal.Models

import com.google.firebase.Timestamp

data class StudentRemarksModel(
    var studentId: String = "",
    var date: String = "",
    var createdAt: Timestamp = Timestamp.now(),
    var id: String = "",
    var remarks: String = "",
    var remarkedBy: String = "",
    var remarkStatus: String = "",
)

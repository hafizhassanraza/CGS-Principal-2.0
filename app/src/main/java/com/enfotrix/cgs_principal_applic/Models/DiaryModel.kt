package com.enfotrix.cgs_principal_applic.Models

data class DiaryModel @JvmOverloads constructor(
    var sectionID: String? = null,
    var classID: String? = null,
    var subjectID: String? = null,
    var docID: String? = null,
    var dueDate: String? = null,
    var img: String? = null,
    var diaryTitle: String? = null      ,
    var diaryContent: String? = null,
    var diaryMarks: String? = null,

){
}
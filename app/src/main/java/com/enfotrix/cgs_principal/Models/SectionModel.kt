package com.enfotrix.cgs_principal.Models

import com.enfotrix.cgs_principal.ModelItem

class SectionModel @JvmOverloads constructor(

    var ClassID: String? = "",
    var ClassName: String = "",
    var ID: String = "", // Not nullable
    var Medium: String? = "",
    var Password: String = "", // Not nullable
    var SectionName: String? = "",

)
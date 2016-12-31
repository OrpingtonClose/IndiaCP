package com.barclays.indiacp.cordapp.dto

import java.time.Instant
import java.util.*

/**
 * Created by chaos on 22/12/16.
 */


data class OrgLevelProgramJSON
(
    val issuer: String = "",

    val org_id: String,

    val name: String = "",

    val purpose: String = "",

    val commencement_date: Date = Date(),

    val borrowing_limit: Double = 0.0,

    val borrowed_value: Double = 0.0,

    val program_currency: String = "",

    val status : String = "",

    val user_id: String = "",

    val last_modified : Date = Date(),

    val version: Integer = Integer(0)
)

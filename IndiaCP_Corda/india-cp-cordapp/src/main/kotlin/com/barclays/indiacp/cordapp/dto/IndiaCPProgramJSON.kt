package com.barclays.indiacp.cordapp.dto

import java.time.Instant
import java.util.*

/**
 * Created by chaos on 22/12/16.
 */


data class IndiaCPProgramJSON
(
    val issuer: String,

    val ipa: String,

    val depository: String,

    val program_id: String,

    val name: String,

    val type: String,

    val purpose: String,

    val issuer_id: String,

    val issuer_name: String,

    val issue_commencement_date: Date,

    val program_size: Double,

    val program_allocated_value: Double,

    val program_currency: String,

    val maturity_days: Date,

    val ipa_id: String,

    val ipa_name: String,

    val depository_id: String,

    val depository_name: String,

    val isin_generation_request_doc_id: String,

    val ipa_verification_request_doc_id: String,

    val ipa_certificate_doc_id: String,

    val corporate_action_form_doc_id: String,

    val allotment_letter_doc_id: String,

    val status : String,

    val last_modified : Date,

    val version: Integer
)
package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * An object used to fully qualify the [IndiaCommercialPaperProgramSchema] family name (i.e. independent of version).
 */
object IndiaCommercialPaperProgramSchema


/**
 * First version of a India commercial paper contract ORM schema that maps all fields of the [IndiaCommercialPaperProgramSchema] contract state
 * as it stood at the time of writing.
 */
object IndiaCommercialPaperProgramSchemaV1 : MappedSchema(schemaFamily = IndiaCommercialPaperProgramSchema.javaClass, version = 1, mappedTypes = listOf(PersistentIndiaCommericalPaperProgramState::class.java)) {
    @Entity
    @Table(name = "indiacp_program_states")
    class PersistentIndiaCommericalPaperProgramState(

            @Column(name = "issuance_key")
            var issuanceParty: String,

            @Column(name = "ipa_key")
            var ipaParty: String,

            @Column(name = "depository_key")
            var depositoryParty: String,

            @Column(name = "program_id")
            var program_id: String,

            @Column(name = "name")
            var name: String,

            @Column(name = "type")
            var type: String,

    @Column(name = "purpose")
            var purpose: String,

    @Column(name = "issuer_id")
            var issuer_id: String,

    @Column(name = "issuer_name")
            var issuer_name: String,

    @Column(name = "issue_commencement_date")
            var issue_commencement_date: Instant,

    @Column(name = "program_size")
            var program_size: Double,

    @Column(name = "program_allocated_value")
            var program_allocated_value: Double,

    @Column(name = "program_currency")
            var program_currency: String,

    @Column(name = "maturity_days")
            var maturity_days: Instant,

    @Column(name = "ipa_id")
            var ipa_id: String,

    @Column(name = "ipa_name")
            var ipa_name: String,

    @Column(name = "depository_id")
            var depository_id: String,

    @Column(name = "depository_name")
            var depository_name: String,

    @Column(name = "isin_generation_request_doc_id")
            var isin_generation_request_doc_id: String,

    @Column(name = "ipa_verification_request_doc_id")
            var ipa_verification_request_doc_id: String,

    @Column(name = "ipa_certificate_doc_id")
            var ipa_certificate_doc_id: String,

    @Column(name = "corporate_action_form_doc_id")
            var corporate_action_form_doc_id: String,

    @Column(name = "allotment_letter_doc_id")
            var allotment_letter_doc_id: String,

     @Column(name = "last_modified")
            var last_modified: Instant,

    @Column(name = "version", nullable=true)
    var version: Integer

    ) : PersistentState()
}

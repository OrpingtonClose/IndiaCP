package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * An object used to fully qualify the [IndiaCommercialPaperSchema] family name (i.e. independent of version).
 */
object IndiaCommercialPaperSchema


/**
 * First version of a India commercial paper contract ORM schema that maps all fields of the [IndiaCommercialPaper] contract state
 * as it stood at the time of writing.
 */
object IndiaCommercialPaperSchemaV1 : MappedSchema(schemaFamily = IndiaCommercialPaperSchema.javaClass, version = 1, mappedTypes = listOf(PersistentIndiaCommericalPaperState::class.java)) {
    @Entity
    @Table(name = "indiacp_states")
    class PersistentIndiaCommericalPaperState(
            @Column(name = "issuance_key")
            var issuanceParty: String,

            @Column(name = "issuance_ref")
            var issuanceRef: ByteArray,

            @Column(name = "owner_key")
            var owner: String,

            @Column(name = "maturity_instant")
            var maturity: Instant,

            @Column(name = "face_value")
            var faceValue: Long,

            @Column(name = "ccy_code", length = 3)
            var currency: String,

            @Column(name = "face_value_issuer_key")
            var faceValueIssuerParty: String,

            @Column(name = "face_value_issuer_ref")
            var faceValueIssuerRef: ByteArray,

            @Column(name = "version", nullable=true)
            var version: Integer,

            @Column(name = "isin", nullable=true)
            var isin: String

    ) : PersistentState()
}

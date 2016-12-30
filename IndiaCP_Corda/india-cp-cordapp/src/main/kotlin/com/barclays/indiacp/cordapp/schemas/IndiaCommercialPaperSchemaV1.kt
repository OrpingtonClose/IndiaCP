package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import java.util.*
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

            @Column(name = "beneficiary_key")
            var beneficiaryParty: String,

            @Column(name = "ipa_key")
            var ipaParty: String,

            @Column(name = "depository_key")
            var depositoryParty: String,

            @Column(name = "face_value")
            var faceValue: Long,

            @Column(name = "ccy_code", length = 3)
            var currency: String,

            @Column(name = "maturity_date")
            var maturity: Date,

            @Column(name = "cp_program_id")
            var cpProgramID: String,

            @Column(name = "cp_trade_id")
            var cpTradeID: String,

            @Column(name = "trade_date")
            var tradeDate: Date,

            @Column(name = "value_date")
            var valueDate: Date,

            @Column(name = "isin", nullable=true)
            var isin: String,

            @Column(name = "version", nullable=true)
            var version: Integer,

            @Column(name = "hash_deal_confirmation_doc", nullable=true)
            var hashDealConfirmationDoc: String?

    ) : PersistentState()
}

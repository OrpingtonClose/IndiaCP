package com.barclays.indiacp.cordapp.schemas

import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.model.SettlementDetails
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.FetchType
import org.hibernate.annotations.Cascade

/**
 * An object used to fully qualify the [IndiaCommercialPaperSchema] family name (i.e. independent of version).
 */
object IndiaCommercialPaperSchema


/**
 * First version of a India commercial paper contract ORM schema that maps all fields of the [IndiaCommercialPaper] contract state
 * as it stood at the time of writing.
 */
object IndiaCommercialPaperSchemaV1 : MappedSchema(schemaFamily = IndiaCommercialPaperSchema.javaClass, version = 1, mappedTypes = listOf(PersistentIndiaCommericalPaperState::class.java, PersistentSettlementSchemaState::class.java, PersistentDepositoryAccountSchemaState::class.java)) {
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

            @Column(name = "maturity_instant")
            var maturity: Instant,

            @Column(name = "cp_program_id")
            var cpProgramID: String,

            @Column(name = "cp_trade_id")
            var cpTradeID: String,

            @Column(name = "trade_date")
            var tradeDate: Instant,

            @Column(name = "value_date")
            var valueDate: Instant,

            @Column(name = "face_value_per_unit")
            var faceValuePerUnit: Int,

            @Column(name = "no_of_units")
            var noOfunits: Int,

            @Column(name = "yield")
            var yieldOnMaturity: Float,

            @Column(name = "isin", nullable=true)
            var isin: String,

            @Column(name = "modified_by")
            var modified_by: String,

            @Column(name = "last_modified")
            var last_modified: Instant,

            @Column(name = "status")
            var status: String,

            @Column(name = "version", nullable=true)
            var version: Int,

            @Column(name = "hash_deal_confirmation_doc", nullable=true)
            var hashDealConfirmationDoc: String?,

            @OneToMany(fetch = FetchType.LAZY, mappedBy = "cpDetails", cascade = arrayOf(javax.persistence.CascadeType.ALL))
            var settlementDetails: List<PersistentSettlementSchemaState>?

    ) : PersistentState()
}

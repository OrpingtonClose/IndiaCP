package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created for Barclays Mumbai Rise Accelerator Demo. This is intended to be a proof of concept of how the primary
 * market flows of India Commercial Paper asset class can be managed by using Smart Contracts on R3 Corda Release-M6.0
 *
 * An object used to fully qualify the [CreditRatingSchema] family name (i.e. independent of version).
 * Created by ritukedia on 07/01/17.
 */
object CreditRatingSchema


/**
 * First version of a Credit Rating contract ORM schema that maps all fields of the [CreditRating] contract state
 * as it stood at the time of writing.
 */
object CreditRatingSchemaV1 : MappedSchema(schemaFamily = CreditRatingSchema.javaClass, version = 1, mappedTypes = listOf(PersistentCreditRatingState::class.java)) {
    @Entity
    @Table(name = "credit_rating_states")
    class PersistentCreditRatingState(
            @Column(name = "issuance_key")
            var issuanceParty: String,

            @Column(name = "owner")
            var owner: String,

            @Column(name = "credit_rating_agency_name")
            var creditRatingAgencyName: String,

            @Column(name = "credit_rating_issuance_date")
            var creditRatingIssuanceDate: Date,

            @Column(name = "credit_rating_effective_date")
            var creditRatingEffectiveDate: Date,

            @Column(name = "credit_rating_expiry_date")
            var creditRatingExpiryDate: Date,

            @Column(name = "credit_rating_amount")
            var creditRatingAmount: Integer,

            @Column(name = "credit_rating")
            var creditRating: String,

            @Column(name = "modifiedBy")
            var modifiedBy: String,

            @Column(name = "last_modified_date")
            var lastModifiedDate: Date,

            @Column(name = "version")
            var version: Integer,

            @Column(name = "status")
            var status: String
    ) : PersistentState()
}

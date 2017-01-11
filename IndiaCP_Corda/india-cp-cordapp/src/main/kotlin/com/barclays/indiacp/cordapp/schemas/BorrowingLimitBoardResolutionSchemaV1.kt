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
 * An object used to fully qualify the [BorrowingLimitBoardResolutionSchema] family name (i.e. independent of version).
 * Created by ritukedia on 07/01/17.
 */
object BorrowingLimitBoardResolutionSchema


/**
 * First version of a Borrowing Limit Board Resolution contract ORM schema that maps all fields of the [BorrowingLimitBoardResolutionSchema] contract state
 * as it stood at the time of writing.
 */
object BorrowingLimitBoardResolutionSchemaV1 : MappedSchema(schemaFamily = BorrowingLimitBoardResolutionSchema.javaClass, version = 1, mappedTypes = listOf(PersistentBorrowingLimitBRState::class.java)) {
    @Entity
    @Table(name = "borrowing_limit_board_resolution_states")
    class PersistentBorrowingLimitBRState(
            @Column(name = "issuance_key")
            var issuanceParty: String,

            @Column(name = "owner")
            var owner: String,

            @Column(name = "board_resolution_borrowing_limit")
            var boardResolutionBorrowingLimit: Long,

            @Column(name = "current_outstanding_credit_borrowing")
            var currentOutstandingCreditBorrowing: Long,

            @Column(name = "board_resolution_issuance_date")
            var boardResolutionIssuanceDate: Date,

            @Column(name = "board_resolution_expiry_date")
            var boardResolutionExpiryDate: Date,

            @Column(name = "modifiedBy")
            var modifiedBy: String,

            @Column(name = "last_modified_date")
            var lastModifiedDate: Date,

            @Column(name = "version")
            var version: Int,

            @Column(name = "status")
            var status: String
    ) : PersistentState()
}

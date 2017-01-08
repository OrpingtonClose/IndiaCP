package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * An object used to fully qualify the [OrgLevelBorrowerSchema] family name (i.e. independent of version).
 */
object OrgLevelBorrowerSchema


/**
 * First version of a Org Level Borrowing contract ORM schema that maps all fields of the [OrgLevelBorrowerSchema] contract state
 * as it stood at the time of writing.
 * This Contract will basically keep track over all CP Programs
 *
 */
object OrgLevelBorrowerSchemaV1 : MappedSchema(schemaFamily = OrgLevelBorrowerSchema.javaClass, version = 1, mappedTypes = listOf(PersistentOrgLevelBorrowerSchemaState::class.java)) {
    @Entity
    @Table(name = "org_level_borrowing_states")
    class PersistentOrgLevelBorrowerSchemaState(

            @Column(name = "issuance_key")
            var issuanceParty: String,

            @Column(name = "org_id")
            var orgId: String,

            @Column(name = "name")
            var name: String,

            @Column(name = "purpose")
            var purpose: String,

            @Column(name = "commencement_date")
            var commencementDate: Instant,

            @Column(name = "borrowing_limit")
            var borrowingLimit: Double,

            @Column(name = "borrowed_value")
            var borrowedValue: Double,

            @Column(name = "program_currency")
            var programCurrency: String,

            @Column(name = "status")
            var status: String,

            @Column(name = "modifiedBy")
            var modifiedBy: String,

            @Column(name = "lastModified")
            var lastModified: Instant,

            @Column(name = "version", nullable=true)
            var version: Integer

    ) : PersistentState()
}

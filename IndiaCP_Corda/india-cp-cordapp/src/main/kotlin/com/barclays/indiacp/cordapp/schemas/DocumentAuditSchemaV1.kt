package com.barclays.indiacp.cordapp.schemas

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * An object used to fully qualify the [DocumentSchema] family name (i.e. independent of version).
 */
object DocumentAuditSchema


/**
 * First version of a India commercial paper contract ORM schema that maps all fields of the [DocumentSchema] contract state
 * as it stood at the time of writing.
 * This will basically keep the history of document for any type of CP Program or CP.
 * Hopefully, we will be able to make this generic.
 */
object DocumentAuditSchemaV1 : MappedSchema(schemaFamily = DocumentAuditSchema.javaClass, version = 1, mappedTypes = listOf(PersistentDocumentAuditSchemaState::class.java)) {
    @Entity
    @Table(name = "document_audit_states")
    class PersistentDocumentAuditSchemaState(

            @Column(name = "cp_program_id")
            var cpProgramID: String,

            @Column(name = "cp_trade_id")
            var cpTradeID: String,

            @Column(name = "docType")
            var docType: String,

            @Column(name = "docSubType")
            var docSubType: String,

            @Column(name = "docHash")
            var docHash: String,

            @Column(name = "doc_status")
            var doc_status: String,

            @Column(name = "modifiedBy")
            var modifiedBy: String,

            @Column(name = "lastModified")
            var lastModified: Instant

    ) : PersistentState()
}

package com.barclays.indiacp.cordapp.schemas

import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType
import javax.persistence.*

@Entity
@Table(name = "settlement_details_states")
class PersistentSettlementSchemaState(

        @Id
        @GeneratedValue
        @Column(name = "settlement_id")
        var id: Long? = null,

        @Column(name = "party_type")
        var party_type: String,

        @Column(name = "creditorName")
        var creditorName: String?,

        @Column(name = "bankAccountDetails")
        var bankAccountDetails: String?,

        @Column(name = "bankName")
        var bankName: String?,

        @Column(name = "rtgsCode")
        var rtgsCode: String?,

        @ManyToOne (fetch = FetchType.LAZY)
        @JoinColumns(JoinColumn(referencedColumnName="transaction_id"), JoinColumn(referencedColumnName = "output_index"))
        var cpDetails: IndiaCommercialPaperSchemaV1.PersistentIndiaCommericalPaperState? = null,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "settlementDetails", cascade = arrayOf(javax.persistence.CascadeType.ALL))
        var depositoryAccounts: List<PersistentDepositoryAccountSchemaState>?
)
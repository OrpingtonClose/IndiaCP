package com.barclays.indiacp.cordapp.schemas

import javax.persistence.*

@Entity
@Table(name = "settlement_details_states")
class PersistentSettlementSchemaState(

        @Id
        @GeneratedValue
        @Column(name = "settlement_id")
        var id: Long?,

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
        @JoinColumn(referencedColumnName = "cp_trade_id")
        var cpDetails: IndiaCommercialPaperSchemaV1.PersistentIndiaCommericalPaperState?,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "settlementDetails", cascade = arrayOf(CascadeType.PERSIST))
        var depositoryAccounts: List<PersistentDepositoryAccountSchemaState>?
)
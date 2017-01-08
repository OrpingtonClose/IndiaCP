package com.barclays.indiacp.cordapp.schemas

import org.hibernate.annotations.Cascade
import javax.persistence.*

@Entity
@Table(name = "depository_account_states")
class PersistentDepositoryAccountSchemaState(

        @Id
        @GeneratedValue
        @Column(name = "id")
        var id: Long? = null,

        @Column(name = "dpID")
        var dpID: String?,

        @Column(name = "dpName")
        var dpName: String?,

        @Column(name = "dpType")
        var dpType: String?,

        @Column(name = "clientId")
        var clientId: String?,

        @ManyToOne (fetch = FetchType.LAZY)
        @JoinColumn(referencedColumnName = "settlement_id")
        var settlementDetails: PersistentSettlementSchemaState? = null

)
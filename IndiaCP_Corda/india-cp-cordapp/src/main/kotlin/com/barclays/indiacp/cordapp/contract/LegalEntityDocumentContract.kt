package com.barclays.indiacp.cordapp.contract

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.crypto.Party

/**
 * Created by ritukedia on 09/01/17.
 */
interface LegalEntityDocumentContract {

    interface Commands : CommandData {
        class Issue : TypeOnlyCommandData()
        class Amend : TypeOnlyCommandData()
        class Cancel : TypeOnlyCommandData()
    }

}

interface LegalEntityDocumentOwnableState {

    abstract val docHash: String
    abstract val issuer: Party

}
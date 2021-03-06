package com.barclays.indiacp.cordapp.search

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.StateRef
import net.corda.core.crypto.SecureHash
import net.corda.core.node.services.ReadOnlyTransactionStorage
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.WireTransaction
import java.util.*
import java.util.concurrent.Callable

/**
 * Given a map of transaction id to [SignedTransaction], performs a breadth first search of the dependency graph from
 * the starting point down in order to find transactions that match the given query criteria.
 *
 * Currently, only one kind of query is supported: find any transaction that contains a command of the given type.
 *
 * In future, this should support restricting the search by time, and other types of useful query.
 *
 * @param transactions map of transaction id to [SignedTransaction].
 * @param startPoints transactions to use as starting points for the search.
 */
class IndiaCPHistorySearch(val transactions: ReadOnlyTransactionStorage,
                             val startPoints: List<WireTransaction>) : Callable<List<WireTransaction>> {

    abstract class Query(open val followInputsOfType: Class<out ContractState>?) {

        abstract fun matches(tx: WireTransaction): Boolean
    }

    class QueryByCommand (val withCommandOfType: Class<out CommandData>?, override val followInputsOfType: Class<out ContractState>?) : Query(followInputsOfType) {

        override fun matches(tx: WireTransaction): Boolean {
            if (withCommandOfType != null) {
                if (tx.commands.any { it.value.javaClass.isAssignableFrom(withCommandOfType) })
                    return true
            }
            return false
        }
    }

    class QueryByInputStateType(override val followInputsOfType: Class<out ContractState>?) : Query(followInputsOfType) {

        override fun matches(tx: WireTransaction): Boolean {
            return true
        }

    }

    var query: Query = QueryByInputStateType(null)

    override fun call(): List<WireTransaction> {
        val q = query

        val alreadyVisited = HashSet<SecureHash>()
        val next = ArrayList<WireTransaction>(startPoints)

        val results = ArrayList<WireTransaction>()

        while (next.isNotEmpty()) {
            val tx = next.removeAt(next.lastIndex)

            if (q.matches(tx))
                results += tx

            val inputsLeadingToUnvisitedTx: Iterable<StateRef> = tx.inputs.filter { it.txhash !in alreadyVisited }
            val unvisitedInputTxs: Map<SecureHash, SignedTransaction> = inputsLeadingToUnvisitedTx.map { it.txhash }.toHashSet().map { transactions.getTransaction(it) }.filterNotNull().associateBy { it.id }
            val unvisitedInputTxsWithInputIndex: Iterable<Pair<SignedTransaction, Int>> = inputsLeadingToUnvisitedTx.filter { it.txhash in unvisitedInputTxs.keys }.map { Pair(unvisitedInputTxs[it.txhash]!!, it.index) }
            next += (unvisitedInputTxsWithInputIndex.filter { q.followInputsOfType == null || it.first.tx.outputs[it.second].data.javaClass == q.followInputsOfType }
                    .map { it.first }.filter { alreadyVisited.add(it.id) }.map { it.tx })
        }

        return results
    }

    fun filterContractStates(searchOutput: List<WireTransaction>): ArrayList<ContractState> {
        val filteredOutputStates : ArrayList<ContractState> = ArrayList<ContractState>()
        val filteredOutputStatesList = searchOutput.map { it.outputs}.forEach { it.filter {it.data.javaClass == query.followInputsOfType}.forEach { filteredOutputStates.add(it.data) } }
        return filteredOutputStates
    }

    inline fun<reified T: LinearState> filterLinearStatesOfType(searchOutput: List<WireTransaction>): List<T> {
        return filterContractStates(searchOutput).filterIsInstance<T>()
    }
}

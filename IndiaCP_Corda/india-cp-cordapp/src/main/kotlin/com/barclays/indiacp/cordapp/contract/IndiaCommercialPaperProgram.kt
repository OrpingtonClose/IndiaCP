package com.barclays.indiacp.cordapp.contract

import com.barclays.indiacp.cordapp.schemas.IndiaCommercialPaperProgramSchemaV1
import com.barclays.indiacp.cordapp.utilities.CPUtils
import com.barclays.indiacp.cordapp.utilities.ModelUtils
import com.barclays.indiacp.model.*
import net.corda.core.contracts.*
import net.corda.core.contracts.clauses.AnyComposition
import net.corda.core.contracts.clauses.Clause
import net.corda.core.contracts.clauses.GroupClauseVerifier
import net.corda.core.contracts.clauses.verifyClause
import net.corda.core.crypto.CompositeKey
import net.corda.core.crypto.Party
import net.corda.core.crypto.SecureHash
import net.corda.core.random63BitValue
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.Emoji
import java.security.PublicKey
import java.time.Instant
import java.util.*

/**
 * This is the Smart Contract to manage the India Commercial Paper Program - the umbrella program under which the
 * Commercial Papers are Issued. There is a ceiling associated with each program and the total allocation cannot
 * exceed the ceiling.
 *
 * Created by ritukedia
 */
val INDIA_CP_PROGRAM_ID = IndiaCommercialPaperProgram()

class IndiaCommercialPaperProgram : Contract {
    // TODO: should reference the content of the legal agreement, not its URI
    override val legalContractReference: SecureHash = SecureHash.sha256("https://en.wikipedia.org/wiki/Commercial_paper")

    override fun verify(tx: TransactionForContract) = verifyClause(tx, IndiaCommercialPaperProgram.Clauses.Group(), tx.commands.select<IndiaCommercialPaperProgram.Commands>())

    data class State(

            val issuer: Party,
            val ipa: Party,
            val depository: Party,

            val programId: String,
            val name: String,
            val type: String,
            val purpose: String,
            val issueCommencementDate: Instant,
            val programSize: Amount<Issued<Currency>>,
            val programCurrency: Currency,
            val programAllocatedValue: Amount<Currency>? = Amount(0, programCurrency),
            val maturityDate: Instant,
            val isin: String?,

            val issuerId: String,
            val issuerName: String,
            val ipaId: String,
            val ipaName: String,
            val depositoryId: String,
            val depositoryName: String,

            val isinGenerationRequestDocId: String?,
            val ipaVerificationRequestDocId: String?,
            val ipaCertificateDocId: String?,
            val corporateActionFormDocId: String?,
            val allotmentLetterDocId: String?,

            val status: String? = IndiaCPProgramStatusEnum.UNKNOWN.name,
            val modifiedBy: String? = "",//todo default to logged in user
            val lastModifiedDate: Instant? = Instant.now(),
            val version: Int? = ModelUtils.STARTING_VERSION

    ) : LinearState, QueryableState {

        override val contract = com.barclays.indiacp.cordapp.contract.INDIA_CP_PROGRAM_ID

        override val linearId: UniqueIdentifier
            get() = UniqueIdentifier(programId)

        val parties: List<Party>
            get() = listOf(issuer, ipa, depository)

        override val participants: List<CompositeKey>
            get() = listOf(issuer, ipa, depository).map { it.owningKey }

        override fun isRelevant(ourKeys: Set<PublicKey>): Boolean {
            return parties.map { it.owningKey }.any { ck -> ck.containsAny(ourKeys) }
        }

        override fun toString() = "${Emoji.newspaper}IndiaCommercialPaperProgram(of $programSize issued by '$issuer' on '$issueCommencementDate' with a maturity period of '$maturityDate')"

        /** Object Relational Mapping support. */
        override fun supportedSchemas(): Iterable<MappedSchema> = listOf(IndiaCommercialPaperProgramSchemaV1)

        /** Object Relational Mapping support. */
        override fun generateMappedObject(schema: MappedSchema): PersistentState {
            return when (schema) {
                is IndiaCommercialPaperProgramSchemaV1 -> IndiaCommercialPaperProgramSchemaV1.PersistentIndiaCommericalPaperProgramState(

                        issuanceParty = this.issuer.owningKey.toBase58String(),
                        ipaParty = this.ipa.owningKey.toBase58String(),
                        depositoryParty = this.depository.owningKey.toBase58String(),

                        program_id = this.programId,
                        name = this.name,
                        type = this.type,
                        purpose = this.purpose,
                        issue_commencement_date = this.issueCommencementDate,
                        program_size = this.programSize.quantity,
                        program_allocated_value = this.programAllocatedValue!!.quantity,
                        program_currency = this.programCurrency.symbol,
                        maturity_days = this.maturityDate,
                        isin = this.isin,

                        issuer_id = this.issuerId,
                        issuer_name = this.issuerName,
                        ipa_id = this.ipaId,
                        ipa_name = this.ipaName,
                        depository_id = this.depositoryId,
                        depository_name = this.depositoryName,

                        isin_generation_request_doc_id = this.isinGenerationRequestDocId,
                        ipa_verification_request_doc_id = this.ipaVerificationRequestDocId,
                        ipa_certificate_doc_id = this.ipaCertificateDocId,
                        corporate_action_form_doc_id = this.corporateActionFormDocId,
                        allotment_letter_doc_id = this.allotmentLetterDocId,

                        last_modified = this.lastModifiedDate!!,
                        version = this.version!!,
                        modified_by = this.modifiedBy!!,
                        status = this.status!!
                )
                else -> throw IllegalArgumentException("Unrecognised schema $schema")
            }
        }
    }

    interface Clauses {
        class Group : GroupClauseVerifier<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, UniqueIdentifier>(
                AnyComposition(
                        IndiaCommercialPaperProgram.Clauses.Issue(),
                        IndiaCommercialPaperProgram.Clauses.Amend(),
                        IndiaCommercialPaperProgram.Clauses.AddIsinGenDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddIsin(),
                        IndiaCommercialPaperProgram.Clauses.AddIPAVerificationDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddIPACertifcateDoc(),
                        IndiaCommercialPaperProgram.Clauses.AddCorpActionFormDoc()
                )) {
            override fun groupStates(tx: TransactionForContract): List<TransactionForContract.InOutGroup<IndiaCommercialPaperProgram.State, UniqueIdentifier>>
                    // Group by Program ID for in / out states
                    = tx.groupStates() { state -> state.linearId }
        }

        abstract class AbstractIndiaCPProgramClause : Clause<IndiaCommercialPaperProgram.State, IndiaCommercialPaperProgram.Commands, UniqueIdentifier>() {

            fun  verifyDocAsPerStatus(doc: Attachment, docId: String?): Boolean {
                val docHashAndStatus = CPUtils.getDocHashAndStatus(docId)

                return verifyDoc(doc, docHashAndStatus.first, docHashAndStatus.second)
            }

            fun  verifyDoc(doc: Attachment, docHash: SecureHash, docStatus: IndiaCPDocumentDetails.DocStatusEnum): Boolean {
                when (docStatus) {
                    IndiaCPDocumentDetails.DocStatusEnum.UNSIGNED -> {
                        //Verify that the Attachment has been uploaded and is accessible
                        return doc.open() != null
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_ISSUER -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_INVESTOR -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_NSDL -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_IPA -> {
                        //TODO: verifyDocSignature(doc.open())
                        return true
                    }
                    else -> return false
                }
            }

            fun  verifyDocIsSignedByIssuer(doc: Attachment, docId: String?): Boolean {
                val docHashAndStatus = CPUtils.getDocHashAndStatus(docId)

                return verifyDoc(doc, docHashAndStatus.first, IndiaCPDocumentDetails.DocStatusEnum.SIGNED_BY_ISSUER)
            }

        }

        class Issue : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.Issue::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.Issue>()
                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                require(outputs.all { time < it.maturityDate }) { "maturity date is not in the past" }

                return setOf(command.value)
            }
        }

        class Amend : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.Amend::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.Amend>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Issuances must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the amendment is for changing programAllocatedValue as part of the new CP Issue" by (output.status.equals(IndiaCPProgramStatusEnum.CP_ISSUEED.name) ||
                                                                                                        output.status.equals(IndiaCPProgramStatusEnum.CP_PROGRAM_FULLY_ALLOCATED.name) )
                }

                return setOf(command.value)
            }
        }

        class AddIsinGenDoc: AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsinGenDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsinGenDoc>()

                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()

                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.after ?: throw IllegalArgumentException("ISIN Request must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                    "the ISIN Request Document - Letter of Intent - has been attached" by (attachment.id.toString() == output.isinGenerationRequestDocId!!.split(":").first())
                    "the ISIN Request Document - Letter of Intent - matches the status as uploaded" by verifyDocAsPerStatus(attachment, output.isinGenerationRequestDocId)
                }
                return setOf(command.value)
            }
        }

        class AddIsin : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIsin::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIsin>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()
                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("AddISIN transaction must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                    "the ISIN Request Document has been uploaded" by (!output.isinGenerationRequestDocId.isNullOrBlank())
                    "the ISIN Request Document - Letter of Intent - has been signed by the Issuer" by (verifyDocIsSignedByIssuer(attachment, output.isinGenerationRequestDocId))
                    "the ISIN is generated" by (!output.isin.isNullOrBlank())
                }

                return setOf(command.value)
            }
        }


        class AddIPAVerificationDoc : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPAVerificationDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPAVerificationDoc>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()
                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Add IPA Verification Doc Transaction must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the IPA" by (output.ipa.owningKey in command.signers)
                    "the ISIN is generated" by (!output.isin.isNullOrBlank())
                    "the IPA Verification Documents have been attached" by (attachment.id.toString() == output.ipaVerificationRequestDocId!!.split(":").first())
                    "the IPA Verification Documents match the status as uploaded" by verifyDocAsPerStatus(attachment, output.ipaVerificationRequestDocId)
                }

                return setOf(command.value)
            }
        }

        class AddIPACertifcateDoc : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()
                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Add IPA Certificate Doc Transaction must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the IPA" by (output.ipa.owningKey in command.signers)
                    "the IPA Verification Documents has been uploaded" by (!output.ipaVerificationRequestDocId.isNullOrBlank())
                    //"the IPA Verification Documents has been signed by Issuer" by (verifyDocIsSignedByIssuer(attachment, output.ipaVerificationRequestDocId))
                    "the IPA Certificate Document has been uploaded" by (!output.ipaCertificateDocId.isNullOrBlank())
                    "the IPA Certificate Document has been attached" by (attachment.id.toString() == output.ipaCertificateDocId!!.split(":").first())
                    "the IPA Certificate Documents matches the status as uploaded" by verifyDocAsPerStatus(attachment, output.ipaCertificateDocId)
                }

                return setOf(command.value)
            }
        }

        class AddCorpActionFormDoc : AbstractIndiaCPProgramClause() {
            override val requiredCommands: Set<Class<out CommandData>> = setOf(IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc::class.java)

            override fun verify(tx: TransactionForContract,
                                inputs: List<IndiaCommercialPaperProgram.State>,
                                outputs: List<IndiaCommercialPaperProgram.State>,
                                commands: List<AuthenticatedObject<IndiaCommercialPaperProgram.Commands>>,
                                groupingKey: UniqueIdentifier?): Set<IndiaCommercialPaperProgram.Commands> {

                val command = commands.requireSingleCommand<IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc>()
                val output = tx.outputs.filterIsInstance<IndiaCommercialPaperProgram.State>().single()
                val attachment = tx.attachments.single()

                val timestamp = tx.timestamp
                val time = timestamp?.before ?: throw IllegalArgumentException("Add Corporate Action Form Transaction must be timestamped")

                requireThat {
                    "the transaction is signed by the Issuer" by (output.issuer.owningKey in command.signers)
                    "the transaction is signed by the Depository" by (output.depository.owningKey in command.signers)
                    "the IPA Verification Documents has been uploaded" by (!output.ipaVerificationRequestDocId.isNullOrBlank())
                    "the IPA Certificate Document has been uploaded" by (!output.ipaCertificateDocId.isNullOrBlank())
                    "the CorpActionForm Document has been uploaded" by (!output.corporateActionFormDocId.isNullOrBlank())
                    "the CorpActionForm Document has been attached" by (attachment.id.toString() == output.corporateActionFormDocId!!.split(":").first())
                    "the CorpActionForm Document matches the status as uploaded" by verifyDocAsPerStatus(attachment, output.corporateActionFormDocId)
                }

                return setOf(command.value)
            }
        }

    }

    interface Commands : CommandData {
        data class Issue(override val nonce: Long = random63BitValue()) : IssueCommand, IndiaCommercialPaperProgram.Commands
        class Amend() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddIsinGenDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddIsin() : IndiaCommercialPaperProgram.Commands
        class AddIPAVerificationDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddIPACertifcateDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddCorpActionFormDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
        class AddAllotmentLetterDoc() : TypeOnlyCommandData(), IndiaCommercialPaperProgram.Commands
    }

    fun generateIssue(cpProgramContractState: IndiaCommercialPaperProgram.State,
                      creditRatingStateRef: StateAndRef<CreditRating.State>,
                      boardResolutionStateRef: StateAndRef<BorrowingLimitBoardResolution.State>,
                      notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(creditRatingStateRef)
        tx.addInputState(boardResolutionStateRef)

        //Adding Outputs
        val creditRatingInputState = creditRatingStateRef.state.data
        val boardResolutionInputState = boardResolutionStateRef.state.data

        tx.addOutputState(
                cpProgramContractState.copy(
                        status = IndiaCPProgramStatusEnum.CP_PROGRAM_CREATED.name,
                        lastModifiedDate = Instant.now(),
                        version = ModelUtils.STARTING_VERSION
                ),
                notary
        )
        tx.addOutputState(
                creditRatingInputState.copy(
                        version = creditRatingInputState.version!! + 1,
                        currentOutstandingCreditBorrowing = creditRatingInputState.currentOutstandingCreditBorrowing!!.copy (
                                quantity = cpProgramContractState.programSize.quantity + creditRatingInputState.currentOutstandingCreditBorrowing!!.quantity
                        )
                ),
                creditRatingStateRef.state.notary
        )
        tx.addOutputState(
                boardResolutionInputState.copy(
                        version = boardResolutionInputState.version!! + 1,
                        currentOutstandingCreditBorrowing = boardResolutionInputState.currentOutstandingCreditBorrowing!!.copy (
                                quantity = cpProgramContractState.programSize.quantity + boardResolutionInputState.currentOutstandingCreditBorrowing!!.quantity
                        )
                ),
                boardResolutionStateRef.state.notary
        )

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.Issue(), listOf(cpProgramContractState.issuer.owningKey))
        tx.addCommand(CreditRating.Commands.Amend(), listOf(cpProgramContractState.issuer.owningKey))
        tx.addCommand(BorrowingLimitBoardResolution.Commands.Amend(), listOf(cpProgramContractState.issuer.owningKey))

        return tx
    }

    fun generateTransactionWithISINDocAttachment (  cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>,
                                                    notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.ISIN_GEN_DOC_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.isinGenerationRequestDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIsinGenDoc(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                                                                                    cpProgramContractStateAndRef.state.data.depository.owningKey))

        return tx
    }

    fun  generateTransactionWithISIN(cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, cpIssues: List<StateAndRef<IndiaCommercialPaper.State>>, notary: Party): TransactionBuilder {

        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)
        for(cpIssue in cpIssues) {
            tx.addInputState(cpIssue)
        }

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.ISIN_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Propogate ISIN to ALL CP Issues under this CP Program umbrella
        for(cpIssue in cpIssues) {
            tx.addOutputState(
                    cpIssue.state.data.copy(
                            isin = cpProgramContractStateAndRef.state.data.isin!!,
                            version = cpIssue.state.data.version!! + 1,
                            lastModifiedDate = Instant.now(),
                            status = IndiaCPIssueStatusEnum.CP_ISIN_ADDED.name
                    ),
                    cpProgramContractStateAndRef.state.notary
            )
        }

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.isinGenerationRequestDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIsin(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.depository.owningKey))
        tx.addCommand(IndiaCommercialPaper.Commands.AddISIN(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.depository.owningKey))

        return tx
    }

    fun  generateTransactionWithIPADocAttachment(cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party): TransactionBuilder? {
        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.IPA_VERIFICATION_DOC_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.ipaVerificationRequestDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIPAVerificationDoc(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.ipa.owningKey))

        return tx
    }

    fun  generateTransactionWithIPACertificateDocAttachment(cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party): TransactionBuilder? {
        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.IPA_CERTIFICATE_DOC_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.ipaCertificateDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddIPACertifcateDoc(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.ipa.owningKey))

        return tx
    }

    fun  generateTransactionWithCAFormDocAttachment(cpProgramContractStateAndRef: StateAndRef<IndiaCommercialPaperProgram.State>, notary: Party): TransactionBuilder? {
        val tx = TransactionType.General.Builder(notary)

        //Adding Inputs
        tx.addInputState(cpProgramContractStateAndRef)

        //Adding Outputs
        tx.addOutputState(
                cpProgramContractStateAndRef.state.data.copy(
                        version = cpProgramContractStateAndRef.state.data.version!! + 1,
                        lastModifiedDate = Instant.now(),
                        status = IndiaCPProgramStatusEnum.CORP_ACTION_FORM_DOC_ADDED.name
                ),
                cpProgramContractStateAndRef.state.notary
        )

        //Adding Attachments
        val docHash = cpProgramContractStateAndRef.state.data.corporateActionFormDocId!!.split(":").first()
        tx.addAttachment(SecureHash.parse(docHash))

        //Adding Required Commands
        tx.addCommand(IndiaCommercialPaperProgram.Commands.AddCorpActionFormDoc(), listOf(cpProgramContractStateAndRef.state.data.issuer.owningKey,
                cpProgramContractStateAndRef.state.data.depository.owningKey))

        return tx    }
}



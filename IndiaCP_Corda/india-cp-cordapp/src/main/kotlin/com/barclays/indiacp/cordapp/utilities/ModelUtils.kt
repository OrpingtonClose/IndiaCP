package com.barclays.indiacp.cordapp.utilities;

import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.CreditRating
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaper
import com.barclays.indiacp.cordapp.contract.IndiaCommercialPaperProgram
import com.barclays.indiacp.model.*
import net.corda.core.contracts.Amount
import net.corda.core.contracts.`issued by`
import net.corda.core.crypto.composite
import net.corda.core.days
import net.corda.core.node.ServiceHub
import java.time.Instant
import java.util.*

/**
 * Created by ritukedia
 */
object ModelUtils {

    final val DEFAULT_MODIFIED_BY : String
        get() = "UNKNOWN"

    final val STARTING_VERSION : Int
            get() = 1

    enum class DocumentStatus() {
        ACTIVE,
        OBSOLETE
    }

    fun creditRatingStateFromModel(creditRatingModel: LegalEntityCreditRatingDocument, serviceHub: ServiceHub): CreditRating.State {
        val currency = Currency.getInstance(creditRatingModel.currency)
        val contractState = CreditRating.State (
            issuer = CPUtils.getPartyByName(serviceHub, creditRatingModel.legalEntityId),
            owner = serviceHub.legalIdentityKey.public.composite,
            currency = currency,
            creditRatingAgencyName = creditRatingModel.creditRatingAgencyName,
            creditRatingAmount = Amount(creditRatingModel.creditRatingAmount, currency),
            currentOutstandingCreditBorrowing = if (creditRatingModel.currentOutstandingCreditBorrowing != null) Amount(creditRatingModel.currentOutstandingCreditBorrowing, currency) else Amount(0, currency),
            creditRating = creditRatingModel.creditRating,
            creditRatingIssuanceDate = creditRatingModel.creditRatingIssuanceDate,
            creditRatingEffectiveDate = creditRatingModel.creditRatingEffectiveDate,
            creditRatingExpiryDate = creditRatingModel.creditRatingExpiryDate,
            docHash = creditRatingModel.docHash ,
            modifiedBy = creditRatingModel.modifiedBy,
            lastModifiedDate = creditRatingModel.lastModifiedDate ?: Date(),
            status = creditRatingModel.status?.name ?: DocumentStatus.ACTIVE.name,
            version = if(creditRatingModel.version != null) creditRatingModel.version else STARTING_VERSION
        )

        return contractState
    }

    fun  creditRatingModelFromState(creditRatingState: CreditRating.State): LegalEntityCreditRatingDocument {
        val creditRatingModel = LegalEntityCreditRatingDocument ()
        creditRatingModel.legalEntityId (creditRatingState.issuer.name)
        creditRatingModel.creditRatingAgencyName = creditRatingState.creditRatingAgencyName
        creditRatingModel.creditRatingAmount = creditRatingState.creditRatingAmount.quantity
        creditRatingModel.currentOutstandingCreditBorrowing = creditRatingState.currentOutstandingCreditBorrowing!!.quantity
        creditRatingModel.creditRating = creditRatingState.creditRating
        creditRatingModel.creditRatingIssuanceDate = creditRatingState.creditRatingIssuanceDate
        creditRatingModel.creditRatingEffectiveDate = creditRatingState.creditRatingEffectiveDate
        creditRatingModel.creditRatingExpiryDate = creditRatingState.creditRatingExpiryDate
        creditRatingModel.docHash = creditRatingState.docHash
        creditRatingModel.modifiedBy = creditRatingState.modifiedBy
        creditRatingModel.lastModifiedDate = creditRatingState.lastModifiedDate
        creditRatingModel.status = LegalEntityCreditRatingDocument.StatusEnum.fromValue(creditRatingState.status)
        creditRatingModel.version = creditRatingState.version!!.toInt()

        return creditRatingModel
    }

    fun  boardResolutionModelFromState(boardResolutionState: BorrowingLimitBoardResolution.State): BoardResolutionBorrowingLimitDocument {
        val boardResolutionModel = BoardResolutionBorrowingLimitDocument ()
        boardResolutionModel.legalEntityId (boardResolutionState.issuer.name)
        boardResolutionModel.boardResolutionBorrowingLimit = boardResolutionState.boardResolutionBorrowingLimit.quantity
        boardResolutionModel.currentOutstandingCreditBorrowing = boardResolutionState.currentOutstandingCreditBorrowing!!.quantity
        boardResolutionModel.boardResolutionIssuanceDate = boardResolutionState.boardResolutionIssuanceDate
        boardResolutionModel.boardResolutionExpiryDate = boardResolutionState.boardResolutionExpiryDate
        boardResolutionModel.docHash = boardResolutionState.docHash
        boardResolutionModel.modifiedBy = boardResolutionState.modifiedBy
        boardResolutionModel.lastModifiedDate = boardResolutionState.lastModifiedDate
        boardResolutionModel.status = BoardResolutionBorrowingLimitDocument.StatusEnum.fromValue(boardResolutionState.status)
        boardResolutionModel.version = boardResolutionState.version!!.toInt()

        return boardResolutionModel
    }

    fun boardResolutionStateFromModel(boardResolutionModel: BoardResolutionBorrowingLimitDocument, serviceHub: ServiceHub): BorrowingLimitBoardResolution.State {
        val currency = Currency.getInstance(boardResolutionModel.currency)
        val contractState = BorrowingLimitBoardResolution.State (
                issuer = CPUtils.getPartyByName(serviceHub, boardResolutionModel.legalEntityId),
                owner = serviceHub.legalIdentityKey.public.composite,
                currency = currency,
                boardResolutionBorrowingLimit = Amount(boardResolutionModel.boardResolutionBorrowingLimit, currency),
                currentOutstandingCreditBorrowing = if (boardResolutionModel.currentOutstandingCreditBorrowing != null) Amount(boardResolutionModel.currentOutstandingCreditBorrowing, currency) else Amount(0, currency),
                boardResolutionIssuanceDate = boardResolutionModel.boardResolutionIssuanceDate,
                boardResolutionExpiryDate = boardResolutionModel.boardResolutionExpiryDate,
                docHash = boardResolutionModel.docHash ,
                modifiedBy = boardResolutionModel.modifiedBy,
                lastModifiedDate = boardResolutionModel.lastModifiedDate ?: Date(),
                status = boardResolutionModel.status?.name ?: DocumentStatus.ACTIVE.name,
                version = boardResolutionModel.version ?: STARTING_VERSION
        )

        return contractState
    }

    fun indiaCPProgramStateFromModel(model: IndiaCPProgram, serviceHub: ServiceHub): IndiaCommercialPaperProgram.State {
        val currency = Currency.getInstance(model.programCurrency)
        val contractState = IndiaCommercialPaperProgram.State (
            issuer = CPUtils.getPartyByName(serviceHub, model.issuerId),
            ipa =  CPUtils.getPartyByName(serviceHub, model.ipaId),
            depository = CPUtils.getPartyByName(serviceHub, model.depositoryId),
            issuerId = model.issuerId,
            issuerName = model.issuerName,
            ipaId = model.ipaId,
            ipaName = model.ipaName,
            depositoryId = model.depositoryId,
            depositoryName = model.depositoryName,

            programId = model.programId,
            name = model.name,
            type = model.type,
            purpose = model.purpose,
            programSize = Amount(model.programSize, currency) `issued by` CPUtils.getCashIssuerForThisNode(serviceHub),
            programAllocatedValue = if(model.programAllocatedValue != null) {
                                        Amount(model.programAllocatedValue, currency)
                                    } else {
                                        Amount(0, currency)
                                    },
            programCurrency = Currency.getInstance("INR"), //TODO fix the hardcoding to INR and DOLLAR
            maturityDate =  Instant.now() + model.maturityDays.days, //TODO fix this just maintain maturity days as maturity days in the Smart Contract
            issueCommencementDate = model.issueCommencementDate?.toInstant() ?: Date().toInstant(),
            isin = model.isin,

            isinGenerationRequestDocId = model.isinGenerationRequestDocId,
            ipaVerificationRequestDocId = model.ipaVerificationRequestDocId ,
            ipaCertificateDocId = model.ipaCertificateDocId,
            corporateActionFormDocId = model.corporateActionFormDocId,
            allotmentLetterDocId = model.allotmentLetterDocId,

            modifiedBy = model.modifiedBy ?: DEFAULT_MODIFIED_BY,//todo default to logged in user
            lastModifiedDate = model.lastModifiedDate?.toInstant() ?: Date().toInstant(),
            status = model.status?.toString() ?: IndiaCPProgramStatusEnum.UNKNOWN.name,
            version = model.version ?: STARTING_VERSION
        )

        return contractState
    }

    fun  indiaCPProgramModelFromState(contractState: IndiaCommercialPaperProgram.State): IndiaCPProgram {
        val model = IndiaCPProgram ()
        model.issuerId = contractState.issuerId
        model.issuerName = contractState.issuerName
        model.ipaId = contractState.ipaId
        model.ipaName = contractState.ipaName
        model.depositoryId = contractState.depositoryId
        model.depositoryName = contractState.depositoryName

        model.programId = contractState.programId
        model.name = contractState.name
        model.type = contractState.type
        model.purpose = contractState.purpose
        model.programSize = contractState.programSize.quantity
        model.programAllocatedValue = contractState.programAllocatedValue?.quantity ?: 0
        model.programCurrency = contractState.programCurrency.symbol
        //model.maturityDays = //TODO calculate maturity days from maturity date
        model.issueCommencementDate = Date(contractState.issueCommencementDate.toEpochMilli())
        model.isin = contractState.isin

        model.isinGenerationRequestDocId = contractState.isinGenerationRequestDocId
        model.ipaVerificationRequestDocId = contractState.ipaVerificationRequestDocId
        model.ipaCertificateDocId = contractState.ipaCertificateDocId
        model.corporateActionFormDocId = contractState.corporateActionFormDocId
        model.allotmentLetterDocId = contractState.allotmentLetterDocId

        model.modifiedBy = contractState.modifiedBy
        model.lastModifiedDate = if (contractState.lastModifiedDate == null) null else Date(contractState.lastModifiedDate.toEpochMilli())
        model.status = IndiaCPProgramStatusEnum.valueOf(contractState.status?.toString() ?: IndiaCPProgramStatusEnum.UNKNOWN.name).toString()
        model.version = contractState.version?.toInt() ?: 1

        return model
    }

    fun  getDocumentDetailsForCP(cpTradeId: String, cpStatesForDocTypeHistory: List<IndiaCommercialPaper.State>, docType: IndiaCPDocumentDetails.DocTypeEnum): List<IndiaCPDocumentDetails> {

        val documentDetailsList = ArrayList<IndiaCPDocumentDetails>()
        for (cpState in cpStatesForDocTypeHistory) {
            val documentDetails = IndiaCPDocumentDetails ()
            documentDetails.docType(docType)
            documentDetails.cpProgramId(cpState.cpProgramID)
            documentDetails.cpIssueId(cpState.cpTradeID)
            val docHashAndStatus = getDocHashAndStatus(cpState.dealConfirmationDocId, IndiaCPDocumentDetails.DocTypeEnum.DEAL_CONFIRMATION_DOC, cpState.cpTradeID)
            documentDetails.docHash(docHashAndStatus.first)
            documentDetails.docStatus(docHashAndStatus.second)
            documentDetails.lastModifiedDate(Date(cpState.lastModifiedDate!!.toEpochMilli()))
            documentDetails.modifiedBy(cpState.modifiedBy)
            documentDetailsList.add(documentDetails)
        }
        return documentDetailsList
    }

    fun  getDocumentDetailsForCPProgram(cpProgId: String, cpProgStatesForDocTypeHistory: List<IndiaCommercialPaperProgram.State>, docType: IndiaCPDocumentDetails.DocTypeEnum): List<IndiaCPDocumentDetails> {

        val documentDetailsList = ArrayList<IndiaCPDocumentDetails>()
        for (cpProgramState in cpProgStatesForDocTypeHistory) {
            val documentDetails = IndiaCPDocumentDetails ()
            documentDetails.docType(docType)
            documentDetails.cpProgramId(cpProgId)
            setDocHashAndStatus(documentDetails, cpProgramState, docType)
            documentDetails.lastModifiedDate(Date(cpProgramState.lastModifiedDate!!.toEpochMilli()))
            documentDetails.modifiedBy(cpProgramState.modifiedBy)
            documentDetailsList.add(documentDetails)
        }
        return documentDetailsList
    }

    private fun  setDocHashAndStatus(documentDetails: IndiaCPDocumentDetails, cpProgramState: IndiaCommercialPaperProgram.State, docType: IndiaCPDocumentDetails.DocTypeEnum) {

        when (docType) {
            IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS -> {
                val docHashAndStatus = getDocHashAndStatus(cpProgramState.isinGenerationRequestDocId, IndiaCPDocumentDetails.DocTypeEnum.DEPOSITORY_DOCS, cpProgramState.programId)
                documentDetails.docHash(docHashAndStatus.first)
                documentDetails.docStatus(docHashAndStatus.second)
            }
        }
    }

    private fun getDocHashAndStatus(docId: String?, docType: IndiaCPDocumentDetails.DocTypeEnum, ref: String) : Pair<String, IndiaCPDocumentDetails.DocStatusEnum> {
        val DOC_HASH_NOT_FOUND_ERROR_MESSAGE = "Document Hash Not Found in Smart Contract for Doc Type ${docType}, Ref: ${ref}"
        val DOC_ID_MALFORMED_ERROR_MESSAGE = "Document ID in the Smart Contract is expected to be of the format 'SecureHash:Status' but found ${docId} for Doc Type ${docType}, Smart Contract Ref: ${ref}"

        if (docId.isNullOrBlank()) {
            throw IndiaCPException(DOC_HASH_NOT_FOUND_ERROR_MESSAGE, Error.SourceEnum.DL_R3CORDA)
        }
        if (!docId!!.contains(":")) {
            throw IndiaCPException(DOC_ID_MALFORMED_ERROR_MESSAGE, Error.SourceEnum.DL_R3CORDA)
        }
        val docHashAndStatus = docId!!.split(":")
        val docHash = docHashAndStatus[0]
        val docStatus = docHashAndStatus[1]
        val docStatusEnum = IndiaCPDocumentDetails.DocStatusEnum.fromValue(docStatus) ?: IndiaCPDocumentDetails.DocStatusEnum.UNKNOWN

        return Pair(docHash, docStatusEnum)
    }

    fun  indiaCPStateFromModel(model: IndiaCPIssue, serviceHub: ServiceHub): IndiaCommercialPaper.State {
        val currency = Currency.getInstance(model.currency)
        val valueDate = model.valueDate?.toInstant() ?: Date().toInstant()
        val faceValue = model.facevaluePerUnit.toLong() * model.noOfUnits
        val contractState = IndiaCommercialPaper.State (
                issuer = CPUtils.getPartyByName(serviceHub, model.issuerId),
                investor = CPUtils.getPartyByName(serviceHub, model.investorId),
                ipa =  CPUtils.getPartyByName(serviceHub, model.ipaId),
                depository = CPUtils.getPartyByName(serviceHub, model.depositoryId),
                beneficiary = CPUtils.getPartyByName(serviceHub, model.beneficiaryId),

                cpProgramID = model.cpProgramId,
                cpTradeID = model.cpTradeId,
                tradeDate = model.tradeDate?.toInstant() ?: Date().toInstant(),
                valueDate = model.valueDate?.toInstant() ?: Date().toInstant(),
                faceValue = Amount(faceValue, currency) `issued by` CPUtils.getCashIssuerForThisNode(serviceHub),
                yieldOnMaturity = model.rate,
                faceValuePerUnit = Amount(model.facevaluePerUnit.toLong(), currency),
                noOfunits = model.noOfUnits,
                maturityDate = valueDate + model.maturityDays.days,
                isin = model.isin,
                issuerSettlementDetails = settlementDetailsFromModel(model.issuerSettlementDetails),
                investorSettlementDetails = settlementDetailsFromModel(model.investorSettlementDetails),
                ipaSettlementDetails = settlementDetailsFromModel(model.ipaSettlementDetails),

                dealConfirmationDocId = model.dealConfirmationDocId,

                modifiedBy = model.modifiedBy ?: DEFAULT_MODIFIED_BY,//todo default to logged in user
                lastModifiedDate = model.lastModifiedDate?.toInstant() ?: Date().toInstant(),
                status = model.status?.toString() ?: IndiaCPIssueStatusEnum.UNKNOWN.name,
                version = model.version ?: STARTING_VERSION
        )

        return contractState
    }

    fun  settlementDetailsFromModel(model: SettlementDetails): IndiaCommercialPaper.SettlementDetails? {
        val depositoryAccountDetails = ArrayList<IndiaCommercialPaper.DepositoryAccountDetails> ()
        for (d in model.depositoryAccountDetails) {
            val dpAccount = IndiaCommercialPaper.DepositoryAccountDetails(
                    dpName = d.dpName,
                    dpType = d.dpType.name,
                    clientId = d.clientId,
                    dpID = d.dpId
            )
            depositoryAccountDetails.add(dpAccount)
        }
        val settlementDetails = IndiaCommercialPaper.SettlementDetails (
                partyType = model.partyType.name,
                paymentAccountDetails = IndiaCommercialPaper.PaymentAccountDetails(
                        creditorName = model.paymentAccountDetails.creditorName,
                        bankAccountDetails = model.paymentAccountDetails.bankAccountNo,
                        bankName = model.paymentAccountDetails.bankName,
                        rtgsCode = model.paymentAccountDetails.rtgsIfscCode
                ),
                depositoryAccountDetails = depositoryAccountDetails
        )

        return settlementDetails
    }

    fun  indiaCPModelFromState(contractState: IndiaCommercialPaper.State): IndiaCPIssue {
        val model = IndiaCPIssue ()
        model.issuerId = contractState.issuer.name
        model.issuerName = contractState.issuer.name
        model.ipaId = contractState.ipa.name
        model.ipaName = contractState.ipa.name
        model.depositoryId = contractState.depository.name
        model.depositoryName = contractState.depository.name

        model.cpProgramId = contractState.cpProgramID
        model.cpTradeId = contractState.cpTradeID
        model.tradeDate = Date(contractState.tradeDate.toEpochMilli())
        model.valueDate = Date(contractState.valueDate.toEpochMilli())
        model.facevaluePerUnit = contractState.faceValuePerUnit.quantity.toInt()
        model.noOfUnits = contractState.noOfunits
        model.rate = contractState.yieldOnMaturity
        model.isin = contractState.isin
        model.maturityDays = 7 //TODO: calculate: contractState.maturityDate. - contractState.valueDate

        model.issuerSettlementDetails = if(contractState.issuerSettlementDetails == null) null else settlementDetailsModelFromState(contractState.issuerSettlementDetails!!)
        model.investorSettlementDetails = if(contractState.investorSettlementDetails == null) null else settlementDetailsModelFromState(contractState.investorSettlementDetails!!)
        model.ipaSettlementDetails = if(contractState.ipaSettlementDetails == null) null else settlementDetailsModelFromState(contractState.ipaSettlementDetails!!)

        model.dealConfirmationDocId = contractState.dealConfirmationDocId

        model.modifiedBy = contractState.modifiedBy
        model.lastModifiedDate = if (contractState.lastModifiedDate == null) null else Date(contractState.lastModifiedDate.toEpochMilli())
        model.status = IndiaCPIssueStatusEnum.valueOf(contractState.status?.toString() ?: IndiaCPIssueStatusEnum.UNKNOWN.name).toString()
        model.version = contractState.version?.toInt() ?: 1

        return model
    }

    private fun  settlementDetailsModelFromState(settlementDetailsContractState: IndiaCommercialPaper.SettlementDetails): SettlementDetails {
        val settlementDetailsModel = SettlementDetails ()
        settlementDetailsModel.partyType = SettlementDetails.PartyTypeEnum.fromValue(settlementDetailsContractState.partyType)
        if (settlementDetailsContractState.paymentAccountDetails != null) {
            settlementDetailsModel.paymentAccountDetails = PaymentAccountDetails()
            settlementDetailsModel.paymentAccountDetails.creditorName = settlementDetailsContractState.paymentAccountDetails.creditorName
            settlementDetailsModel.paymentAccountDetails.bankAccountNo = settlementDetailsContractState.paymentAccountDetails.bankAccountDetails
            settlementDetailsModel.paymentAccountDetails.bankName = settlementDetailsContractState.paymentAccountDetails.bankName
            settlementDetailsModel.paymentAccountDetails.rtgsIfscCode = settlementDetailsContractState.paymentAccountDetails.rtgsCode
        }
        if (settlementDetailsContractState.depositoryAccountDetails != null && settlementDetailsContractState.depositoryAccountDetails.isNotEmpty()) {
            val depositoryAccountDetails = ArrayList<DepositoryAccountDetails> ()
            for (dpStateDetails in settlementDetailsContractState.depositoryAccountDetails!!) {
                val dpAccountModel = DepositoryAccountDetails()
                dpAccountModel.clientId = dpStateDetails.clientId
                dpAccountModel.dpId = dpStateDetails.dpID
                dpAccountModel.dpName = dpStateDetails.dpName
                dpAccountModel.dpType = DepositoryAccountDetails.DpTypeEnum.fromValue(dpStateDetails.dpType)

                depositoryAccountDetails.add(dpAccountModel)
            }
            settlementDetailsModel.depositoryAccountDetails = depositoryAccountDetails
        }

        return settlementDetailsModel

    }
}

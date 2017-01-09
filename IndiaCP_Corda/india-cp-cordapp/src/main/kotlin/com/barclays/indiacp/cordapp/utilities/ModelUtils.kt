package com.barclays.indiacp.cordapp.utilities;

import com.barclays.indiacp.cordapp.contract.BorrowingLimitBoardResolution
import com.barclays.indiacp.cordapp.contract.CreditRating;
import com.barclays.indiacp.model.BoardResolutionBorrowingLimitDocument
import com.barclays.indiacp.model.LegalEntityCreditRatingDocument;
import net.corda.core.crypto.composite
import net.corda.core.node.ServiceHub
import java.util.*

/**
 * Created by ritukedia on 08/01/17.
 */


object ModelUtils {

    enum class DocumentStatus() {
        ACTIVE,
        OBSOLETE
    }

    fun creditRatingStateFromModel(creditRatingModel: LegalEntityCreditRatingDocument, serviceHub: ServiceHub): CreditRating.State {
        val contractState = CreditRating.State (
            issuer = CPUtils.getPartyByName(serviceHub, creditRatingModel.legalEntityId),
            owner = serviceHub.legalIdentityKey.public.composite,
            creditRatingAgencyName = creditRatingModel.creditRatingAgencyName,
            creditRatingAmount = Integer(creditRatingModel.creditRatingAmount),
            creditRating = creditRatingModel.creditRating,
            creditRatingIssuanceDate = creditRatingModel.creditRatingIssuanceDate,
            creditRatingEffectiveDate = creditRatingModel.creditRatingEffectiveDate,
            creditRatingExpiryDate = creditRatingModel.creditRatingExpiryDate,
            docHash = creditRatingModel.docHash ,
            modifiedBy = creditRatingModel.modifiedBy,
            lastModifiedDate = creditRatingModel.lastModifiedDate ?: Date(),
            status = creditRatingModel.status?.name ?: DocumentStatus.ACTIVE.name,
            version = if(creditRatingModel.version != null) Integer(creditRatingModel.version) else Integer(1)
        )

        return contractState
    }

    fun  creditRatingModelFromState(creditRatingState: CreditRating.State): LegalEntityCreditRatingDocument {
        val creditRatingModel = LegalEntityCreditRatingDocument ()
        creditRatingModel.legalEntityId (creditRatingState.issuer.name)
        creditRatingModel.creditRatingAgencyName = creditRatingState.creditRatingAgencyName
        creditRatingModel.creditRatingAmount = creditRatingState.creditRatingAmount.toInt()
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
        boardResolutionModel.boardResolutionBorrowingLimit = boardResolutionState.boardResolutionBorrowingLimit.toInt()
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
        val contractState = BorrowingLimitBoardResolution.State (
                issuer = CPUtils.getPartyByName(serviceHub, boardResolutionModel.legalEntityId),
                owner = serviceHub.legalIdentityKey.public.composite,
                boardResolutionBorrowingLimit = Integer(boardResolutionModel.boardResolutionBorrowingLimit),
                boardResolutionIssuanceDate = boardResolutionModel.boardResolutionIssuanceDate,
                boardResolutionExpiryDate = boardResolutionModel.boardResolutionExpiryDate,
                docHash = boardResolutionModel.docHash ,
                modifiedBy = boardResolutionModel.modifiedBy,
                lastModifiedDate = boardResolutionModel.lastModifiedDate ?: Date(),
                status = boardResolutionModel.status?.name ?: DocumentStatus.ACTIVE.name,
                version = if(boardResolutionModel.version != null) Integer(boardResolutionModel.version) else Integer(1)
        )

        return contractState
    }

}

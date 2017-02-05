contract BoardResolutionBorrowingLimitDocument{

 string legalEntityId;
 uint boardResolutionBorrowingLimit;
 uint currentOutstandingCreditBorrowing;
 string currency;
 string docHash;

 uint boardResolutionIssuanceDate;
 uint boardResolutionExpiryDate;

 uint version;
 string modifiedBy;
 uint lastModifiedDate;
 string status;

 function BoardResolutionBorrowingLimitDocument(string _legalEntityId, uint _boardResolutionBorrowingLimit,
    uint _boardResolutionIssuanceDate, uint _boardResolutionExpiryDate,
    string _docHash, string _modifiedBy){

    legalEntityId=_legalEntityId;
    boardResolutionBorrowingLimit=_boardResolutionBorrowingLimit;
    boardResolutionIssuanceDate=_boardResolutionIssuanceDate;
    boardResolutionExpiryDate=_boardResolutionExpiryDate;
    docHash=_docHash;
    modifiedBy=_modifiedBy;

    //default values

    currentOutstandingCreditBorrowing=0;
    version=0;
    currency="INR";
    lastModifiedDate=now;
    status="ACTIVE";

 }



 function fetchBRDetails() constant returns(string _legalEntityId, uint _boardResolutionBorrowingLimit, uint _currentOutstandingCreditBorrowing, string _currency, string _docHash, uint _boardResolutionIssuanceDate, uint _boardResolutionExpiryDate){

    return (legalEntityId, boardResolutionBorrowingLimit, currentOutstandingCreditBorrowing, currency, docHash, boardResolutionIssuanceDate, boardResolutionExpiryDate);

 }

 function fetchBRStatus() constant returns(uint _version, string _modifiedBy, uint _lastModifiedDate, string _status){

    return (version, modifiedBy, lastModifiedDate, status);
 }

}


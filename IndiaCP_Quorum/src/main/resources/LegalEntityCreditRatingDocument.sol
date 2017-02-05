contract LegalEntityCreditRatingDocument{

 string legalEntityId;
 string creditRatingAgencyName;
 uint creditRatingAmount;
 uint currentOutstandingCreditBorrowing;
 string currency;
 string creditRating;
 string docHash;

 uint creditRatingIssuanceDate;
 uint creditRatingEffectiveDate;
 uint creditRatingExpiryDate;

 uint version;
 string modifiedBy;
 uint lastModifiedDate;
 string status;

 function LegalEntityCreditRatingDocument(string _legalEntityId, string _creditRatingAgencyName, uint _creditRatingAmount,
    string _creditRating, uint _creditRatingIssuanceDate, uint _creditRatingEffectiveDate, uint _creditRatingExpiryDate,
    string _docHash, string _modifiedBy){

    legalEntityId=_legalEntityId;
    creditRatingAgencyName=_creditRatingAgencyName;
    creditRatingAmount=_creditRatingAmount;
    creditRating=_creditRating;
    creditRatingIssuanceDate=_creditRatingIssuanceDate;
    creditRatingEffectiveDate=_creditRatingEffectiveDate;
    creditRatingExpiryDate=_creditRatingExpiryDate;
    docHash=_docHash;
    modifiedBy=_modifiedBy;

    //default values

    currentOutstandingCreditBorrowing=0;
    version=0;
    currency="INR";
    lastModifiedDate=now;
    status="ACTIVE";

 }

 function fetchCRDetails() constant returns(string _legalEntityId, string _creditRatingAgencyName, uint _creditRatingAmount, uint _currentOutstandingCreditBorrowing, string _currency, string _creditRating, string _docHash){

   return (legalEntityId, creditRatingAgencyName, creditRatingAmount, currentOutstandingCreditBorrowing, currency, creditRating, docHash);

 }

 function fetchCRDates() constant returns(uint _creditRatingIssuanceDate, uint _creditRatingEffectiveDate, uint _creditRatingExpiryDate){

    return (creditRatingIssuanceDate, creditRatingEffectiveDate, creditRatingExpiryDate);
 }

 function fetchCRStatus() constant returns(uint _version, string _modifiedBy, uint _lastModifiedDate, string _status){

    return (version, modifiedBy, lastModifiedDate, status);
 }

}


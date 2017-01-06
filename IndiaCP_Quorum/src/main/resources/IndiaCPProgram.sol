contract IndiaCPProgram{

  string programId;
  string name;
  string _type;
  string purpose;
  uint issueCommencementDate;
  string programCurrency;
  uint programSize;
  uint programAllocatedValue;

  string isin;
  uint maturityDays;

  //Document Hash Identifiers
  string isinGenerationRequestDocId;
  string ipaVerificationRequestDocId;
  string ipaCertificateDocId;
  string corporateActionFormDocId;
  string allotmentLetterDocId;

  //status fields
  uint version;
  string status;
  uint lastModifiedDate;
  string modifiedBy;

  //Parties
  string issuerId;
  string issuerName;
  string ipaId;
  string ipaName;
  string depositoryId;
  string depositoryName;

  function IndiaCPProgram(string _programId, string _name, string __type, string _purpose, uint _programSize, uint _maturityDays){
    programId = _programId;
    name = _name;
    _type = __type;
    purpose = _purpose;
    programSize = _programSize;
    maturityDays = _maturityDays;

    //Default values
    programAllocatedValue = 0;
    programCurrency = "INR";
    version = 1;
    status = "Initiated";
    lastModifiedDate = now;
    issueCommencementDate=lastModifiedDate;

  }


  function fetchCPProgramTradeDetails() constant returns (string _programId, string _name, string __type, string _purpose, uint _issueCommencementDate, string _programCurrency, uint _maturityDays, string _isin) {
    return (programId, name, _type, purpose, issueCommencementDate, programCurrency, maturityDays, isin);
  }
/*
  function fetchCPProgramIssueStatus() constant returns (uint _programSize, uint _programAllocatedValue){
    return (programSize, programAllocatedValue);
  }
*/

  function fetchCPProgramDocuments() constant returns (string _isinGenerationRequestDocId, string _ipaVerificationRequestDocId, string _ipaCertificateDocId, string _corporateActionFormDocId, string _allotmentLetterDocId) {
    return (isinGenerationRequestDocId, ipaVerificationRequestDocId, ipaCertificateDocId, corporateActionFormDocId, allotmentLetterDocId);
  }

  function fetchCPProgramParties() constant returns (string _issuerId, string _issuerName, string _ipaId, string _ipaName, string _depositoryId, string _depositoryName) {
    return (issuerId, issuerName, ipaId, ipaName, depositoryId, depositoryName);
  }

  function fetchCPProgramStatus() constant returns (uint _version, string _status, uint _lastModifiedDate, string _modifiedBy) {
    return (version, status, lastModifiedDate, modifiedBy);
  }

}

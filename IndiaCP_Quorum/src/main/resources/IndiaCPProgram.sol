contract IndiaCPProgram{

  string programId;
  string name;
  string _type;
  string purpose;
  uint issueCommencementDate;
  string programCurrency;
  uint programSize;
  uint programAllocatedValue;
  uint maturityDays;

  //Document Hash Identifiers
  string isinGenerationDocId;
  string ipaVerificationDocId;
  string ipaCertificateDocId;
  string corporateActionFormDocId;
  string allotmentLetterDocId;

  //Populate through network mapping service
  address issuerAddress;
  address ipaAddress;
  address depositoryAddress;

  //status fields
  uint version;
  string status;
  uint lastModifiedDate;

  function IndiaCPProgram(string _programId, string _name, string __type, string _purpose, uint _programSize, uint _maturityDays){
    programId = _programId;
    name = _name;
    _type = __type;
    purpose = _purpose;
    programSize = _programSize;
    maturityDays = _maturityDays;

    //Default values
    programAllocatedValue = 0;
    issuerAddress = msg.sender;
    programCurrency = "INR";
    //ipaAddress = "";
    version = 1;
    status = "Initiated";
    lastModifiedDate = now;
    issueCommencementDate=lastModifiedDate;

  }


  function fetchCPProgramTradeDetails() constant returns (string _programId, string _name, string __type, string _purpose, uint _issueCommencementDate, string _programCurrency, uint _programSize, uint _programAllocatedValue) {
    return (programId, name, _type, purpose, issueCommencementDate, programCurrency, programSize, programAllocatedValue);
  }

  function fetchCPProgramDocuments() constant returns (string _isinGenerationDocId, string _ipaVerificationDocId, string _ipaCertificateDocId, string _corporateActionFormDocId, string _allotmentLetterDocId) {
    return (isinGenerationDocId, ipaVerificationDocId, ipaCertificateDocId, corporateActionFormDocId, allotmentLetterDocId);
  }

  function fetchCPProgramParties() constant returns (address _issuerAddress, address _ipaAddress, address _depositoryAddress) {
    return (issuerAddress, ipaAddress, depositoryAddress);
  }

  function fetchCPProgramStatus() constant returns (uint _version, string _status, uint _lastModifiedDate) {
    return (version, status, lastModifiedDate);
  }

}

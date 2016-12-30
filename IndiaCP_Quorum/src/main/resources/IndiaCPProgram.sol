contract IndiaCPProgram{

  string public programId;
  string public name;
  string public _type;
  string public purpose;
  string public issueCommencementDate;
  string public programCurrency;
  uint public programSize;
  uint public programAllocatedValue;
  uint public maturityDays;

  //Document Hash Identifiers
  string public isinGenerationDocId;
  string public ipaVerificationDocId;
  string public ipaCertificateDocId;
  string public corporateActionFormDocId;
  string public allotmentLetterDocId;

  //Populate through network mapping service
  address public issuerAddress;
  address public ipaAddress;
  address public depositoryAddress;

  //status fields
  uint public version;
  string public status;
  uint public lastModified;

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
    lastModified = now;

  }


  function fetchCPProgramTradeDetails() constant returns (string _programId, string _name, string __type, string _purpose, string issueCommencementDate, string programCurrency, uint programSize, uint programAllocatedValue) {
    return (programId, name, _type, purpose, issueCommencementDate, programCurrency, programSize, programAllocatedValue);
  }

  function fetchCPProgramDocuments() constant returns (string _isinGenerationDocId, string _ipaVerificationDocId, string _ipaCertificateDocId, string _corporateActionFormDocId, string _allotmentLetterDocId) {
    return (isinGenerationDocId, ipaVerificationDocId, ipaCertificateDocId, corporateActionFormDocId, allotmentLetterDocId);
  }

  function fetchCPProgramParties() constant returns (address _issuerAddress, address _ipaAddress, address _depositoryAddress) {
    return (issuerAddress, ipaAddress, depositoryAddress);
  }

  function fetchCPProgramStatus() constant returns (uint _version, string _status, uint _lastModified) {
    return (version, status, lastModified);
  }

}

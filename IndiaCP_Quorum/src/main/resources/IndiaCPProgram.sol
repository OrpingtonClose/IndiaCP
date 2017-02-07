contract IndiaCPProgram{

  bool locked;
  address owner;

  string programId;
  string name;
  string _type;
  string purpose;
  uint issueCommencementDate;
  string programCurrency;
  uint programSize;
  uint programAllocatedValue;
  string memberCPsIssued;

  string isin;
  uint maturityDays;

  //Document Hash Identifiers
  string isinGenerationRequestDocId;
  string ipaVerificationRequestDocId;
  string ipaCertificateDocId;
  string corporateActionFormDocId;
  string allotmentLetterDocId;

  mapping(string => string) docHashMap;

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
    owner = msg.sender;
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
    memberCPsIssued="";

  }

   modifier onlyOwner {
        if (msg.sender != owner)
            throw;
        _
   }

   modifier noReentrancy() {
        if (locked) throw;
        locked = true;
        _
        locked = false;
   }

/*
  function embedDoc(string docName, string docHash) {
    docHashMap[docName] = docHash;
  }
  */

  function setIsinGenerationRequestDocId(string dochash) {
    isinGenerationRequestDocId = dochash;
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

  function issueCP(uint issueVal, string cpAddr) onlyOwner noReentrancy {
    programAllocatedValue = programAllocatedValue - issueVal;
    memberCPsIssued = strConcat(strConcat(memberCPsIssued, " "), cpAddr);
  }

 function kill() onlyOwner { selfdestruct(owner); }


 function strConcat(string _a, string _b) returns (string) {
        bytes memory a = bytes(_a);
        bytes memory b = bytes(_b);
        string memory ab = new string(a.length + b.length);
        bytes memory result = bytes(ab);
        uint k = 0;
        for (uint i = 0; i < a.length; i++) result[k++] = a[i];
        for (i = 0; i < b.length; i++) result[k++] = b[i];
        return string(result);
 }

}

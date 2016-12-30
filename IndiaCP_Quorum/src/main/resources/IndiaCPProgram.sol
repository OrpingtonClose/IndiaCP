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
  //address public ipaAddress;
  //address public depositoryAddress;

  //status fields
  uint public version;
  string public status;
  //uint public lastModified;

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
    //lastModified = now;

  }

  function fetchCPProgram() returns (string _programId, string _name, string __type, string _purpose) {
    return (programId, name, _type, purpose);
  }

}

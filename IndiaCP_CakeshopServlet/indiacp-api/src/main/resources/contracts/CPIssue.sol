contract CPIssue{
string public cpProgramId;
string public cpTradeId;
string public bookId;
string public traderId;
string public valueDate;
string public maturityDate;
uint public faceValue;
uint public rate;
//populated later
string public isin;
string public dealConfirmationDocId;
//default values
uint public tradeDate;
string public currency;
//TODO: to be populated from network mapping service
address public issuerAddress;
address public investorAddress;
address public ipaAddress;
address public depositoryAddress;
//assigned values
string public status;
uint public version;
uint public lastModified;
function CPIssue(string _cpProgramId, string _cpTradeId, string _bookId, string _traderId, string _valueDate, string _maturityDate, uint _faceValue, uint _rate) {
    cpProgramId = _cpProgramId;
    cpTradeId = _cpTradeId;
    bookId = _bookId;
    traderId = _traderId;
    valueDate = _valueDate;
    maturityDate = _maturityDate;
    faceValue = _faceValue;
    rate = _rate;
    //default values
    tradeDate = now;
    currency = "INR";
    //assigned values
    status = "Issued";
    version = 1;
    lastModified = tradeDate;
}
function fetchCPIssue() constant returns (string) {
    return cpTradeId;
}
}
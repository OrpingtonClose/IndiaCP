contract IndiaCPIssue{

string public cpProgramId;
string public cpTradeId;
string public bookId;
string public traderId;
string public valueDate;
string public maturityDate;
uint public notionalAmount;
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

function IndiaCPIssue(string _cpProgramId, string _cpTradeId, string _bookId, string _traderId, string _valueDate, string _maturityDate, uint _notionalAmount) {
    cpProgramId = _cpProgramId;
    cpTradeId = _cpTradeId;
    bookId = _bookId;
    traderId = _traderId;
    valueDate = _valueDate;
    maturityDate = _maturityDate;
    notionalAmount = _notionalAmount;

    //default values
    tradeDate = now;
    currency = "INR";

    //assigned values
    status = "Issued";
    version = 1;
    lastModified = tradeDate;

}

function fetchIndiaCPIssue() constant returns (string _cpTradeId, string _cpProgramId, string _traderId, uint _notionalAmount, uint _rate) {
    return (cpTradeId, cpProgramId, traderId, notionalAmount, rate);
}

function fetchCpIssueTradeId() returns (string _cpTradeId){

    return cpTradeId;
}

}
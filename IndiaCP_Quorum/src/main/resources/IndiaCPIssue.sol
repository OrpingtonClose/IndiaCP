contract IndiaCPIssue{
    
    string cpProgramId;
    string cpTradeId;
    string bookId;
    string traderId;
    string valueDate;
    string maturityDate;
    uint notionalAmount;
    uint rateFloat;
    
    //populated later
    string isin;
    string cpProgramId;
    string cpTradeId;
    string bookId;
    string traderId;
    string valueDate;
    string maturityDate;
    uint notionalAmount;
    uint rateFloat;
    
    //populated later
    string isin;
    string dealConfirmationDocId;
    
    //default values
    uint tradeDate;
    string currency;
    
    //TODO: to be populated from network mapping service
    address issuerAddress;
    address investorAddress;
    address ipaAddress;
    address depositoryAddress;
    
    //assigned values
    string status;
    uint version;
    uint lastModifiedDate;

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
    uint public lastModifiedDate;

    string settlement_details_json;
    
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
    
    function putSettlementDetails (string _details) {
        settlement_details_json = _details;
    }
    
    

}
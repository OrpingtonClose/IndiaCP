package com.barclays.indiacp.quorum.contract;

import com.jpmorgan.cakeshop.client.model.Contract;

/**
 * Created by ritukedia on 28/12/16.
 */
public interface SolidityContractCode {

    public String getContractCode();

    public String getContractBinary();

    public default Contract.CodeTypeEnum getCodeType() {
        return Contract.CodeTypeEnum.SOLIDITY;
    }

    public Object[] getArgs();
}

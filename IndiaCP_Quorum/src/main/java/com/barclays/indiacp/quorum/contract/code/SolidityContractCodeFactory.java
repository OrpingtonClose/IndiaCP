package com.barclays.indiacp.quorum.contract.code;

import java.util.HashMap;

/**
 * Created by ritukedia on 29/12/16.
 */
public class SolidityContractCodeFactory {

    private static HashMap<String, SolidityContractCode> contractCodeMap = new HashMap();

    public static SolidityContractCode getInstance(String contractName) {
        if (!contractCodeMap.containsKey(contractName)) {
            contractCodeMap.put(contractName, SolidityContractCode.getSingleInstance(contractName));
        }
        return contractCodeMap.get(contractName);
    }
}

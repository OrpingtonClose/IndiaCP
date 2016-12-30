package com.barclays.indiacp.quorum.contract.code;

import java.util.HashMap;

/**
 * Created by ritukedia on 29/12/16.
 */
public class SolidityContractCodeFactory {

    private static HashMap<String, SolidityContract> contractCodeMap = new HashMap();

    public static SolidityContract getInstance(String contractName) {
        if (!contractCodeMap.containsKey(contractName)) {
            contractCodeMap.put(contractName, SolidityContract.getSingleInstance(contractName));
        }
        return contractCodeMap.get(contractName);
    }
}

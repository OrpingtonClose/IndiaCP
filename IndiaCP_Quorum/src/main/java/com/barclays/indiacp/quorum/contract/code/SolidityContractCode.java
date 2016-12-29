package com.barclays.indiacp.quorum.contract.code;

import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.model.ContractABI;

import java.util.HashMap;

/**
 * Created by ritukedia on 28/12/16.
 */
public class SolidityContractCode {

    private String contractName;
    private String contractCode;
    private String contractBinary;
    private ContractABI contractABI;

    private static SolidityContractCode singleInstance;

    public static SolidityContractCode getSingleInstance(String contractName) {
        if (singleInstance == null) {
            singleInstance = new SolidityContractCode(contractName);
        }
        return singleInstance;
    }

    private SolidityContractCode(String contractName) {
        //Load contractName.sol

        //Compile contractName.sol

        //populate contract code

        //populate contract binary

        //populate contract abi

    }

    public String getContractCode() {
        return contractCode;
    }

    public String getContractBinary() {
        return contractBinary;
    }

    public ContractABI getContractABI() {
        return contractABI;
    }

    public Contract.CodeTypeEnum getCodeType() {
        return Contract.CodeTypeEnum.SOLIDITY;
    }

    public Object[] getConstructorArgs() {
        //TODO use the ABI and reflection to extract constructor arguments
        return null;
    }
}

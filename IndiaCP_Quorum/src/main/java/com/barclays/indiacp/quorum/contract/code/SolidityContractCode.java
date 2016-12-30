package com.barclays.indiacp.quorum.contract.code;

import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.model.ContractABI;
import com.jpmorgan.cakeshop.model.SolidityType;
import com.jpmorgan.cakeshop.model.SolidityType.StringType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ritukedia on 28/12/16.
 *
 * CONSIDER RENAMING TO SolidityContract
 *
 */
public class SolidityContractCode {

    private Contract contract;
    private String contractName;

    private static SolidityContractCode singleInstance;

    public static SolidityContractCode getSingleInstance(String contractName) {
        if (singleInstance == null) {
            singleInstance = new SolidityContractCode(contractName);
        }
        return singleInstance;
    }

    private SolidityContractCode(String contractName) {
        this.contractName = contractName;
        String code;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(this.contractName+".sol").getFile());
            code = FileUtils.readFileToString(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Compile contractName.sol into a Contract object
        this.contract = CakeshopUtils.compileSolidity(code);
    }

    public String getContractCode() {
        return contract.getCode();
    }

    public String getContractBinary() {
        return contract.getBinary();
    }

    public ContractABI getContractABI() { return ContractABI.fromJson(contract.getAbi()); }

    public Contract.CodeTypeEnum getCodeType() {
        return contract.getCodeType();
    }
}

package com.barclays.indiacp.quorum.contract.code;

import com.barclays.indiacp.quorum.utils.CakeshopUtils;
import com.jpmorgan.cakeshop.client.model.Contract;
import com.jpmorgan.cakeshop.model.ContractABI;
import com.jpmorgan.cakeshop.model.SolidityType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ritukedia on 28/12/16.
 *
 */

public class SolidityContract {

    private Contract contract;
    private String contractName;

    private static SolidityContract singleInstance;

    public static SolidityContract getSingleInstance(String contractName) {
        if (singleInstance == null) {
            singleInstance = new SolidityContract(contractName);
        }
        return singleInstance;
    }

    private SolidityContract(String contractName) {
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

    public Object[] getConstructorArgs(Object contractModel) {
        try {
            List<Object> constructorArgs = new ArrayList<Object>();
            //TODO use the ABI and reflection to extract constructor arguments
            ContractABI.Constructor constructor = this.getContractABI().getConstructor();
            List<ContractABI.Entry.Param> constructorParams = constructor.inputs;
            for (ContractABI.Entry.Param param : constructorParams) {
                String argName = param.getName();
                SolidityType argType = param.getType();
                String getterMethodName = "get" + argName.substring(1,2).toUpperCase() + argName.substring(2,argName.length());

                Method method = contractModel.getClass().getMethod(getterMethodName);
                Object paramValue = method.invoke(contractModel);

                constructorArgs.add(paramValue);
            }
            return constructorArgs.toArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getJavaType(SolidityType solType) {
        switch (solType.getName()) {
            case "string":
                return String.class;
            case "uint256":
                return Integer.class;
            default: return Object.class;
        }
    }
}

package com.barclays.indiacp.quorum.utils;

import com.barclays.indiacp.quorum.contract.code.SolidityContract;
import com.jpmorgan.cakeshop.model.ContractABI;
import com.jpmorgan.cakeshop.model.SolidityType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikhil on 29/12/16.
 */
public class IndiaCPContractUtils {

//    public static CPProgram getContractBy(String cpProgramAddress) {
//
//        return (CPProgram) CakeshopUtils.getContractState(cpProgramAddress);
//    }

    public static Object[] getConstructorArgs(SolidityContract contract, Object contractModel) {
        try {
            List<Object> constructorArgs = new ArrayList<Object>();
            ContractABI.Constructor constructor = contract.getContractABI().getConstructor();
            List<ContractABI.Entry.Param> constructorParams = constructor.inputs;
            for (ContractABI.Entry.Param param : constructorParams) {
                String argName = param.getName();
                SolidityType argType = param.getType();
                String getterMethodName = getGetterMethodName(argName);

                Method method = contractModel.getClass().getMethod(getterMethodName);
                Object paramValue = method.invoke(contractModel);

                constructorArgs.add(getParamValue(paramValue, argType));
            }
            return constructorArgs.toArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getParamValue(Object paramValue, SolidityType argType) {
        if (paramValue == null) {
            if (argType.getClass().equals(SolidityType.StringType.class)) {
                paramValue = "";
            } else {
                paramValue = 0;
            }
        }
        return paramValue;
    }

    private static String getGetterMethodName(String argName) {
        // Param name type is a special variable in Solidity hence the IndiaCP Contract classes have used _type instead.
        // Handling type param differently
        String getterMethodName = null;
        if (argName.equals("__type")) {
            getterMethodName = "getType";
        } else {
            getterMethodName = "get" + argName.substring(1,2).toUpperCase() + argName.substring(2,argName.length());
        }
        return getterMethodName;
    }

    private static String getSetterMethodName(String argName) {
        // Param name type is a special variable in Solidity hence the IndiaCP Contract classes have used _type instead.
        // Handling type param differently
        String getterMethodName = null;
        if (argName.equals("__type")) {
            getterMethodName = "setType";
        } else {
            getterMethodName = "set" + argName.substring(1,2).toUpperCase() + argName.substring(2,argName.length());
        }
        return getterMethodName;
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

    public static <T> T populateContractModel(T contractModelInstance, SolidityContract contract, String functionName, Class<T> contractModel, List<Object> dataAsList) {
        try {
            if(contractModelInstance==null){
                contractModelInstance = instantiateObjectOfType(contractModel);
            }
            ContractABI.Function function = contract.getContractABI().getFunction(functionName);
            List<ContractABI.Entry.Param> functionParams = function.outputs;
            int i = 0;
            for (ContractABI.Entry.Param param : functionParams) {
                String argName = param.getName();
                SolidityType argType = param.getType();
                String setterMethodName = getSetterMethodName(argName);

                Method method = contractModel.getMethod(setterMethodName, getJavaType(argType));
                method.invoke(contractModelInstance, dataAsList.get(i++));
            }
            return contractModelInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T instantiateObjectOfType(Class<T> contractModel) {
        try {
            return (T)Class.forName(contractModel.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

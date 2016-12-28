package com.barclays.indiacp.quorum.contract;

import com.jpmorgan.cakeshop.client.model.Contract;

/**
 * Created by ritukedia on 28/12/16.
 */
public class SSContractCode implements SolidityContractCode {

    private static SSContractCode singleInstance;

    private SSContractCode() {
    }

    public static SSContractCode getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new SSContractCode();
        }
        return singleInstance;
    }

    public String getContractCode () {
        return "contract SimpleStorage {\n" +
                "\n" +
                "    uint public storedData;\n" +
                "\n" +
                "    event Change(string message, uint newVal);\n" +
                "\n" +
                "    function SimpleStorage(uint initVal) {\n" +
                "        Change(\"initialized\", initVal);\n" +
                "        storedData = initVal;\n" +
                "    }\n" +
                "\n" +
                "    function set(uint x) {\n" +
                "        Change(\"set\", x);\n" +
                "        storedData = x;\n" +
                "    }\n" +
                "\n" +
                "    function get() constant returns (uint retVal) {\n" +
                "        return storedData;\n" +
                "    }\n" +
                "\n" +
                "}\n";
    }

    public String getContractBinary() {
        return "60606040526040516020806101f7833981016040528080519060200190919050505b7fbb1cb5be1009ed69d54a8f3da20ed253be50c4dbbcade4f0a12114dedd9be5d78160405180806020018381526020018281038252600b8152602001807f696e697469616c697a65640000000000000000000000000000000000000000008152602001506020019250505060405180910390a1806000600050819055505b50610149806100ae6000396000f360606040526000357c0100000000000000000000000000000000000000000000000000000000900480632a1afcd91461004f57806360fe47b1146100725780636d4ce63c1461008a5761004d565b005b61005c60048050506100ad565b6040518082815260200191505060405180910390f35b61008860048080359060200190919050506100b6565b005b6100976004805050610137565b6040518082815260200191505060405180910390f35b60006000505481565b7fbb1cb5be1009ed69d54a8f3da20ed253be50c4dbbcade4f0a12114dedd9be5d7816040518080602001838152602001828103825260038152602001807f73657400000000000000000000000000000000000000000000000000000000008152602001506020019250505060405180910390a1806000600050819055505b50565b60006000600050549050610146565b9056";
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }

    public String getPrivateFor() {
        return "0x2e219248f44546d966808cdd20cb6c36df6efa82";
    }

}

package main.java.com.barclays.indiacp.client.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.java.com.barclays.indiacp.client.model.Contract;

public class ContractCompileCommand {

    private String code;
    private Contract.CodeTypeEnum codeType;
    private Boolean optimize;

    public ContractCompileCommand() {
    }

    public ContractCompileCommand code(String code) {
        this.code = code;
        return this;
    }

    public ContractCompileCommand codeType(Contract.CodeTypeEnum codeType) {
        this.codeType = codeType;
        return this;
    }

    public ContractCompileCommand optimize(Boolean optimize) {
        this.optimize = optimize;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("code_type")
    public Contract.CodeTypeEnum getCodeType() {
        return codeType;
    }

    public void setCodeType(Contract.CodeTypeEnum codeType) {
        this.codeType = codeType;
    }

    public Boolean getOptimize() {
        return optimize;
    }

    public void setOptimize(Boolean optimize) {
        this.optimize = optimize;
    }

}

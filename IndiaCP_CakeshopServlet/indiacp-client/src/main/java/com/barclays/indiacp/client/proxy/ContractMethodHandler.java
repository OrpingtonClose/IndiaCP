package main.java.com.barclays.indiacp.client.proxy;

import main.java.com.barclays.indiacp.client.ContractUtil;
import main.java.com.barclays.indiacp.client.api.ContractApi;
import main.java.com.barclays.indiacp.client.model.TransactionResult;
import main.java.com.barclays.indiacp.client.model.req.ContractMethodCallCommand;
import main.java.com.barclays.indiacp.client.model.res.APIData;
import main.java.com.barclays.indiacp.client.model.res.APIResponse;
import main.java.com.barclays.indiacp.client.proxy.annotation.Read;
import main.java.com.barclays.indiacp.client.proxy.annotation.Transact;
import com.jpmorgan.cakeshop.model.ContractABI;
import com.jpmorgan.cakeshop.model.ContractABI.Function;

import java.lang.reflect.Method;
import java.util.List;

abstract class ContractMethodHandler {

    static class ReadHandler extends ContractMethodHandler {
        protected ReadHandler(String contractAddress, ContractApi contractApi, Method method, ContractABI abi) {
            super(contractAddress, contractApi, method, abi, method.getAnnotation(Read.class).value());
            this.treatBytesAsStrings = method.getAnnotation(Read.class).treatBytesAsStrings();
        }

        @Override
        public Object invoke(Object[] args) {
            APIResponse<List<Object>, Object> res = contractApi.read(createCall(args));
            return ContractUtil.processOutputArgs(res.getApiData(), function.outputs, treatBytesAsStrings);
        }
    }

    static class TransactHandler extends ContractMethodHandler {
        protected TransactHandler(String contractAddress, ContractApi contractApi, Method method, ContractABI abi) {
            super(contractAddress, contractApi, method, abi, method.getAnnotation(Transact.class).value());
        }

        @Override
        public Object invoke(Object[] args) {
            APIResponse<APIData<TransactionResult>, TransactionResult> res = contractApi.transact(createCall(args));
            return res.getData();
        }
    }

    protected final ContractApi contractApi;
    protected final String contractAddress;
    protected final Method method;
    protected Function function;

    protected String contractMethodName;
    protected boolean treatBytesAsStrings;

    public static ContractMethodHandler create(String contractAddress, ContractApi contractApi, Method method, ContractABI abi) {
        if (method.isAnnotationPresent(Read.class)) {
            return new ReadHandler(contractAddress, contractApi, method, abi);
        } else if (method.isAnnotationPresent(Transact.class)) {
            return new TransactHandler(contractAddress, contractApi, method, abi);
        } else {
            throw new RuntimeException("API method " + method.getName() + " missing one of @Read or @Transact annotations");
        }
    }

    protected ContractMethodHandler(String contractAddress, ContractApi contractApi, Method method, ContractABI abi, String contractMethodName) {
        this.contractApi = contractApi;
        this.contractAddress = contractAddress;
        this.method = method;

        if (contractMethodName != null && !contractMethodName.contentEquals(ValueConstants.DEFAULT_NONE)) {
            this.contractMethodName = contractMethodName;
        } else {
            this.contractMethodName = method.getName();
        }

        this.function = abi.getFunction(this.contractMethodName);
        if (this.function == null) {
            throw new RuntimeException("Contract method '" + this.contractMethodName + "' doesn't exist in the ABI");
        }
    }

    public abstract Object invoke(Object[] args);

    protected ContractMethodCallCommand createCall(Object[] args) {
        ContractUtil.processInputArgs(args);
        return new ContractMethodCallCommand()
                .address(contractAddress)
                .method(method.getName())
                .args(args);
    }
}
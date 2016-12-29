package com.barclays.indiacp.test;

import static org.testng.Assert.*;

import com.barclays.indiacp.model.Contract;
import com.barclays.indiacp.service.ContractService;
import com.barclays.indiacp.service.GethHttpService;
import com.barclays.indiacp.service.TransactionService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

@Test(enabled=false)
public class Util extends BaseGethRpcTest {

	@Autowired
    ContractService contractService;

	@Autowired
    TransactionService transactionService;

	@Autowired
    GethHttpService geth;

	/**
	 * It turns out that the compiled binary code (using solc) differs from the
	 * binary code as deployed to the chain and retrieved using eth_getCode. This
	 * helper method assists in retrieving the deployed version for use in, e.g.,
	 * the genesis block file.
	 *
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(enabled=false)
	public void testGetDeployedBinaryForContract() throws IOException, InterruptedException {

	    String code = readTestFile("contracts/ContractRegistry.sol");

		String contractAddress = createContract(code, null);

		Contract contract = contractService.get(contractAddress);
		assertNotNull(contract);
		assertNotNull(contract.getBinary(), "Binary code should be present");

		System.out.println("BINARY CODE:");
		System.out.println(contract.getBinary());
	}

}

package com.barclays.indiacp.service.task;

import com.barclays.indiacp.error.APIException;
import com.barclays.indiacp.service.ContractRegistryService;
import com.barclays.indiacp.service.GethHttpService;
import com.barclays.indiacp.service.QuorumService;
import com.barclays.indiacp.service.TransactionService;
import com.barclays.indiacp.util.StringUtils;
import com.barclays.indiacp.bean.GethConfigBean;
import com.barclays.indiacp.dao.WalletDAO;
import com.barclays.indiacp.model.Account;
import com.barclays.indiacp.model.TransactionResult;
import com.barclays.indiacp.service.BlockService;
import com.barclays.indiacp.service.ContractService;
import com.barclays.indiacp.service.ContractService.CodeType;
import com.barclays.indiacp.service.NodeService;
import com.barclays.indiacp.service.WalletService;
import com.barclays.indiacp.util.CakeshopUtils;
import com.barclays.indiacp.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BlockchainInitializerTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(BlockchainInitializerTask.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private GethHttpService geth;

    @Autowired
    private ContractRegistryService contractRegistry;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletDAO walletDAO;

    @Value("${contract.registry.addr:}")
    private String contractRegistryAddress;

    @Autowired
    private GethConfigBean gethConfig;

    @Autowired
    private QuorumService quorumService;

    @Override
    public void run() {

        quorumService.isQuorum(); // discover quorum state

        deployContractRegistry();

        syncWalletDb();

    }

    private void syncWalletDb() {

        if (!gethConfig.isDbEnabled()) {
            return;
        }

        LOG.info("Storing existing wallet accounts");
        try {
            List<Account> list = walletService.list();
            for (Account account : list) {
                walletDAO.save(account);
            }
        } catch (APIException e) {
            LOG.error("Error reading local wallet", e);
        }

    }

    /**
     * Deploy ContractRegistry (and SimpleStorage) if they don't already exist on the chain
     */
    private void deployContractRegistry() {

        // Read reg addr from file
        String regAddr = getSharedNetworkConfig();
        if (StringUtils.isNotBlank(regAddr)) {
            LOG.info("Overriding contract registry address from shared config " + regAddr);
            contractRegistryAddress = regAddr;
        }

        // Read from env
        regAddr = System.getenv("CAKESHOP_REGISTRY_ADDR");
        if (StringUtils.isNotBlank(regAddr)) {
            LOG.info("Overriding contract registry address with " + regAddr);
            contractRegistryAddress = regAddr;
        }

        if (StringUtils.isNotBlank(contractRegistryAddress)) {
            Map<String, Object> contractRes;
            try {
                contractRes = geth.executeGethCall("eth_getCode", new Object[]{contractRegistryAddress, "latest"});
            } catch (APIException e) {
                LOG.error("Failed to verify contract " + contractRegistryAddress, e);
                return;
            }

            String binaryCode = (String) contractRes.get("_result");
            if (!binaryCode.contentEquals("0x")) {
                try {
                    contractRegistry.updateRegistryAddress(this.contractRegistryAddress);
                } catch (APIException e) {
                }
                return; // already exists on chain
            }

            // continue to deploy
        }

        LOG.info("Initializing empty blockchain");
        try {
            nodeService.update(null, null, null, true, null, null); // make sure mining is enabled

            LOG.info("Deploying ContractRegistry to chain");
            contractRegistry.deploy();

        } catch (APIException e) {
            LOG.error("Error deploying ContractRegistry to chain: " + e.getMessage(), e);
        }


        LOG.info("Deploying sample contract (SimpleStorage) to chain");
        try {
            String code = FileUtils.readClasspathFile("contracts/SimpleStorage.sol");
            TransactionResult txr = contractService.create(null, code, CodeType.solidity, null, null, null, null);
            transactionService.waitForTx(txr, 200, TimeUnit.MILLISECONDS);

        } catch (IOException | InterruptedException e) {
            LOG.error("Error deploying SimpleStorage to chain: " + e.getMessage(), e);
        }

    }

    /**
     * Get the shared contract registry address, if configured
     *
     * @return String   shared registry address
     */
    private String getSharedNetworkConfig() {

        // TODO this is a temp solution to the problem of sharing the ContractRegistry
        // address among multiple Cakeshop nodes running on the same machine.

        File fSharedConfig = CakeshopUtils.getSharedNetworkConfigFile();
        if (fSharedConfig == null) {
            return null;
        }

        if (!fSharedConfig.exists()) {
            LOG.debug("CAKESHOP_SHARED_CONFIG file not found: " + fSharedConfig.toString());
            return null; // not found, skip it
        }

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(fSharedConfig));
        } catch (IOException e) {
            LOG.warn("Error loading CAKESHOP_SHARED_CONFIG at " + fSharedConfig.toString() + ": " + e.getMessage(), e);
            return null;
        }

        String addr = (String) props.get("contract.registry.addr");
        if (StringUtils.isNotBlank(addr)) {
            return addr;
        }

        return null;
    }


}

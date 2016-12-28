package com.barclays.indiacp.quorum.contract;

import com.jpmorgan.cakeshop.client.model.TransactionResult;
import com.jpmorgan.cakeshop.client.proxy.ContractProxyBuilder;
import com.jpmorgan.cakeshop.client.proxy.annotation.Read;
import com.jpmorgan.cakeshop.client.proxy.annotation.Transact;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This Contract implementation uses the {@link ContractProxyBuilder} to dynamically build
 * an implementation at runtime. The returned implementation instance is fully thread-safe and
 * should be cached and reused.
 *
 * @author Chetan Sarva
 *
 */
@Path("simplestoragecontract")
public interface SimpleStorageContractApi {

    @GET
    @Path("get/{contractAddress}")
    @Read
    public List<Object> get();

    @POST
    @Path("set/{contractAddress}/{value}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transact
    public TransactionResult set(@PathParam("cpIssueId") String val);

}

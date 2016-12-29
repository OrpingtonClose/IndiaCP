package com.barclays.indiacp.service;

import com.barclays.indiacp.model.QuorumInfo;
import com.barclays.indiacp.error.APIException;

public interface QuorumService {

    public boolean isQuorum();

    public QuorumInfo getQuorumInfo() throws APIException;

}

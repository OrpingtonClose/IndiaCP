package com.barclays.indiacp.reference.data.service;

import com.barclays.indiacp.reference.data.dao.SettlementDetailsDAO;
import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by chaos on 22/12/16.
 */

@Component
public class SettlementDetailsService
{

    @Autowired
    private SettlementDetailsDAO settlementDetailsDAO;

    @Transactional
    public void add(SettlementDetails settlementDetails) {
        settlementDetailsDAO.persist(settlementDetails);
    }

    @Transactional
    public void addAll(Collection<SettlementDetails> settlementDetailsList) {
        for (SettlementDetails settlementDetails : settlementDetailsList) {
            settlementDetailsDAO.persist(settlementDetails);
        }
    }

    @Transactional
    public List<SettlementDetails> getCount()
    {
       return settlementDetailsDAO.getCount();
    }

    @Transactional(readOnly = true)
    public List<SettlementDetails> listAll() {
        return settlementDetailsDAO.findAll();

    }

    @Transactional(readOnly = true)
    public List<SettlementDetails> getSettlementDetailsForEntity(String legalEntityID) {
        return settlementDetailsDAO.getSettlementDetailsForEntity(Integer.parseInt(legalEntityID));

    }


}

package com.barclays.indiacp.reference.data.service;

import com.barclays.indiacp.reference.data.dao.LegalEntityDAO;
import com.barclays.indiacp.reference.data.dao.SettlementDetailsDAO;
import com.barclays.indiacp.reference.data.dao.UserDetailsDAO;
import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@Component
public class LegalEntityService
{

    @Autowired
    private LegalEntityDAO legalEntityDAO;

    @Transactional
    public void add(LegalEntity legalEntity) {
        legalEntityDAO.persist(legalEntity);
    }

    @Transactional
    public void addAll(Collection<LegalEntity> legalEntityCollection) {
        for (LegalEntity legalEntity : legalEntityCollection) {
            legalEntityDAO.persist(legalEntity);
        }
    }

    @Transactional(readOnly = true)
    public List<LegalEntity> listAll() {
        return legalEntityDAO.findAll();

    }

    @Transactional(readOnly = true)
    public LegalEntity getLegalEntityByID(String legalEntityId) {
        return legalEntityDAO.getLegalEntityByID(legalEntityId);

    }

    @Transactional(readOnly = true)
    public Integer getLegalEntityByName(String legalEntityName) {
        return legalEntityDAO.getLegalEntityByName(legalEntityName);
    }



}
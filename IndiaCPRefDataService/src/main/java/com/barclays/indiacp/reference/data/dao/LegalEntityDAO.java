package com.barclays.indiacp.reference.data.dao;

import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;



@Component
public class LegalEntityDAO
{

    @PersistenceContext
    private EntityManager em;

    public LegalEntity getLegalEntityByID(String legalEntityId)
    {
        LegalEntity le = null;

        List<LegalEntity> list = em.createQuery("SELECT l FROM LegalEntity l where legalEntityId =" + legalEntityId).getResultList();

        if(list!=null && list.size()>0)
        {
            le = list.get(0);
        }
        return le;
    }



    public List<LegalEntity> findAll() {
        return em.createQuery("SELECT l FROM LegalEntity l").getResultList();
    }
    public void persist(LegalEntity legalEntityList) {
        em.persist(legalEntityList);
    }

}
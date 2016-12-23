package com.barclays.indiacp.reference.data.dao;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by chaos on 22/12/16.
 */

@Component
public class SettlementDetailsDAO
{

    @PersistenceContext
    private EntityManager em;

    public List<SettlementDetails> getCount() {
        return em.createQuery("SELECT count(*) FROM SettlementDetails").getResultList();
    }

    public void persist(SettlementDetails settlementDetails) {
        em.persist(settlementDetails);
    }

    public List<SettlementDetails> findAll() {
        return em.createQuery("SELECT s FROM SettlementDetails s").getResultList();
    }

}

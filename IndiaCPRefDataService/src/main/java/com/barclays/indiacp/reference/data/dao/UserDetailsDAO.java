package com.barclays.indiacp.reference.data.dao;

import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Component
public class UserDetailsDAO
{

    @PersistenceContext
    private EntityManager em;

    public List<UserDetails> getUserDetailsForEntity(String legalEntityId) {
        return em.createQuery("SELECT * FROM UserDetail where legalEntityId =" + legalEntityId).getResultList();
    }

    public void persist(UserDetails userDetailsList) {
        em.persist(userDetailsList);
    }

    public List<UserDetails> userDetailByDept(String dept) {
        return em.createQuery("SELECT * FROM UserDetail where dept = " + dept).getResultList();
    }

}
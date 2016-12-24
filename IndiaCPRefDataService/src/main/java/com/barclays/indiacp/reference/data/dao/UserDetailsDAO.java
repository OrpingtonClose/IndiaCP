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

    public List<UserDetails> getUserDetailsForEntity(Integer legalEntityId) {
        return em.createQuery("SELECT u FROM UserDetails u where legal_entity_id =" + legalEntityId).getResultList();
    }

    public void persist(UserDetails userDetailsList) {

        try{
            em.persist(userDetailsList);
        }
        finally{
            em.flush();
        }
    }

    public List<UserDetails> userDetailByDept(String dept) {
        return em.createQuery("SELECT u FROM UserDetails u where dept = " + dept).getResultList();
    }

    public List<UserDetails> findAll() {
        return em.createQuery("SELECT u FROM UserDetails u").getResultList();
    }
}
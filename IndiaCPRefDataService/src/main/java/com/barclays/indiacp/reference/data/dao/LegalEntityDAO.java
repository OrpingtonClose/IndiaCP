package com.barclays.indiacp.reference.data.dao;

import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.SettlementDetails;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;



@Component
public class LegalEntityDAO
{

    @PersistenceContext
    private EntityManager em;


    @Autowired
    UserDetailsDAO userDetailsDAO;

    @Autowired
    SettlementDetailsDAO settlementDetailsDAO;



    public LegalEntity getLegalEntityByID(String legalEntityId)
    {
        LegalEntity le = null;


        List<LegalEntity> list = em.createQuery("FROM LegalEntity l where l.legal_entity_id =" + legalEntityId).getResultList();

        if(list!=null && list.size()>0)
        {
            le = list.get(0);
            List<UserDetails> uarr = userDetailsDAO.getUserDetailsForEntity(le.getLegal_entity_id());
            le.setUserDetails(uarr);

            List<SettlementDetails> sarr = settlementDetailsDAO.getSettlementDetailsForEntity(le.getLegal_entity_id());
            le.setSettlementDetails(sarr);
        }


        return le;
    }

    public Integer getLegalEntityByName(String legalEntityName)
    {
        LegalEntity le = null;
        Integer legalEntityId = 0;
        Query query = em.createQuery("FROM LegalEntity l where l.legal_entity_name = :legalEntityName");
        query.setParameter("legalEntityName", legalEntityName );
        List<LegalEntity> list = query.getResultList();
        //List<LegalEntity> list = em.createQuery("SELECT l FROM LegalEntity l where l.legal_entity_name =:" + legalEntityName).getResultList();

        if(list!=null && list.size()>0)
        {
            le = list.get(0);
            legalEntityId = le.getLegal_entity_id();

        }


        return legalEntityId;
    }


    public List<LegalEntity> findAll()
    {
//        SELECT emp FROM Employee emp JOIN FETCH emp.address
        List<LegalEntity> arr = em.createQuery("SELECT l FROM LegalEntity l").getResultList();

        for(LegalEntity le : arr)
        {
            List<UserDetails> uarr = userDetailsDAO.getUserDetailsForEntity(le.getLegal_entity_id());
            le.setUserDetails(uarr);

            List<SettlementDetails> sarr = settlementDetailsDAO.getSettlementDetailsForEntity(le.getLegal_entity_id());
            le.setSettlementDetails(sarr);
        }

        return arr;
//        List<LegalEntity> list = em.find(LegalEntity.class, )
    }

    public void persist(LegalEntity legalEntity) {

        try
        {
            em.persist(legalEntity);
            Integer legalEntityId = getLegalEntityByName(legalEntity.getLegal_entity_name());
            if (legalEntity.getUserDetails() != null) {
                for (UserDetails u : legalEntity.getUserDetails()) {
                    u.setLegal_entity_id(legalEntityId);
                    userDetailsDAO.persist(u);
                }
            }

            if (legalEntity.getSettlementDetails() != null) {
                for (SettlementDetails s : legalEntity.getSettlementDetails()) {
                    s.setLegal_entity_id(legalEntityId);
                    settlementDetailsDAO.persist(s);
                }
            }
            em.flush();

         }
        finally{

        }
    }

}
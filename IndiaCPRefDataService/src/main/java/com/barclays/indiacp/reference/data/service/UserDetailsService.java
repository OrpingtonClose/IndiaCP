package com.barclays.indiacp.reference.data.service;

import com.barclays.indiacp.reference.data.dao.LegalEntityDAO;
import com.barclays.indiacp.reference.data.dao.UserDetailsDAO;
import com.barclays.indiacp.reference.data.entities.LegalEntity;
import com.barclays.indiacp.reference.data.entities.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Electrania.com on 12/23/2016.
 */
@Component
public class UserDetailsService {

    @Autowired
    private UserDetailsDAO userDetailsDAO;

    @Transactional
    public void add(UserDetails userDetails) {
        userDetailsDAO.persist(userDetails);
    }

    @Transactional
    public void addAll(Collection<UserDetails> userDetailsCollection) {
        for (UserDetails userDetails : userDetailsCollection) {
            userDetailsDAO.persist(userDetails);
        }
    }

    @Transactional(readOnly = true)
    public List<UserDetails> listAll() {
        return userDetailsDAO.findAll();

    }

}

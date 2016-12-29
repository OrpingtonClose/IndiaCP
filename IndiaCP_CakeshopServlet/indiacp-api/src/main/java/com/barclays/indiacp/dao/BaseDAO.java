package com.barclays.indiacp.dao;

import com.barclays.indiacp.config.rdbms.AbstractDataSourceConfig;
import com.barclays.indiacp.util.StringUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseDAO {

    @Value(value = "${" + AbstractDataSourceConfig.JDBC_BATCH_SIZE + "}:20")
    private String batchSize;

    @Autowired(required=false)
    private SessionFactory sessionFactory;

    protected final Integer BATCH_SIZE = StringUtils.isNotBlank(System.getProperty(AbstractDataSourceConfig.JDBC_BATCH_SIZE))
            ? Integer.valueOf(System.getProperty(AbstractDataSourceConfig.JDBC_BATCH_SIZE))
            : StringUtils.isNotBlank(batchSize) ? Integer.valueOf(batchSize)
            : 20;

    protected Session getCurrentSession() {
        Session session = null != sessionFactory ?  sessionFactory.getCurrentSession() : null;
        return session;
    }

    public abstract void reset();

}

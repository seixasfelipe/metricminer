package org.metricminer.infra.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.BeforeClass;

public class DaoTest {
    
    protected static Session session;
    
    @BeforeClass
    public static void setUpClass() {
        SessionFactory sessionFactory = new Configuration().configure(
                "/hibernate.test.cfg.xml").buildSessionFactory();
        session = sessionFactory.openSession();
    }
}

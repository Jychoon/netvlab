package com.netvlab.dao.impl;

import com.netvlab.dao.TabExperimentRecordDao;
import com.netvlab.model.experimentmanage.TabExperimentrecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Repository("tabExperimentRecordDao")
public class TabExperimentRecordDaoImpl implements TabExperimentRecordDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public List<TabExperimentrecord> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from TabExperimentrecord");
        return query.list();
    }
}

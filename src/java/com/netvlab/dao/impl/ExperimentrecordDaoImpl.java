package com.netvlab.dao.impl;

import com.netvlab.dao.ExperimentrecordDao;
import com.netvlab.model.Experimentrecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository("experimentrecordDao")
public class ExperimentrecordDaoImpl implements ExperimentrecordDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void saveExperimentrecord(Experimentrecord experimentrecord) {
        Session session = sessionFactory.getCurrentSession();
        session.save(experimentrecord);
        return;
    }

    @Override
    public List<Experimentrecord> getExprimentrecordByUserName(String userName) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Experimentrecord where userName = ?1");
        query.setParameter(1,userName);
        List<Experimentrecord> res = query.list();
        return res;
    }
}

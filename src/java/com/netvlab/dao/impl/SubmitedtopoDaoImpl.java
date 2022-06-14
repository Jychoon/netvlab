package com.netvlab.dao.impl;

import com.netvlab.dao.SubmitedtopoDao;
import com.netvlab.model.Submitedtopo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
@Repository("submitedtopoDao")
public class SubmitedtopoDaoImpl implements SubmitedtopoDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void saveTopo(Submitedtopo submitedtopo) {
        Session session = sessionFactory.getCurrentSession();
        session.save(submitedtopo);
        return;
    }

    @Override
    public void deleteByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete  Submitedtopo where userId = ?1");
        query.setParameter(1,userId);
        query.executeUpdate();
    }

    @Override
    public List<Submitedtopo> getToposByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Submitedtopo where userId = ?1");
        query.setParameter(1,userId);
        List<Submitedtopo> res = query.list();
        return res;
    }
}

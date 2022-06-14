package com.netvlab.dao.impl;

import com.netvlab.dao.UserDao;
import com.netvlab.model.Userinfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    public Userinfo getUser(Integer uid) {
        Session session = sessionFactory.getCurrentSession();
        Userinfo user = session.get(Userinfo.class,uid);
        return user;
    }

    public List<Userinfo> getUserByStudentId(String stuid) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Userinfo where studentId = ?1");
        query.setParameter(1,stuid);
        List<Userinfo> res = query.list();
        return res;
    }

    public String saveUser(Userinfo user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        return user.getStudentId();
    }

    public List<Userinfo> getAllUser() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Userinfo");
        return query.list();
    }
}

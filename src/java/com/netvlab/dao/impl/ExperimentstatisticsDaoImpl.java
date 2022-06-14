package com.netvlab.dao.impl;

import com.netvlab.dao.ExperimentstatisticsDao;
import com.netvlab.model.Userinfo;
import com.netvlab.model.experimentmanage.Experimentstatistics;
import com.netvlab.model.experimentmanage.TabExperimentrecord;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Repository("experimentstatisticsDao")
public class ExperimentstatisticsDaoImpl implements ExperimentstatisticsDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void saveExperimentstatistics(Experimentstatistics experimentstatistics) {
        Session session = sessionFactory.getCurrentSession();
        session.save(experimentstatistics);
        return;
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Experimentstatistics getExpByUserName(String userName) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Experimentstatistics where userName = ?1");
        query.setParameter(1,userName);
        List<Experimentstatistics> res = query.list();
        if(res.size() == 0) return null;
        else return res.get(0);
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void updateExp(TabExperimentrecord tabExperimentrecord) {
        Experimentstatistics experimentstatistics = getExpByUserName(tabExperimentrecord.getUserName());
        if(experimentstatistics == null){
            experimentstatistics = new Experimentstatistics();
            experimentstatistics.setUserName(tabExperimentrecord.getUserName());
            experimentstatistics.setConfigureHttpServer("0/0");
            experimentstatistics.setEthernetFrame("0/0");
            experimentstatistics.setArp("0/0");
            experimentstatistics.setiPandIcmp("0/0");
            experimentstatistics.setRoutingTableLearning("0/0");
            experimentstatistics.setRip("0/0");
            experimentstatistics.setOspf("0/0");
            experimentstatistics.setTcp("0/0");
            experimentstatistics.setHttp("0/0");
            experimentstatistics.setFtp("0/0");
            experimentstatistics.setDns("0/0");
            experimentstatistics.setSmtp("0/0");
            saveExperimentstatistics(experimentstatistics);
            experimentstatistics = getExpByUserName(tabExperimentrecord.getUserName());
        }
        int totaltime = tabExperimentrecord.getTotalTime();
        String[] oldstr;
        int timesnew;
        int totaltimesnew;
        switch (tabExperimentrecord.getExperimentName()){
            case "配置http服务器":
                oldstr = experimentstatistics.getConfigureHttpServer().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setConfigureHttpServer(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "以太网帧分析":
                oldstr = experimentstatistics.getEthernetFrame().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setEthernetFrame(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "ARP":
                oldstr = experimentstatistics.getArp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setArp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "IP/ICMP":
                oldstr = experimentstatistics.getiPandIcmp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setiPandIcmp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "路由表学习与配置":
                oldstr = experimentstatistics.getRoutingTableLearning().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setRoutingTableLearning(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "RIP":
                oldstr = experimentstatistics.getRip().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setRip(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "OSPF":
                oldstr = experimentstatistics.getOspf().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setOspf(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "TCP协议分析":
                oldstr = experimentstatistics.getTcp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setTcp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "HTTP":
                oldstr = experimentstatistics.getHttp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setHttp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "FTP":
                oldstr = experimentstatistics.getFtp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setFtp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "DNS":
                oldstr = experimentstatistics.getDns().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setDns(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
            case "SMTP":
                oldstr = experimentstatistics.getSmtp().split("/");
                timesnew = Integer.parseInt(oldstr[0]) + 1;
                totaltimesnew = Integer.parseInt(oldstr[1]) + totaltime;
                experimentstatistics.setSmtp(Integer.toString(timesnew) + "/" + Integer.toString(totaltimesnew));
                break;
        }
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("update Experimentstatistics e set e.configureHttpServer=:configureHttpServer,e.ethernetFrame=:ethernetFrame,e.arp=:arp,e.iPandIcmp=:iPandIcmp,e.routingTableLearning=:routingTableLearning,e.rip=:rip,e.ospf=:ospf,e.tcp=:tcp,e.http=:http,e.ftp=:ftp,e.dns=:dns,e.smtp=:smtp where e.userName=:userName");
        query.setProperties(experimentstatistics);
        query.executeUpdate();
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void updateOthers() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Userinfo");
        List<Userinfo> userinfos = query.list();
        System.out.println("size:    "+userinfos.size());
        session.close();
        Experimentstatistics experimentstatistics = new Experimentstatistics();
        for(Userinfo userinfo : userinfos){
            experimentstatistics.setUserName(userinfo.getName());
            experimentstatistics.setConfigureHttpServer("0/0");
            experimentstatistics.setEthernetFrame("0/0");
            experimentstatistics.setArp("0/0");
            experimentstatistics.setiPandIcmp("0/0");
            experimentstatistics.setRoutingTableLearning("0/0");
            experimentstatistics.setRip("0/0");
            experimentstatistics.setOspf("0/0");
            experimentstatistics.setTcp("0/0");
            experimentstatistics.setHttp("0/0");
            experimentstatistics.setFtp("0/0");
            experimentstatistics.setDns("0/0");
            experimentstatistics.setSmtp("0/0");
            session.save(experimentstatistics);
        }
    }
}

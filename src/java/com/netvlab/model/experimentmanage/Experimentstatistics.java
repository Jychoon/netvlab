package com.netvlab.model.experimentmanage;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Experimentstatistics {
    private int id;
    private String userName;
    private String configureHttpServer;
    private String ethernetFrame;
    private String arp;
    private String iPandIcmp;
    private String routingTableLearning;
    private String rip;
    private String ospf;
    private String tcp;
    private String http;
    private String ftp;
    private String dns;
    private String smtp;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UserName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "ConfigureHttpServer")
    public String getConfigureHttpServer() {
        return configureHttpServer;
    }

    public void setConfigureHttpServer(String configureHttpServer) {
        this.configureHttpServer = configureHttpServer;
    }

    @Basic
    @Column(name = "EthernetFrame")
    public String getEthernetFrame() {
        return ethernetFrame;
    }

    public void setEthernetFrame(String ethernetFrame) {
        this.ethernetFrame = ethernetFrame;
    }

    @Basic
    @Column(name = "ARP")
    public String getArp() {
        return arp;
    }

    public void setArp(String arp) {
        this.arp = arp;
    }

    @Basic
    @Column(name = "IPandICMP")
    public String getiPandIcmp() {
        return iPandIcmp;
    }

    public void setiPandIcmp(String iPandIcmp) {
        this.iPandIcmp = iPandIcmp;
    }

    @Basic
    @Column(name = "RoutingTableLearning")
    public String getRoutingTableLearning() {
        return routingTableLearning;
    }

    public void setRoutingTableLearning(String routingTableLearning) {
        this.routingTableLearning = routingTableLearning;
    }

    @Basic
    @Column(name = "RIP")
    public String getRip() {
        return rip;
    }

    public void setRip(String rip) {
        this.rip = rip;
    }

    @Basic
    @Column(name = "OSPF")
    public String getOspf() {
        return ospf;
    }

    public void setOspf(String ospf) {
        this.ospf = ospf;
    }

    @Basic
    @Column(name = "TCP")
    public String getTcp() {
        return tcp;
    }

    public void setTcp(String tcp) {
        this.tcp = tcp;
    }

    @Basic
    @Column(name = "HTTP")
    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    @Basic
    @Column(name = "FTP")
    public String getFtp() {
        return ftp;
    }

    public void setFtp(String ftp) {
        this.ftp = ftp;
    }

    @Basic
    @Column(name = "DNS")
    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    @Basic
    @Column(name = "SMTP")
    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Experimentstatistics that = (Experimentstatistics) o;

        if (id != that.id) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (configureHttpServer != null ? !configureHttpServer.equals(that.configureHttpServer) : that.configureHttpServer != null)
            return false;
        if (ethernetFrame != null ? !ethernetFrame.equals(that.ethernetFrame) : that.ethernetFrame != null)
            return false;
        if (arp != null ? !arp.equals(that.arp) : that.arp != null) return false;
        if (iPandIcmp != null ? !iPandIcmp.equals(that.iPandIcmp) : that.iPandIcmp != null) return false;
        if (routingTableLearning != null ? !routingTableLearning.equals(that.routingTableLearning) : that.routingTableLearning != null)
            return false;
        if (rip != null ? !rip.equals(that.rip) : that.rip != null) return false;
        if (ospf != null ? !ospf.equals(that.ospf) : that.ospf != null) return false;
        if (tcp != null ? !tcp.equals(that.tcp) : that.tcp != null) return false;
        if (http != null ? !http.equals(that.http) : that.http != null) return false;
        if (ftp != null ? !ftp.equals(that.ftp) : that.ftp != null) return false;
        if (dns != null ? !dns.equals(that.dns) : that.dns != null) return false;
        if (smtp != null ? !smtp.equals(that.smtp) : that.smtp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (configureHttpServer != null ? configureHttpServer.hashCode() : 0);
        result = 31 * result + (ethernetFrame != null ? ethernetFrame.hashCode() : 0);
        result = 31 * result + (arp != null ? arp.hashCode() : 0);
        result = 31 * result + (iPandIcmp != null ? iPandIcmp.hashCode() : 0);
        result = 31 * result + (routingTableLearning != null ? routingTableLearning.hashCode() : 0);
        result = 31 * result + (rip != null ? rip.hashCode() : 0);
        result = 31 * result + (ospf != null ? ospf.hashCode() : 0);
        result = 31 * result + (tcp != null ? tcp.hashCode() : 0);
        result = 31 * result + (http != null ? http.hashCode() : 0);
        result = 31 * result + (ftp != null ? ftp.hashCode() : 0);
        result = 31 * result + (dns != null ? dns.hashCode() : 0);
        result = 31 * result + (smtp != null ? smtp.hashCode() : 0);
        return result;
    }
}

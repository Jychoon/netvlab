package com.netvlab.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Submitedtopo {
    private int id;
    private Integer userId;
    private String networksJson;
    private String subnetsJson;
    private String serversJson;
    private String submitTime;

    @Id
    @Column(name = "Id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UserID")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "NetworksJSON")
    public String getNetworksJson() {
        return networksJson;
    }

    public void setNetworksJson(String networksJson) {
        this.networksJson = networksJson;
    }

    @Basic
    @Column(name = "SubnetsJSON")
    public String getSubnetsJson() {
        return subnetsJson;
    }

    public void setSubnetsJson(String subnetsJson) {
        this.subnetsJson = subnetsJson;
    }

    @Basic
    @Column(name = "ServersJSON")
    public String getServersJson() {
        return serversJson;
    }

    public void setServersJson(String serversJson) {
        this.serversJson = serversJson;
    }

    @Basic
    @Column(name = "submitTime")
    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Submitedtopo that = (Submitedtopo) o;

        if (id != that.id) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (networksJson != null ? !networksJson.equals(that.networksJson) : that.networksJson != null) return false;
        if (subnetsJson != null ? !subnetsJson.equals(that.subnetsJson) : that.subnetsJson != null) return false;
        if (serversJson != null ? !serversJson.equals(that.serversJson) : that.serversJson != null) return false;
        if (submitTime != null ? !submitTime.equals(that.submitTime) : that.submitTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (networksJson != null ? networksJson.hashCode() : 0);
        result = 31 * result + (subnetsJson != null ? subnetsJson.hashCode() : 0);
        result = 31 * result + (serversJson != null ? serversJson.hashCode() : 0);
        result = 31 * result + (submitTime != null ? submitTime.hashCode() : 0);
        return result;
    }
}

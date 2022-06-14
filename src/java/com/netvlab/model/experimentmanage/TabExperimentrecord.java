package com.netvlab.model.experimentmanage;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "tab_experimentrecord", schema = "vnetwork", catalog = "")
public class TabExperimentrecord {
    private int id;
    private String userName;
    private String experimentName;
    private String occupySource;
    private Timestamp submitTime;
    private Timestamp releaseTime;
    private Integer totalTime;

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
    @Column(name = "ExperimentName")
    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    @Basic
    @Column(name = "OccupySource")
    public String getOccupySource() {
        return occupySource;
    }

    public void setOccupySource(String occupySource) {
        this.occupySource = occupySource;
    }

    @Basic
    @Column(name = "SubmitTime")
    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }

    @Basic
    @Column(name = "ReleaseTime")
    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Basic
    @Column(name = "TotalTime")
    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabExperimentrecord that = (TabExperimentrecord) o;

        if (id != that.id) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (experimentName != null ? !experimentName.equals(that.experimentName) : that.experimentName != null)
            return false;
        if (occupySource != null ? !occupySource.equals(that.occupySource) : that.occupySource != null) return false;
        if (submitTime != null ? !submitTime.equals(that.submitTime) : that.submitTime != null) return false;
        if (releaseTime != null ? !releaseTime.equals(that.releaseTime) : that.releaseTime != null) return false;
        if (totalTime != null ? !totalTime.equals(that.totalTime) : that.totalTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (experimentName != null ? experimentName.hashCode() : 0);
        result = 31 * result + (occupySource != null ? occupySource.hashCode() : 0);
        result = 31 * result + (submitTime != null ? submitTime.hashCode() : 0);
        result = 31 * result + (releaseTime != null ? releaseTime.hashCode() : 0);
        result = 31 * result + (totalTime != null ? totalTime.hashCode() : 0);
        return result;
    }
}

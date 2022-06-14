package com.netvlab.dao;

import com.netvlab.model.Userinfo;
import com.netvlab.model.experimentmanage.Experimentstatistics;
import com.netvlab.model.experimentmanage.TabExperimentrecord;

import java.util.List;

public interface ExperimentstatisticsDao {
    public void saveExperimentstatistics(Experimentstatistics experimentstatistics);

    public Experimentstatistics getExpByUserName(String userName);

    public void updateExp(TabExperimentrecord tabExperimentrecord);

    public void updateOthers();
}

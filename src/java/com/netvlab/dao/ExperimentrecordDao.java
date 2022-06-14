package com.netvlab.dao;

import com.netvlab.model.Experimentrecord;

import java.util.List;

public interface ExperimentrecordDao {
    void saveExperimentrecord(Experimentrecord experimentrecord);
    List<Experimentrecord> getExprimentrecordByUserName(String userName);
}

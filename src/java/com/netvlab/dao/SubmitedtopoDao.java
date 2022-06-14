package com.netvlab.dao;

import com.netvlab.model.Submitedtopo;

import java.util.List;

public interface SubmitedtopoDao {
    void saveTopo(Submitedtopo submitedtopo);
    List<Submitedtopo> getToposByUserId(int userId);
    void deleteByUserId(int userId);
}

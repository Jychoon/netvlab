package com.netvlab.service.impl;

import com.netvlab.dao.SubmitedtopoDao;
import com.netvlab.model.Submitedtopo;
import com.netvlab.service.SubmitTopoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("submitTopoService")
public class SubmitTopoServiceImpl implements SubmitTopoService {
    @Resource
    private SubmitedtopoDao submitedtopoDao;
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void SaveSubmitTopo(Submitedtopo submitedtopo) {
        submitedtopoDao.saveTopo(submitedtopo);
    }
}

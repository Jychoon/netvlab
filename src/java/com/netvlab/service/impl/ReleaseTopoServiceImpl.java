package com.netvlab.service.impl;

import com.netvlab.dao.ExperimentrecordDao;
import com.netvlab.dao.SubmitedtopoDao;
import com.netvlab.model.Experimentrecord;
import com.netvlab.model.Submitedtopo;
import com.netvlab.service.ReleaseTopoService;
import com.openstack.compute.ServersService;
import com.openstack.network.NetworksService;
import com.openstack.network.RoutersService;
import com.openstack.network.SubnetsService;
import com.openstack.util.OSConfigReader;
import net.sf.json.JSONArray;
import org.openstack4j.api.OSClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("releaseTopoService")
public class ReleaseTopoServiceImpl implements ReleaseTopoService {
    @Resource
    private SubmitedtopoDao submitedtopoDao;
    @Resource
    private ServersService serversService;
    @Resource
    private NetworksService networksService;
    @Resource
    private SubnetsService subnetsService;
    @Resource
    private ExperimentrecordDao experimentrecordDao;
    @Resource
    private RoutersService routersService;

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void DeleteServersByUserId(OSClient.OSClientV3 os, int userId) {
        List<Submitedtopo> submitedtopos = submitedtopoDao.getToposByUserId(userId);
        submitedtopoDao.deleteByUserId(userId);
        if(submitedtopos == null)   return;
        for(Submitedtopo submitedtopo : submitedtopos){
            Object[] assignedServersID = JSONArray.fromObject(submitedtopo.getServersJson()).toArray();
            Object[] assignedSubnetsID = JSONArray.fromObject(submitedtopo.getSubnetsJson()).toArray();
            Object[] assignedNetworksID = JSONArray.fromObject(submitedtopo.getNetworksJson()).toArray();
            for(int i=0;i<assignedServersID.length;i++){
                serversService.DeleteServerById(os,assignedServersID[i].toString());
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<assignedSubnetsID.length;i++){
                subnetsService.DeleteSubnetById(os,assignedSubnetsID[i].toString());
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<assignedNetworksID.length;i++){
                networksService.DeleteNetworkById(os,assignedNetworksID[i].toString());
            }
        }
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void InsertUserRecord(Experimentrecord experimentrecord) {
        experimentrecordDao.saveExperimentrecord(experimentrecord);
    }

    @Override
    public void DetachInterfaceAboutROUTER(OSClient.OSClientV3 os,String routerId, List<String> subnetIDs) {
        for(String subnetId : subnetIDs){
            routersService.DetachInterface(os, routerId,subnetId);
        }
    }
}

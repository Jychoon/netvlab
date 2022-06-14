package com.netvlab.service;

import com.netvlab.model.Experimentrecord;
import com.netvlab.model.Submitedtopo;
import org.openstack4j.api.OSClient;

import java.util.List;

public interface ReleaseTopoService {
    public void DeleteServersByUserId(OSClient.OSClientV3 os,int userId);
    public void InsertUserRecord(Experimentrecord experimentrecord);
    public void DetachInterfaceAboutROUTER(OSClient.OSClientV3 os,String routerId,List<String> subnetIDs);
}

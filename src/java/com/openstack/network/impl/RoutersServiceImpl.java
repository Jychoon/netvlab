package com.openstack.network.impl;

import com.openstack.network.RoutersService;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.AttachInterfaceType;
import org.springframework.stereotype.Service;

@Service("routersService")
public class RoutersServiceImpl implements RoutersService {

    public void AttachInterface(OSClient.OSClientV3 os, String routerId, String subnetId){
        os.networking().router()
                .attachInterface(routerId, AttachInterfaceType.SUBNET,subnetId);
    }

    public void DetachInterface(OSClient.OSClientV3 os, String routerId, String subnetId) {
        os.networking().router()
                .detachInterface(routerId, subnetId, null);
    }
}

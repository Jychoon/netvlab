package com.openstack.network;

import org.openstack4j.api.OSClient;

public interface RoutersService {
    public void AttachInterface(OSClient.OSClientV3 os, String routerId, String subnetId);

    public void DetachInterface(OSClient.OSClientV3 os, String routerId,String subnetId);
}

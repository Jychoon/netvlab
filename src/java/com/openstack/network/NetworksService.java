package com.openstack.network;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.Router;
import org.openstack4j.model.network.Subnet;

import java.util.List;

public interface NetworksService {
    public Network CreateNetwork(OSClient.OSClientV3 os, String name);
    public List<? extends Network> ListNetworks(OSClient.OSClientV3 os);
    public Network GetNetworkById(OSClient.OSClientV3 os,String networkId);
    public Boolean DeleteNetworkById(OSClient.OSClientV3 os,String networkId);
}

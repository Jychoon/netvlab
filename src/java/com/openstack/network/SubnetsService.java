package com.openstack.network;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Subnet;
import java.util.List;

public interface SubnetsService {
    public Subnet CreateSubnet(OSClient.OSClientV3 os, String name, String networkId, String cidr);
    public List<? extends Subnet> ListSubnets(OSClient.OSClientV3 os);
    public Subnet GetSubnetById(OSClient.OSClientV3 os,String subnetId);
    public Boolean DeleteSubnetById(OSClient.OSClientV3 os,String subnetId);
}

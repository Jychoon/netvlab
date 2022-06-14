package com.openstack.network;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Port;

import java.util.List;

public interface PortsService {
    public Port CreatePort(OSClient.OSClientV3 os, String name, String networkId);
    public List<? extends Port> ListPorts(OSClient.OSClientV3 os);
    public Boolean OpenRouteforwardingByServerId(OSClient.OSClientV3 os,String serverId);
    public Port GetPortById(OSClient.OSClientV3 os,String portId);
    public Boolean DeletePort(OSClient.OSClientV3 os,String portId);
    public Port UpdatePortAllowPair(OSClient.OSClientV3 os,String portId,String ipAddress , String macAddress);
    public List<? extends Port> ListPortsByServerId(OSClient.OSClientV3 os,String serverId);
}

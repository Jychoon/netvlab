package com.openstack.network.impl;

import com.openstack.network.PortsService;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Port;
import org.openstack4j.model.network.options.PortListOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service("portsService")
public class PortsServiceImpl implements PortsService {

    @Override
    public Boolean OpenRouteforwardingByServerId(OSClient.OSClientV3 os,String serverId) {
        PortListOptions portListOptions = PortListOptions.create().deviceId(serverId);
        List<? extends Port> ports = os.networking().port().list(portListOptions);
        if(ports == null)   return false;
        if(ports.size() == 0) return false;
        for(Port port : ports){
            os.networking().port().update(port.toBuilder().allowedAddressPair("0.0.0.0/0",null).build());
        }
        return true;
    }

    @Override
    public Port CreatePort(OSClient.OSClientV3 os,String name, String networkId) {
        return null;
    }

    @Override
    public List<? extends Port> ListPorts(OSClient.OSClientV3 os) {
        return null;
    }

    @Override
    public Port GetPortById(OSClient.OSClientV3 os,String portId) {
        Port port = os.networking().port().get(portId);
        return port;
    }

    @Override
    public Boolean DeletePort(OSClient.OSClientV3 os,String portId) {
        return null;
    }

    @Override
    public Port UpdatePortAllowPair(OSClient.OSClientV3 os,String portId , String ipAddress, String macAddress) {
        Port port = GetPortById(os,portId);
        Port update = os.networking().port().update(port.toBuilder()
                        .allowedAddressPair(ipAddress, macAddress)
                        .build());
        return update;
    }

    @Override
    public List<? extends Port> ListPortsByServerId(OSClient.OSClientV3 os,String serverId) {
        PortListOptions portListOptions = PortListOptions.create().deviceId(serverId);
        List<? extends Port> ports = os.networking().port().list(portListOptions);
        return ports;
    }
}

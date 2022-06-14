package com.openstack.compute.Impl;

import com.openstack.compute.ServersService;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("serversService")
public class ServersServiceImpl implements ServersService {
    @Override
    public Server BootServer(OSClient.OSClientV3 os, String name, String flavorId, String imageId, List<String> networks) {
        ServerCreate sc = Builders.server().name(name).networks(networks).flavor(flavorId).image(imageId).build();
        Server server = os.compute().servers().boot(sc);
        return server;
    }

    @Override
    public Boolean ServerAction(OSClient.OSClientV3 os,String serverId, Action action) {
        ActionResponse actionResponse = os.compute().servers().action(serverId,action);
        return actionResponse.isSuccess();
    }

    @Override
    public VNCConsole GetVNCConsole(OSClient.OSClientV3 os,String serverId) {
        VNCConsole vncConsole = os.compute().servers().getVNCConsole(serverId, VNCConsole.Type.NOVNC);
        return vncConsole;
    }

    @Override
    public String GetConsoleOutput(OSClient.OSClientV3 os,String serverId, int lines) {
        String consoleOutput = os.compute().servers().getConsoleOutput(serverId,lines);
        return consoleOutput;
    }

    @Override
    public List<? extends Server> ListAll(OSClient.OSClientV3 os) {
        List<? extends Server> servers = os.compute().servers().list();
        return servers;
    }

    @Override
    public List<? extends Server> ListAll(OSClient.OSClientV3 os,Boolean detail) {
        List<? extends Server> servers = os.compute().servers().list(false);
        return null;
    }

    @Override
    public Server GetServerById(OSClient.OSClientV3 os,String serverId) {
        Server server = os.compute().servers().get(serverId);
        return server;
    }

    @Override
    public Boolean DeleteServerById(OSClient.OSClientV3 os,String serverId) {
       ActionResponse actionResponse =  os.compute().servers().delete(serverId);
       return actionResponse.isSuccess();
    }
}

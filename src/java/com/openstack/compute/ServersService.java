package com.openstack.compute;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.openstack.compute.domain.ConsoleOutput;

import java.util.List;

public interface ServersService {
    //Boot a Server VM
    public Server BootServer(OSClient.OSClientV3 os,String name,String flavorId,String imageId,List<String> networks);
    //Server Action:PAUSE UNPAUSE STOP START LOCK UNLOCK SUSPEND RESUME RESCUE UNRESCUE SHELVE SHELVE_OFFLOAD UNSHELVE
    public Boolean ServerAction(OSClient.OSClientV3 os,String serverId, Action action);
    //VNC and Console Output
    public VNCConsole GetVNCConsole(OSClient.OSClientV3 os,String serverId);
    public String GetConsoleOutput(OSClient.OSClientV3 os,String serverId,int lines);
    //Diagnostics

    //Querying for Servers
    public List<? extends Server> ListAll(OSClient.OSClientV3 os);
    public List<? extends Server> ListAll(OSClient.OSClientV3 os,Boolean detail);
    public Server GetServerById(OSClient.OSClientV3 os,String serverId);
    //Delete a Server
    public Boolean DeleteServerById(OSClient.OSClientV3 os, String serverId);
}

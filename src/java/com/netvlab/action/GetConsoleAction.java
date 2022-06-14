package com.netvlab.action;

import com.openstack.util.OSClientProvider;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.VNCConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.openstack.compute.ServersService;

import javax.annotation.Resource;

@Controller("getConsoleAction")
@Scope("prototype")
public class GetConsoleAction extends ActionSupport {
    @Autowired(required = false)
    private String deviceID;
    @Autowired(required = false)
    private String serverID;
    @Autowired(required = false)
    private JSONObject returnJsonData = new JSONObject();

    @Resource
    private ServersService serversService;

    public String getConsole(){
        OSClient.OSClientV3 os = OSClientProvider.GetOSClient();
        VNCConsole vncConsole = serversService.GetVNCConsole(os,serverID);
        if(vncConsole != null){
            returnJsonData.put("consoleURL",vncConsole.getURL());
        }else {
            returnJsonData.put("consoleURL","");
        }
        return SUCCESS;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public JSONObject getReturnJsonData() {
        return returnJsonData;
    }

    public void setReturnJsonData(JSONObject returnJsonData) {
        this.returnJsonData = returnJsonData;
    }
}

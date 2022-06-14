package com.netvlab.action;

import com.openstack.network.PortsService;
import com.openstack.util.OSClientProvider;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Port;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import java.util.List;

@Controller("getServerIpAction")
@Scope("prototype")
public class GetServerIpAction extends ActionSupport {
    @Autowired(required = false)
    private String deviceID;
    @Autowired(required = false)
    private String serverID;
    @Autowired(required = false)
    private JSONObject returnJsonData = new JSONObject();
    @Resource
    private PortsService portsService;

    public String getServerIp(){
        OSClient.OSClientV3 os = OSClientProvider.GetOSClient();
        List<? extends Port> ports = portsService.ListPortsByServerId(os,serverID);
        System.out.println(ports);
        String interfaces = com.alibaba.fastjson.JSONObject.toJSONString(ports);
        returnJsonData.put("interfaces",interfaces);
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

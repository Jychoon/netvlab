package com.netvlab.action;

import com.netvlab.model.Experimentrecord;
import com.netvlab.model.Submitedtopo;
import com.netvlab.model.Userinfo;
import com.netvlab.model.topo.*;
import com.netvlab.service.SubmitTopoService;
import com.openstack.compute.ServersService;
import com.openstack.network.NetworksService;
import com.openstack.network.PortsService;
import com.openstack.network.RoutersService;
import com.openstack.network.SubnetsService;
import com.openstack.util.OSClientProvider;
import com.openstack.util.OSConfigReader;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Subnet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller("submitAction")
@Scope("prototype")
public class SubmitAction extends ActionSupport {
    @Autowired(required = false)
    private String inpuJsonData;
    @Autowired(required = false)
    private String experimentname;
    @Autowired(required = false)
    private JSONObject returnJsonData = new JSONObject();
    @Resource
    private NetworksService networksService;
    @Resource
    private SubnetsService subnetsService;
    @Resource
    private ServersService serverService;
    @Resource
    private SubmitTopoService submitTopoService;
    @Resource
    private PortsService portsService;
    @Resource
    private RoutersService routersService;

    public String submit(){
        Map<String,Object> session = ActionContext.getContext().getSession();
        Userinfo user = (Userinfo)session.get("user");
        if(null == user){
            returnJsonData.put("result","notlogin");
            return SUCCESS;
        }

        /*****************禁止多人一起提交***************/
        ServletContext servletContext = ServletActionContext.getServletContext();
        List requestList;
        synchronized (ServletActionContext.getServletContext()){
            if (servletContext.getAttribute("requestList") == null) {
                requestList = Collections.synchronizedList(new LinkedList());
                servletContext.setAttribute("requestList", requestList);
            } else {
                requestList = (List) servletContext.getAttribute("requestList");
            }
            if(requestList.size() > 0){
                returnJsonData.put("result","busy");
                return SUCCESS;
            }
        }
        //***********************申请资源***************************
        System.out.println("用户" + user.getName() + "提交实验......");
        System.out.println("inpuJsonData:" + inpuJsonData);
        OSClient.OSClientV3 os = OSClientProvider.GetOSClient();
        JSONObject jsonObject = JSONObject.fromObject(inpuJsonData);
        Map<String, Class> classMap = new HashMap<String, Class>();
        // java反射机制形成对象
        classMap.put("devices", Device.class);
        classMap.put("lines", Line.class);
        // json反序列化
        NetTopo netTopo = (NetTopo) JSONObject.toBean(jsonObject, NetTopo.class, classMap);
        List<Line> lines = netTopo.getLines();
        List<Device> devices = netTopo.getDevices();

        //create network
        Map<Integer, Integer> scannedLines = new HashMap<Integer, Integer>();
        List<String> networksID = new ArrayList<String>();
        List<String> subnetsID = new ArrayList<String>();
        //记录需要实际创建的network
        Map<Integer, Line> netToLine = new HashMap<Integer, Line>();
        int networkNum = 0;
        //network Associated with switch
        for (Device d : devices) {
            if ("Switch".equals(d.getName())) {
                SwitchLine sl = null;
                List<String> linkedSwitchDevicesID = new ArrayList<String>();// 记下与交换机相连的设备
                Line l = new Line();
                for (int j = 0; j < lines.size(); j++) {
                    l = lines.get(j);
                    if (l.getSrcDeviceId().equals(d.getId())) {
                        linkedSwitchDevicesID.add(l.getDstDeviceId());
                        if (l.getSubnet_cidr() != null) {
                            sl = new SwitchLine(l);
                        }
                        scannedLines.put(j, j);
                    } else if (l.getDstDeviceId().equals(d.getId())) {
                        linkedSwitchDevicesID.add(l.getSrcDeviceId());
                        if (l.getSubnet_cidr() != null) {
                            sl = new SwitchLine(l);
                        }
                        scannedLines.put(j, j);
                    }
                }
                if (sl != null) {
                    sl.setDeviceIds(linkedSwitchDevicesID);
                } else {
                    //交换机网络未设置子网地址
                    returnJsonData.put("result", "noSwitchNetwork");
                    return SUCCESS;
                }
                netToLine.put(networkNum++, sl);
            }
        }
        //network not Associated with switch
        for (int i = 0; i < lines.size(); i++) {
            if (!scannedLines.containsKey(i)) {
                Line l = lines.get(i);
                if(l.getSubnet_cidr() == null){
                    returnJsonData.put("result", "noSubnet");
                    return SUCCESS;
                }
                netToLine.put(networkNum++, l);
            }
        }
        System.out.println("netToLine:" + netToLine.size());
        for(Integer key : netToLine.keySet()){
            System.out.println("key = " + key + ": " + netToLine.get(key));
        }
        List<String> subnetsAttachROUTER = new ArrayList<>();
        List<String> subnetsAttachSharedROUTER = new ArrayList<>();
        /*********************Create Networks Subnets*************************/
        for (Entry<Integer, Line> entry : netToLine.entrySet()) {
            // 前端可以设置一条线对应的两个设备的指定网卡，没设置则默认设为eth1，ip后面有用不能为空
            Line ls = entry.getValue();
            if (ls.getSrcIf() == null) {
                ls.setSrcIf("eth1");
                ls.setSrcip("");
            }
            if (ls.getDstIf() == null) {
                ls.setDstIf("eth1");
                ls.setDstip("");
            }

            String netName = "network" + user.getName() + entry.getKey();
            String subName = "subnet" + user.getName() + entry.getKey();
            // =====================Create Network==================
            Network network = networksService.CreateNetwork(os,netName);
            if(network == null){
                returnJsonData.put("result","createNetFailed");
                return SUCCESS;
            }
            // =====================Create Subnet==================
            Subnet subnet = subnetsService.CreateSubnet(os,subName,network.getId(),ls.getSubnet_cidr());
            if(subnet == null){
                returnJsonData.put("result","createSubFailed");
                return SUCCESS;
            }
            networksID.add(network.getId());
            subnetsID.add(subnet.getId());
            //连接外网
            if(ls.getSrcDeviceId().contains("extranet") || ls.getDstDeviceId().contains("extranet")){
                routersService.AttachInterface(os,OSConfigReader.getValue("ROUTER_ID"),subnet.getId());
                subnetsAttachROUTER.add(subnet.getId());
            }
            //共享Server
            if(ls.getSrcDeviceId().contains("shareserver") || ls.getDstDeviceId().contains("shareserver")){
                routersService.AttachInterface(os,OSConfigReader.getValue("SHAREDSERVER_ROUTER_ID"),subnet.getId());
                subnetsAttachSharedROUTER.add(subnet.getId());
            }

            if (ls instanceof SwitchLine) {// 如果是与交换机相连的网络
                for (String deviceId : ((SwitchLine) ls).getDeviceIds()) {
                    Device swDevice = null;
                    for (Device device : devices) {
                        if (device.getId().equals(deviceId)) {
                            swDevice = device;
                            break;
                        }
                    }
                    List<DeviceIf> dfs = swDevice.getDeviceIfs();
                    if (null == dfs) {
                        dfs = new ArrayList<DeviceIf>();
                    }
                    DeviceIf df = new DeviceIf();
                    df.setName(ls.getSrcIf());
                    // df.setIp(ls.getSrcip()); IP功能暂时跳过
                    df.setIp("");
                    df.setNetwork(network.getId());
                    dfs.add(df);
                    swDevice.setDeviceIfs(dfs);
                }
            } else {// 如果是非交换机网线
                for (int j = 0; j < devices.size(); j++) {
                    Device d = devices.get(j);
                    List<DeviceIf> dfs = d.getDeviceIfs();
                    // 设备每在一根线中出现就说明它属于这个网络，就将它的一个指定网卡加入这个网络
                    if (ls.getSrcDeviceId().equals(d.getId())) {
                        if (null == dfs) {
                            dfs = new ArrayList<DeviceIf>();
                        }
                        DeviceIf df = new DeviceIf();
                        df.setName(ls.getSrcIf());
                        // df.setIp(ls.getSrcip()); IP功能暂时跳过
                        df.setIp("");
                        df.setNetwork(network.getId());
                        dfs.add(df);
                        d.setDeviceIfs(dfs);
                    }
                    if (ls.getDstDeviceId().equals(d.getId())) {
                        if (null == dfs) {
                            dfs = new ArrayList<DeviceIf>();
                        }
                        DeviceIf df = new DeviceIf();
                        df.setName(ls.getDstIf());
                        // df.setIp(ls.getDstip());IP功能暂时跳过
                        df.setIp("");
                        df.setNetwork(network.getId());
                        dfs.add(df);
                        d.setDeviceIfs(dfs);
                    }
                }
            }
        }
        session.put("subnetsAttachROUTER",subnetsAttachROUTER);
        session.put("subnetsAttachSharedROUTER",subnetsAttachSharedROUTER);
        String assignedNetworksID = JSONArray.fromObject(networksID).toString();
        String assignedSubnetsID = JSONArray.fromObject(subnetsID).toString();
        System.out.println("assignedNetworksID:"+assignedNetworksID);
        System.out.println("assignedSubnetsID:"+assignedSubnetsID);

        //waiting for creating networks and subnets
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /***************************Create Server*********************************/
        List<String> serversID = new ArrayList<String>();
        List<String> routersID = new ArrayList<String>();// 用于路由器创建后改变Port安全规则，实现路由转发
        List<String> ApplicationServerID = new ArrayList<String>();

        // jdata为返回前台的设备数据
        Map<String, Object> jdata = new HashMap<String, Object>();

        for (int i = 0; i < devices.size(); i++) {
            List<String> networks = new ArrayList<>();
            Device device = devices.get(i);
            if ("Switch".equals(device.getName()))  continue;
            if ("EXTRANET".equals(device.getName()))    continue;
            if ("SHARESERVER".equals(device.getName()))    continue;
            List<DeviceIf> deviceIfs = device.getDeviceIfs();
            for (int j = 0; j < deviceIfs.size(); j++) {
                DeviceIf deviceIf = deviceIfs.get(j);
                String networkID = deviceIf.getNetwork();
                networks.add(networkID);
            }
            String serverName = user.getName() + device.getName();
            String serverID = "";
            // create server .................
            if (device.getName().equals("ROUTER")) {
                serverID = serverService.BootServer(os,
                        serverName,
                        OSConfigReader.getValue("FLAVOR_OPW_ID"),
                        OSConfigReader.getValue("IMAGE_ROUTER_ID"),
                        networks)
                        .getId();
                routersID.add(serverID);
            }else if (device.getName().equals("WinXP")) {
                serverID = serverService.BootServer(os,
                        serverName,
                        OSConfigReader.getValue("FLAVOR_WINXP_ID"),
                        OSConfigReader.getValue("IMAGE_WINXP_ID"),
                        networks)
                        .getId();
            } else if (device.getName().equals("ApplicationServer")) {
                System.out.println("创建ApplicationServer......");
                serverID = serverService.BootServer(os,
                        serverName,
                        OSConfigReader.getValue("FLAVOR_CENTOS_ID"),
                        OSConfigReader.getValue("IMAGE_CENTOS_ID"),
                        networks)
                        .getId();
                ApplicationServerID.add(serverID);
            }
            if(serverID == ""){
                returnJsonData.put("result","createServerFailed");
                return SUCCESS;
            }
            serversID.add(serverID);
            jdata.put(device.getId(), device);
            returnJsonData.put(device.getId(), serverID);
        }
        String assignedServersID = JSONArray.fromObject(serversID).toString();
        System.out.println("assignedServersID:"+assignedServersID);

        Submitedtopo submitedtopo = new Submitedtopo();
        submitedtopo.setUserId(user.getId());
        submitedtopo.setNetworksJson(assignedNetworksID);
        submitedtopo.setSubnetsJson(assignedSubnetsID);
        submitedtopo.setServersJson(assignedServersID);
        submitedtopo.setSubmitTime(new Date().toString());
        submitTopoService.SaveSubmitTopo(submitedtopo);

        Experimentrecord experimentrecord = new Experimentrecord();
        experimentrecord.setUserName(user.getName());
        experimentrecord.setOccupySource(experimentname);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        experimentrecord.setSubmitTime(sdf.format(new Date()));
        experimentrecord.setExperimentName(experimentname);
        session.put("experimentrecord",experimentrecord);
        session.put("jsonStr",inpuJsonData.toString());

        returnJsonData.put("result", "success");
        returnJsonData.put("ifData", jdata);

        try {
            Thread.sleep(4*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String routerID : routersID) {
            portsService.OpenRouteforwardingByServerId(os,routerID);
        }

        for(String centosID : ApplicationServerID){
            portsService.OpenRouteforwardingByServerId(os,centosID);
        }
        System.out.println(user.getName() + " get the resources");
        return SUCCESS;
    }

    public String getInpuJsonData() {
        return inpuJsonData;
    }

    public void setInpuJsonData(String inpuJsonData) {
        this.inpuJsonData = inpuJsonData;
    }

    public JSONObject getReturnJsonData() {
        return returnJsonData;
    }

    public void setReturnJsonData(JSONObject returnJsonData) {
        this.returnJsonData = returnJsonData;
    }

    public String getExperimentname() {
        return experimentname;
    }

    public void setExperimentname(String experimentname) {
        this.experimentname = experimentname;
    }
}

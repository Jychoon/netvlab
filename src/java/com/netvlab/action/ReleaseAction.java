package com.netvlab.action;

import com.netvlab.model.Experimentrecord;
import com.netvlab.model.Userinfo;
import com.netvlab.service.ReleaseTopoService;
import com.openstack.util.OSClientProvider;
import com.openstack.util.OSConfigReader;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller("releaseAction")
@Scope("prototype")
public class ReleaseAction extends ActionSupport {
    @Autowired(required = false)
    private String inpuJsonData;
    @Autowired(required = false)
    private JSONObject returnJsonData = new JSONObject();

    @Resource
    private ReleaseTopoService releaseTopoService;

    public String release() throws ParseException {
        Map<String, Object> session = ActionContext.getContext().getSession();
        Userinfo user = (Userinfo) session.get("user");
        if(null == user){
            returnJsonData.put("result", "notLogin");
            return SUCCESS;
        }
        OSClient.OSClientV3 os = OSClientProvider.GetOSClient();
        //detach interface about router(外网)
        List<String> subnetsAttachROUTER = (List<String>)session.get("subnetsAttachROUTER");
        if(subnetsAttachROUTER.size() > 0){
            releaseTopoService.DetachInterfaceAboutROUTER(os, OSConfigReader.getValue("ROUTER_ID"),subnetsAttachROUTER);
        }
        session.remove("subnetsAttachROUTER");
        //detach interface about share router(共享server)
        List<String> subnetsAttachSharedROUTER = (List<String>)session.get("subnetsAttachSharedROUTER");
        if(subnetsAttachSharedROUTER.size() > 0){
            releaseTopoService.DetachInterfaceAboutROUTER(os,OSConfigReader.getValue("SHAREDSERVER_ROUTER_ID"),subnetsAttachSharedROUTER);
        }
        session.remove("subnetsAttachSharedROUTER");
        //delete Servers By UserId
        releaseTopoService.DeleteServersByUserId(os,user.getId());
        Experimentrecord experimentrecord = (Experimentrecord) session.get("experimentrecord");
        if(experimentrecord != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            experimentrecord.setReleaseTime(sdf.format(new Date()));
            long t1 = sdf.parse(experimentrecord.getSubmitTime()).getTime();
            long t2 = sdf.parse(experimentrecord.getReleaseTime()).getTime();
            int TotalTime = (int) ((t2 - t1) / (1000 * 60));
            experimentrecord.setTotalTime(TotalTime);
            //Insert Record
            releaseTopoService.InsertUserRecord(experimentrecord);
        }
        System.out.println(user.getName() + " release the sources!");
        returnJsonData.put("result", "success");
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
}

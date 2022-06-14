package com.openstack.util;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.OSFactory;

import java.util.Date;

public final class OSClientProvider {
    public static OSClientV3 os;
    public static OSClientV3 UpdateOSClient(){
        OSClientV3 osnew = OSFactory.builderV3()
                .endpoint(OSConfigReader.getValue("OS_INDENTITY_URL"))
                .credentials(OSConfigReader.getValue("OS_USERNAME"),
                        OSConfigReader.getValue("OS_PASSWORD"),
                        Identifier.byName(OSConfigReader.getValue("OS_DOMAIN_NAME")))
                .scopeToProject(Identifier.byName(OSConfigReader.getValue("OS_PROJECT_NAME")),
                        Identifier.byName(OSConfigReader.getValue("OS_DOMAIN_NAME")))
                .withConfig(
                        Config.newConfig()
                                .withConnectionTimeout(1000 * 60)
                                .withReadTimeout(1000 * 60 * 5))
                .authenticate();
        return osnew;
    }
    public static OSClientV3 GetOSClient(){
        System.out.println("Geting OSClient..........................................");
        /*if(os == null){
            UpdateOSClient();
        }else{
            long lasttime = os.getToken().getIssuedAt().getTime();
            long now = System.currentTimeMillis();
            long TOKEN_VALIDITY_TIME = Long.parseLong(OSConfigReader.getValue("TOKEN_VALIDITY_TIME"));
            System.out.println("time period:" + (now - lasttime)/(1000*60));
            if((now - lasttime)/(1000*60) > TOKEN_VALIDITY_TIME ){
                UpdateOSClient();
            }
        }*/
        return UpdateOSClient();
    }
}

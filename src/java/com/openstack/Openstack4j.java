package com.openstack;

import com.openstack.util.OSClientProvider;
import com.openstack.util.OSConfigReader;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.InterfaceAttachment;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.network.*;
import org.openstack4j.openstack.OSFactory;

import java.util.ArrayList;
import java.util.List;

public class Openstack4j {
    public static void main(String[] args) {
        OSClientV3 os = OSClientProvider.GetOSClient();
        /*Network network = os.networking().network()
                .create(Builders.network().name("yth_network").tenantId("85627a7c334549caa571bef2a5333889").build());
        Subnet subnet = os.networking().subnet().create(Builders.subnet()
                .name("yth_subnet")
                .networkId(network.getId())
                .tenantId("85627a7c334549caa571bef2a5333889")
                .ipVersion(IPVersionType.V4)
                .cidr("192.168.0.0/24")
                .enableDHCP(true)
                .build());*/
        /*try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       /* List<String> networks = new ArrayList<>();
        networks.add(network.getId());
        ServerCreate sc = Builders.server().networks(networks).name("YTH_CENTOS")
                .flavor(OSConfigReader.getValue("FLAVOR_CENTOS_ID"))
                .image(OSConfigReader.getValue("IMAGE_CENTOS_ID")).build();
        Server server = os.compute().servers().boot(sc);
        System.out.println(server.toString());*/
       /*os.networking().router().attachInterface("f6a09f61-978a-4b5e-af08-eb900cbeb1b7",
               AttachInterfaceType.SUBNET,
               "caa988c4-d81d-4a55-97ca-acefb66066f6");*/
       Router router = os.networking().router().get(OSConfigReader.getValue("ROUTER_ID"));
       System.out.println(router.toString());
       //os.networking().router().detachInterface(OSConfigReader.getValue("ROUTER_ID"),"caa988c4-d81d-4a55-97ca-acefb66066f6",null);
    }
}

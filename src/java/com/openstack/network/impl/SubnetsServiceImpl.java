package com.openstack.network.impl;

import com.openstack.network.SubnetsService;
import com.openstack.util.OSConfigReader;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.Subnet;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("subnetsService")
public class SubnetsServiceImpl implements SubnetsService {
    @Override
    public Subnet CreateSubnet(OSClient.OSClientV3 os, String name, String networkId, String cidr) {
        Subnet subnet = os.networking().subnet().create(Builders.subnet()
                .name(name)
                .networkId(networkId)
                .tenantId(OSConfigReader.getValue("OS_TENANT_ID"))
                .ipVersion(IPVersionType.V4)
                .cidr(cidr)
                .enableDHCP(true)
                .build());
        return subnet;
    }

    @Override
    public List<? extends Subnet> ListSubnets(OSClient.OSClientV3 os) {
        return null;
    }

    @Override
    public Subnet GetSubnetById(OSClient.OSClientV3 os,String subnetId) {
        return null;
    }

    @Override
    public Boolean DeleteSubnetById(OSClient.OSClientV3 os,String subnetId) {
        ActionResponse actionResponse = os.networking().subnet().delete(subnetId);
        return actionResponse.isSuccess();
    }
}

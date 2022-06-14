package com.openstack.network.impl;

import com.openstack.network.NetworksService;
import com.openstack.util.OSConfigReader;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Network;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("networkService")
public class NetworksServiceImpl implements NetworksService {

    @Override
    public Network CreateNetwork(OSClient.OSClientV3 os,String name) {
        Network network = os.networking().network()
                .create(Builders.network()
                        .name(name)
                        .tenantId(OSConfigReader.getValue("OS_TENANT_ID"))
                        .adminStateUp(true)
                        .build());
        return network;
    }

    @Override
    public List<? extends Network> ListNetworks(OSClient.OSClientV3 os) {
        return null;
    }

    @Override
    public Network GetNetworkById(OSClient.OSClientV3 os,String networkId) {
        return null;
    }

    @Override
    public Boolean DeleteNetworkById(OSClient.OSClientV3 os,String networkId) {
        ActionResponse response = os.networking().network().delete(networkId);
        return response.isSuccess();
    }
}

package com.netvlab.model.topo;

import java.util.List;

public class Network {
    private String name;
    private String netAdd;
    private byte mask;
    private List<DeviceIf> deviceIfs;
    private short vlanId;
    private PortGroupResource portGroupResource;

    public PortGroupResource getPortGroupResource() {
        return portGroupResource;
    }

    public void setPortGroupResource(PortGroupResource portGroupResource) {
        this.portGroupResource = portGroupResource;
    }

    public short getVlanId() {
        return vlanId;
    }

    public void setVlanId(short vlanId) {
        this.vlanId = vlanId;
    }

    public List<DeviceIf> getDeviceIfs() {
        return deviceIfs;
    }

    public void setDeviceIfs(List<DeviceIf> deviceIfs) {
        this.deviceIfs = deviceIfs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetAdd() {
        return netAdd;
    }

    public void setNetAdd(String netAdd) {
        this.netAdd = netAdd;
    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }

}

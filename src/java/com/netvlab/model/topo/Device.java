package com.netvlab.model.topo;

import java.util.List;


public class Device {
    private String id;
    private String name;
    private int x;
    private int y;
    private int width;
    private int heigth;
    private List<DeviceIf> deviceIfs;
    private String remoteId;
    private String eth1;
    private String eth2;
    private String eth3;

    public String getEth1() {
        return eth1;
    }

    public void setEth1(String eth1) {
        this.eth1 = eth1;
    }

    public String getEth2() {
        return eth2;
    }

    public void setEth2(String eth2) {
        this.eth2 = eth2;
    }

    public String getEth3() {
        return eth3;
    }

    public void setEth3(String eth3) {
        this.eth3 = eth3;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public List<DeviceIf> getDeviceIfs() {
        return deviceIfs;
    }

    public void setDeviceIfs(List<DeviceIf> deviceIfs) {
        this.deviceIfs = deviceIfs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
}

package com.netvlab.model.topo;

public class Line {
    private String name;
    private String srcDeviceId;
    private String srcIf;
    private String dstDeviceId;
    private String dstIf;
    private String color;
    private int width;
    private String subnet_cidr;
    private String srcip;
    private String dstip;

    public String getSubnet_cidr() {
        return subnet_cidr;
    }

    public void setSubnet_cidr(String subnet_cidr) {
        this.subnet_cidr = subnet_cidr;
    }

    public String getSrcip() {
        return srcip;
    }

    public void setSrcip(String srcip) {
        this.srcip = srcip;
    }

    public String getDstip() {
        return dstip;
    }

    public void setDstip(String dstip) {
        this.dstip = dstip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcIf() {
        return srcIf;
    }

    public void setSrcIf(String srcIf) {
        this.srcIf = srcIf;
    }

    public String getDstIf() {
        return dstIf;
    }

    public void setDstIf(String dstIf) {
        this.dstIf = dstIf;
    }

    public String getSrcDeviceId() {
        return srcDeviceId;
    }

    public void setSrcDeviceId(String srcDeviceId) {
        this.srcDeviceId = srcDeviceId;
    }

    public String getDstDeviceId() {
        return dstDeviceId;
    }

    public void setDstDeviceId(String dstDeviceId) {
        this.dstDeviceId = dstDeviceId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", srcDeviceId='" + srcDeviceId + '\'' +
                ", srcIf='" + srcIf + '\'' +
                ", dstDeviceId='" + dstDeviceId + '\'' +
                ", dstIf='" + dstIf + '\'' +
                ", color='" + color + '\'' +
                ", width=" + width +
                ", subnet_cidr='" + subnet_cidr + '\'' +
                ", srcip='" + srcip + '\'' +
                ", dstip='" + dstip + '\'' +
                '}';
    }
}

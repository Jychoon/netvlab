package com.netvlab.model.topo;

public class DeviceIf {
	private String deviceId;
	private String name;
	private String ip;
	private String macAdd;
	private String network;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMacAdd() {
		return macAdd;
	}
	public void setMacAdd(String macAdd) {
		this.macAdd = macAdd;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getNetwork() {
		return network;
	}
}

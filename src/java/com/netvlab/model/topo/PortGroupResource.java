package com.netvlab.model.topo;

public class PortGroupResource {
	private String id;
	private String portGroupName;
	private String portGroupKey;
	private int vlanId;
	private int currentUserNumber;
	
	public int getCurrentUserNumber() {
		return currentUserNumber;
	}
	public void setCurrentUserNumber(int currentUserNumber) {
		this.currentUserNumber = currentUserNumber;
	}
	public int getVlanId() {
		return vlanId;
	}
	public void setVlanId(int vlanId) {
		this.vlanId = vlanId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortGroupName() {
		return portGroupName;
	}
	public void setPortGroupName(String portGroupName) {
		this.portGroupName = portGroupName;
	}
	public String getPortGroupKey() {
		return portGroupKey;
	}
	public void setPortGroupKey(String portGroupKey) {
		this.portGroupKey = portGroupKey;
	}
}

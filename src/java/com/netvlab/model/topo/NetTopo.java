package com.netvlab.model.topo;

import java.util.ArrayList;
import java.util.List;

public class NetTopo {
	private String name;
	private List<Device> devices = new ArrayList<Device>();
	private List<Line> lines;
	private List<Network> networks;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Device> getDevices() {
		return devices;
	}
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	public List<Network> getNetworks() {
		return networks;
	}
	public void setNetworks(List<Network> networks) {
		this.networks = networks;
	}
}

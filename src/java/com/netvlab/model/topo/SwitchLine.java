package com.netvlab.model.topo;

import java.util.List;

public class SwitchLine extends Line{
  private List<String> deviceIds;
  public SwitchLine(Line line){
	  this.setColor(line.getColor());
	  this.setDstDeviceId(line.getDstDeviceId());
	  this.setDstip(line.getDstip());
	  this.setDstIf(line.getDstDeviceId());
	  this.setName(line.getName());
	  this.setSrcDeviceId(line.getSrcDeviceId());
	  this.setSrcIf(line.getSrcIf());
	  this.setSrcip(line.getSrcip());
	  this.setSubnet_cidr(line.getSubnet_cidr());
	  this.setWidth(line.getWidth());
  }
  public List<String> getDeviceIds(){
	  return deviceIds;
  }
  public void setDeviceIds(List<String> deviceIds){
	  this.deviceIds = deviceIds;
  }
  
}

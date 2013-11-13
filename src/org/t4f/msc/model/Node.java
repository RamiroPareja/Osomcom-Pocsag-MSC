package org.t4f.msc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class Node implements Serializable{

	private static final long serialVersionUID = -4816292542108429759L;
	private static final int timeoutMinutes = 1;
	private static final int timeout = timeoutMinutes * 60 * 1000;

	private int id;
	private String name;
	private Protocol protocol;
	private String host;
	private Integer port;
	private Integer group;
	private Timestamp lastPing;
	

	private Map<String, String> properties;

	// Node States:
	// Active: seen in the last 'timeoutMinutes' minutes
	// Unknow: seen in the last 2*'timeoutMinutes' minutes
	// Inactive: Not seen in the last 2*'timeoutMinutes' minutes
	public enum NodeState {
		ACTIVE, UNKNOW, INACTIVE
	}
	
	public enum Protocol {
		REST, MQTT
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getGroup() {
		return group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Timestamp getLastPing() {
		return lastPing;
	}

	public void setLastPing(Timestamp lastPing) {
		this.lastPing = lastPing;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "Node [name=" + name + ", host=" + host + ", port=" + port
				+ ", group=" + group + ", lastPing=" + lastPing
				+ ", properties=" + properties + "]";
	}

	// Returns true if the node has returned a ping in the last 5 minutes
	public NodeState getState() {

		long diffTime = (new Date()).getTime() - lastPing.getTime();

		if (diffTime < timeout)
			return NodeState.ACTIVE;
		else if (diffTime < 2 * timeout)
			return NodeState.UNKNOW;
		else
			return NodeState.INACTIVE;

	}

}

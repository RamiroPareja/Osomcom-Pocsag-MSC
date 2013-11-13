package org.t4f.msc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable{

	private static final long serialVersionUID = 4482078157474030515L;
	public enum MessageType {
		ALPHA, NUMERIC, TONE
	}
	
	private transient List <Destination> destinationList = new ArrayList<Destination>();
	
	private String text;
	private MessageType type;
	
	
	public List<Destination> getDestinationList() {
		return destinationList;
	}
	public void setDestinationList(List<Destination> destinationList) {
		this.destinationList = destinationList;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	} 
	
	
	
	
	
}

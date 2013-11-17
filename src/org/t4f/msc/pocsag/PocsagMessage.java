package org.t4f.msc.pocsag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PocsagMessage {

	private int RIC;
	private int frequency;
	private int bauds;
	private MessageType msgType;
	private String message;
	
	public enum MessageType {
		TONE, NUMERIC, ALPHA, IDLE
	}

	public PocsagMessage() {
		
	}
	
	public PocsagMessage(int RIC, int frequency, int bauds,
			MessageType msgType, String message) {		
		this.RIC = RIC;
		this.frequency = frequency;
		this.bauds = bauds;
		this.msgType = msgType;
		this.message = message;
	}

	
	
	
	@Override
	public String toString() {
		return "PocsagMessage [RIC=" + RIC + ", frequency=" + frequency
				+ ", bauds=" + bauds + ", msgType=" + msgType + ", message="
				+ message + "]";
	}
	
	public String toMqttString() {
		return "PM:{"+ RIC + "," + frequency+ "," + bauds + "," + msgType + ","
				+ message + "}";
	}
	
	public static PocsagMessage parseMqttString(String mqttString) {
		PocsagMessage pm = new PocsagMessage();
		
		Pattern p = Pattern.compile("ascii: PM:\\{(\\d+),(\\d+),(\\d+),(\\w+),([\\w\\s\\n]+)\\}-(\\d+)");		Matcher m = p.matcher(mqttString);
		
		if (m.find()) {
			pm.setRIC(new Integer(m.group(1)));
			pm.setFrequency(new Integer(m.group(2)));
			pm.setBauds(new Integer(m.group(3)));
			pm.setMsgType(MessageType.valueOf(m.group(4)));
			pm.setMessage(m.group(5));
			
			return pm;
		} else 
			return null;
	}

	public int getRIC() {
		return RIC;
	}

	public void setRIC(int rIC) {
		RIC = rIC;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getBauds() {
		return bauds;
	}

	public void setBauds(int bauds) {
		this.bauds = bauds;
	}

	public MessageType getMsgType() {
		return msgType;
	}

	public void setMsgType(MessageType msgType) {
		this.msgType = msgType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
}

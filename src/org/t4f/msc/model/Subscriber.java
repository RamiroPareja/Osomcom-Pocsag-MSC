package org.t4f.msc.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Subscriber extends Destination implements Serializable {

	private static final long serialVersionUID = -1040373647179203784L;

	
	private String nickName;
	private String realName;
	private Integer RIC;
	private Integer frequency;
	private Integer bauds;
	

	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public Integer getRIC() {
		return RIC;
	}
	public void setRIC(Integer rIC) {
		RIC = rIC;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer getBauds() {
		return bauds;
	}
	public void setBauds(Integer bauds) {
		this.bauds = bauds;
	}
	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", nickName=" + nickName
				+ ", realName=" + realName + ", RIC=" + RIC + ", frequency="
				+ frequency + ", bauds=" + bauds + "]";
	}
	@Override
	public String getName() {
		return getNickName();
	}
	@Override
	public String getType() {

		return "Subscriber";
	}
	
	
	public int hashCode() {
		return new HashCodeBuilder(23, 41).
				append(id).append(nickName).append(realName).append(RIC).append(frequency).append(bauds).toHashCode();
	}

	 public boolean equals(Object obj) {
	        if (obj == null)
	            return false;
	        if (obj == this)
	            return true;
	        if (!(obj instanceof Subscriber))
	            return false;

	        Subscriber s = (Subscriber) obj;
	        return new EqualsBuilder().
	            append(id, s.id).
	            append(nickName, s.nickName).
	            append(realName, s.realName).
	            append(RIC, s.RIC).
	            append(frequency, s.frequency).
	            append(bauds, s.bauds).
	            isEquals();
	    }
	 
	
}

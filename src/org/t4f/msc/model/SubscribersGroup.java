package org.t4f.msc.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SubscribersGroup extends Destination implements Serializable{

	private static final long serialVersionUID = -6116197115029195721L;
	private String name;
	private String description;
	private boolean member; // Non DB-mapped field that indicates if a
							// Subscriber is member of a group


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isMember() {
		return member;
	}

	public void setMember(boolean member) {
		this.member = member;
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 31).
				append(id).append(name).append(description).toHashCode();
	}

	 public boolean equals(Object obj) {
	        if (obj == null)
	            return false;
	        if (obj == this)
	            return true;
	        if (!(obj instanceof SubscribersGroup))
	            return false;

	        SubscribersGroup group = (SubscribersGroup) obj;
	        return new EqualsBuilder().
	            append(id, group.id).
	            append(name, group.name).
	            append(description,group.description).
	            isEquals();
	    }
	 
		@Override
		public String getType() {

			return "Group";
		}	 
	
}

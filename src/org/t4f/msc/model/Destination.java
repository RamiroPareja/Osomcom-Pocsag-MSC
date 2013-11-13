package org.t4f.msc.model;

import java.io.Serializable;

public abstract class Destination implements Serializable{

	private static final long serialVersionUID = -4144207173896818147L;
	protected int id;
	
	public abstract String getName();
    public abstract String getType();
    
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}

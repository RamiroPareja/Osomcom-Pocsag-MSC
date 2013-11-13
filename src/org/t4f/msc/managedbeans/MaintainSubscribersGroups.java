package org.t4f.msc.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.t4f.msc.model.Subscriber;
import org.t4f.msc.model.SubscribersGroup;
import org.t4f.msc.services.SubscriberServices;
import org.t4f.msc.services.SubscribersGroupServices;

@ManagedBean
@ViewScoped
public class MaintainSubscribersGroups extends ManagedBeanCommon implements Serializable{


	private static final long serialVersionUID = -475468359499246280L;

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	private SubscribersGroup subscribersGroupEdit;
	private transient List<SubscribersGroup> subscribersGroupList;
	private transient List<Subscriber> subscriberList;
	
	
	private boolean popupEditMode;	//This bool indicates that popup has to show in edit mode instead of create mode
		

	
	public List<SubscribersGroup> getSubscribersGroupList() {
		return subscribersGroupList;
	}

	public void setSubscribersGroupList(List<SubscribersGroup> subscribersGroupList) {
		this.subscribersGroupList = subscribersGroupList;
	}
	
	public SubscribersGroup getSubscribersGroupEdit() {
		return subscribersGroupEdit;
	}

	public void setSubscribersGroupEdit(SubscribersGroup subscribersGroupEdit) {
		this.subscribersGroupEdit = subscribersGroupEdit;
	}

	
	
	public List<Subscriber> getSubscriberList() {
		return subscriberList;
	}

	public void setSubscribersList(List<Subscriber> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public boolean isPopupEditMode() {
		return popupEditMode;
	}

	public void setPopupEditMode(boolean popupEditMode) {
		this.popupEditMode = popupEditMode;
	}

	public void loadSubscribersGroups(ComponentSystemEvent event) {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();
		
		subscribersGroupList = subscribersGroupServices.findAll();
		
	}
	
	public String loadSubscribersGroupToEdit() {
		
		popupEditMode = true;
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if ((this.subscribersGroupEdit = subscribersGroupServices.read(id)) == null)
			return "main";	//error
		else 		
			return null;
	}
	
	public String loadSubscribersGroupToCreate() {
		
		popupEditMode = false;
		
		this.subscribersGroupEdit = new SubscribersGroup();
		
		return null;
	}
	
	public String editSubscribersGroup() {
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		
		if (subscribersGroupServices.update(subscribersGroupEdit))
			addMessage("message", "Error saving changes.");
		else 		
			addMessage("message", "Changes saved correctly.");
			
		return null;
	}
	
	public String createSubscribersGroup() {
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		
		if (subscribersGroupServices.create(subscribersGroupEdit))
			addMessage("message", "Error creating subscribersGroup.");
		else 		
			addMessage("message", "SubscribersGroup created correctly.");
			
		return null;
	}
	
	public String deleteSubscribersGroup() {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if (subscribersGroupServices.delete(id))
			addMessage("message", "Error deleting subscribersGroup.");
		else 		
			addMessage("message", "SubscribersGroup deleted.");
		
		return null;
	}
	
	public String loadSubscribers() {
		
		SubscriberServices subscriberServices = new SubscriberServices();		
		int id = Integer.parseInt(getParameter("id"));
		
		subscriberList=subscriberServices.findByGroupId(id);				
		return null;
		
	}
	
}

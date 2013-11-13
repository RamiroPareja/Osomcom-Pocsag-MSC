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
public class MaintainSubscribers extends ManagedBeanCommon implements Serializable {

	private static final long serialVersionUID = 8845202700145663082L;

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	private Subscriber subscriberFilter = new Subscriber();
	private Subscriber subscriberEdit;
	private transient List<Subscriber> subscriberList;	
	private transient List<SubscribersGroup> groupList;	
	private boolean popupEditMode;	//This bool indicates that popup has to show in edit mode instead of create mode
	
	private int idSubscriber;
	
	
	public List<Subscriber> getSubscriberList() {
		return subscriberList;
	}

	public void setSubscriberList(List<Subscriber> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public Subscriber getSubscriberFilter() {
		return subscriberFilter;
	}

	public void setSubscriberFilter(Subscriber subscriber) {
		this.subscriberFilter = subscriber;
	}
	
	public Subscriber getSubscriberEdit() {
		return subscriberEdit;
	}

	public void setSubscriberEdit(Subscriber subscriberEdit) {
		this.subscriberEdit = subscriberEdit;
	}

	
	
	public List<SubscribersGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<SubscribersGroup> groupList) {
		this.groupList = groupList;
	}

	public boolean isPopupEditMode() {
		return popupEditMode;
	}

	public void setPopupEditMode(boolean popupEditMode) {
		this.popupEditMode = popupEditMode;
	}

	public void loadSubscribers(ComponentSystemEvent event) {
		
		SubscriberServices subscriberServices = new SubscriberServices();
		
		subscriberList = subscriberServices.find(subscriberFilter);
		
	}
	
	public String loadSubscribersGroups() {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		idSubscriber = Integer.parseInt(getParameter("id"));		
		
		if ((groupList = subscribersGroupServices.getGroupsMembershipByUserId(idSubscriber)) == null)
			return "main";	//error
		else 		
			return null;
	}
	
	public String loadSubscriberToEdit() {
		
		popupEditMode = true;
		
		SubscriberServices subscriberServices = new SubscriberServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if ((this.subscriberEdit = subscriberServices.read(id)) == null)
			return "main";	//error
		else 		
			return null;
	}
	
	public String loadSubscriberToCreate() {
		
		popupEditMode = false;
		
		this.subscriberEdit = new Subscriber();
		
		return null;
	}
	
	public String editSubscriber() {
		SubscriberServices subscriberServices = new SubscriberServices();		
		
		if (subscriberServices.update(subscriberEdit))
			addMessage("message", "Error saving changes.");
		else 		
			addMessage("message", "Changes saved correctly.");
			
		return null;
	}
	
	public String createSubscriber() {
		SubscriberServices subscriberServices = new SubscriberServices();		
		
		if (subscriberServices.create(subscriberEdit))
			addMessage("message", "Error creating subscriber.");
		else 		
			addMessage("message", "Subscriber created correctly.");
			
		return null;
	}
	
	public String deleteSubscriber() {
		
		SubscriberServices subscriberServices = new SubscriberServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if (subscriberServices.delete(id))
			addMessage("message", "Error deleting subscriber.");
		else 		
			addMessage("message", "Subscriber deleted.");
		
		return null;
	}
	
	public String changeSubscriberGroupsMembership() {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
				
		if (subscribersGroupServices.setGroupsMembershipByUserId(idSubscriber, groupList))
			addMessage("message", "Error editing groups.");
		else 		
			addMessage("message", "Subscriber's groups modified correctly.");
		return null;
	}
	
}

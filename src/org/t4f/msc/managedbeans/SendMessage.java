package org.t4f.msc.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.t4f.msc.model.Destination;
import org.t4f.msc.model.Message;
import org.t4f.msc.model.Subscriber;
import org.t4f.msc.model.SubscribersGroup;
import org.t4f.msc.services.MessageServices;
import org.t4f.msc.services.SubscriberServices;
import org.t4f.msc.services.SubscribersGroupServices;

@ManagedBean
@ViewScoped
public class SendMessage extends ManagedBeanCommon implements Serializable{


	private static final long serialVersionUID = 5141235849033388971L;

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
		
	private transient List<SubscribersGroup> subscribersGroupList;
	private transient List<Subscriber> subscriberList;
	private Subscriber subscriberFilter = new Subscriber();	
	
	
	private Message message = new Message();
		
	
	public List<SubscribersGroup> getSubscribersGroupList() {
		return subscribersGroupList;
	}

	public void setSubscribersGroupList(List<SubscribersGroup> subscribersGroupList) {
		this.subscribersGroupList = subscribersGroupList;
	}

	public List<Subscriber> getSubscriberList() {
		return subscriberList;
	}

	public void setSubscriberList(List<Subscriber> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public Subscriber getSubscriberFilter() {
		return subscriberFilter;
	}

	public void setSubscriberFilter(Subscriber subscriberFilter) {
		this.subscriberFilter = subscriberFilter;
	}

	

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String loadSubscribersGroups() {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();
		
		subscribersGroupList = subscribersGroupServices.findAll();
		
		return null;
		
	}
	
	public String loadSubscribers() {
		
		SubscriberServices subscriberServices = new SubscriberServices();
		
		subscriberList = subscriberServices.find(subscriberFilter);
		
		return null;
		
	}	
	
	
	
	public String addSubscribersGroupToDestinationList() {
		
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		int id = Integer.parseInt(getParameter("id"));				
		
		SubscribersGroup group = subscribersGroupServices.read(id);
		if (!message.getDestinationList().contains(group))
			message.getDestinationList().add(group);
		return null;

		
	}

	
	public String addSubscriberToDestinationList() {
		
		SubscriberServices subscriberServices = new SubscriberServices();		
		int id = Integer.parseInt(getParameter("id"));				
		
		Subscriber subscriber = subscriberServices.read(id);
		if (!message.getDestinationList().contains(subscriber))
			message.getDestinationList().add(subscriber);
		return null;
		
	}	
	

	
	public String deleteDestination() {
		SubscribersGroupServices subscribersGroupServices = new SubscribersGroupServices();		
		SubscriberServices subscriberServices = new SubscriberServices();
		
		Destination destination;
		
		int id = Integer.parseInt(getParameter("id"));
		String type = getParameter("type");
		
		
		if (type.equals("Group"))
			 destination = subscribersGroupServices.read(id);
		else 
			destination = subscriberServices.read(id);		
		
		message.getDestinationList().remove(destination);
		
		return null;
	}
	
	
	public String send() {
		
		MessageServices messageServices = new MessageServices();
		
		messageServices.sendMessage(message);
		
		addMessage("message", "Message queued to send.");
			
		return null;
	}
	
	public void load(ComponentSystemEvent event) {
		
		String type = getParameter("preload");		
		
		if (type == null) {
			return;
		}
	
		if (type.equals("subscriber")) {
			addSubscriberToDestinationList();		
		} else if (type.equals("group")) {
			addSubscribersGroupToDestinationList();			
		}
		
		
	}
	
}

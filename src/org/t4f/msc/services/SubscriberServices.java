package org.t4f.msc.services;

import java.util.List;
import java.util.logging.Logger;

import org.t4f.msc.db.dao.SubscriberDAO;
import org.t4f.msc.model.Subscriber;

public class SubscriberServices {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	public boolean create(Subscriber subscriber) {
		
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		
		Subscriber subscriberTmp = new Subscriber();
		subscriberTmp.setNickName(subscriber.getNickName());
		
		List<Subscriber> listTmp = subscriberDAO.find(subscriberTmp);
		
		if (listTmp != null && !listTmp.isEmpty())
			return false;				// There is already a user with that nickname
		
		return subscriberDAO.create(subscriber);
		
	}
	
	public Subscriber read(int id) {
		
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.read(id);		
	}
	
	public boolean update(Subscriber subscriber) {
		
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.update(subscriber);		
	}

	
	public boolean delete(Subscriber subscriber) {
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.delete(subscriber);
	}
	
	public boolean delete(int id) {
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.delete(id);
	}
	
	public List<Subscriber> find(Subscriber subscriber) {
		
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.find(subscriber);
		
	}	
	
	public List<Subscriber> findByGroupId(int groupId) {
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		return subscriberDAO.findByGroupId(groupId);
	}
	
}

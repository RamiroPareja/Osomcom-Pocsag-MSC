package org.t4f.msc.services;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.t4f.msc.db.dao.SubscribersGroupDAO;
import org.t4f.msc.model.SubscribersGroup;

public class SubscribersGroupServices {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	public boolean create(SubscribersGroup subscribersGroup) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		
		if (subscribersGroupDAO.readByName(subscribersGroup.getName()) != null)
		{
			LOGGER.warning("Can't create subscribersGroup because it already exists");
			return true;
		}
		
		return subscribersGroupDAO.create(subscribersGroup);
		
	}
	
	public SubscribersGroup read(int id) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.read(id);		
	}
		
	public SubscribersGroup readByName(String name) {		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.readByName(name);		
	}
	
	public boolean update(SubscribersGroup subscribersGroup) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.update(subscribersGroup);		
	}

	
	public boolean delete(SubscribersGroup subscribersGroup) {
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.delete(subscribersGroup);
	}
	
	public boolean delete(int id) {
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.delete(id);
	}
	
	public List<SubscribersGroup> findAll() {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.findAll();
		
	}
	
	
	// Returns all the groups and sets the member attribute of each group in case the user is member
	public List<SubscribersGroup> getGroupsMembershipByUserId(int userId) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();		
		
		List<SubscribersGroup> list = subscribersGroupDAO.findAll();
		List<SubscribersGroup> tmpList = subscribersGroupDAO.findByUserId(userId);
				
		for (SubscribersGroup group: list) {			
			if (tmpList.contains(group))
				group.setMember(true);			
		}
				
				
		return list;
		
	}
	
	
	// Returns only the groups the user is member 
	public List<SubscribersGroup> findByUserId(int userId) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.findByUserId(userId);
		
	}
	
	// Update the membership of the user. 
	public boolean setGroupsMembershipByUserId(int userId, List<SubscribersGroup> list) {	
			
		List<SubscribersGroup> oldList = getGroupsMembershipByUserId(userId);
				
		Iterator<SubscribersGroup> oldListIterator = oldList.iterator();
		Iterator<SubscribersGroup> listIterator = list.iterator();
		boolean errors = false;
		while (listIterator.hasNext()) {
			SubscribersGroup group = listIterator.next();
			SubscribersGroup oldGroup = oldListIterator.next();
			
			if (group.isMember() && !oldGroup.isMember()) {
				// Add group memebership
				errors |= addSubscriberToGroup(userId, group.getId());
			} else if (!group.isMember() && oldGroup.isMember()) {
				// Remove group membership
				errors |= deleteSubscriberFromGroup(userId, group.getId());
			}			
			
		}
		
		return errors;
		
	}
	
	
	public boolean addSubscriberToGroup(int idSubscriber, int idGroup) {
		
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.addSubscriberToGroup(idSubscriber, idGroup);		
	}
	
	public boolean deleteSubscriberFromGroup(int idSubscriber, int idGroup) {
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		return subscribersGroupDAO.deleteSubscriberFromGroup(idSubscriber, idGroup);
	}
	
}

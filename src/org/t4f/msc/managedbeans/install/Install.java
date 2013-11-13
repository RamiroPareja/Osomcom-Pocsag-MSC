package org.t4f.msc.managedbeans.install;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.t4f.msc.db.dao.NodeDAO;
import org.t4f.msc.db.dao.SubscriberDAO;
import org.t4f.msc.db.dao.SubscribersGroupDAO;

@ManagedBean
@ViewScoped
public class Install {
	
	private boolean nodeTableExist;
	private boolean subscriberTableExist;
	private boolean subscribersGroupsTableExist;

	
	
	public boolean isNodeTableExist() {
		return nodeTableExist;
	}



	public void setNodeTableExist(boolean nodeTableExist) {
		this.nodeTableExist = nodeTableExist;
	}



	public boolean isSubscriberTableExist() {
		return subscriberTableExist;
	}


	
	public void setSubscriberTableExist(boolean subscriberTableExist) {
		this.subscriberTableExist = subscriberTableExist;
	}

	


	public boolean isSubscribersGroupsTableExist() {
		return subscribersGroupsTableExist;
	}



	public void setSubscribersGroupsTableExist(boolean subscribersGroupsTableExist) {
		this.subscribersGroupsTableExist = subscribersGroupsTableExist;
	}



	public void checkInstallation(ComponentSystemEvent event) {
		
		NodeDAO nodeDAO = new NodeDAO();
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		
		nodeTableExist = nodeDAO.existTable();
		subscriberTableExist = subscriberDAO.existTable();
		subscribersGroupsTableExist = subscribersGroupDAO.existTable();
		
	}
	
	public String createNodesTable() {
		NodeDAO nodeDAO = new NodeDAO();
		nodeDAO.createTable();
		return null;
	}
	
	public String dropNodesTable() {
		NodeDAO nodeDAO = new NodeDAO();
		nodeDAO.dropTable();
		return null;
	}
	
	public String createSubscribersTable() {
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		subscriberDAO.createTable();
		return null;
	}
	
	public String dropSubscribersTable() {
		SubscriberDAO subscriberDAO = new SubscriberDAO();
		subscriberDAO.dropTable();
		return null;
	}

	public String createSubscribersGroupsTable() {
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		subscribersGroupDAO.createTable();
		return null;
	}
	
	public String dropSubscribersGroupsTable() {
		SubscribersGroupDAO subscribersGroupDAO = new SubscribersGroupDAO();
		subscribersGroupDAO.dropTable();
		return null;
	}
	
	
}


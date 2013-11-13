package org.t4f.msc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.t4f.msc.db.ConnectionManager;


public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		ContextServlet.closeContext();
		
		ConnectionManager.shutdownDataSource();
		ConnectionManager.deregisterDriver();

    
	}
	
	

}

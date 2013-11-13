package org.t4f.msc.servlet;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.t4f.msc.Config;
import org.t4f.msc.network.mqtt.MqttService;
import org.t4f.msc.pocsag.MessageProcessor;

/**
 * Servlet implementation class ContextServlet
 */
public class ContextServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	private static MessageProcessor messageProcessor;

	@Override
	public void init() throws ServletException {
		super.init();

		Level logLevel; 
		String configurationFile = getServletContext().getRealPath("/WEB-INF/");		
		
		

		String tmp = getServletConfig().getInitParameter("configFile");
		
		if (tmp == null || tmp.equals(""))
			configurationFile = getServletContext().getRealPath("/WEB-INF/pocsag-msc.config");
		else 
			configurationFile = getServletContext().getRealPath("/WEB-INF/" + tmp);		
		
		Config.loadFromFile(configurationFile); 
		
		
		logLevel = Level.parse(Config.logLevel);
		 
		 
		ConsoleHandler ch = new ConsoleHandler(); 
		ch.setLevel(logLevel);		
		LOGGER.addHandler(ch);		
		LOGGER.setLevel(logLevel);
		
		LOGGER.finest("Logging system initialized"); 	
		
		
		if (messageProcessor == null) {
			messageProcessor = new MessageProcessor();
			messageProcessor.start();
			LOGGER.info("Message processor started");
		}
		
		if (Config.commUseMqtt)
			MqttService.init();
		
		LOGGER.info("Context Servlet initialized");
		
	}

	
	public static void closeContext() {		
		
		if (messageProcessor != null) {
			messageProcessor.shutdown();
			messageProcessor = null;
		}
		
		if (Config.commUseMqtt)
			MqttService.disconnect();
		
	}
	
	

}

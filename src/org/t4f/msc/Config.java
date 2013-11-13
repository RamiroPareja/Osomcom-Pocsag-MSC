package org.t4f.msc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {

	//private final static Logger LOGGER = Logger.getLogger("POCSAG-BSC");

	
	public static String logLevel = "FINEST";
	public static boolean commUseRest = true ;
	public static boolean commUseMqtt = true ;
	public static String restNodePath = "/OsomcomPOCSAG-BSC/rest/node";
	public static String mqttBroker= "localhost";
	public static int mqttPort= 1883;
	public static String mqttSubscribingTopic= "t4f.org/msc";
	public static String mqttPostingTopic= "t4f.org/bsc";
	public static int commUpdatePeriod = 45;

	
	
	
	public static boolean loadFromFile(String file) {

		
		Properties prop = new Properties();

		try {
			//
			prop.load(new FileInputStream(file));
			
			//LOGGER.info("Loading config file " + file);
			
			logLevel = prop.containsKey("log.level") ? prop.getProperty("log.level") : logLevel;
			commUseRest = prop.containsKey("comm.useRest") ? new Boolean(prop.getProperty("comm.useRest")) : commUseRest;
			commUseMqtt = prop.containsKey("comm.useMqtt") ? new Boolean(prop.getProperty("comm.useMqtt")) : commUseMqtt;
			restNodePath = prop.containsKey("rest.nodePath") ? prop.getProperty("rest.nodePath") : restNodePath;			
			mqttBroker = prop.containsKey("mqtt.broker") ? prop.getProperty("mqtt.broker") : mqttBroker;
			try {
				mqttPort = prop.containsKey("mqtt.port") ? new Integer(prop.getProperty("mqtt.port")) : mqttPort;
			} catch (NumberFormatException ne) {
				System.err.println("Error parsing config file. 'mqtt.port' value '" +  prop.getProperty("mqtt.port") + "' is incorrect");
			}
			mqttSubscribingTopic = prop.containsKey("mqtt.subscribingTopic") ? prop.getProperty("mqtt.subscribingTopic") : mqttSubscribingTopic;
			mqttPostingTopic = prop.containsKey("mqtt.postingTopic") ? prop.getProperty("mqtt.postingTopic") : mqttPostingTopic;
			
			
			try {
				commUpdatePeriod = prop.containsKey("comm.updatePeriod") ? new Integer(prop.getProperty("comm.updatePeriod")) : commUpdatePeriod;				
			} catch (NumberFormatException ne) {
				//LOGGER.warning("Error parsing config file. 'masterUpdatePeriod' value '" +  prop.getProperty("masterUpdatePeriod") + "' is incorrect");
				System.err.println("Error parsing config file. 'masterUpdatePeriod' value '" +  prop.getProperty("masterUpdatePeriod") + "' is incorrect");
			}

		} catch (IOException ex) {
			//LOGGER.severe("Error Loading config file:\n " + ex);
			System.err.println("Error Loading config file:\n " + ex);
			return true;
		}
		
		return false;

	}

}

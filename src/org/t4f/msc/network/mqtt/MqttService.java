package org.t4f.msc.network.mqtt;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.t4f.msc.Config;
import org.t4f.msc.model.Node;
import org.t4f.msc.pocsag.PocsagMessage;
import org.t4f.msc.services.NodeServices;

public class MqttService {
	
	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	private static boolean error = false;
	private static boolean initialized = false;
	private static MQTT mqtt;
	private static CallbackConnection connection;
	
	public MqttService() {

			init();
	}
	
	public static boolean init() {
		
		if (initialized) 
			return true;
		
		error = false;
		initialized = false;
		
		// Configure the broker
		mqtt = new MQTT();
		try {
			mqtt.setHost(Config.mqttBroker, Config.mqttPort);
		} catch (URISyntaxException e) {
			LOGGER.severe("Error configuring MQTT broker URL: " + e);
			return true;
		}
		
		
		// Configure the listener that will process the incoming messages
		connection = mqtt.callbackConnection();
		//final Boolean error = new Boolean(false);
		connection.listener(new Listener() {

		    public void onDisconnected() {
		    }
		    public void onConnected() {
		    }

		    public void onFailure(Throwable value) {
		        connection.disconnect(null); // a connection failure occured.
		    }
		    
		    // Callback for processing the published topics
			public void onPublish(UTF8Buffer topic, Buffer message, Runnable arg2) {			
		    	LOGGER.fine("Received MQTT message on topic '"+ topic + "': " + message);
		    	
		    	final String tmp = message.toString();
		    	
		    	Runnable MqttMsgProcessorTask = new Runnable() {
					
					@Override
					public void run() {

				    	Node node = new Node();
						NodeServices nodeServices = new NodeServices();
						
						node.setGroup(1);
						node.setName(tmp.substring(7, tmp.indexOf(':',7)));				
						node.setProtocol(Node.Protocol.MQTT);
						node.setLastPing(new Timestamp((new Date()).getTime()));
						
						nodeServices.createOrUpdateByName(node);
						
					}
				};
				
				new Thread(MqttMsgProcessorTask).start();
		    	
		    	
		    	arg2.run();	// Must be executed after processing the message
			}
		});
		
		connection.connect(new Callback<Void>() {
		    public void onFailure(Throwable value) {
		        //con result.failure(value); // If we could not connect to the server.
		    	LOGGER.severe("Error connection broker " + Config.mqttBroker + ":" + Config.mqttPort);
		    	error = true;
		    }


		    public void onSuccess(Void v) {

		    	LOGGER.fine("Connected to " + Config.mqttBroker + ":" + Config.mqttPort);

		        // Subscribe to topics
		        Topic[] topics = {new Topic(Config.mqttSubscribingTopic, QoS.AT_LEAST_ONCE)};
		        connection.subscribe(topics, new Callback<byte[]>() {
		            public void onSuccess(byte[] qoses) {
				    	LOGGER.fine("Subscribed to " + Config.mqttSubscribingTopic);
				    	initialized = true;
		            }
		            public void onFailure(Throwable value) {
		            	LOGGER.severe("Failed to subscribe to " + Config.mqttSubscribingTopic);
		            	error = true;
		                connection.disconnect(null); // subscribe failed.
		            }
		        });

		    
		      }
		});
		
		return error;
		
	}
	
	private boolean postMessage(String message) {

		if (!initialized) 
			return true;
		
        connection.publish(Config.mqttPostingTopic, message.getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
            public void onSuccess(Void v) {
		    	LOGGER.finer("Message send to " + Config.mqttPostingTopic);
            }
            public void onFailure(Throwable value) {
            	LOGGER.finer("Message failed to send to " + Config.mqttPostingTopic);
            }
        });
        
        return false;
	}
	
	public boolean sendPocsagMessageToNode(PocsagMessage message) {

		String msg = message.toMqttString() + "-" + System.currentTimeMillis();
		return postMessage(msg);		
		
	}
	
	public static boolean disconnect() {
		
		if (!initialized) 
			return true;
		
		initialized = false;
		
		connection.disconnect(new Callback<Void>() {
            public void onSuccess(Void v) {
            	LOGGER.fine("Disconnected from MQTT broker");
            }
            public void onFailure(Throwable value) {
            	LOGGER.warning("Error disconnecting from MQTT broker");
            }
        });
		
		return false;
		
	}

}

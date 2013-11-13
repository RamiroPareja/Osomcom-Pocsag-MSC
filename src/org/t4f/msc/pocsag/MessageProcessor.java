package org.t4f.msc.pocsag;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.t4f.msc.Config;
import org.t4f.msc.model.Node;
import org.t4f.msc.model.Node.Protocol;
import org.t4f.msc.network.mqtt.MqttService;
import org.t4f.msc.network.rest.consumer.NodeRestConsumer;
import org.t4f.msc.services.NodeServices;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class MessageProcessor extends Thread{
	
	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
    
	private volatile boolean running = true;
	
	private static LinkedBlockingQueue<PocsagMessage> pocsagMessagesQueue = new LinkedBlockingQueue<PocsagMessage>();
	
	public MessageProcessor() {		
		super("MessageProcessor");
	}
	
	public synchronized void run() {
		
		while (running) {

			try {
				PocsagMessage pocsagMessage = takeMessageQueue();
				processPocsagMessage(pocsagMessage);
			} catch (InterruptedException e) {
				shutdown();
			}
			
		}		
	}
	
	public void	shutdown() {
		running = false;
		LOGGER.fine("Stopping MessageProcessor thread");
	}
	
	public synchronized boolean enqueuePocsagMessage(PocsagMessage pocsagMessage) {
		
		try {
			pocsagMessagesQueue.put(pocsagMessage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;		
	}
	
	public synchronized boolean isMessageQueueEmpty() {
		
		return pocsagMessagesQueue.isEmpty();
				
	}
	
	private synchronized PocsagMessage takeMessageQueue() throws InterruptedException {
		
		return pocsagMessagesQueue.take();
						
	}
	
	private boolean processPocsagMessage(PocsagMessage pocsagMessage) {
		
		NodeServices nodeServices = new NodeServices();
		NodeRestConsumer nodeRestConsumer = new NodeRestConsumer();
		MqttService mqttService = new MqttService();
		
		if (Config.commUseMqtt) {
			mqttService.sendPocsagMessageToNode(pocsagMessage);			
		}
		
		if (Config.commUseRest) {
		
			List<Node> list = nodeServices.findAll();		
			
			for (Node node : list) {
				if (node.getState() == Node.NodeState.ACTIVE && node.getProtocol() == Protocol.REST) 
					nodeRestConsumer.sendPocsagMessageToNode(pocsagMessage, node);
	
			}
		}
		
		return false;
		
	}	

}

package org.t4f.msc.services;

import java.util.ArrayList;
import java.util.List;

import org.t4f.msc.model.Destination;
import org.t4f.msc.model.Message;
import org.t4f.msc.model.Subscriber;
import org.t4f.msc.model.SubscribersGroup;
import org.t4f.msc.pocsag.MessageProcessor;
import org.t4f.msc.pocsag.PocsagMessage;

public class MessageServices {

	public boolean sendMessage(Message message) {

		SubscriberServices subscriberServices = new SubscriberServices();
		MessageProcessor messageProcessor = new MessageProcessor();

		List<Subscriber> destinationList = new ArrayList<Subscriber>();

		// Copy Subscribers checking for repited ones. Unfold Subscribers groups
		for (Destination destination : message.getDestinationList()) {

			if (destination instanceof Subscriber) {
				if (!destinationList.contains(destination)) {
					destinationList.add((Subscriber) destination);
				}
			} else if (destination instanceof SubscribersGroup) {

				List<Subscriber> subscriberList = subscriberServices.findByGroupId(destination.getId());

				for (Subscriber subscriber : subscriberList) {
					if (!destinationList.contains(subscriber)) {
						destinationList.add(subscriber);
					}
				}
			}		
		}
		
		// Create and enqueue pocsag messages
		for (Subscriber subscriber:destinationList) {
			
			PocsagMessage pocsagMessage = new PocsagMessage(); 
			pocsagMessage.setMessage(message.getText());
			pocsagMessage.setMsgType(PocsagMessage.MessageType.valueOf(message.getType().toString()));
			pocsagMessage.setRIC(subscriber.getRIC());
			pocsagMessage.setFrequency(subscriber.getFrequency());
			pocsagMessage.setBauds(subscriber.getBauds());
			
			messageProcessor.enqueuePocsagMessage(pocsagMessage);
			
		}
		
		
		return false;

	}

}

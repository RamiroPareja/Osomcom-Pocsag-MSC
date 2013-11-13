package org.t4f.msc.network.rest.consumer;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.t4f.msc.Config;
import org.t4f.msc.model.Node;
import org.t4f.msc.pocsag.PocsagMessage;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class NodeRestConsumer {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	public boolean sendPocsagMessageToNode(PocsagMessage message, Node node) {
		
		String restURL = "http://" + node.getHost() + ":" + node.getPort()
				+ Config.restNodePath + "/" + message.getRIC();
	
		LOGGER.finer("Sending Message using REST petition to " + restURL);

		Client client = Client.create();

		WebResource webResource = client.resource(restURL);

		Form form = new Form();
		form.add("frequency", message.getFrequency());
		form.add("bauds", message.getBauds());
		form.add("msgType", message.getMsgType().toString());
		form.add("message", message.getMessage());

		ClientResponse response = webResource.accept(MediaType.TEXT_PLAIN)
				.type(MediaType.APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, form);

		if (response.getStatus() != 200) {
			LOGGER.warning("Unable to connect to node. HTTP error code: "
					+ response.getStatus());
			return true;
		}

		//String output = response.getEntity(String.class);
		
		return false;
	}
}

package org.t4f.msc.network.rest.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.t4f.msc.model.Node;
import org.t4f.msc.services.NodeServices;

@Path("/node")
public class NodeREST {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	@Context 
	HttpServletRequest request; 
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String send(@FormParam("port") int port, @FormParam("name") String name, @FormParam("time") long time ) {
		
		Node node = new Node();
		
		String host = request.getRemoteAddr();
		
		LOGGER.finer("REST NODE PUT - host: " + host + " - port: " + port + " - name: " + name + " - time: " + time);
		
		node.setName(name);
		node.setHost(host);
		node.setPort(port);
		node.setGroup(1);
		node.setProtocol(Node.Protocol.REST);
		node.setLastPing(new Timestamp((new Date()).getTime()));
		
		NodeServices nodeServices = new NodeServices();
		
		if (nodeServices.createOrUpdateByName(node)) {
			return "ERROR CREATING OR UPDATING NODE";			
		} else {
			return "OK";
		}
		
		
		
	}
	
}

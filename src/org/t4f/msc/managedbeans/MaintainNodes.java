package org.t4f.msc.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;

import org.t4f.msc.model.Node;
import org.t4f.msc.services.NodeServices;



@ManagedBean
@ViewScoped
public class MaintainNodes extends ManagedBeanCommon implements Serializable {


	private static final long serialVersionUID = 566165431044999030L;

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	
	private Node nodeFilter = new Node();
	private Node nodeEdit;
	private transient List<Node> nodeList;
	

	
		
	
	public Node getNodeFilter() {
		return nodeFilter;
	}

	public void setNodeFilter(Node nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	public Node getNodeEdit() {
		return nodeEdit;
	}

	public void setNodeEdit(Node nodeEdit) {
		this.nodeEdit = nodeEdit;
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public void loadNodes(ComponentSystemEvent event) {
		loadNodes();
	}
	public void loadNodes() {
		
		NodeServices nodeServices = new NodeServices();
		
		nodeList = nodeServices.find(nodeFilter);
		
		
	}
	
	public String loadNodeToEdit() {		
		
		NodeServices nodeServices = new NodeServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if ((this.nodeEdit = nodeServices.read(id)) == null)
			return "main";	//error
		else 		
			return null;
	}

	
	public String editNode() {
		NodeServices nodeServices = new NodeServices();		
		
		if (nodeServices.update(nodeEdit))
			addMessage("message", "Error saving changes.");
		else 		
			addMessage("message", "Changes saved correctly.");
			
		return null;
	}
	
	
	public String deleteNode() {
		
		NodeServices nodeServices = new NodeServices();		
		int id = Integer.parseInt(getParameter("id"));		
		
		
		if (nodeServices.delete(id))
			addMessage("message", "Error deleting Node.");
		else 		
			addMessage("message", "Node deleted.");
		
		return null;
	}
	
}

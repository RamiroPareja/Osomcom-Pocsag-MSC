package org.t4f.msc.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.t4f.msc.db.dao.NodeDAO;
import org.t4f.msc.model.Node;

public class NodeServices {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	public boolean create(Node node) {

		NodeDAO nodeDAO = new NodeDAO();

		if (nodeDAO.readByName(node.getName()) != null
				|| nodeDAO.readByHost(node.getHost()) != null) {
			LOGGER.warning("Can't create node because it already exists in NODES table");
			return true;
		}

		return nodeDAO.create(node);

	}

	public Node read(int id) {

		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.read(id);
	}

	public Node readByHost(String host) {
		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.readByHost(host);
	}

	public Node readByName(String name) {
		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.readByName(name);
	}

	public boolean update(Node node) {

		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.update(node);
	}

	public boolean delete(Node node) {
		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.delete(node);
	}

	public boolean delete(int id) {
		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.delete(id);
	}

	public List<Node> findAll() {

		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.findAll();

	}

	// Create or Update Node using name as reference to know if it is already stored
	public boolean createOrUpdateByName(Node node) {

		Node nodeTmp = readByName(node.getName());

		if (nodeTmp != null) {
			LOGGER.finer("Updating node " + node.getName());
			return update(node);				
		} else {
			LOGGER.finer("Creating node " + node.getName());			
			return create(node);
		}

	}

	public List<Node> find(Node node) {
		NodeDAO nodeDAO = new NodeDAO();
		return nodeDAO.find(node);
	}

}

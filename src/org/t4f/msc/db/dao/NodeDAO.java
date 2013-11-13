package org.t4f.msc.db.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.t4f.msc.db.ConnectionManager;
import org.t4f.msc.model.Node;
import org.t4f.msc.model.Node.Protocol;

public class NodeDAO {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	private static final String QUERY_CREATE_TABLE = "CREATE TABLE NODES"
			+ "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "NAME VARCHAR(40) NOT NULL UNIQUE, PROTOCOL VARCHAR(10) NOT NULL, HOST VARCHAR(100), PORT SMALLINT, "
			+ "NODEGROUP SMALLINT, LASTPING TIMESTAMP, CONSTRAINT PRIMARY_KEY_NODES PRIMARY KEY (ID))";
	
	private static final String QUERY_DROP_TABLE = "DROP TABLE NODES";

	private static final String QUERY_CREATE = "INSERT INTO NODES (NAME, PROTOCOL, HOST, PORT, NODEGROUP, LASTPING)"
			+ " VALUES (?,?,?,?,?,?)";

	private static final String QUERY_READ = "SELECT * FROM NODES WHERE ID = ?";
	private static final String QUERY_READ_BY_HOST = "SELECT * FROM NODES WHERE HOST = ?";
	private static final String QUERY_READ_BY_NAME = "SELECT * FROM NODES WHERE NAME = ?";
	
	private static final String QUERY_UPDATE = "UPDATE NODES SET NAME=?, PROTOCOL=?, HOST=?, PORT=?, NODEGROUP=?, LASTPING=?"
			+ " WHERE ID = ?";
	
	private static final String QUERY_DELETE = "DELETE FROM NODES WHERE ID = ?";

	private static final String QUERY_FINDALL = "SELECT * FROM NODES";

	private static final String QUERY_FIND = "SELECT * FROM NODES WHERE UPPER(NAME) LIKE ? "
			+ "AND UPPER(HOST) LIKE ?";



	public boolean existTable() {
		try {
			return ConnectionManager.checkTableExist("NODES");
		} catch (SQLException e) {
			LOGGER.warning("Can't access DB to check if table NODES if exists\n" + e.toString());
			return false;
		}		
	}

	public boolean createTable() {

		LOGGER.info("Creating table NODES");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + QUERY_CREATE_TABLE);
			st.executeUpdate(QUERY_CREATE_TABLE);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't create table NODES\n" + e.toString());
			return true;
		}

	}
	
	public boolean dropTable() {

		LOGGER.info("Dropping table NODES");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + QUERY_DROP_TABLE);
			st.executeUpdate(QUERY_DROP_TABLE);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't drop table NODES\n" + e.toString());
			return true;
		}

	}


	public boolean create(Node node) {

		LOGGER.finest("Inserting into table NODES:\n"
				+ node.toString());

		try {
			
			String query = QUERY_CREATE.replaceFirst(Pattern.quote("?"), "'" + node.getName() + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + node.getProtocol() + "'");
			query = query.replaceFirst(Pattern.quote("?"),node.getHost()==null?"''":"'" + node.getHost() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "" + node.getPort());
			query = query.replaceFirst(Pattern.quote("?"), "" + node.getGroup());
			query = query.replaceFirst(Pattern.quote("?"), "'" + node.getLastPing() + "'");
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();		
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
		
		} catch (SQLException e) {
			LOGGER.severe("Can't insert into table NODES.\n"
					+ e.toString());
			return true;
		}

	}

	public Node read(int id) {

		LOGGER.finest("Reading from table NODES for node with ID " + id);

			
		String query = QUERY_READ.replaceFirst(Pattern.quote("?"), "" + id);

		return readGeneric(query);


	}
	
	public Node readByHost(String host) {

		LOGGER.finest("Reading from table NODES for node with HOST "
				+ host);

		String query = QUERY_READ_BY_HOST.replaceFirst(Pattern.quote("?"), "'" + host + "'");

		return readGeneric(query);

	}
	
	public Node readByName(String name) {

		LOGGER.finest("Reading from table NODES for node with NAME "
				+ name);

		String query = QUERY_READ_BY_NAME.replaceFirst(Pattern.quote("?"), "'" + name + "'");

		return readGeneric(query);

	}
	
	
	
	private Node readGeneric(String query) {



		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			ResultSet rs = st.executeQuery(query);
		
			if (rs.next()) {
				Node s = rs2Node(rs);
				rs.close();
				st.close();
				conn.close();
				return s;
			}			
			else
				return null;
		} catch (SQLException e) {
			LOGGER.finest("Can't read from table NODES.\n" + e.toString());
			return null;
		}

	}
	
	public boolean update(Node node) {

		LOGGER.finest("Updating in table NODE:\n"
				+ node.toString());

		try {
			
			String query = QUERY_UPDATE.replaceFirst(Pattern.quote("?"), "'" + node.getName() + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + node.getProtocol() + "'");
			query = query.replaceFirst(Pattern.quote("?"),node.getHost()==null?"NULL":"'" + node.getHost() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "" + node.getPort());
			query = query.replaceFirst(Pattern.quote("?"), "" + node.getGroup());
			query = query.replaceFirst(Pattern.quote("?"), "'" + node.getLastPing() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "" + node.getId());			
						
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();	
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
			
		
		} catch (SQLException e) {
			LOGGER.severe("Can't update in table NODES.\n"
					+ e.toString());
			return true;
		}

	}
	
	public boolean delete(int id) {

		LOGGER.finest("Deleting from table NODES node with id " + id);

		try {
			

			String query = QUERY_DELETE.replaceFirst(Pattern.quote("?"), "" + id);
			
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();		
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
	
		} catch (SQLException e) {
			LOGGER.severe("Can't delete in table NODES.\n"
					+ e.toString());
			return true;
		}

	}
	
	public boolean delete(Node node) {
		return delete(node.getId());
	}
	
	

	public List<Node> findAll() {

		LOGGER.finest("Finding all nodes in table NODES");

		ArrayList<Node> list = new ArrayList<Node>();		
		
		
		try {
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();		
			LOGGER.finest("Executing query: " + QUERY_FINDALL);
			ResultSet rs = st.executeQuery(QUERY_FINDALL);
			

			while (rs.next()) {
				list.add(rs2Node(rs));
			}
			
			rs.close();
			st.close();
			conn.close();
			return list;
			
		} catch (SQLException e) {
			LOGGER.finest("Can't get elements in table NODES.\n" + e.toString());
			return null;
		}

	}
	
	
	public List<Node> find(Node node) {

		LOGGER.finest("Finding in table NODE for subscribers that match: " + node);

		ArrayList<Node> list = new ArrayList<Node>();
		
		String name = node.getName();
		name = (name == null || name.equals("")) ? "%" : name.toUpperCase().replace("*", "%");
		
		String host = node.getHost();
		host  = (host  == null || host.equals("")) ? "%" : host.toUpperCase().replace("*", "%");
		
		String group  = (node.getGroup() == null) ? "%" : node.getGroup().toString();		
		
		try {
			

			String query = QUERY_FIND.replaceFirst(Pattern.quote("?"), "'" + name + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + host + "'");
						
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();	
			LOGGER.finest("Executing query: " + query);
			ResultSet rs = st.executeQuery(query);
			

			while (rs.next()) {
				list.add(rs2Node(rs));
			}
			
			rs.close();
			st.close();
			conn.close();
			return list;
			
		} catch (SQLException e) {
			LOGGER.finest("Can't find in table NODES.\n" + e.toString());
			return null;
		}

	}
	
	
	
	private Node rs2Node(ResultSet rs) throws SQLException {

		Node node = new Node();

		node.setId(rs.getInt("ID"));
		node.setName(rs.getString("NAME"));
		node.setProtocol(Protocol.valueOf(rs.getString("PROTOCOL")));
		node.setHost(rs.getString("HOST"));
		node.setPort(rs.getInt("PORT"));
		node.setGroup(rs.getInt("NODEGROUP"));
		node.setLastPing(rs.getTimestamp("LASTPING"));
		
		return node;

	}
}

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
import org.t4f.msc.model.Subscriber;

public class SubscriberDAO {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	private static final String QUERY_CREATE_TABLE = "CREATE TABLE SUBSCRIBERS"
			+ "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "NICKNAME VARCHAR(20) NOT NULL UNIQUE, REALNAME VARCHAR(100), RIC INTEGER NOT NULL, "
			+ " FREQUENCY INTEGER NOT NULL, BAUDS SMALLINT NOT NULL, CONSTRAINT PRIMARY_KEY_SUBSCRIBERS PRIMARY KEY (ID))";
	
	private static final String QUERY_DROP_TABLE = "DROP TABLE SUBSCRIBERS";

	private static final String QUERY_CREATE = "INSERT INTO SUBSCRIBERS (NICKNAME, REALNAME, RIC, FREQUENCY,"
			+ " BAUDS) VALUES (?,?,?,?,?)";

	private static final String QUERY_READ = "SELECT * FROM SUBSCRIBERS WHERE ID = ?";
	
	private static final String QUERY_UPDATE = "UPDATE SUBSCRIBERS SET NICKNAME=?, REALNAME=?, RIC=?, FREQUENCY=?,"
			+ " BAUDS=? WHERE ID =?";
	
	private static final String QUERY_DELETE = "DELETE FROM SUBSCRIBERS WHERE ID = ?";

	private static final String QUERY_FIND = "SELECT * FROM SUBSCRIBERS WHERE UPPER(NICKNAME) LIKE ? "
			+ "AND UPPER(REALNAME) LIKE ? AND CHAR(RIC) LIKE ? AND CHAR(FREQUENCY) LIKE ? AND CHAR(BAUDS) LIKE ?";

	private static final String QUERY_FIND_BY_GROUPID = "SELECT s.* FROM SUBSCRIBERS s, SUBSCRIBER_SUBSCRIBERS_GROUPS ssg where s.ID = ssg.ID_SUBSCRIBER and ssg.ID_SUBSCRIBERS_GROUP = ?";

	public boolean existTable() {
		try {
			return ConnectionManager.checkTableExist("SUBSCRIBERS");
		} catch (SQLException e) {
			LOGGER.warning("Can't access DB to check if table NODES if exists\n" + e.toString());
			return false;
		}		
	}

	public boolean createTable() {

		LOGGER.info("Creating table SUBSCRIBER");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(QUERY_CREATE_TABLE);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't create table SUBSCRIBERS\n" + e.toString());
			return true;
		}

	}
	
	public boolean dropTable() {

		LOGGER.info("Dropping table SUBSCRIBER");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			st.executeUpdate(QUERY_DROP_TABLE);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't drop table SUBSCRIBERS\n" + e.toString());
			return true;
		}

	}


	public boolean create(Subscriber subscriber) {

		LOGGER.finest("Inserting into table SUBSCRIBER:\n"
				+ subscriber.toString());

		try {
			
			String query = QUERY_CREATE.replaceFirst(Pattern.quote("?"), "'" + subscriber.getNickName() + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + subscriber.getRealName() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getRIC());
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getFrequency());
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getBauds());
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();			
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
		
		} catch (SQLException e) {
			LOGGER.severe("Can't insert into table SUBSCRIBERS.\n"
					+ e.toString());
			return true;
		}

	}

	public Subscriber read(int id) {

		LOGGER.finest("Reading from table SUBSCRIBER for subscriber with ID "
				+ id);

		try {
			
			String query = QUERY_READ.replaceFirst(Pattern.quote("?"), "" + id);
			
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
		
			if (rs.next()) {
				Subscriber s = rs2Subscriber(rs);
				rs.close();
				st.close();
				conn.close();
				return s;
			}			
			else
				return null;
		} catch (SQLException e) {
			LOGGER.finest("Can't read from table SUBSCRIBERS.\n" + e.toString());
			return null;
		}

	}
	
	public boolean update(Subscriber subscriber) {

		LOGGER.finest("Updating in table SUBSCRIBER:\n"
				+ subscriber.toString());

		try {
			
			
			String query = QUERY_UPDATE.replaceFirst(Pattern.quote("?"), "'" + subscriber.getNickName() + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + subscriber.getRealName() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getRIC());
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getFrequency());
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getBauds());
			query = query.replaceFirst(Pattern.quote("?"), "" + subscriber.getId());
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();			
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
			
		
		} catch (SQLException e) {
			LOGGER.severe("Can't update in table SUBSCRIBERS.\n"
					+ e.toString());
			return true;
		}

	}
	
	public boolean delete(int id) {

		LOGGER.finest("Deleting from table SUBSCRIBER subscriber with id " + id);

		try {
			

			String query = QUERY_DELETE.replaceFirst(Pattern.quote("?"), "" + id);
			
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();			
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);
	
		} catch (SQLException e) {
			LOGGER.severe("Can't delete in table SUBSCRIBERS.\n"
					+ e.toString());
			return true;
		}

	}
	
	public boolean delete(Subscriber subscriber) {
		return delete(subscriber.getId());
	}
	
	

	public List<Subscriber> find(Subscriber subscriber) {

		LOGGER.finest("Finding in table SUBSCRIBER for subscribers that match: " + subscriber);

		ArrayList<Subscriber> list = new ArrayList<Subscriber>();
		
		String nickName = subscriber.getNickName();
		nickName = (nickName == null || nickName.equals("")) ? "%" : nickName.toUpperCase().replace("*", "%");
		
		String realName = subscriber.getRealName();
		realName  = (realName  == null || realName.equals("")) ? "%" : realName.toUpperCase().replace("*", "%");
		
		String RIC  = (subscriber.getRIC() == null) ? "%" : subscriber.getRIC().toString();
		
		String frequency = (subscriber.getFrequency() == null) ? "%" : subscriber.getFrequency().toString();
		
		String bauds = (subscriber.getBauds() == null) ? "%" : subscriber.getBauds().toString();
		
		try {
			

			String query = QUERY_FIND.replaceFirst(Pattern.quote("?"), "'" + nickName + "'");
			query = query.replaceFirst(Pattern.quote("?"),"'" + realName + "'");
			query = query.replaceFirst(Pattern.quote("?"), "'" + RIC + "'");
			query = query.replaceFirst(Pattern.quote("?"), "'" + frequency + "'");
			query = query.replaceFirst(Pattern.quote("?"), "'" + bauds + "'");
			
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();			
			ResultSet rs = st.executeQuery(query);
			

			while (rs.next()) {
				list.add(rs2Subscriber(rs));
			}
			
			rs.close();
			st.close();
			conn.close();
			return list;
			
		} catch (SQLException e) {
			LOGGER.finest("Can't find in table SUBSCRIBERS.\n" + e.toString());
			return null;
		}

	}
	
	
	public List<Subscriber> findByGroupId(int groupId) {

		LOGGER.finest("Finding subscribers in table SUBSCRIBERS_GROUPS for group id = "
				+ groupId);

		ArrayList<Subscriber> list = new ArrayList<Subscriber>();

		String query = QUERY_FIND_BY_GROUPID.replaceFirst(Pattern.quote("?"), ""
				+ groupId);

		try {

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				list.add(rs2Subscriber(rs));
			}

			rs.close();
			st.close();
			conn.close();
			return list;

		} catch (SQLException e) {
			LOGGER.finest("Can't get elements in table SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return null;
		}

	}
	
	private Subscriber rs2Subscriber(ResultSet rs) throws SQLException {

		Subscriber subscriber = new Subscriber();

		subscriber.setId(rs.getInt("ID"));
		subscriber.setNickName(rs.getString("NICKNAME"));
		subscriber.setRealName(rs.getString("REALNAME"));
		subscriber.setRIC(rs.getInt("RIC"));
		subscriber.setFrequency(rs.getInt("FREQUENCY"));
		subscriber.setBauds(rs.getInt("BAUDS"));

		return subscriber;

	}
}

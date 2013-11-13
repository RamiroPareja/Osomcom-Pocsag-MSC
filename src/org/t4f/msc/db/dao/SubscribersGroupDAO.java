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
import org.t4f.msc.model.SubscribersGroup;

public class SubscribersGroupDAO {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");

	private static final String QUERY_CREATE_TABLE = "CREATE TABLE SUBSCRIBERS_GROUPS"
			+ "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "NAME VARCHAR(20) NOT NULL UNIQUE, DESCRIPTION VARCHAR(100), CONSTRAINT PRIMARY_KEY_SUBSCRIBERS_GROUP PRIMARY KEY (ID))";

	private static final String QUERY_CREATE_TABLE_RELATION_SUBSCRIBER_GROUP = "CREATE TABLE SUBSCRIBER_SUBSCRIBERS_GROUPS"
			+ "(ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "ID_SUBSCRIBER INTEGER NOT NULL, ID_SUBSCRIBERS_GROUP INTEGER, CONSTRAINT PRIMARY_KEY_SUBSCRIBER_SUBSCRIBERS_GROUP PRIMARY KEY (ID))";

	private static final String QUERY_DROP_TABLE = "DROP TABLE SUBSCRIBERS_GROUPS";
	private static final String QUERY_DROP_TABLE_RELATION_SUBSCRIBER_GROUP = "DROP TABLE SUBSCRIBER_SUBSCRIBERS_GROUPS";

	private static final String QUERY_CREATE = "INSERT INTO SUBSCRIBERS_GROUPS (NAME,DESCRIPTION) VALUES (?,?)";

	private static final String QUERY_READ = "SELECT * FROM SUBSCRIBERS_GROUPS WHERE ID = ?";
	private static final String QUERY_READ_BY_NAME = "SELECT * FROM SUBSCRIBERS_GROUPS WHERE NAME = ?";

	private static final String QUERY_UPDATE = "UPDATE SUBSCRIBERS_GROUPS SET NAME=?, DESCRIPTION=? WHERE ID=?";

	private static final String QUERY_DELETE = "DELETE FROM SUBSCRIBERS_GROUPS WHERE ID = ?";

	private static final String QUERY_FINDALL = "SELECT * FROM SUBSCRIBERS_GROUPS";
	private static final String QUERY_FIND_BY_USERID = "SELECT sg.* FROM SUBSCRIBERS_GROUPS sg, SUBSCRIBER_SUBSCRIBERS_GROUPS ssg where sg.ID = ssg.ID_SUBSCRIBERS_GROUP and ssg.ID_SUBSCRIBER = ?";

	private static final String QUERY_ADD_SUBSCRIBER_TO_GROUP = "INSERT INTO SUBSCRIBER_SUBSCRIBERS_GROUPS (ID_SUBSCRIBER, ID_SUBSCRIBERS_GROUP) VALUES (?,?)";
	private static final String QUERY_DELETE_SUBSCRIBER_TO_GROUP = "DELETE FROM SUBSCRIBER_SUBSCRIBERS_GROUPS WHERE ID_SUBSCRIBER=? AND ID_SUBSCRIBERS_GROUP=?";

	public boolean existTable() {
		try {
			return (ConnectionManager.checkTableExist("SUBSCRIBERS_GROUPS") && ConnectionManager
					.checkTableExist("SUBSCRIBER_SUBSCRIBERS_GROUPS"));
		} catch (SQLException e) {
			LOGGER.warning("Can't access DB to check if table SUBSCRIBERS_GROUPS and st.execute(QUERY_CREATE_TABLE); exists\n"
					+ e.toString());
			return false;
		}
	}

	public boolean createTable() {

		LOGGER.info("Creating table SUBSCRIBERS_GROUPS and SUBSCRIBER_SUBSCRIBERS_GROUPS");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + QUERY_CREATE);
			st.execute(QUERY_CREATE_TABLE);
			LOGGER.finest("Executing query: "
					+ QUERY_CREATE_TABLE_RELATION_SUBSCRIBER_GROUP);
			st.executeUpdate(QUERY_CREATE_TABLE_RELATION_SUBSCRIBER_GROUP);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't create table SUBSCRIBERS_GROUPS and SUBSCRIBER_SUBSCRIBERS_GROUPS\n"
					+ e.toString());
			return true;
		}

	}

	public boolean dropTable() {

		LOGGER.info("Dropping table SUBSCRIBERS_GROUPS and SUBSCRIBER_SUBSCRIBERS_GROUPS");

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + QUERY_DROP_TABLE);
			st.execute(QUERY_DROP_TABLE);
			LOGGER.finest("Executing query: "
					+ QUERY_DROP_TABLE_RELATION_SUBSCRIBER_GROUP);
			st.executeUpdate(QUERY_DROP_TABLE_RELATION_SUBSCRIBER_GROUP);
			st.close();
			conn.close();
			return false;
		} catch (SQLException e) {
			LOGGER.warning("Can't drop table SUBSCRIBERS_GROUPS and SUBSCRIBER_SUBSCRIBERS_GROUPS\n"
					+ e.toString());
			return true;
		}

	}

	public boolean create(SubscribersGroup subscribersGroup) {

		LOGGER.finest("Inserting into table SUBSCRIBERS_GROUPS:\n"
				+ subscribersGroup.toString());

		try {

			String query = QUERY_CREATE.replaceFirst(Pattern.quote("?"), "'"
					+ subscribersGroup.getName() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "'"
					+ subscribersGroup.getDescription() + "'");

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);

		} catch (SQLException e) {
			LOGGER.severe("Can't insert into table SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return true;
		}

	}

	public SubscribersGroup read(int id) {

		LOGGER.finest("Reading from table SUBSCRIBERS_GROUPS for subscriberGroup with ID "
				+ id);

		String query = QUERY_READ.replaceFirst(Pattern.quote("?"), "" + id);

		return readGeneric(query);

	}

	public SubscribersGroup readByName(String name) {

		LOGGER.finest("Reading from table SUBSCRIBERS_GROUPS for subscriberGroups with NAME "
				+ name);

		String query = QUERY_READ_BY_NAME.replaceFirst(Pattern.quote("?"), "'"
				+ name + "'");

		return readGeneric(query);

	}

	public SubscribersGroup readGeneric(String query) {

		try {
			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			ResultSet rs = st.executeQuery(query);

			if (rs.next()) {
				SubscribersGroup s = rs2SubscribersGroup(rs);
				rs.close();
				st.close();
				conn.close();
				return s;
			} else
				return null;
		} catch (SQLException e) {
			LOGGER.finest("Can't read from table SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return null;
		}

	}

	public boolean update(SubscribersGroup subscribersGroup) {

		LOGGER.finest("Updating in table SUBSCRIBERS_GROUPS:\n"
				+ subscribersGroup.toString());

		try {

			String query = QUERY_UPDATE.replaceFirst(Pattern.quote("?"), "'"
					+ subscribersGroup.getName() + "'");
			query = query.replaceFirst(Pattern.quote("?"), "'"
					+ subscribersGroup.getDescription() + "'");
			query = query.replaceFirst(Pattern.quote("?"), ""
					+ subscribersGroup.getId());

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);

		} catch (SQLException e) {
			LOGGER.severe("Can't update in table SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return true;
		}

	}

	public boolean delete(int id) {

		LOGGER.finest("Deleting from table SUBSCRIBERS_GROUPS subscribersGroup with id "
				+ id);

		try {

			String query = QUERY_DELETE.replaceFirst(Pattern.quote("?"), ""
					+ id);

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);

		} catch (SQLException e) {
			LOGGER.severe("Can't delete in table SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return true;
		}

	}

	public boolean delete(SubscribersGroup subscribersGroup) {
		return delete(subscribersGroup.getId());
	}

	public List<SubscribersGroup> findAll() {

		LOGGER.finest("Finding all groups in table SUBSCRIBERS_GROUPS");

		ArrayList<SubscribersGroup> list = new ArrayList<SubscribersGroup>();

		try {

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + QUERY_FINDALL);
			ResultSet rs = st.executeQuery(QUERY_FINDALL);

			while (rs.next()) {
				list.add(rs2SubscribersGroup(rs));
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

	public List<SubscribersGroup> findByUserId(int userId) {

		LOGGER.finest("Finding groups in table SUBSCRIBERS_GROUPS for used id = "
				+ userId);

		ArrayList<SubscribersGroup> list = new ArrayList<SubscribersGroup>();

		String query = QUERY_FIND_BY_USERID.replaceFirst(Pattern.quote("?"), ""
				+ userId);

		try {

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				list.add(rs2SubscribersGroup(rs));
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

	public boolean addSubscriberToGroup(int idSubscriber, int idGroup) {

		try {

			String query = QUERY_ADD_SUBSCRIBER_TO_GROUP.replaceFirst(Pattern.quote("?"), "" + idSubscriber);
			query = query.replaceFirst(Pattern.quote("?"), ""+ idGroup);

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);

		} catch (SQLException e) {
			LOGGER.severe("Can't insert into table SUBSCRIBER_SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return true;
		}

	}
	
	public boolean deleteSubscriberFromGroup(int idSubscriber, int idGroup) {

		try {

			String query = QUERY_DELETE_SUBSCRIBER_TO_GROUP.replaceFirst(Pattern.quote("?"), "" + idSubscriber);
			query = query.replaceFirst(Pattern.quote("?"), ""+ idGroup);

			Connection conn = ConnectionManager.getConnection();
			Statement st = conn.createStatement();
			LOGGER.finest("Executing query: " + query);
			int result = st.executeUpdate(query);
			st.close();
			conn.close();
			return (result==0);

		} catch (SQLException e) {
			LOGGER.severe("Can't DELETE from table SUBSCRIBER_SUBSCRIBERS_GROUPS.\n"
					+ e.toString());
			return true;
		}

	}

	private SubscribersGroup rs2SubscribersGroup(ResultSet rs)
			throws SQLException {

		SubscribersGroup subscribersGroup = new SubscribersGroup();

		subscribersGroup.setId(rs.getInt("ID"));
		subscribersGroup.setName(rs.getString("NAME"));
		subscribersGroup.setDescription(rs.getString("DESCRIPTION"));

		return subscribersGroup;

	}
}

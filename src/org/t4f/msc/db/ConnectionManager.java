package org.t4f.msc.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionManager {

	private final static Logger LOGGER = Logger.getLogger("POCSAG-MSC");
	private final static String DATASOURCE_NAME = "jdbc/pocsag";
	private final static String DATASOURCE_SHUTDOWN_NAME = "jdbc/pocsagShutDown";
	private static DataSource datasource;

	private static DataSource getDataSource(String dataSourceLocation)
			throws NamingException {

        try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Context ctx = new InitialContext();
		Context envContext = (Context) ctx.lookup("java:/comp/env");

		javax.sql.DataSource ds = (javax.sql.DataSource) envContext
				.lookup(dataSourceLocation);

		return ds;
	}

	public static Connection getConnection() throws SQLException {

		// if (datasource==null) {
		try {
			LOGGER.finest("Looking for datasource " + DATASOURCE_NAME);
			datasource = getDataSource(DATASOURCE_NAME);
		} catch (NamingException e) {
			LOGGER.severe("Can't find datasource " + DATASOURCE_NAME + "\n"
					+ e.toString());
			throw new SQLException("Can't open datasource");
		}
		// }
		LOGGER.finest("Trying to get connection");
		return datasource.getConnection();
	}

	public static boolean checkTableExist(String tableName) throws SQLException {
		Connection conn = getConnection();
		DatabaseMetaData metadata = conn.getMetaData();
		ResultSet resultSet = metadata.getTables(null, null, tableName, null);
		boolean exist = resultSet.next();
		conn.close();
		return exist;
	}

	public static boolean shutdownDataSource() {

		try {

			LOGGER.info("Closing datasource...");

			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");

			envContext.lookup(DATASOURCE_SHUTDOWN_NAME);
			LOGGER.info("Datasource closed");
		} catch (Exception e) {
			System.out.println(e);
			return true;
		}

		return false;

	}

	//
	// public static boolean execute(String sql) throws SQLException {
	//
	//
	// Connection conn = getConnection();
	// Statement st = conn.createStatement();
	//
	// boolean state = st.execute(sql);
	//
	// st.close();
	//
	// return state;
	//
	// }
	//
	//
	// public static ResultSet executeQuery(String sql) throws SQLException {
	//
	// Connection conn = getConnection();
	// Statement st = conn.createStatement();
	//
	// return st.executeQuery(sql);
	//
	// // WARNING: Not closing the Resulset or the statement.
	// }
	//

	public static void deregisterDriver() {
		
		Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.info(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                LOGGER.warning(String.format("Error deregistering driver %s", driver));
            }
        }
        
        datasource = null;

	}

}

package com.checqbook.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  Utility class for creating sqlite connection
 * @author Pradeep
 *
 */
public class ConnectionHelper {

	public static String sqliteClassName="org.sqlite.JDBC";
	public static Connection sqliteConnection;
	public static String prefix = "jdbc:sqlite:";
	
	
	public static Connection openSqliteConnection(String dbPath) throws ClassNotFoundException, SQLException {

		Class.forName(sqliteClassName);

		sqliteConnection = DriverManager.getConnection(prefix+dbPath);

		return sqliteConnection;

	}
}

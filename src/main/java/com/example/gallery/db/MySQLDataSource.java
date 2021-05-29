package com.example.gallery.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDataSource {

	private Connection connection = null;

	private void initConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot initialize the database driver.");
		}
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gallery?user=root&password=amadeusz92");
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot initialize the database connection.");
		}
	}

	public Connection getConnection() {
		if (connection == null) {
			initConnection();
		}
		return connection;
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}
}

package com.oracle.connect.sink;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.connect.model.TransactionRecord;

public class SqlDatabaseWriter {

	private static final Logger log = LoggerFactory.getLogger(SqlDatabaseWriter.class);

	SqlDatabaseSinkConnectorConfig config;
	Connection connection = null;
	PreparedStatement statement=null;

	public SqlDatabaseWriter(SqlDatabaseSinkConnectorConfig config) {
		this.config = config;
	}

	public SqlDatabaseWriter() {
	}

	public void writeRecordToDB(TransactionRecord tr) throws IOException {
		
		try
		{
			// Create a connection to the database
			
			String driverName = config.getDatabaseDrivername();
			String url = config.getDatabaseUrl();
			String username = config.getDatabaseUsername();
			String password = config.getDatabasePassword();
			
			System.out.println("Database URL "+url);
			System.out.println("Database Username "+username);
			System.out.println("Database Password "+password);
			
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, username, password);

			System.out.println("Successfully Connected to the database!");

		} catch (ClassNotFoundException e) {

			System.out.println("Could not find the database driver " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Could not connect to the database " + e.getMessage());
		}

		try {

			// Get a result set containing all data from test_table

			String query = "insert into "+config.getDatabaseTable()+" (id,day,month,year,note) values (?,?,?,?,?)";
			statement = connection.prepareStatement(query);
			statement.setInt(1,tr.getId());
			statement.setString(2,tr.getDay());
			statement.setString(3,tr.getMonth());
			statement.setString(4,tr.getYear());
			statement.setString(5,tr.getNote());
			int size= statement.executeUpdate();
			System.out.println("written record "+tr.toString());
			System.out.println("Records written "+size);
		}
		catch (Exception e) {
			System.out.println("Excpetion in writing Transaction Records "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) throws IOException {
		SqlDatabaseWriter re = new SqlDatabaseWriter();
		TransactionRecord tr=new TransactionRecord(3,"4","12","2019","pizza");
		re.writeRecordToDB(tr);

//		String driverName = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://localhost:3306/sys";
//		String username = "root";
//		String password = "test1234";
	}
}
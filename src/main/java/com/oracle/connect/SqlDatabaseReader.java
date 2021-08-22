package com.oracle.connect;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.connect.model.TransactionRecord;

public class SqlDatabaseReader {

	private static final Logger log = LoggerFactory.getLogger(SqlDatabaseReader.class);

	SqlDatabaseSourceConnectorConfig config;
	Connection connection = null;
	PreparedStatement statement=null;
	ResultSet rs=null;
	

	public SqlDatabaseReader(SqlDatabaseSourceConnectorConfig config) {
		this.config = config;
	}

	public SqlDatabaseReader() {
	}


	public List<TransactionRecord> getNoteFromDb() throws IOException {

		int offset = 0; 
		List<TransactionRecord> list=new ArrayList<TransactionRecord>();
		try
		{
			// Create a connection to the database
			
			String driverName = config.getDatabaseDrivername();
			String url = config.getDatabaseUrl();
			String username = config.getDatabaseUsername();
			String password = config.getDatabasePassword();
			
			String offsetFileLocation=config.getSinceConfigFile();
			log.debug("OFFSET FILE LOCATION "+offsetFileLocation);
			log.debug("Database URL "+url);
			log.debug("Database Username "+username);
			log.debug("Database Password "+password);
			offset=getOffset(offsetFileLocation);
			

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

			String query = "select * from "+config.getDatabaseTable()+ " where id > ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1,offset);
			rs = statement.executeQuery();
			TransactionRecord transRecord = null;

			while (rs.next()) {
				transRecord=new TransactionRecord();
				transRecord.setId(rs.getInt("id"));
				transRecord.setDay(rs.getString("day"));
				transRecord.setMonth(rs.getString("month"));
				transRecord.setYear(rs.getString("year"));
				transRecord.setNote(rs.getString("note"));
				list.add(transRecord);
				System.out.println("Record fetched " + transRecord);
				offset=transRecord.getId();
			}
		}
		catch (Exception e) {
			System.out.println("Excpetion in fetching Transaction Records "+e.getMessage());
			e.printStackTrace();
		}
		updateOffset(offset);
		return list;
	}
	
	private int getOffset(String offsetFileLocation) throws IOException {
		  FileReader fr=new FileReader(new File(offsetFileLocation));
		  //FileReader fr=new FileReader(new File("C:\\kafka_2.12-2.6.0\\myconnect\\offset.txt"));
		  int i;
		  String str="";
          while((i=fr.read())!=-1)    
        	  str+=(char)i;   
	       System.out.println("LAST OFFSET "+str);
	       fr.close();
	       return Integer.parseInt(str);
	}
	
   private void updateOffset(int offset) {
	   FileWriter fwriter=null;
	    try {
	    	//fwriter=new FileWriter(new File("C:\\kafka_2.12-2.6.0\\myconnect\\offset.txt"));
	    	//PrintWriter pw = new PrintWriter("C:\\kafka_2.12-2.6.0\\myconnect\\offset.txt");
	    	
	    	PrintWriter pw = new PrintWriter(config.getSinceConfigFile());
	    	pw.close();
			fwriter=new FileWriter(new File(config.getSinceConfigFile()));
			System.out.println("writing offset "+offset);
			fwriter.write(offset+"");
		} catch (IOException e) {
			System.out.println("Exception in writing offset "+offset);
			e.printStackTrace();
		}
	    finally {
	    	if(fwriter!=null)
				try {
					fwriter.close();
				} catch (IOException e) {
					System.out.println("Ignored "+e);
				}
	    }
	    	
	 }

	public static void main(String[] args) throws IOException {
		//SqlDatabaseReader re = new SqlDatabaseReader();
		//re.getNoteFromDb();

//		String driverName = "com.mysql.jdbc.Driver";
//		String url = "jdbc:mysql://localhost:3306/sys";
//		String username = "root";
//		String password = "test1234";
		//writeDataFetchedInFile("I m pratik");
		System.out.println(LocalDateTime.now());
	}
}
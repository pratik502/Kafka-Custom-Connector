package com.oracle.connect.sink;


import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.connect.model.TransactionRecord;


public class SqlDatabaseSinkTask extends SinkTask {
    private static final Logger log = LoggerFactory.getLogger(SqlDatabaseSinkTask.class);
    public SqlDatabaseSinkConnectorConfig config;

    public SqlDatabaseWriter sqlDatabaseWriter;

	public String version() {
	    return SqlVersionUtil.getVersion();
	}

	@Override
	public void start(Map<String, String> props) {
		 config = new SqlDatabaseSinkConnectorConfig(props);
		 sqlDatabaseWriter = new SqlDatabaseWriter(config);
		
	}

	@Override
	public void put(Collection<SinkRecord> records) {
		for(SinkRecord record:records) {
			System.out.println("Sink Records got "+record);
			System.out.println("Sink Records key "+record.key());
			System.out.println("Sink Records value "+record.value());
			
			try {
			ObjectMapper mapper=new ObjectMapper();
			JsonNode node = mapper.convertValue(record.value(), JsonNode.class);
			int id=node.findValue("id").asInt();
			String day=node.findValue("day").asText();
			String month=node.findValue("month").asText();
			String year=node.findValue("year").asText();
			String note=node.findValue("note").asText();
			
			System.out.println("FINAL NOTE "+id+" "+day + " "+month+" "+year+" "+note);
			
			
			TransactionRecord trecord=new TransactionRecord(id,day,month,year,note);
			sqlDatabaseWriter.writeRecordToDB(trecord);
			
			}
			catch(ClassCastException e) {
				System.out.println("Class cast Exception in transaforming sink record value "+e.getMessage());
				e.printStackTrace();
			}
			catch(Exception e) {
				System.out.println("Exception while putting sink record value "+e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void stop() {
		try {
			if(sqlDatabaseWriter!=null) {
				if (sqlDatabaseWriter.connection != null && !sqlDatabaseWriter.connection.isClosed())
					sqlDatabaseWriter.connection.close();
				if (sqlDatabaseWriter.statement != null && !sqlDatabaseWriter.statement.isClosed())
					sqlDatabaseWriter.statement.close();
				System.out.println("Closed resources for Sink DB Task");
			}
		} catch (SQLException e) {
			System.out.println("Error in Sink Task Stop method " + e.toString());
		}
		
	}


   

}
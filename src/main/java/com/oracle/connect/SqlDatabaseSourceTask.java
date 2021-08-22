package com.oracle.connect;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.connect.model.TransactionRecord;



public class SqlDatabaseSourceTask extends SourceTask {
    private static final Logger log = LoggerFactory.getLogger(SqlDatabaseSourceTask.class);
    public SqlDatabaseSourceConnectorConfig config;

    protected long lastId;

    SqlDatabaseReader sqlDatabaseReader;

    //@Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        //Do things here that are required to start your task. This could be open a connection to a database, etc.
        config = new SqlDatabaseSourceConnectorConfig(map);
        sqlDatabaseReader = new SqlDatabaseReader(config);
    }



    @Override
    public List<SourceRecord> poll() throws InterruptedException {

        // fetch data
        final ArrayList<SourceRecord> records = new ArrayList<SourceRecord>();
        List<TransactionRecord> recordsList = null;
		try {
			recordsList = sqlDatabaseReader.getNoteFromDb();
		} catch (IOException e) {
			System.out.println("Error in fetching records from table "+e);
			e.printStackTrace();
		}
        // we'll count how many results we get with i
        int i = 0;
        for (TransactionRecord trecord : recordsList) {
            SourceRecord sourceRecord = generateSourceRecord(trecord);
            records.add(sourceRecord);
            i += 1;
        }
        return records;
    }


    private SourceRecord generateSourceRecord(TransactionRecord trecord) {
        return new SourceRecord(
                sourcePartition(trecord),
                sourceOffset(trecord),
                config.getTopicConfig(),
                null, // partition will be inferred by the framework
                SqlDatabaseSchemas.KEY_SCHEMA,
                buildRecordKey(trecord),
                SqlDatabaseSchemas.VALUE_SCHEMA,
                buildRecordValue(trecord),
                new Date().getTime());
    }

    @Override
    public void stop() {
    	try {
    		if(sqlDatabaseReader!=null) {
    			if(sqlDatabaseReader.connection!=null && !sqlDatabaseReader.connection.isClosed())
    				sqlDatabaseReader.connection.close();
    			if(sqlDatabaseReader.statement!=null && !sqlDatabaseReader.statement.isClosed())
    				sqlDatabaseReader.statement.close();
    			if(sqlDatabaseReader.rs!=null && sqlDatabaseReader.rs!=null)
    				sqlDatabaseReader.rs.close();
    			System.out.println("Closed Resources for Source Task");
    		}
		} catch (SQLException e) {
			System.out.println("Error in source stop method"+e.toString());
		}
    }

    private Map<String, String> sourcePartition(TransactionRecord trecord) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(SqlDatabaseSchemas.ID, trecord.getId()+"");
        return map;
    }

    private Map<String, String> sourceOffset(TransactionRecord trecord) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(SqlDatabaseSchemas.ID, trecord.getId()+"");
        return map;
    }

    private Struct buildRecordKey(TransactionRecord trecord){
        // Key Schema
        Struct key = new Struct(SqlDatabaseSchemas.KEY_SCHEMA)
                .put(SqlDatabaseSchemas.ID, trecord.getId());

        return key;
    }

    private Struct buildRecordValue(TransactionRecord trecord){

        // Issue top level fields
        Struct valueStruct = new Struct(SqlDatabaseSchemas.VALUE_SCHEMA)
        		.put(SqlDatabaseSchemas.ID, trecord.getId())
        		.put(SqlDatabaseSchemas.DAY, trecord.getDay())
        		.put(SqlDatabaseSchemas.MONTH, trecord.getMonth())
        		.put(SqlDatabaseSchemas.YEAR, trecord.getYear())
        		.put(SqlDatabaseSchemas.NOTE, trecord.getNote());


        return valueStruct;
    }

}
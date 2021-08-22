package com.oracle.connect;


import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;


public class SqlDatabaseSourceConnectorConfig extends AbstractConfig {

    public static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to write to";

    public static final String DATABASE_URL = "dburl";
    private static final String DATABASE_URL_DOC = "database url where you want to connect";
    
    public static final String DATABASE_DRIVERNAME = "driverName";
    private static final String DATABASE_DRIVERNAME_DOC = "database driver name for example :com.mysql.jdbc.Driver";
    
    public static final String DATABASE_USERNAME = "dbusername";
    private static final String DATABASE_USERNAME_DOC = "username to connect to database";
    
    public static final String DATABASE_PASSWORD = "dbpassword";
    private static final String DATABASE_PASSWORD_DOC = "password to connect to database";
    
    public static final String DATABASE_TABLE = "dbtable";
    private static final String DATABASE_TABLE_DOC = "destination database table name";
    

    public static final String SINCE_CONFIG_FILE = "last.record.file.location";
    private static final String SINCE_CONFIG_FILE_DOC ="last record offset stored in file, please give file location";

    public static final String BATCH_SIZE_CONFIG = "batch.size";
    private static final String BATCH_SIZE_DOC = "Number of data points to retrieve at a time. Defaults to 100 (max value)";


    public SqlDatabaseSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public SqlDatabaseSourceConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC_CONFIG, Type.STRING, Importance.HIGH, TOPIC_DOC)
                .define(DATABASE_URL, Type.STRING, Importance.HIGH, DATABASE_URL_DOC)
                .define(DATABASE_DRIVERNAME, Type.STRING, "", Importance.HIGH, DATABASE_DRIVERNAME_DOC)
                .define(DATABASE_USERNAME, Type.STRING, Importance.HIGH, DATABASE_USERNAME_DOC)
                .define(DATABASE_PASSWORD, Type.STRING, "", Importance.HIGH, DATABASE_PASSWORD_DOC)
                .define(DATABASE_TABLE, Type.STRING, "", Importance.HIGH, DATABASE_TABLE_DOC)
                .define(BATCH_SIZE_CONFIG, Type.INT, 100, Importance.LOW, BATCH_SIZE_DOC)
                .define(SINCE_CONFIG_FILE, Type.STRING, Importance.HIGH, SINCE_CONFIG_FILE_DOC);
                
    }

	public String getTopicConfig() {
		return this.getString(TOPIC_CONFIG);
	}

	public  String getTopicDoc() {
		return this.getString(TOPIC_DOC);
	}

	public String getDatabaseUrl() {
	  return this.getString(DATABASE_URL);
	}

	public String getDatabaseUrlDoc() {
		return this.getString(DATABASE_URL_DOC);
	}

	public String getDatabaseUsername() {
		return this.getString(DATABASE_USERNAME);
	}

	public String getDatabaseUsernameDoc() {
		return this.getString(DATABASE_USERNAME_DOC);
	}

	public String getDatabasePassword() {
		return this.getString(DATABASE_PASSWORD);
	}

	public String getDatabasePasswordDoc() {
		return this.getString(DATABASE_PASSWORD_DOC);
	}

	public String getDatabaseTable() {
		return this.getString(DATABASE_TABLE);
	}

	public String getDatabaseTableDoc() {
		return this.getString(DATABASE_TABLE_DOC);
	}


	public int getBatchSizeConfig() {
		return this.getInt(BATCH_SIZE_CONFIG);
	}

	public String getBatchSizeDoc() {
		return this.getString(BATCH_SIZE_DOC);
	}

	public String getDatabaseDrivername() {
		return this.getString(DATABASE_DRIVERNAME);
	}

	public String getDatabaseDrivernameDoc() {
		return this.getString(DATABASE_DRIVERNAME_DOC);
	}

	public String getSinceConfigFile() {
		return this.getString(SINCE_CONFIG_FILE);
	}

	public String getSinceConfigFileDoc() {
		return this.getString(SINCE_CONFIG_FILE_DOC);
	}

	
   
}

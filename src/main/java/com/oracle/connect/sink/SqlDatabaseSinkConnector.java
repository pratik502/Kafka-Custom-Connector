package com.oracle.connect.sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlDatabaseSinkConnector extends SinkConnector {
	private static Logger log = LoggerFactory.getLogger(SqlDatabaseSinkConnector.class);
	private SqlDatabaseSinkConnectorConfig config;

	@Override
	public String version() {
		return SqlVersionUtil.getVersion();
	}

	@Override
	public void start(Map<String, String> map) {
		config = new SqlDatabaseSinkConnectorConfig(map);
	}

	@Override
	public Class<? extends Task> taskClass() {
		return SqlDatabaseSinkTask.class;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, String>> taskConfigs(int i) {
		// Define the individual task configurations that will be executed.
		List<Map<String, String>> configs = new ArrayList(1);
		configs.add(config.originalsStrings());
		return configs;
	}

	@Override
	public void stop() {

	}

	@Override
	public ConfigDef config() {
		return SqlDatabaseSinkConnectorConfig.conf();
	}
}

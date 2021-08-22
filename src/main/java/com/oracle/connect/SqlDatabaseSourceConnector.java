package com.oracle.connect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlDatabaseSourceConnector extends SourceConnector {
    private static Logger log = LoggerFactory.getLogger(SqlDatabaseSourceConnector.class);
    private SqlDatabaseSourceConnectorConfig config;

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        config = new SqlDatabaseSourceConnectorConfig(map);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SqlDatabaseSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        // Define the individual task configurations that will be executed.
        List<Map<String, String>> configs = new ArrayList(1);
        configs.add(config.originalsStrings());
        return configs;
    }

    @Override
    public void stop() {
        // Do things that are necessary to stop your connector.
        // nothing is necessary to stop for this connector
    }

    @Override
    public ConfigDef config() {
        return SqlDatabaseSourceConnectorConfig.conf();
    }
}

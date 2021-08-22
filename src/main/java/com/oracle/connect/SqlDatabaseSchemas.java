package com.oracle.connect;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

public class SqlDatabaseSchemas {


    // Issue fields
    public static String ID = "id";
    public static String DAY = "day";
    public static String MONTH = "month";
    public static String YEAR = "year";
    public static String NOTE = "note";


    // Schema names
    public static String SCHEMA_KEY = "id";
    public static String SCHEMA_VALUE_ISSUE = "value";

    // Key Schema
    public static Schema KEY_SCHEMA = SchemaBuilder.struct().name(SCHEMA_KEY)
            .version(1)
            .field(ID, Schema.INT32_SCHEMA)
            .build();


    public static Schema VALUE_SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE_ISSUE)
            .version(1)
            .field(ID, Schema.INT32_SCHEMA)
            .field(DAY, Schema.STRING_SCHEMA)
            .field(MONTH, Schema.STRING_SCHEMA)
            .field(YEAR, Schema.STRING_SCHEMA)
            .field(NOTE, Schema.STRING_SCHEMA)
            .build();
}

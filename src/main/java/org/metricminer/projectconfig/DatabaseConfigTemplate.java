package org.metricminer.projectconfig;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigTemplate {

	public MapConfig configBasedOnInput(String host, String schema, String user, String password, boolean createTables) {
		Map<String, String> cfgs = new HashMap<String, String>();

		cfgs.put("driver_class", "com.mysql.jdbc.Driver");
		cfgs.put("connection_string", "jdbc:mysql://" + host
				+ "/" + schema
				+ "?useUnicode=true&characterEncoding=UTF-8");
		cfgs.put("dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		cfgs.put("db_user", user);
		cfgs.put("db_pwd", password);
		cfgs.put("create_tables", String.valueOf(createTables));

		MapConfig config = new MapConfig(cfgs);
		return config;
	}

}

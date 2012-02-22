package de.marcphilipp.dbunit.example;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public class TestDataSource {

	public static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	public static final String USER = "sa";
	public static final String PASSWORD = "";

	public static DataSource dataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(JDBC_URL);
		dataSource.setUser(USER);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}
}
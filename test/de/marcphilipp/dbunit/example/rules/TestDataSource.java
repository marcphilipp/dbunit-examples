package de.marcphilipp.dbunit.example.rules;

import static org.junit.rules.RuleChain.outerRule;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.rules.TestRule;


public class TestDataSource {

	public static final String DEFAULT_JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	public static final String DEFAULT_USER = "sa";
	public static final String DEFAULT_PASSWORD = "";

	public static DataSource dataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(System.getProperty("test.jdbc.url", DEFAULT_JDBC_URL));
		dataSource.setUser(System.getProperty("test.jdbc.user", DEFAULT_USER));
		dataSource.setPassword(System.getProperty("test.jdbc.password", DEFAULT_PASSWORD));
		return dataSource;
	}

	public static TestRule prepareDatabase(DataSource dataSource, Object testInstance) {
		return outerRule(new OnlyRunOracleTestsOnOracle(dataSource)).around(
				new CreateSchemaIfNecessary(dataSource, "schema.sql")).around(
				new ImportDataSet(dataSource, testInstance));
	}
}
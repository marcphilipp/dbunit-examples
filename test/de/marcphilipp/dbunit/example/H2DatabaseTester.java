package de.marcphilipp.dbunit.example;

import javax.sql.DataSource;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.h2.H2Connection;

public class H2DatabaseTester extends AbstractDatabaseTester {

	private final DataSource dataSource;

	public H2DatabaseTester(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public IDatabaseConnection getConnection() throws Exception {
		return new H2Connection(dataSource.getConnection(), getSchema());
	}
}

package de.marcphilipp.dbunit.example;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.junit.rules.ExternalResource;

public class CreateSchema extends ExternalResource implements Using {

	private String sqlFilePath;
	private final DataSource dataSource;

	public static Using in(DataSource dataSource) {
		return new CreateSchema(dataSource);
	}

	private CreateSchema(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected void before() throws Throwable {
		FileReader reader = null;
		Connection connection = null;
		try {
			reader = new FileReader(new File(sqlFilePath));
			connection = dataSource.getConnection();
			RunScript.execute(connection, reader);
		} finally {
			closeQuietly(connection);
			closeQuietly(reader);
		}
	}

	@Override
	public CreateSchema using(String sqlFilePath) {
		this.sqlFilePath = sqlFilePath;
		return this;
	}

	private void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException exception) {
			}
		}
	}

	private void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException exception) {
			}
		}
	}
}

interface Using {

	CreateSchema using(String sqlFilePath);

}
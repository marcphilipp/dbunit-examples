package de.marcphilipp.dbunit.example.rules;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.h2.tools.RunScript;
import org.junit.rules.ExternalResource;

public class CreateSchemaIfNecessary extends ExternalResource {

	private final DataSource dataSource;
	private final String sqlFilePath;

	public CreateSchemaIfNecessary(DataSource dataSource, String sqlFilePath) {
		this.dataSource = dataSource;
		this.sqlFilePath = sqlFilePath;
	}

	@Override
	protected void before() throws Throwable {
		FileReader reader = null;
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (isInMemoryH2(connection)) {
				reader = new FileReader(new File(sqlFilePath));
				RunScript.execute(connection, reader);
			}
		} finally {
			closeQuietly(connection);
			closeQuietly(reader);
		}
	}

	private boolean isInMemoryH2(Connection connection) throws SQLException {
		return connection.getMetaData().getURL().startsWith("jdbc:h2:mem:");
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
